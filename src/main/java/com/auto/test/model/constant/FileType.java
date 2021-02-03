package com.auto.test.model.constant;

public enum FileType {
  JSON("json"), EXCEL("excel"),XLSX("xlsx");
  public String value;
  
  FileType(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
}
