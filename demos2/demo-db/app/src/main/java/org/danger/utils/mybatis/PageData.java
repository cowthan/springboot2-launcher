package org.danger.utils.mybatis;


import java.io.Serializable;
import java.util.List;

public class PageData<T> implements Serializable {
    private List<T> list;
    private Integer pages;
    private Integer totalCount;
    private Integer pageSize;

    public PageData() {
    }

    public PageData(List<T> data, long totalCount, int pageSize) {
        this.list = data;
        this.pageSize = pageSize;
        this.pages = (int)((totalCount + (long)pageSize - 1L) / (long)pageSize);
        this.totalCount = (int)totalCount;
    }

    public static long getPageOffset(int page, int pageSize) {
        return ((long)page - 1L) * (long)pageSize;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPages() {
        return this.pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
