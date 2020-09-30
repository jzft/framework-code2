package com.framework.dao.plugin;


import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.framework.dao.base.BaseMapper;

public class PageUtil{

	public static <T> void getPage(@SuppressWarnings("rawtypes") BaseMapper baseMapper,Object example, Page<T> page) {
		getPage(baseMapper,example,page,false);
	}

	public static <T> void getPageWithBlob(@SuppressWarnings("rawtypes") BaseMapper baseMapper,Object example, Page<T> page) {
		getPage(baseMapper,example,page,true);
	}

	@SuppressWarnings("unchecked")
	private static <T> void getPage(@SuppressWarnings("rawtypes") BaseMapper baseMapper, Object example, Page<T> page, Boolean blob) {
		int pageSize = page.getPageSize();
		int pageIndex = page.getPageIndex();

		if (pageSize < 1 || pageSize > 1000) {
			page.setPageSize(10);
			pageSize = 10;
		}
		
		if (pageIndex < 1) {
			page.setPageIndex(1);
			pageIndex = 1;
		}

		RowBounds rowBounds = new RowBounds((pageIndex - 1) * pageSize, pageSize);
		Long total = baseMapper.countByExample(example);
		List<T> pageList = null;
		if (blob) {
			pageList = baseMapper.selectByExampleWithBLOBsWithRowbounds(example,rowBounds);
		} else {
			pageList = baseMapper.selectByExampleWithRowbounds(example,rowBounds);
		}
		page.setTotal(total);
		page.setList(pageList);
	}

}