package com.yiban.erp.controller.sell;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiban.erp.dao.SellOrderBackDetailMapper;
import com.yiban.erp.dao.SellOrderBackMapper;
import com.yiban.erp.dto.SellBackCheck;
import com.yiban.erp.dto.SellBackQuery;
import com.yiban.erp.entities.SellOrderBack;
import com.yiban.erp.entities.SellOrderBackDetail;
import com.yiban.erp.entities.User;
import com.yiban.erp.service.sell.SellOrderBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sell/back")
public class SellOrderBackController {

    private static final Logger logger = LoggerFactory.getLogger(SellOrderBackController.class);

    @Autowired
    private SellOrderBackService sellOrderBackService;


    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> add(@RequestBody SellOrderBack orderBack,
                                      @AuthenticationPrincipal User user) throws Exception {

        logger.info("user:{} request add sell order back by:{}", user.getId(), JSON.toJSONString(orderBack));
        sellOrderBackService.add(orderBack, user);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> list(@RequestBody SellBackQuery query,
                                       @AuthenticationPrincipal User user) throws Exception {
        query.setCompanyId(user.getCompanyId());
        List<SellOrderBack> orderBacks = sellOrderBackService.getOrderList(query);
        return ResponseEntity.ok().body(JSON.toJSONString(orderBacks, SerializerFeature.DisableCircularReferenceDetect));
    }

    @RequestMapping(value = "/{backOrderId}/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> details(@PathVariable Long backOrderId) {
        List<SellOrderBackDetail> details = sellOrderBackService.getDetails(backOrderId);
        return ResponseEntity.ok().body(JSON.toJSONString(details, SerializerFeature.DisableCircularReferenceDetect));
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> check(@RequestBody SellBackCheck backCheck,
                                        @AuthenticationPrincipal User user) throws Exception {
        logger.info("user:{} request save sell order back check result by params:{}", user.getId(), backCheck);
        sellOrderBackService.checkBackOrder(backCheck, user);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{backOrderId}/check/cancel", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelQualityCheck(@PathVariable Long backOrderId,
                                                     @AuthenticationPrincipal User user) throws Exception {
        logger.info("user:{} cancel quality check request backOrderId:{}", user.getId(), backOrderId);
        sellOrderBackService.cancelQualityCheck(backOrderId, user);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/delete/{backOrderId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeBackOrder(@PathVariable Long backOrderId,
                                                  @AuthenticationPrincipal User user) throws Exception {
        logger.info("user:{} request remove sell back order:{}", user.getId(), backOrderId);
        sellOrderBackService.removeBackOrder(backOrderId, user);
        return ResponseEntity.ok().build();
    }

}
