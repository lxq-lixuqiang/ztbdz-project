package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.TypeInfoMapper;
import com.ztbdz.tenderee.pojo.Category;
import com.ztbdz.tenderee.service.TypeInfoService;
import com.ztbdz.web.util.Result;
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
public class TypeInfoServiceImpl implements TypeInfoService {

    @Autowired
    private TypeInfoMapper typeInfoMapper;


    @Override
    public Integer insert(Category category) throws Exception {
        return typeInfoMapper.insert(category);
    }

    @Override
    public Category selectById(Long id) throws Exception {
        return typeInfoMapper.selectById(id);
    }

    @Override
    public List<Category> selectByIds(List<Long> ids) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", ids);
        return typeInfoMapper.selectList(queryWrapper);
    }

    @Override
    public List<Category> selectList(Category category) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(category.getId())) queryWrapper.eq("id", category.getId());
        if(!StringUtils.isEmpty(category.getCategoryClassify())) queryWrapper.eq("category_classify", category.getCategoryClassify());
        if(!StringUtils.isEmpty(category.getParamId())) queryWrapper.eq("param_id", category.getParamId());
        if(!StringUtils.isEmpty(category.getCategoryName())) queryWrapper.like("category_name", category.getCategoryName());
        if(!StringUtils.isEmpty(category.getTypePath())) queryWrapper.likeRight("category_path", category.getTypePath());
        if(!StringUtils.isEmpty(category.getCategoryCode())) queryWrapper.like("category_code", category.getCategoryCode());
        return typeInfoMapper.selectList(queryWrapper);
    }

    @Override
    public Integer countByTypeCode(String id, String typeCode) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(id)) queryWrapper.ne("id", id);
        if(!StringUtils.isEmpty(typeCode)) queryWrapper.eq("type_code", typeCode);
        return typeInfoMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer deletes(List<Long> ids) throws Exception {
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return typeInfoMapper.delete(queryWrapper);
    }

    @Override
    public Integer updateById(Category category) throws Exception {
        return typeInfoMapper.updateById(category);
    }

    @Override
    public PageInfo<Category> selectList(Integer page, Integer size, Category category) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("update_date");

        if(!StringUtils.isEmpty(category.getCategoryClassify())) queryWrapper.eq("type_classify", category.getCategoryClassify());
        if(!StringUtils.isEmpty(category.getParamId())) queryWrapper.eq("param_id", category.getParamId());
        if(!StringUtils.isEmpty(category.getCategoryName())) queryWrapper.eq("category_name", category.getCategoryName());
        if(!StringUtils.isEmpty(category.getTypePath())) queryWrapper.eq("category_path", category.getTypePath());
        if(!StringUtils.isEmpty(category.getCategoryCode())) queryWrapper.eq("category_code", category.getCategoryCode());
        return new PageInfo(typeInfoMapper.selectList(queryWrapper));
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
    public Result list(Integer page, Integer size, Category category) {
        try{
            PageInfo<Category> typeInfoPageInfo =  selectList(page,size, category);
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
                // 防止在使用中的类型被删除
                if(category.getIsUse()==1){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("类型被使用中，不能删除！");
                }
                // 类型下的子类型都删除
                Category category2 = new Category();
                category2.setTypePath(category.getTypePath());
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
}
