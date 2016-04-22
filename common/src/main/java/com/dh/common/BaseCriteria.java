/**
 *  
 */
package com.dh.common;


/**
 * 基础查询对象 
 * 
 * haibin_chen 2015年4月28日
 *
 */
public class BaseCriteria {
	
	/**
	 * 每页记录数，默认为10
	 */
	private int pageSize = 10;
	
	/**
	 * 当前页，默认第一页
	 */
	private int currentPage = 1;
	
	/**
	 * 在数据库里的开始记录
	 */
	private int startIndex = (currentPage - 1) * pageSize;

	public BaseCriteria(int pageSize, int currentPage) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.startIndex = (currentPage - 1) * pageSize;
	}
	
	public BaseCriteria(int currentPage) {
		this.currentPage = currentPage;
		this.startIndex = (currentPage - 1) * pageSize;
	}
	
	
	public int getPageSize() {
		return pageSize;
	}

	public BaseCriteria setPageSize(int pageSize) {
		if (pageSize < 1) {
			throw new IllegalArgumentException("每页记录数必须大于0");
		}
		this.pageSize = pageSize;
		setStartIndex();
		return this;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public BaseCriteria setCurrentPage(int currentPage) {
		if (currentPage < 1) {
			throw new IllegalArgumentException("当前页码必须大于0");
		}
		this.currentPage = currentPage;
		setStartIndex();
		return this;
	}
	
	private void setStartIndex() {
		startIndex = (currentPage - 1) * pageSize;
	}
	
	/**
	 * 计算起始记录
	 * @return
	 */
	public int getStartIndex() {
		if (startIndex <= 0) {
			setStartIndex();
		}
		return startIndex;
	}
}
