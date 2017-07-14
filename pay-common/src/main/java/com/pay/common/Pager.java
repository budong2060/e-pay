package com.pay.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *                       
 * @Filename Pager.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @Author heyinbo
 *
 * @History
 *<li>Author: heyinbo</li>
 *<li>Date: 2014年9月18日</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class Pager<E> implements Serializable {
	
	/** Comment for <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= 1L;
	
	public enum OrderType {
		asc, desc
	}
	
	public static final Integer	MAX_PAGE_SIZE		= 500;								// 每页最大记录数限制
																						
	public static final Integer	DEFAULT_PAGE_NUM	= 20;								// 默认每页记录数
																						
	private Integer				startIndex			= 1;								//起始位置
																						
	private Integer				pageNum				= 1;								// 当前页码
																						
	private Integer				pageSize			= DEFAULT_PAGE_NUM;				// 每页记录数
																						
	private Long				totalCount			= 0L;								// 总记录数
																						
	private Integer				pageCount			= 0;								// 总页数
																						
	private String				orderBy				= "ID";							//排序字段
																						
	private OrderType			orderType			= OrderType.desc;					//排序方式
																						
	private List<E>				list;													// 数据List
																						
	private Map<String, Object>	params				= new HashMap<String, Object>();
	
	public Pager() {
	}
	
	public Pager(Integer pageNum, Integer pageSize) {
		this(pageNum, pageSize, 0L, null);
	}
	
	public Pager(Integer pageNum, Integer pageSize, Long totalCount, List<E> list) {
		if (pageNum > 1) {
			startIndex = (pageNum - 1) * pageSize;
		}
		if (totalCount > 0L) {
			pageCount = (int) (totalCount / pageSize);
			if (totalCount % pageSize > 0) {
				pageCount++;
			}
		}
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.list = list;
		this.totalCount = totalCount;
	}
	
	public Integer getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	
	public Integer getPageNum() {
		return pageNum;
	}
	
	public void setPageNum(Integer pageNum) {
		if (pageNum < 1) {
			pageNum = 1;
		}
		this.pageNum = pageNum;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		} else if (pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}
	
	public Long getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
		this.getPageCount();
	}
	
	public Integer getPageCount() {
		return pageCount;
	}
	
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}
	
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	public List<E> getList() {
		return list;
	}
	
	public void setList(List<E> list) {
		this.list = list;
	}
	
	/**
	 * 是否含有下一页
	 * @return
	 */
	public boolean hasNextPage() {
		return this.pageNum < this.pageCount - 1;
	}
	
	/**
	 * 是否含有下一页
	 * @return
	 */
	public boolean hasPreviousPage() {
		return this.pageNum > 1;
	}
	
	public Map<String, Object> getParams() {
		params.put("startIndex", this.getStartIndex());
		params.put("pageSize", this.getPageSize());
		params.put("orderBy", this.getOrderBy());
		params.put("orderType", this.getOrderType().toString());
		return params;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
