package com.ztbdz.web.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ztbdz.tenderee.pojo.Tenderee;
import lombok.Data;

import java.util.*;

public class TendereeExport {

    public static Map<Integer,String> IS_AUDIT = new HashMap(){{
        put(0,"未审核");
        put(1,"通过");
        put(2,"不通过");
    }};

    /**
     * 项目已报名_导出数据
     */
    @Data
    public static class PageExport {
        @ExcelProperty("项目名称")
        private String projectName;

        @ExcelProperty("项目编号")
        private String projectCode;

        @ExcelProperty("报名截止时间")
        private Date enrollEndDate;

        @ExcelProperty("预算金额")
        private Long money;

        @ExcelProperty("状态")
        private String state;

        public static List<PageExport> converter(List<Tenderee> tendereeList){
            List<PageExport> selectByProjectExportList = new ArrayList();
            for(Tenderee tenderee : tendereeList){
                PageExport pageExport = new PageExport();
                pageExport.setProjectName(tenderee.getProject().getProjectName());
                pageExport.setProjectCode(tenderee.getProject().getProjectCode());
                pageExport.setEnrollEndDate(tenderee.getProject().getEnrollEndDate());
                pageExport.setMoney(tenderee.getProject().getMoney());
                pageExport.setState(IS_AUDIT.get(tenderee.getProject().getIsAudit()));
                selectByProjectExportList.add(pageExport);
            }
            return selectByProjectExportList;
        }
    }

}
