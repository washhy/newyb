package com.yiban.erp.entities;

import java.math.BigDecimal;
import java.util.Date;

public class SellOrder {
    private Long id;

    private Integer companyId;

    private String orderNumber;

    private String refNo;

    private String status;

    private Integer customerId;

    private Integer customerRepId;

    private Integer salerId;

    private Integer temperControlId;

    private Date createOrderDate;

    private String takeGoodsUser;

    private Date payOrderDate;

    private Integer payMethod;

    private String payFileNo;

    private BigDecimal markUpRate;

    private BigDecimal payAmount;

    private BigDecimal notSmallAmount;

    private Integer shipMethod;

    private Integer shipTool;

    private String comment;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo == null ? null : refNo.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerRepId() {
        return customerRepId;
    }

    public void setCustomerRepId(Integer customerRepId) {
        this.customerRepId = customerRepId;
    }

    public Integer getSalerId() {
        return salerId;
    }

    public void setSalerId(Integer salerId) {
        this.salerId = salerId;
    }

    public Integer getTemperControlId() {
        return temperControlId;
    }

    public void setTemperControlId(Integer temperControlId) {
        this.temperControlId = temperControlId;
    }

    public Date getCreateOrderDate() {
        return createOrderDate;
    }

    public void setCreateOrderDate(Date createOrderDate) {
        this.createOrderDate = createOrderDate;
    }

    public String getTakeGoodsUser() {
        return takeGoodsUser;
    }

    public void setTakeGoodsUser(String takeGoodsUser) {
        this.takeGoodsUser = takeGoodsUser;
    }

    public Date getPayOrderDate() {
        return payOrderDate;
    }

    public void setPayOrderDate(Date payOrderDate) {
        this.payOrderDate = payOrderDate;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayFileNo() {
        return payFileNo;
    }

    public void setPayFileNo(String payFileNo) {
        this.payFileNo = payFileNo == null ? null : payFileNo.trim();
    }

    public BigDecimal getMarkUpRate() {
        return markUpRate;
    }

    public void setMarkUpRate(BigDecimal markUpRate) {
        this.markUpRate = markUpRate;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getNotSmallAmount() {
        return notSmallAmount;
    }

    public void setNotSmallAmount(BigDecimal notSmallAmount) {
        this.notSmallAmount = notSmallAmount;
    }

    public Integer getShipMethod() {
        return shipMethod;
    }

    public void setShipMethod(Integer shipMethod) {
        this.shipMethod = shipMethod;
    }

    public Integer getShipTool() {
        return shipTool;
    }

    public void setShipTool(Integer shipTool) {
        this.shipTool = shipTool;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}