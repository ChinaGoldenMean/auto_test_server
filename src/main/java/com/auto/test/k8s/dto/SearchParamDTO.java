package com.auto.test.k8s.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
@ApiModel(value = "SearchParamDTO", description = "命名空间列表查询参数")
public class SearchParamDTO implements Serializable {
  private static final long serialVersionUID = 3886609240081575338L;
  @ApiModelProperty(value = "每页的数量")
  private Long itemsPerPage = 10L;
  
  @ApiModelProperty(value = "当前页")
  private Long currentPage = 1L;
  
  @ApiModelProperty(value = "关键词搜索")
  private String filterBy;
  //  @ApiModelProperty(value = "排序关键字 a(升序) d(降序)  name (名称) createTimeStamp (创建时间) 默认(d,createTimeStamp)")
//  private String sortBy = "d,createTimeStamp";
  @ApiModelProperty(value = "排序关键字  column 只有 name (名称) createTimeStamp (创建时间) 两个值.")
  private OrderItem order;
  @ApiModelProperty(value = "模块id集合,指定查询这些集合中的模块内容")
  private List<String> modelNames;
  @ApiModelProperty(hidden = true)
  private Long skip;
  
  public Long getSkip() {
    return (this.getCurrentPage() <= 0 ? 0 : this.getCurrentPage() - 1) * this.getItemsPerPage();
  }
  
  public void validateParams() {
    String comma = ",";
    if (this.currentPage == null || this.currentPage <= 0) {
      throw new NullPointerException("当前页currentPage 参数不能为空,不能小于0");
    }
    if (this.itemsPerPage == null || this.itemsPerPage <= 0) {
      throw new NullPointerException("每页的数量itemsPerPage 参数不能为空,不能小于0");
    }
    
    OrderItem order = this.order;
    if (order == null) {
      throw new NullPointerException("排序关键字orders错误");
    }
    
  }
  
}
