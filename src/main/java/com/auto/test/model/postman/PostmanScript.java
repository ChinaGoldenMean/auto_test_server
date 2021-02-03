package com.auto.test.model.postman;

import lombok.Data;

import java.util.List;

@Data
public class PostmanScript {
	public String id;
	public String type;
	public List<String> exec;
}
