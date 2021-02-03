package com.auto.test.model.postman;

import lombok.Data;

import java.util.List;

@Data
public class PostmanBody {
	public String mode;
	public String raw;
	public List<PostmanUrlEncoded> urlencoded;
	public List<PostmanFormData> formdata;
}
