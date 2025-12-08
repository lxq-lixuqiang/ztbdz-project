package com.ztbdz.web.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import lombok.Data;

import java.util.*;

public class ProjectRegisterExport {
    // 预览文件地址
    public static String FILE_PREVIEW_URL = "/file/preview/";

    public static Map<Integer,String> STATE = new HashMap(){{
        put(0,"报名审核中");
        put(1,"报名通过");
        put(2,"报名未通过");
        put(3,"资质未审核");
        put(4,"资质通过");
        put(5,"资质不通过");
    }};
    public static Map<Integer,String> STATE_02 = new HashMap(){{
        put(0,"未审核");
        put(1,"已审核");
    }};
    public static Map<Integer,String> STATE_03 = new HashMap(){{
        put(0,"未审核");
        put(1,"通过");
        put(2,"不通过");
    }};
    public static Map<Integer,String> IS_INVOICE = new HashMap(){{
        put(0,"待处理");
        put(1,"已开票");
    }};


    /**
     * 项目已报名_导出数据
     */
    @Data
    public static class SelectByProjectExport {
        @ExcelProperty("项目名称")
        private String projectName;

        @ExcelProperty("报名截止时间")
        private Date enrollEndDate;

        @ExcelProperty("业主单位")
        private String tendereeName;

        @ExcelProperty("报名单位")
        private String accountName;

        @ExcelProperty("资质状态")
        private String state;

        public static List<SelectByProjectExport> converter(List<ProjectRegister> projectRegisterList){
            List<SelectByProjectExport> selectByProjectExportList = new ArrayList();
            for(ProjectRegister projectRegister : projectRegisterList){
                SelectByProjectExport selectByProjectExport = new SelectByProjectExport();
                selectByProjectExport.setProjectName(projectRegister.getProject().getProjectName());
                selectByProjectExport.setEnrollEndDate(projectRegister.getProject().getEnrollEndDate());
                selectByProjectExport.setTendereeName(projectRegister.getProject().getTenderee().getTendereeName());
                selectByProjectExport.setAccountName(projectRegister.getMember().getAccount().getAccountName());
                selectByProjectExport.setState(STATE.get(projectRegister.getState()));
                selectByProjectExportList.add(selectByProjectExport);
            }
            return selectByProjectExportList;
        }
    }

    /**
     * 开具发票申请列表_导出数据
     */
    @Data
    public static class SelectInvoiceExport {
        @ExcelProperty("项目名称")
        private String projectName;

        @ExcelProperty("申请单位")
        private String accountName;

        @ExcelProperty("统一社会信用代码")
        private String accountCode;

        @ExcelProperty("电子邮箱")
        private String email;

        @ExcelProperty("开标时间")
        private Date bidOpeningTime;

        @ExcelProperty("发票类型")
        private String type;

        @ExcelProperty("发票金额")
        private Long paymentMoney;

        @ExcelProperty("申请开票日期")
        private Date invoiceDate;

        @ExcelProperty("状态")
        private String invoice;

        public static List<SelectInvoiceExport> converter(List<ProjectRegister> projectRegisterList){
            List<SelectInvoiceExport> selectInvoiceExportList = new ArrayList();
            for(ProjectRegister projectRegister : projectRegisterList){
                SelectInvoiceExport selectInvoiceExport = new SelectInvoiceExport();
                selectInvoiceExport.setProjectName(projectRegister.getProject().getProjectName());
                selectInvoiceExport.setAccountName(projectRegister.getMember().getAccount().getAccountName());
                selectInvoiceExport.setAccountCode(projectRegister.getMember().getAccount().getAccountCode());
                selectInvoiceExport.setEmail(projectRegister.getMember().getAccount().getEmail());
                selectInvoiceExport.setBidOpeningTime(projectRegister.getProject().getBidOpeningTime());
                selectInvoiceExport.setType(projectRegister.getInvoiceType());
                selectInvoiceExport.setPaymentMoney(projectRegister.getPaymentMoney());
                selectInvoiceExport.setInvoiceDate(projectRegister.getInvoiceDate());
                selectInvoiceExport.setInvoice(IS_INVOICE.get(projectRegister.getIsInvoice()));
                selectInvoiceExportList.add(selectInvoiceExport);
            }
            return selectInvoiceExportList;
        }
    }

    /**
     * 项目列表_导出数据
     */
    @Data
    public static class PageExport {
        @ExcelProperty("项目名称")
        private String projectName;

        @ExcelProperty("项目编号")
        private String projectCode;

        @ExcelProperty("业主单位")
        private String tendereeName;

        @ExcelProperty("报名开始时间")
        private Date senrollStartDate;

        @ExcelProperty("报名截止时间")
        private Date enrollEndDate;

        @ExcelProperty("开标时间")
        private Date bidOpeningTime;

        @ExcelProperty("项目概况")
        private String projectOverview;

        @ExcelProperty("审核状态")
        private String state;

        @ExcelProperty("投标单位")
        private String accountName;

        @ExcelProperty("联系人")
        private String member;

        @ExcelProperty("联系电话")
        private String phone;

        @ExcelProperty("邮箱")
        private String email;

        @ExcelProperty("报名时间")
        private Date startDate;

        @ExcelProperty("缴费金额")
        private String money;

        @ExcelProperty("报名审核状态")
        private String signUpState;

        @ExcelProperty("付款凭证")
        private String payUrl;

        public static List<PageExport> converter(List<Project> projectList){
            List<PageExport> pageExportList = new ArrayList();
            for(Project project : projectList){
                PageExport pageExport = new PageExport();
                pageExport.setProjectName(project.getProjectName());
                pageExport.setProjectCode(project.getProjectCode());
                pageExport.setSenrollStartDate(project.getSenrollStartDate());
                pageExport.setEnrollEndDate(project.getEnrollEndDate());
                pageExport.setBidOpeningTime(project.getBidOpeningTime());
                pageExport.setTendereeName(project.getTenderee().getTendereeName());
                pageExport.setProjectOverview(project.getProjectOverview());
                pageExport.setState(STATE_02.get(project.getState()));
                pageExport.setAccountName(project.getProjectRegisters().getMember().getAccount().getAccountName());
                pageExport.setMember(project.getProjectRegisters().getMember().getAccount().getMember());
                pageExport.setPhone(project.getProjectRegisters().getMember().getAccount().getPhone());
                pageExport.setEmail(project.getProjectRegisters().getMember().getAccount().getEmail());
                pageExport.setStartDate(project.getProjectRegisters().getCreateDate());
                pageExport.setMoney(project.getProjectRegisters().getPaymentMoney().toString());
                pageExport.setSignUpState(STATE_03.get(project.getProjectRegisters().getState()));
                pageExport.setPayUrl(FILE_PREVIEW_URL+project.getProjectRegisters().getPaymentVoucher());
                pageExportList.add(pageExport);
            }
            return pageExportList;
        }
    }


}
