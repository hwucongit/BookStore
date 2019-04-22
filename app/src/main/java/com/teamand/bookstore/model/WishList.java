package com.teamand.bookstore.model;

import java.util.List;

public class WishList {
	private int id;
	private List<BookInfo> listBook;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<BookInfo> getListBook() {
		return listBook;
	}
	public void setListBook(List<BookInfo> listBook) {
		this.listBook = listBook;
	}
	
	
	
}
