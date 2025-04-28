package com.ztbdz.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析树形图工具类
 */
public class TreeUtil {

    /**
     * 将平铺的对象列表转换为树形结构
     * @param items 所有节点列表
     * @return 根节点列表（森林）
     */
    public static <T> List<TreeNode<T>> buildTree(List<TreeNode<T>> items) {
        Map<String, TreeNode<T>> nodeMap = new HashMap();
        List<TreeNode<T>> roots = new ArrayList();

        // 第一次遍历：将所有节点存入Map，并找到根节点
        for (TreeNode<T> node : items) {
            nodeMap.put(node.getId(), node);
            if (node.getParentId() == null) {
                roots.add(node);
            }
        }

        // 第二次遍历：建立父子关系
        for (TreeNode<T> node : items) {
            if (node.getParentId() != null) {
                TreeNode<T> parent = nodeMap.get(node.getParentId());
                if (parent != null) {
                    parent.addChild(node);
                }
            }
        }

        return roots;
    }
}
