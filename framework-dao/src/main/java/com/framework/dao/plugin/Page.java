package com.framework.dao.plugin;



import java.util.List;

public class Page<T> {

	private Long total = 0L;

	private int pageSize = 10;

	private int pageIndex = 1;

	private int totalPage = 0;

	private List<T> list = null;

	public Page() {

	}

	public Page(int pageIndex, int pageSize) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}

	public Page(int page) {
		this.pageIndex = page;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getTotalPage() {
		totalPage = Integer.parseInt(((total + pageSize - 1) / pageSize)+"");
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}