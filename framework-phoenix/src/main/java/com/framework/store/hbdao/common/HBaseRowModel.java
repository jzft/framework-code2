package com.framework.store.hbdao.common;


import java.util.HashSet;
import java.util.Set;
/**
 * hbase查询结果记录行模型
 *
 */
public class HBaseRowModel {
	
	private String rowkey;

	private Set<HBaseCellModel> cells = new HashSet<HBaseCellModel>();
	
	public HBaseRowModel(){}
	
	public HBaseRowModel(String rowkey) {
		super();
		this.rowkey = rowkey;
	}

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

	public void addCell(HBaseCellModel cell){
		cells.add(cell);
	}
	
	public Set<HBaseCellModel> getCells() {
		return cells;
	}
	
	public HBaseCellModel lookupCell(String family, String qualifier){
		HBaseCellModel cell = null;
		for(HBaseCellModel m : cells){
			if(m.getFamily().equalsIgnoreCase(family)
					&& m.getQualifier().equalsIgnoreCase(qualifier)){
				cell = m;
				break;
			}
		}
		return cell;
	}
}
