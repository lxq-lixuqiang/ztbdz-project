package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.AccountMapper;
import com.ztbdz.user.pojo.Account;
import com.ztbdz.user.service.AccountService;
import com.ztbdz.user.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Integer insert(Account account) throws Exception {
        return accountMapper.insert(account);
    }

    @Override
    public Integer delete(Long id) throws Exception {
        QueryWrapper<Account> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        return accountMapper.delete(queryWrapper);
    }

    @Override
    public Integer updateById(Account account) throws Exception {
        QueryWrapper<Account> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", account.getId().toString());
        return accountMapper.update(account,queryWrapper);
    }

    @Override
    public Account getById(Long id) throws Exception {
        QueryWrapper<Account> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        return accountMapper.selectOne(queryWrapper);
    }

    @Override
    public PageInfo<Account> selectList(Integer page, Integer size, Account account) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Account> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_date");

        if(!StringUtils.isEmpty(account.getAccountName())) queryWrapper.like("account_name", account.getAccountName());
        if(!StringUtils.isEmpty(account.getAccountType())) queryWrapper.eq("account_type", account.getAccountType());
        return new PageInfo(accountMapper.selectList(queryWrapper));
    }

    @Override
    public Result get(Long id) {
        try{
            Account account = getById(id);
            return Result.ok("查询成功！",account);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询企业异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result list(Integer page, Integer size, Account account) {
        try{
            PageInfo<Account> accountPage = selectList(page, size, account);
            return Result.ok("查询成功！",accountPage);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询企业列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result update(Account account) {
        try{
            Integer num = updateById(account);
            if(num>0) return Result.ok("更新成功！");
            return Result.fail("更新失败！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新企业异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer count(Account account) throws Exception {
        QueryWrapper<Account> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(account.getAccountName())) queryWrapper.eq("account_name",account.getAccountName());
        if(!StringUtils.isEmpty(account.getAccountCode())) queryWrapper.eq("account_code",account.getAccountCode());
        return accountMapper.selectCount(queryWrapper);
    }
}
