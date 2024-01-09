package com.ddy.dyy.web.models.biz;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页请求响应对象
 */
@Getter
@Setter
public class PageList {

    private List<?> list;
    private Integer pages;
    private Integer totalCount;
    private Integer pageSize;

    public PageList() {

    }

    public PageList(List<?> data, long totalCount, int pageSize) {
        this.list = data;
        this.pageSize = pageSize;
        this.pages = (int) ((totalCount + pageSize - 1) / pageSize);
        this.totalCount = (int) totalCount;
    }
}
