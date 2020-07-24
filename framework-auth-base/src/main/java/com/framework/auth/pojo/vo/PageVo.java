package com.framework.auth.pojo.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * date 2020/6/18 下午5:10
 *
 * @author casper
 **/
public class PageVo<T> extends ResultVo<List<T>> {

    /**
     * 总页数
     */
    @ApiModelProperty(name = "总页数")
    private long totalPage;

    /**
     * 总条数
     */
    @ApiModelProperty(name = "总条数")
    private long totals;

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotals() {
        return totals;
    }

    public void setTotals(long totals) {
        this.totals = totals;
    }

    public PageVo() {
    }

    public PageVo(int code, String msg, List<T> data, long totalPage, long totals) {
        super(code, msg, data);
        this.totalPage = totalPage;
        this.totals = totals;
    }

    public static <T> PageVo<T> build(int code, String msg, long totalPage, long totals, List<T> data) {
        return new PageVo<>(code, msg, data, totalPage, totals);
    }

}

