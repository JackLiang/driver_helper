package com.dh.vo.message;

public class MessageCriteria {

	/**
	 * 查询区域
	 */
	private String address;
	
	/**
	 * 兴趣偏好（二进制：1<<1警察1<<2拥堵1<<3车祸1<<4封路1<<5施工）
	 */
	private int interest;
	
	/**
	 * 限制条数
	 */
	private int limit;
	
	
	private String location;
	
	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getInterest() {
		return interest;
	}

	public void setInterest(int interest) {
		this.interest = interest;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	
}
