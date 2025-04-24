package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.TendereeInformMapper;
import com.ztbdz.tenderee.pojo.TendereeInform;
import com.ztbdz.tenderee.service.TendereeInformService;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TendereeInformServiceImpl implements TendereeInformService {
    @Autowired
    private TendereeInformMapper tendereeInformMapper;


    @Override
    public List<TendereeInform> selectListByTendereeId(Long tendereeId) throws Exception {
        QueryWrapper<TendereeInform> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("type");
        queryWrapper.eq("tenderee_id", tendereeId.toString());
        return tendereeInformMapper.selectList(queryWrapper);
    }

    @Override
    public Result create(TendereeInform tendereeInform) {
        try{
            if(this.insert(tendereeInform)<=0) return Result.fail("发布失败！");
            return Result.ok("发布成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("发布招标公告异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer insert(TendereeInform tendereeInform) throws Exception {
        return tendereeInformMapper.insert(tendereeInform);
    }

    @Override
    public Result update(TendereeInform tendereeInform) {
        try{
            if(this.updateById(tendereeInform)<=0) return Result.fail("更新失败！");
            return Result.ok("更新成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新招标公告异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer updateById(TendereeInform tendereeInform) throws Exception {
        return tendereeInformMapper.updateById(tendereeInform);
    }

}
