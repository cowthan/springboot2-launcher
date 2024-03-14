package org.danger.utils.mybatis;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

public class PageCriteria implements Serializable {
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int DEFAULT_PAGE_NUM = 1;
    private Integer page = 1;
    private Integer size = 20;

    public PageCriteria() {
    }

    public PageCriteria(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @JsonIgnore
    public Integer getOffset() {
        return (this.page - 1) * this.size;
    }

    @JsonIgnore
    public Integer getLimit() {
        return this.size;
    }
}
