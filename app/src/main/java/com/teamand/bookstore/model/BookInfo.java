package com.teamand.bookstore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class BookInfo {
	@SerializedName("author")
	@Expose
	private Author author;
	@SerializedName("publisher")
	@Expose
	private Publisher publisher;
	@SerializedName("category")
	@Expose
	private List<Category> category;
	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("price")
	@Expose
	private int price;
	@SerializedName("discount")
	@Expose
	private int discount;
	@SerializedName("imgUrl")
	@Expose
	private String imgUrl;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("averageRating")
	@Expose
	private double averageRating;
	@SerializedName("availableQuantity")
	@Expose
	private int availableQuantity;
    @SerializedName("quantity")
    @Expose
	private int quantity;
	
	public int getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BookInfo()
	{
		category = new ArrayList<Category>();
	}
	
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	public List<Category> getCategory() {
		return category;
	}
	public void setCategory(List<Category> category) {
		this.category = category;
	}
	public void addCategory(Category category){
		this.category.add(category);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
