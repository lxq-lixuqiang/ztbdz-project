package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.CategoryMapper;
import com.ztbdz.tenderee.pojo.Category;
import com.ztbdz.tenderee.service.CategoryService;
import com.ztbdz.web.util.Result;
import com.ztbdz.web.util.TreeNode;
import com.ztbdz.web.util.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public Integer insert(Category category) throws Exception {
        category.setCategoryPath(this.getPath(category));
        if(StringUtils.isEmpty(category.getParentId())) category.setParentId(-1L);
        return categoryMapper.insert(category);
    }

    public String getPath(Category category) throws Exception{
        String pathString;
        Category category1 = new Category();
        if(StringUtils.isEmpty(category.getParentId())){
            category1.setParentId(-1L);
            List<Category> categoryList = this.selectList(category1);
            pathString = "000";
            if(categoryList.size()<2){
                pathString="00"+categoryList.size();
            }else if(categoryList.size()<3){
                pathString="0"+categoryList.size();
            }else if(categoryList.size()>=3){
               throw new Exception("分类路径不能超过3位数！");
            }
        }else{
            Category parentCategory = this.selectById(category.getParentId());

            category1.setParentId(category.getParentId());
            List<Category> categoryList = this.selectList(category1);
            if(categoryList.size()>0){
                Integer newPath = Integer.valueOf(categoryList.get(categoryList.size()-1).getCategoryPath());
                pathString = String.valueOf(++newPath);
                if(pathString.length()<2){
                    pathString=parentCategory.getCategoryPath()+"00"+pathString;
                }else if(pathString.length()<3){
                    pathString=parentCategory.getCategoryPath()+"0"+pathString;
                }
            }else{
                pathString=parentCategory.getCategoryPath()+"001";
            }
        }
        return pathString;
    }

    @Override
    public Category selectById(Long id) throws Exception {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<Category> selectByIds(List<Long> ids) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", ids);
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<Category> selectList(Category category) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("category_sort");
        if(!StringUtils.isEmpty(category.getId())) queryWrapper.eq("id", category.getId());
        if(!StringUtils.isEmpty(category.getCategoryClassify())) queryWrapper.eq("category_classify", category.getCategoryClassify());
        if(!StringUtils.isEmpty(category.getCategoryName())) queryWrapper.like("category_name", category.getCategoryName());
        if(!StringUtils.isEmpty(category.getCategoryPath())) queryWrapper.likeRight("category_path", category.getCategoryPath());
        if(!StringUtils.isEmpty(category.getCategoryCode())) queryWrapper.like("category_code", category.getCategoryCode());
        if(!StringUtils.isEmpty(category.getParentId())) queryWrapper.eq("parent_id", category.getParentId());
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public Integer countByTypeCode(String id, String typeCode) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(id)) queryWrapper.ne("id", id);
        if(!StringUtils.isEmpty(typeCode)) queryWrapper.eq("category_code", typeCode);
        return categoryMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer deletes(List<Long> ids) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return categoryMapper.delete(queryWrapper);
    }

    @Override
    public Integer updateById(Category category) throws Exception {
        return categoryMapper.updateById(category);
    }

    @Override
    public PageInfo<Category> selectPage(Integer page, Integer size, Category category) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("update_date");

        if(!StringUtils.isEmpty(category.getCategoryClassify())) queryWrapper.eq("type_classify", category.getCategoryClassify());
        if(!StringUtils.isEmpty(category.getParentId())) queryWrapper.eq("param_id", category.getParentId());
        if(!StringUtils.isEmpty(category.getCategoryName())) queryWrapper.eq("category_name", category.getCategoryName());
        if(!StringUtils.isEmpty(category.getCategoryPath())) queryWrapper.eq("category_path", category.getCategoryPath());
        if(!StringUtils.isEmpty(category.getCategoryCode())) queryWrapper.eq("category_code", category.getCategoryCode());
        return new PageInfo(categoryMapper.selectList(queryWrapper));
    }

    @Override
    public Result getTreeNode(String categoryClassify) {
        try{
            List<TreeNode<Category>> items = new ArrayList();
            Category category = new Category();
            category.setCategoryClassify(categoryClassify);
            List<Category> categoryList = selectList(category);
            for(Category category1 : categoryList){
                TreeNode<Category> treeNodes = new TreeNode<Category>(category1.getId().toString(),category1.getCategoryName(), category1, category1.getParentId()==-1L?null:category1.getParentId().toString());
                items.add(treeNodes);
            }
            List<TreeNode<Category>> roots = TreeUtil.buildTree(items);
            return Result.ok("查询成功！",roots);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询类别树形图列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result get(Long id) {
        try{
            return Result.ok("查询成功", this.selectById(id));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询类型信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result page(Integer page, Integer size, Category category) {
        try{
            PageInfo<Category> typeInfoPageInfo =  selectPage(page,size, category);
            return Result.ok("查询成功！",typeInfoPageInfo);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询类型信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result create(Category category) {
        try{
            // 类型代码不能一样
            if(this.countByTypeCode(null, category.getCategoryCode())>0) return Result.fail("类型代码不能重复！");

            Integer num = this.insert(category);
            if(num<=0) return Result.fail("创建失败！");
            return Result.ok("创建成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("创建类型信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result update(Category category) {
        try{
            // 标识和名称不能一样
            if(this.countByTypeCode(category.getId().toString(), category.getCategoryCode())>0) return Result.fail("类型代码不能重复！");

            Integer num = this.updateById(category);
            if(num<=0) return Result.fail("更新失败！");
            return Result.ok("更新成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新类型信息异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteList(List<Long> ids) {
        try{
            Category category;
            for(Long id : ids){
                category =  this.selectById(id);
                if(category.getCategoryPath().length()==3) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("初始类型被不能删除！");
                }
                // 防止在使用中的类型被删除
                if(category.getIsUse()==1){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("类型被使用中，不能删除！");
                }
                // 类型下的子类型都删除
                Category category2 = new Category();
                category2.setCategoryPath(category.getCategoryPath());
                List<Category> categoryList = this.selectList(category2);
                List<Long> zids = new ArrayList();
                for(Category category1 : categoryList){
                    if(category1.getIsUse()==1){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return Result.fail("类型下的子类型被使用中，不能删除！");
                    }
                    zids.add(category1.getId());
                }
                if(this.deletes(zids)<=0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("类型下的子类型删除失败！");
                }
            }
            if(this.deletes(ids)<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.fail("删除失败！");
            }
            return Result.ok("删除成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("删除类型信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result classify() {
        try{
            return Result.ok("查询成功！",this.getGroupByClassify());
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询分类异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<Category> getGroupByClassify() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.groupBy("category_classify");
        return categoryMapper.selectList(queryWrapper);
    }
}
