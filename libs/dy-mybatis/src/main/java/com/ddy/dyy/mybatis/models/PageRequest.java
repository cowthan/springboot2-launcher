package com.ddy.dyy.mybatis.models;


/**
 * 分页请求
 */
public class PageRequest {

    private Integer page = 1;

    private Integer size = 20;
    private Integer pageNum = 1;

    private Integer pageSize = 20;

    public PageRequest() {
    }

    public PageRequest(Integer page, Integer size) {
        this.page = page;
        this.size = size;
        this.pageNum = page;
        this.pageSize = size;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
        this.pageSize = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
        this.pageNum = page;
    }

    public void setPageNum(Integer pageNum) {
        this.page = pageNum;
        this.pageNum = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        this.size = pageSize;
        this.pageSize = pageSize;
    }

    // @JsonIgnore
    public Integer getOffset() {
        return (page - 1) * size;
    }

    //@JsonIgnore
    public Integer getLimit() {
        return this.size;
    }
}
