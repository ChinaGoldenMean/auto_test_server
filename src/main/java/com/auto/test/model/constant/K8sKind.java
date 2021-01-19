package com.auto.test.model.constant;

public enum K8sKind {
  CRON_JOB("CronJob"), NAMESPACE("Namespace");
  public String value;
  
  K8sKind(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
}
