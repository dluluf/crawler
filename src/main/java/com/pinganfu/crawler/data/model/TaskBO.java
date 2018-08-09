package com.pinganfu.crawler.data.model;

import java.util.Map;

public class TaskBO {
    private String taskName;
    private String seedUrl;
    private String domainUrl;
    private String contentListCssSelector;
    private String pageCountCssSelector;
    private Map<String,String> fieldsCssSelector;
    private int pageSize;
    private String urlTemplate;
    private String pageParams;
    private String cron;
    private int sleepTime;
    private String javascript;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSeedUrl() {
        return seedUrl;
    }

    public void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
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

    public Map<String, String> getFieldsCssSelector() {
        return fieldsCssSelector;
    }

    public void setFieldsCssSelector(Map<String, String> fieldsCssSelector) {
        this.fieldsCssSelector = fieldsCssSelector;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    public String getPageParams() {
        return pageParams;
    }

    public void setPageParams(String pageParams) {
        this.pageParams = pageParams;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getJavascript() {
        return javascript;
    }

    public void setJavascript(String javascript) {
        this.javascript = javascript;
    }
}
