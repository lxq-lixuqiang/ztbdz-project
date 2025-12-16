package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.RetrievePasswordMapper;
import com.ztbdz.user.pojo.RetrievePassword;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.RetrievePasswordService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.web.util.MD5;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Service
public class RetrievePasswordServiceImpl implements RetrievePasswordService {

    @Autowired
    private RetrievePasswordMapper retrievePasswordMapper;
    @Autowired
    private UserService userService;

    @Override
    public Integer insert(RetrievePassword retrievePassword) throws Exception {
        return retrievePasswordMapper.insert(retrievePassword);
    }

    @Override
    public Integer delete(Long id) throws Exception {
        return retrievePasswordMapper.deleteById(id);
    }

    @Override
    public Integer updateById(RetrievePassword retrievePassword) throws Exception {
        return retrievePasswordMapper.updateById(retrievePassword);
    }

    @Override
    public RetrievePassword getById(Long id) throws Exception {
        return retrievePasswordMapper.selectById(id);
    }

    @Override
    public List<RetrievePassword> selectList(RetrievePassword retrievePassword) throws Exception {
        QueryWrapper<RetrievePassword> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("is_pass");
        queryWrapper.orderByDesc("create_date");
        if(!StringUtils.isEmpty(retrievePassword.getUsername())) queryWrapper.like("username", retrievePassword.getUsername());
        if(!StringUtils.isEmpty(retrievePassword.getIsPass())) queryWrapper.like("is_pass", retrievePassword.getIsPass());
        return retrievePasswordMapper.selectList(queryWrapper);
    }

    @Override
    public PageInfo<RetrievePassword> selectPage(Integer page, Integer size, RetrievePassword retrievePassword) throws Exception {
        PageHelper.startPage(page, size);
        return new PageInfo(this.selectList(retrievePassword));
    }

    @Override
    public Result page(Integer page, Integer size, RetrievePassword retrievePassword) {
        try{
            return Result.ok("分页查询成功！",this.selectPage(page,size,retrievePassword));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("分页查询异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result createORupdate(RetrievePassword retrievePassword) {
        try{
            Integer num=0;
            if(!StringUtils.isEmpty(retrievePassword.getId())){
                //更新
                num = this.updateById(retrievePassword);
                if(num<=0){
                    return Result.error("更新失败！");
                }
                return Result.ok("更新成功！");
            }
            //新增
            User user = new User();
            user.setUsername(retrievePassword.getUsername());
            Integer getNum = userService.count(user);
            if(getNum<=0){
                return Result.error("“"+retrievePassword.getUsername()+"”用户名未查询到登录信息，请核对用户名是否正确！");
            }
            retrievePassword.setIsPass(0);
            List<RetrievePassword> retrievePasswordList = this.selectList(retrievePassword);
            if(retrievePasswordList.size()>0){
                return Result.error("“"+retrievePassword.getUsername()+"”用户名已进行找回密码信息审核中，请勿重复提交！");
            }
            retrievePassword.setNewPassword(MD5.md5String(retrievePassword.getNewPassword()));
            num = this.insert(retrievePassword);
            if(num<=0){
                return Result.error("新增失败！");
            }
            return Result.ok("新增成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("新增或更新异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result selectById(Long id) {
        try{
            return Result.ok("查询成功！",this.getById(id));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result review(RetrievePassword retrievePassword) {
        try{
            if(retrievePassword.getIsPass()==0){
                //审核中 ，撤回审核
                RetrievePassword newRetrievePassword = this.getById(retrievePassword.getId());
                if(newRetrievePassword.getIsPass()==1){ // 上一次是审核不通过，撤回审核
                    User user = userService.getById(retrievePassword.getUser().getId());
                    user.setPassword(newRetrievePassword.getOldPassword());
                    Integer num = userService.updateById(user);
                    if(num<=0){
                        return Result.error("更新登录信息失败！");
                    }
                }
            }else if(retrievePassword.getIsPass()==1){
                RetrievePassword newRetrievePassword = this.getById(retrievePassword.getId());
                User user = userService.getById(retrievePassword.getUser().getId());
                retrievePassword.setOldPassword(user.getPassword());
                user.setPassword(newRetrievePassword.getNewPassword());
                Integer num = userService.updateById(user);
                if(num<=0){
                    return Result.error("更新登录信息失败");
                }
            }
            Integer num = this.updateById(retrievePassword);
            if(num<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error("更新找回密码信息失败！");
            }
            return Result.ok("审核成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("审核异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result selectByUsername(String username) {
        try{
            RetrievePassword retrievePassword = new RetrievePassword();
            retrievePassword.setUsername(username);
            List<RetrievePassword> retrievePasswordList = this.selectList(retrievePassword);
            if(retrievePasswordList.size()<=0){
                return Result.ok("“"+username+"” 用户名未查询到审核信息!");
            }
            retrievePassword = retrievePasswordList.get(0);
            if(retrievePassword.getIsPass()== 0){

                return Result.ok("正在审核中，提交时间："+new SimpleDateFormat("yyyy年MM月dd日 HH:mm分").format(retrievePassword.getCreateDate()));
            }else if(retrievePassword.getIsPass()== 1){
                return Result.ok("审核通过，请用新密码进行登录！");
            }
            return Result.ok("审核不通过原因："+retrievePassword.getNotPassContent());
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("审核查询异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result selectBidderInfo(Long id) {
        try{
            return Result.ok("查询成功！",retrievePasswordMapper.selectBidderInfo(id));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询异常，原因："+e.getMessage());
        }
    }
}
