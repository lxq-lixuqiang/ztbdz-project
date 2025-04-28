package com.ztbdz.web.util;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("树形节点")
@Data
public class TreeNode<T> {

    @ApiModelProperty("节点id")
    private String id;
    @ApiModelProperty("节点名称")
    private String name;
    @ApiModelProperty("节点关联的数据")
    private T data;
    @ApiModelProperty("父节点id")
    private String parentId;
    @ApiModelProperty("子节点列表")
    private List<TreeNode<T>> children = new ArrayList();;


    public TreeNode(String id,String name, T data, String parentId) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.parentId = parentId;
    }

    public void addChild(TreeNode<T> child) {
        children.add(child);
    }
}
