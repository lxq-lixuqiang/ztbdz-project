package com.ztbdz.tenderee.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.CategoryMapper;
import com.ztbdz.tenderee.pojo.Category;
import com.ztbdz.tenderee.service.CategoryService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.Result;
import com.ztbdz.web.util.TreeNode;
import com.ztbdz.web.util.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@CacheConfig(cacheNames = "category")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @CacheEvict(cacheNames = "category",allEntries = true)
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
            if(categoryList.size()<10){
                pathString="00"+categoryList.size();
            }else if(categoryList.size()<100){
                pathString="0"+categoryList.size();
            }else if(categoryList.size()>=100){
                pathString=""+categoryList.size();
            }
        }else{
            Category parentCategory = this.selectById(category.getParentId());

            category1.setParentId(category.getParentId());
            List<Category> categoryList = this.selectList(category1);
            if(categoryList.size()>0){
                Integer newPath = Integer.valueOf(categoryList.get(categoryList.size()-1).getCategoryPath());
                pathString = String.valueOf(++newPath);
                if(pathString.length()<10){
                    pathString=parentCategory.getCategoryPath()+"00"+(categoryList.size()+1);
                }else if(pathString.length()<100){
                    pathString=parentCategory.getCategoryPath()+"0"+(categoryList.size()+1);
                }else if(pathString.length()>=100){
                    pathString=parentCategory.getCategoryPath()+""+(categoryList.size()+1);
                }
            }else{
                pathString=parentCategory.getCategoryPath()+"001";
            }
        }
        return pathString;
    }

    @Cacheable
    @Override
    public Category selectById(Long id) throws Exception {
        return categoryMapper.selectById(id);
    }

    @Cacheable
    @Override
    public List<Category> selectByIds(List<Long> ids) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", ids);
        return categoryMapper.selectList(queryWrapper);
    }

    @Cacheable
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

    @Cacheable
    @Override
    public Integer countByTypeCode(String id, String typeCode) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(id)) queryWrapper.ne("id", id);
        if(!StringUtils.isEmpty(typeCode)) queryWrapper.eq("category_code", typeCode);
        return categoryMapper.selectCount(queryWrapper);
    }

    @CacheEvict(cacheNames = "category",allEntries = true)
    @Override
    public Integer deletes(List<Long> ids) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return categoryMapper.delete(queryWrapper);
    }

    @CacheEvict(cacheNames = "category",allEntries = true)
    @Override
    public Integer updateById(Category category) throws Exception {
        return categoryMapper.updateById(category);
    }

    @Cacheable
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result uploadExcel(MultipartFile file) {
        try{
            if(file.getOriginalFilename().indexOf("xls")<0) return Result.fail("文件类型错误，请上传Excel文件的xls，xlsx");

            String[] fields = new String[]{"类别名称","类别分类","类别代码","父ID"};
            List<Map<String,String>> dataList = SystemConfig.importExcelData(file,fields);
            for(Map<String,String> dataMap : dataList){
                String message = dataMap.get(fields[0]);
                Category category = new Category();
                category.setCategoryName(message);
                if(StringUtils.isEmpty(dataMap.get(fields[1]))) return Result.fail("解析'"+fields[0]+"'为【"+message+"】创建失败，原因：类别分类不能为空！");
                category.setCategoryClassify(dataMap.get(fields[1]));
                if(StringUtils.isEmpty(dataMap.get(fields[2]))) return Result.fail("解析'"+fields[0]+"'为【"+message+"】创建失败，原因：类别代码不能为空！");
                category.setCategoryCode(dataMap.get(fields[2]));
                if(!StringUtils.isEmpty(dataMap.get(fields[3]))) category.setParentId(Long.valueOf(dataMap.get(fields[3])));
                category.setIsUse(1);
                Result result = this.create(category);
                if(result.getStatus()!=200){
                    log.error("解析'"+fields[0]+"'为【"+message+"】创建失败，原因："+result.getMessage()+",具体参数："+JSON.toJSONString(dataMap));
                    return Result.fail("解析'"+fields[0]+"'为【"+message+"】创建失败，原因："+result.getMessage());
                }
            }
            return Result.ok("解析Excel类别文件成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("解析Excel类别文件异常，原因："+e.getMessage());
        }
    }
}
