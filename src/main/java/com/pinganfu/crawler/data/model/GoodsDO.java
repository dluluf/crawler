package com.pinganfu.crawler.data.model;

import java.io.Serializable;

public class GoodsDO implements Serializable{


    private static final long serialVersionUID = -4850306382835457727L;

    private String id;
    /**
     * 记录落地商品是拿一次任务调度获取的数据。
     * goodsBatchNo = taskId+任务调度时间戳
     */
    private String goodsBatchNo;
    /**
     * 商品类型
     */
    private String goodsType;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 商品sku
     */
    private String goodsSku;
    /**
     * 价格
     */
    private String goodsPrice;
    /**
     * 销售量
     */
    private String goodsSales;
    /**
     * 商品评论数
     */
    private String goodsCommentCount;
    /**
     * 商品详情链接
     */
    private String goodsDetailUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsBatchNo() {
        return goodsBatchNo;
    }

    public void setGoodsBatchNo(String goodsBatchNo) {
        this.goodsBatchNo = goodsBatchNo;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGoodsSku() {
        return goodsSku;
    }

    public void setGoodsSku(String goodsSku) {
        this.goodsSku = goodsSku;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsSales() {
        return goodsSales;
    }

    public void setGoodsSales(String goodsSales) {
        this.goodsSales = goodsSales;
    }

    public String getGoodsCommentCount() {
        return goodsCommentCount;
    }

    public void setGoodsCommentCount(String goodsCommentCount) {
        this.goodsCommentCount = goodsCommentCount;
    }

    public String getGoodsDetailUrl() {
        return goodsDetailUrl;
    }

    public void setGoodsDetailUrl(String goodsDetailUrl) {
        this.goodsDetailUrl = goodsDetailUrl;
    }
}
