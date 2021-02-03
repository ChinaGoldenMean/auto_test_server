package com.auto.test.model.postman;

import lombok.Data;

@Data
public class PostmanEvent {
	public String listen;
	public PostmanScript script;
}
