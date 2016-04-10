package com.dh.common;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 分页展示内容
 * 
 * @author ying_liu 2015年04月20日
 */
public class Pagination<T> implements Serializable {

	private static final long serialVersionUID = 6925016030680603630L;

	/**
	 * 返回对象
	 */
	private List<T> results;

	/**
	 * 每页记录数，默认为10
	 */
	private int pageSize = 10;

	/**
	 * 当前页，默认第一页
	 */
	private int currentPage = 1;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 总记录数
	 */
	private int count;
	
	/**
	 * 分页链接
	 */
	private String linkUrl;

	public Pagination(int pageSize, int currentPage, int count, List<T> results) {
		if (pageSize < 0 || currentPage <= 0 || count < 0) {
			throw new IllegalArgumentException("分页参数不合法。");
		}

		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.count = count;
		this.totalPage = getTotalPage(pageSize, count);
		this.results = results;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	/**
	 * 获取总页数
	 * 
	 * @param pageSize
	 *            每页记录数
	 * @param count
	 *            当前页
	 * @return
	 */
	private int getTotalPage(int pageSize, int count) {
		int totalPage = 0;

		if (count % pageSize == 0)
			totalPage = count / pageSize;
		else
			totalPage = count / pageSize + 1;

		return totalPage;
	}

	
	/**
	 * @return the linkUrl
	 */
	public String getLinkUrl() {
		return linkUrl;
	}

	/**
	 * @param linkUrl the linkUrl to set
	 */
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
