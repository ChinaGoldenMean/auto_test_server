package com.auto.test.model.bo.base;

import com.auto.test.k8s.dto.SearchParamDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "分页数据")
public class Page<T> implements Serializable {
  
  private static final long serialVersionUID = -7770484662988510394L;
  @ApiModelProperty(value = "总页数")
  private Long countPage; //d
  @ApiModelProperty(value = "当前页")
  private Long currentPage;
  @ApiModelProperty(value = "总条数")
  private Long totalItem;
  @ApiModelProperty(value = "当前页行数")
  private Long itemsPerPage;
  //@ApiModelProperty(value = "状态")
  // private Status status = null;
  @ApiModelProperty(value = "返回集合数据")
  private List<T> data;
  @ApiModelProperty(value = "返回集合数据")
  private T obj;
  
  public Page(IPage iPage) {
    this.data = iPage.getRecords();
    this.totalItem = iPage.getTotal();
    this.currentPage = iPage.getCurrent();
    this.itemsPerPage = iPage.getSize();
    this.countPage = iPage.getPages();
  }
  
  public Page(SearchParamDTO vo, List<T> data, Long totalItemVo, Long itemsPerPageVo) {
    this.data = data;
    this.totalItem = totalItemVo;
    this.currentPage = vo.getCurrentPage();
    this.itemsPerPage = itemsPerPageVo;
    this.countPage = (totalItemVo + vo.getItemsPerPage() - 1)
        / vo.getItemsPerPage();
  }
  
  public Page(SearchParamDTO vo, T data, Long totalItemVo, Long itemsPerPageVo) {
    this.obj = data;
    this.totalItem = totalItemVo;
    this.currentPage = vo.getCurrentPage();
    this.itemsPerPage = itemsPerPageVo;
    this.countPage = (totalItemVo + vo.getItemsPerPage() - 1)
        / vo.getItemsPerPage();
  }
  
  public Page() {
  
  }
  
}
