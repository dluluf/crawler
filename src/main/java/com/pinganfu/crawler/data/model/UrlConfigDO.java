package com.pinganfu.crawler.data.model;

public class UrlConfigDO {
    private Integer id;

    private String domainUrl;

    private String fieldsCssSelector;

    private String contentListCssSelector;

    private String pageCountCssSelector;

    private Integer sleepTime;

    private String javascript;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getFieldsCssSelector() {
        return fieldsCssSelector;
    }

    public void setFieldsCssSelector(String fieldsCssSelector) {
        this.fieldsCssSelector = fieldsCssSelector;
    }

    public String getContentListCssSelector() {
        return contentListCssSelector;
    }

    public void setContentListCssSelector(String contentListCssSelector) {
        this.contentListCssSelector = contentListCssSelector;
    }

    public String getPageCountCssSelector() {
        return pageCountCssSelector;
    }

    public void setPageCountCssSelector(String pageCountCssSelector) {
        this.pageCountCssSelector = pageCountCssSelector;
    }

    public Integer getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getJavascript() {
        return javascript;
    }

    public void setJavascript(String javascript) {
        this.javascript = javascript;
    }
}