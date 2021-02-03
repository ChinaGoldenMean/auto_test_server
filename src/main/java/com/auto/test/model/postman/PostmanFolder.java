package com.auto.test.model.postman;

import lombok.Data;

import java.util.List;

@Data
public class PostmanFolder {
	public String name;
	public String description;
	public List<PostmanItem> item;
}
