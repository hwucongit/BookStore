package com.teamand.bookstore.model;

public class Client extends User{
	private WishList wishList;

	public WishList getWishList() {
		return wishList;
	}

	public void setWishList(WishList wishList) {
		this.wishList = wishList;
	}
	
	
}
