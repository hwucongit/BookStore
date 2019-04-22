package com.teamand.bookstore.model;

public class User {
	private int id;
	private Account account;
	private Address address;
	private String email;
	private String role;	
	private String name;
	
	public User() {
	}
	public User(Builder builder){
		this.id = builder.id;
		this.account = builder.account;
		this.address = builder.address;
		this.email = builder.email;
		this.role = builder.role;
		this.name = builder.name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public static class Builder {
		private int id;
		private Account account;
		private Address address;
		private String email;
		private String role;
		private String name;

		public Builder(Account account, String name){
			this.account = account;
			this.name = name;
		}
		public Builder email(String email){
			this.email = email;
			return this;
		}
		public Builder address(Address address){
			this.address = address;
			return this;
		}
		public Builder uid(int id){
			this.id = id;
			return this;
		}
		public Builder role(String role){
			this.role = role;
			return this;
		}
		public User build(){
			User user =new  User(this);
			validateUserObject(user);
			return user;
		}
		public void validateUserObject(User user){

		}
	}
				
}
