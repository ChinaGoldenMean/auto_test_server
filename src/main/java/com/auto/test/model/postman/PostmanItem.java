package com.auto.test.model.postman;

import lombok.Data;

import java.util.List;

@Data
public class PostmanItem {
	public String name;
	public List<PostmanEvent> event;
	public PostmanRequest request;
	public List<PostmanItem> item;
}
