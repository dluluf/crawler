package com.pinganfu.crawler.data.model;

public class TaskConfigDO {
    /**
     * 任务配置id
     * 自增
     */
    private String id;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 链接入库url
     */
    private String seedUrl;

    /**
     * cron时间表达式
     */
    private String cron;

    /**
     * url选择器(包含正则表达式)
     */
    private String urlCssSelector;
    /**
     * 列表页面 内容选择权
     */
    private String contentListCssSelector;
    /**
     * 需要抓取的字段选择器
     * key-value对的形式
     */
    private String fieldsCssSelector;
    /**
     * 分页需要替换的参数
     */
    private String pageParams;
    /**
     * 分页每页的条数
     */
    private String pageSize;
    /**
     * 分页url模版
     */
    private String urlTemplate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getUrlCssSelector() {
        return urlCssSelector;
    }

    public void setUrlCssSelector(String urlCssSelector) {
        this.urlCssSelector = urlCssSelector;
    }

    public String getContentListCssSelector() {
        return contentListCssSelector;
    }

    public void setContentListCssSelector(String contentListCssSelector) {
        this.contentListCssSelector = contentListCssSelector;
    }

    public String getFieldsCssSelector() {
        return fieldsCssSelector;
    }

    public void setFieldsCssSelector(String fieldsCssSelector) {
        this.fieldsCssSelector = fieldsCssSelector;
    }

    public String getPageParams() {
        return pageParams;
    }

    public void setPageParams(String pageParams) {
        this.pageParams = pageParams;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }
}
