package com.miaoshaproject.service.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hjg
 * @date 2020/8/8 14:56
 */
public class PromoModel {
    private Integer id;

    //秒杀活动名称
    private String promoName;

    //秒杀活动的开始时间
    private DateTime startDate;

    private DateTime endData;
    //秒杀活动的使用商品
    private Integer itemId;

    //秒杀活动状态码 1表示还未开始，2表示进行中，3表示结束
    private Integer status;


    public DateTime getEndData() {
        return endData;
    }

    public void setEndData(DateTime endData) {
        this.endData = endData;
    }

    //秒杀活动的商品价格
    private BigDecimal promoItemPrice;

    public Integer getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }
}
