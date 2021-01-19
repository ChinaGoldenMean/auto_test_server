package com.auto.test.k8s.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.kubernetes.client.openapi.models.V1PodList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "容器组信息")
public class PodInfo extends Status {
  @ApiModelProperty(value = "当前数")
  private Integer current = 0;
  @ApiModelProperty(value = "预期数")
  private Integer desired = 0;
  @ApiModelProperty(value = "警告数")
  private Set<Event> warnings = new HashSet<>();
  
  public PodInfo jobListInitPodInfo(List<Job> jobList) {
    if (CollectionUtils.isNotEmpty(jobList)) {
      jobList.stream().forEach(job -> {
        PodInfo podInfo = job.getPodInfo();
        this.current += podInfo.current;
        this.desired += podInfo.desired;
        this.pending += podInfo.pending;
        this.running += podInfo.running;
        this.failed += podInfo.failed;
        this.succeeded += podInfo.succeeded;
      });
    }
    return this;
  }
  
  public PodInfo() {
  
  }
  
  public PodInfo(V1PodList v1PodList, Set<Event> eventList) {
    super(v1PodList);
    if (CollectionUtils.isNotEmpty(v1PodList.getItems())) {
      this.current = running;
      this.desired = v1PodList.getItems().size();
      this.warnings = eventList;
    }
  }
  
  public PodInfo(V1PodList v1PodList, Set<Event> eventList, Integer replicas) {
    super(v1PodList);
    if (CollectionUtils.isNotEmpty(v1PodList.getItems())) {
      this.current = running;
      this.desired = replicas;
      this.warnings = eventList;
    }
  }
}
