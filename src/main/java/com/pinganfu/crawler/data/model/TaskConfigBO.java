package com.pinganfu.crawler.data.model;

import java.util.Map;

public class TaskConfigBO {
    /**
     * 处理job的类
     */
    private String jobClass;
    private String cron;
    private String seedUrl;

    private String contentListCssSelector;

    private Map<String,String> fieldsCssSelector;
    private String taskId;
    //任务名称即商品类型
    private String taskName;
    private String batchNo;
    private int pageSize;
    private String urlTemplate;
    private String pageParams;

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

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getContentListCssSelector() {
        return contentListCssSelector;
    }

    public void setContentListCssSelector(String contentListCssSelector) {
        this.contentListCssSelector = contentListCssSelector;
    }

    public Map<String, String> getFieldsCssSelector() {
        return fieldsCssSelector;
    }

    public void setFieldsCssSelector(Map<String, String> fieldsCssSelector) {
        this.fieldsCssSelector = fieldsCssSelector;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
