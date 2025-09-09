package com.ztbdz.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.ExpertInfoMapper;
import com.ztbdz.user.pojo.*;
import com.ztbdz.user.service.AccountService;
import com.ztbdz.user.service.ExpertInfoService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.Common;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExpertInfoServiceImpl implements ExpertInfoService {

    @Autowired
    private ExpertInfoMapper expertInfoMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result create(ExpertInfo expertInfo) {
        try{
            if(StringUtils.isEmpty(expertInfo.getMember())){
                return Result.fail("专家信息不能为空！");
            }
            // 手机号不能为空
            if( StringUtils.isEmpty(expertInfo.getMember().getPhone())){
                return Result.fail("专家手机号不能为空！");
            }
            if( StringUtils.isEmpty(expertInfo.getIdCard())){
                return Result.fail("专家身份证号不能为空！");
            }
            User user = new User();
            Role role = new Role();
            role.setType("expert");
            expertInfo.getMember().setRole(role);
            user.setMember(expertInfo.getMember());
            user.setUsername(expertInfo.getIdCard());
            user.setPassword(expertInfo.getIdCard()+SystemConfig.DEFAULT_PASSWORD);
            Result reslut = userService.create(user,Common.DEFAULT_VALUE);
            if(reslut.getStatus()!=200){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return reslut;
            }
            expertInfo.setMemberId(user.getMember().getId());
            Integer num = this.insert(expertInfo);
            if(num<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.fail("添加失败！");
            }
            return Result.ok("添加成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("添加专家异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result getMemberInfo(Long memberId) {
        try{
            ExpertInfo expertInfo = this.selectByMemberId(memberId);
            if(expertInfo==null){
                expertInfo = new ExpertInfo(); // 初始化投标方信息
                expertInfo.setIsCheck(1);
                expertInfo.setMemberId(memberId);
                this.insert(expertInfo);
            }
            expertInfo.setMember(memberService.getById(memberId));
            return Result.ok("查询成功！",expertInfo);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询投标方信息异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result update(ExpertInfo expertInfo) {
        try{
            if(expertInfo.getMember()!=null && expertInfo.getMember().getId()!=null){
                if(memberService.updateById(expertInfo.getMember())==0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("更新人员失败！");
                }
            }
            if(expertInfo.getMember()!=null && expertInfo.getMember().getAccount()!=null && expertInfo.getMember().getAccount().getId()!=null){
                if(accountService.updateById(expertInfo.getMember().getAccount())==0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("更新单位失败！");
                }
            }
            Integer num = this.updateById(expertInfo);
            if(num<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.fail("更新专家失败！");
            }
            return Result.ok("更新成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新专家方异常，原因："+e.getMessage());
        }
    }

    @Override
    public ExpertInfo selectByMemberId(Long memberId) throws Exception {
        QueryWrapper<ExpertInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", memberId.toString());
        return expertInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateById(ExpertInfo expertInfo) throws Exception {
        return expertInfoMapper.updateById(expertInfo);
    }

    @Override
    public Integer insert(ExpertInfo expertInfo) throws Exception {
        return expertInfoMapper.insert(expertInfo);
    }

    @Override
    public Result list(Integer page, Integer size, ExpertInfo expertInfo) {
        try{
            return Result.ok("查询成功！",this.page(page, size, expertInfo));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询未审核专家异常，原因："+e.getMessage());
        }
    }

    @Override
    public PageInfo<ExpertInfo> page(Integer page, Integer size, ExpertInfo expertInfo) throws Exception {
        PageHelper.startPage(page, size);
        return new PageInfo(expertInfoMapper.selectMember(expertInfo));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result uploadExcel(MultipartFile file) {
        try{
            if(file.getOriginalFilename().indexOf("xls")<0) return Result.fail("文件类型错误，请上传Excel文件的xls，xlsx");

            String[] fields = new String[]{"姓名","手机号","企业名称","专家编号","身份证号","专家证号","职称","专家类型","银行名称","银行卡号","是否组长（0=不是 1=是）","参加工作日期"};
            List<Map<String,String>> dataList = SystemConfig.importExcelData(file,fields);
            for(Map<String,String> dataMap : dataList){
                String message = dataMap.get(fields[0]);
                if(StringUtils.isEmpty(dataMap.get(fields[10]))) return Result.fail("解析'"+fields[0]+"'为【"+message+"】创建失败，原因：是否组长不能为空！");
                ExpertInfo expertInfo = new ExpertInfo();
                expertInfo.setIsCheck(1);
                Member member = new Member();
                member.setName(message);
                member.setPhone(dataMap.get(fields[1]));
                Account account = new Account();
                account.setAccountName(dataMap.get(fields[2]));
                member.setAccount(account);
                expertInfo.setExpertCode(dataMap.get(fields[3]));
                expertInfo.setIdCard(dataMap.get(fields[4]));
                expertInfo.setExpertCard(dataMap.get(fields[5]));
                expertInfo.setPosition(dataMap.get(fields[6]));
                expertInfo.setExpertType(dataMap.get(fields[7]));
                expertInfo.setBankName(dataMap.get(fields[8]));
                expertInfo.setBankCard(dataMap.get(fields[9]));
                expertInfo.setIsAdmin(Integer.valueOf(dataMap.get(fields[10])));
                if(!StringUtils.isEmpty(dataMap.get(fields[11]))) expertInfo.setWorkDate(new SimpleDateFormat("yyyy-MM-dd").parse(dataMap.get(fields[11])));

                expertInfo.setMember(member);
                Result result = this.create(expertInfo);
                if(result.getStatus()!=200){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    log.error("解析'"+fields[0]+"'为【"+message+"】创建失败，原因："+result.getMessage()+",具体参数："+JSON.toJSONString(dataMap));
                    return Result.fail("解析'"+fields[0]+"'为【"+message+"】创建失败，原因："+result.getMessage());
                }
            }
            return Result.ok("解析Excel专家文件成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("解析Excel专家文件异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<ExpertInfo> selectExpertTo(List<Long> expertIds)  throws Exception{
        return expertInfoMapper.selectExpertTo(expertIds);
    }
}
