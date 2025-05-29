package com.ztbdz.web.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ztbdz.tenderee.pojo.Tenderee;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TendereeExport {

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
                String state = "";
                switch (tenderee.getProject().getIsAudit()){
                    case 0:
                        state = "未审核";
                        break;
                    case 1:
                        state = "通过";
                        break;
                    case 2:
                        state = "不通过";
                        break;
                }
                pageExport.setState(state);
                selectByProjectExportList.add(pageExport);
            }
            return selectByProjectExportList;
        }
    }

}
