package com.framework.store.hbdao.common;



/**
 * hbase查询结果列单元模型
 */
public class HBaseCellModel {
	
	private HBaseRowModel rowModel;
	
	private String family;
	
	private String qualifier;
	
	private Long timestamp;
	/**
	 * hbase值不分数据类型，统一以字符串表示
	 */
	private String value;

	public HBaseCellModel(){}
	
	public HBaseCellModel(String family, String qualifier,
			Long timestamp, String value) {
		super();
		this.family = family;
		this.qualifier = qualifier;
		this.timestamp = timestamp;
		this.value = value;
	}

	public HBaseRowModel getRowModel() {
		return rowModel;
	}

	public void setRowModel(HBaseRowModel rowModel) {
		this.rowModel = rowModel;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((family == null) ? 0 : family.hashCode());
		result = prime * result
				+ ((qualifier == null) ? 0 : qualifier.hashCode());
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HBaseCellModel other = (HBaseCellModel) obj;
		if (family == null) {
			if (other.family != null)
				return false;
		} else if (!family.equals(other.family))
			return false;
		if (qualifier == null) {
			if (other.qualifier != null)
				return false;
		} else if (!qualifier.equals(other.qualifier))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HBaseModel [family=");
		builder.append(family);
		builder.append(", qualifier=");
		builder.append(qualifier);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
