package com.auto.test.k8s.vo;

import com.auto.test.k8s.model.Job;
import com.auto.test.k8s.model.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1JobStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "任务列表集合")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobVo implements Serializable {
  
  private static final long serialVersionUID = 3886609240081575338L;
  
  @ApiModelProperty(value = "任务集合")
  private List<Job> jobs = new ArrayList<>();
  
  private Status status = new Status();
  
  public JobVo(List<Job> jobs, List<V1Job> v1jobs) {
    this.jobs = jobs;
    setStatus(v1jobs);
  }
  
  private void setStatus(List<V1Job> v1jobs) {
    if (CollectionUtils.isNotEmpty(v1jobs)) {
      Integer running = 0;
      Integer failed = 0;
      for (V1Job v1Job : v1jobs) {
        V1JobStatus jobStatus = v1Job.getStatus();
        if (jobStatus == null) {
          running++;
        } else {
          Integer succeeded = jobStatus.getSucceeded();
          if (succeeded != null && succeeded > 0) {
            running++;
          } else {
            failed++;
          }
        }
        
      }
      this.status.setRunning(running);
      this.status.setFailed(failed);
    }
  }
}
