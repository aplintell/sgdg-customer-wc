package com.pns.sgdg.model;

import java.util.List;

public class SearchPagingResult {
	private List<?> searchList;
	private int totalRecord;
	private int totalPage;

	public SearchPagingResult(List<?> searchList, int totalRecord, int totalPage) {
		super();
		this.searchList = searchList;
		this.totalRecord = totalRecord;
		this.totalPage = totalPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<?> getSearchList() {
		return searchList;
	}

	public void setSearchList(List<?> searchList) {
		this.searchList = searchList;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

}
