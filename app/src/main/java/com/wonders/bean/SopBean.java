package com.wonders.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SopBean {
	@SerializedName(value = "id",alternate = {"dicId"})
	private String id;
	@SerializedName(value = "name",alternate = {"dicName"})
	private String name;
	@SerializedName(value = "items",alternate = {"dicLtChekItems"})
	private ArrayList<SopItemModel> items;
	@SerializedName(value = "tradeType",alternate = {})
	private String tradeType;

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<SopItemModel> getItems() {
		return items;
	}

	public void setItems(ArrayList<SopItemModel> items) {
		this.items = items;
	}

}
