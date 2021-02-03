package com.auto.test.model.postman;

import lombok.Data;

@Data
public class PostmanUrl {
	public String raw;
	 public String[] host;
	public String[] path;
	public String getPathStr(){
		String[] paths = this.path;
		String pathStr="";
		if(paths!=null&&paths.length>0){
			for(int i=0;i<paths.length;i++){
				String path = paths[i];
				pathStr += "/"+ path;
				
			}
		}
		return pathStr;
	}
}
