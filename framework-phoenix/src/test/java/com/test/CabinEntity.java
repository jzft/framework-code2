package com.test;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jettison.json.JSONString;

import com.framework.store.hbdao.annontation.HBaseColumn;
import com.framework.store.hbdao.annontation.HBaseEntity;
import com.framework.store.hbdao.annontation.Transient;
import com.framework.store.hbdao.common.entity.BaseHBaseEntity;



/**
 * 仓位实体
 * 
 * @author Administrator
 * 
 */
@HBaseEntity(name = "CabinEntity")
public class CabinEntity extends BaseHBaseEntity implements JSONString {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1508169393090681111L;
//	/**
//	 * 所属航班
//	 */
//	@ManyToOne
//	private AbstractPlaneInfoEntity planeInfoEntity;
	/**
	 * 外键，去程的rowkey
	 */
	@HBaseColumn(qualifier = "sourceForeignKey", family = "cabinsInfo")
	private  String sourceForeignKey ;
	
	@HBaseColumn(qualifier = "fromCity", family = "cabinsInfo")
	private String fromCity;
	
	@HBaseColumn(qualifier = "toCity", family = "cabinsInfo")
	private String toCity;
	
	@HBaseColumn(qualifier = "startTime", format = "yyyyMMddHHmmss", family = "cabinsInfo")
	private Date startTime;
	
	@HBaseColumn(qualifier = "endTime", format = "yyyyMMddHHmmss", family = "cabinsInfo")
	private Date endTime;
	
	
	/**
	 * 航班号，如： MU3540
	 */
	@HBaseColumn(qualifier = "flightNo", family = "cabinsInfo")
	private String flightNo;
	
	/**
	 * 是否直达，0：直达，1：中转，2：经停
	 */
	@HBaseColumn(qualifier = "isNonStop", family = "cabinsInfo")
	private Integer isNonStop;
	
	

	/**
	 * 裸价
	 */
	@HBaseColumn(qualifier = "price", family = "cabinsInfo")
	private Double price;

	
	/**
	 * 优惠券信息
	 */
	@HBaseColumn(qualifier = "coupons", family = "cabinsInfo",forceSave=true)
	private Set<String> coupons = new HashSet<String>();

	
	
	/**
	 * 是否为套餐
	 */
	@Transient
	private Boolean isTariffPackages = false;
	
	/**
	 * 套餐描述
	 */
	@Transient
	private String tariffPackageDesc;
	
	@Override
	public String generateRowKey() {
//		return planeInfoEntity.generateRowKey();
		return null;
	}



	public String getSourceForeignKey() {
		return sourceForeignKey;
	}

	public void setSourceForeignKey(String sourceForeignKey) {
		this.sourceForeignKey = sourceForeignKey;
	}

//	public String getIndex() {
//		return index;
//	}
//
//	public void setIndex(String index) {
//		this.index = index;
//	}

//	public String getIndexGrab() {
//		return indexGrab;
//	}
//
//	public void setIndexGrab(String indexGrab) {
//		this.indexGrab = indexGrab;
//	}
//
//	public String getIndexCarrier() {
//		return indexCarrier;
//	}
//
//	public void setIndexCarrier(String indexCarrier) {
//		this.indexCarrier = indexCarrier;
//	}
//
//	public String getIndexFlight() {
//		return indexFlight;
//	}
//
//	public void setIndexFlight(String indexFlight) {
//		this.indexFlight = indexFlight;
//	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}



	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}


	public Set<String> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<String> coupons) {
		this.coupons = coupons;
	}

	

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public Integer getIsNonStop() {
		return isNonStop;
	}

	public void setIsNonStop(Integer isNonStop) {
		this.isNonStop = isNonStop;
	}

	
	public Boolean getIsTariffPackages() {
		return isTariffPackages;
	}

	public void setIsTariffPackages(Boolean isTariffPackages) {
		this.isTariffPackages = isTariffPackages;
	}

	public String getTariffPackageDesc() {
		return tariffPackageDesc;
	}

	public void setTariffPackageDesc(String tariffPackageDesc) {
		this.tariffPackageDesc = tariffPackageDesc;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@Override
	public String toJSONString() {
		
		return super.convertJson();
	}

	@Override
	public String setSaltBuckets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setTimeToLive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setTimeStamp(String trueTtl) {
		// TODO Auto-generated method stub
		return null;
	}

}
