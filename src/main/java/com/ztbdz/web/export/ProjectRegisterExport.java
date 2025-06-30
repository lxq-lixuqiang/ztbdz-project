package com.ztbdz.web.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import lombok.Data;

import java.util.*;

public class ProjectRegisterExport {

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

        @ExcelProperty("发票金额")
        private Long paymentMoney;

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
                selectInvoiceExport.setPaymentMoney(projectRegister.getPaymentMoney());
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

        @ExcelProperty("报名截止时间")
        private Date enrollEndDate;

        @ExcelProperty("业主单位")
        private String tendereeName;

        @ExcelProperty("项目概况")
        private String projectOverview;

        @ExcelProperty("审核状态")
        private String state;

        public static List<PageExport> converter(List<Project> projectList){
            List<PageExport> pageExportList = new ArrayList();
            for(Project project : projectList){
                PageExport pageExport = new PageExport();
                pageExport.setProjectName(project.getProjectName());
                pageExport.setEnrollEndDate(project.getEnrollEndDate());
                pageExport.setTendereeName(project.getProjectRegisters().getProject().getTenderee().getTendereeName());
                pageExport.setProjectOverview(project.getProjectOverview());
                pageExport.setState(STATE_02.get(project.getProjectRegisters().getState()));
                pageExportList.add(pageExport);
            }
            return pageExportList;
        }
    }


}
