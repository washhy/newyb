package com.yiban.erp.service.buy;

import com.yiban.erp.dao.BuyOrderMapper;
import com.yiban.erp.dao.RepertoryInfoMapper;
import com.yiban.erp.dao.RepositoryOrderDetailMapper;
import com.yiban.erp.dao.RepositoryOrderMapper;
import com.yiban.erp.dto.CurrentBalanceResp;
import com.yiban.erp.entities.RepositoryOrder;
import com.yiban.erp.entities.RepositoryOrderDetail;
import com.yiban.erp.entities.User;
import com.yiban.erp.exception.BizException;
import com.yiban.erp.exception.BizRuntimeException;
import com.yiban.erp.exception.ErrorCode;
import com.yiban.erp.util.UtilTool;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ReceiveService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveService.class);

    @Autowired
    private RepertoryInfoMapper repertoryInfoMapper;
    @Autowired
    private BuyOrderMapper buyOrderMapper;
    @Autowired
    private RepositoryOrderMapper repositoryOrderMapper;
    @Autowired
    private RepositoryOrderDetailMapper repositoryOrderDetailMapper;

    /**
     * 获取某商品当前库存和申购订单信息
     * @param warehouseId
     * @param goodsId
     * @return
     */
    public CurrentBalanceResp getCurrentBalance(Integer warehouseId, Long goodsId) {
        if (warehouseId == null || goodsId == null) {
            return null;
        }
        //获取当前仓库内的所有数量
        Integer balance = repertoryInfoMapper.getBalance(warehouseId, goodsId);
        BigDecimal lastPrice = repertoryInfoMapper.getLastBuyPrice(warehouseId, goodsId);
        CurrentBalanceResp orderResp = buyOrderMapper.getGoodsOrderCount(goodsId);
        CurrentBalanceResp resp = new CurrentBalanceResp();
        resp.setBalance(balance);
        resp.setBuyOrderCount(orderResp != null ? orderResp.getBuyOrderCount() : 0);
        resp.setOngoing(orderResp.getOngoing());
        resp.setLastPrice(lastPrice);
        return resp;
    }

    /**
     * 保存收货入库订单信息
     * @param user
     * @param order
     * @throws BizException
     */
    public void saveOrder(User user, RepositoryOrder order) throws BizException {
        if (!saveValidate(order)) {
            throw new BizException(ErrorCode.RECEIVE_SAVE_PRAMS_INVALID);
        }
        if (order.getId() != null) {
            //新建
            order.setCompanyId(user.getCompanyId());
            order.setOrderNumber(getOrderNumber(user));
            order.setCreateBy(user.getNickname());
            order.setCreateTime(new Date());
            int count = repositoryOrderMapper.insert(order);
            if (count <= 0 || order.getId() == null) {
                logger.warn("save order insert fail.");
                throw new BizRuntimeException(ErrorCode.FAILED_INSERT_FROM_DB);
            }
        }else {
            RepositoryOrder oldOrder = repositoryOrderMapper.selectByPrimaryKey(order.getId());
            if (oldOrder == null) {
                logger.warn("get repository order fail by id:{}", order.getId());
                throw new BizException(ErrorCode.RECEIVE_ORDER_NOT_FOUND);
            }
            if (oldOrder.getCompanyId() == null || !oldOrder.getCompanyId().equals(user.getCompanyId())) {
                logger.warn("old order company is not match user company. order id:{}, user id:{}", order.getId(), user.getId());
                throw new BizException(ErrorCode.ACCESS_PERMISSION);
            }
            if (!oldOrder.canUpdateDetail()) {
                logger.warn("order can not update. order id:{}", oldOrder.getId());
                throw new BizException(ErrorCode.RECEIVE_ORDER_CANNOT_UPDATE);
            }
            order.setUpdateBy(user.getNickname());
            order.setUpdateTime(new Date());
            int count = repositoryOrderMapper.updateByPrimaryKeySelective(order);
            if (count <= 0) {
                logger.warn("save order update fail.");
                throw new BizRuntimeException(ErrorCode.FAILED_UPDATE_FROM_DB);
            }
        }
        logger.info("begin save repository order details.");
        saveOrderDetail(user, order);
    }

    private int saveOrderDetail(User user, RepositoryOrder order) {
        List<RepositoryOrderDetail> details = order.getDetails();
        if (!order.canUpdateDetail()) {
            logger.error("can update detail result is false. user:{} orderId:{}", user.getId(), order.getId());
            return -1;
        }
        //直接删除原有的，然后重新插入数据
        repositoryOrderDetailMapper.deleteByOrderId(order.getId());
        details.stream().forEach(item -> {
            item.setRepositoryOrderId(order.getId());
            item.setCreateBy(user.getNickname());
            item.setCreateTime(new Date());
        });
        return repositoryOrderDetailMapper.insertBatch(details);
    }

    private String getOrderNumber(User user) {
        StringBuilder orderNo = new StringBuilder("R");
        orderNo.append(user.getCompanyId());
        orderNo.append(UtilTool.DateFormat(new Date(), "yyyyMMddHHmmss"));
        orderNo.append(RandomStringUtils.randomNumeric(4));
        return orderNo.toString();
    }

    private boolean saveValidate(RepositoryOrder order) {
        if (order == null) {
            logger.warn("save order is null");
            return false;
        }
        if (order.getSupplierId() == null) {
            logger.warn("save order but supplier id is null.");
            return false;
        }
        if (order.getSupplierContactId() == null) {
            logger.warn("save order but supplier contact id is null.");
            return false;
        }
        if (order.getWarehouseId() == null) {
            logger.warn("save order but warehouse id is null.");
            return false;
        }
        if (order.getDetails() == null || order.getDetails().isEmpty()) {
            logger.warn("save order but details list is empty.");
            return false;
        }
        return true;
    }
}