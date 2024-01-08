package com.ddy.dyy.mybatis.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页请求响应对象
 */
@Getter
@Setter
public class PageList<T> {

    private List<T> list;
    private Integer pages;
    private Integer totalCount;
    private Integer pageSize;

    public PageList() {

    }

    public PageList(List<T> data, long totalCount, int pageSize) {
        this.list = data;
        this.pageSize = pageSize;
        this.pages = (int) ((totalCount + pageSize - 1) / pageSize);
        this.totalCount = (int) totalCount;
    }
}
