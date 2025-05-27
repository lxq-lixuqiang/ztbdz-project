package com.ztbdz.web.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectRegisterExport {

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
                String state = "";
                switch (projectRegister.getState()){
                    case 0:
                        state = "报名审核中";
                        break;
                    case 1:
                        state = "报名通过";
                        break;
                    case 2:
                        state = "报名未通过";
                        break;
                    case 3:
                        state = "资质未审核";
                        break;
                    case 4:
                        state = "资质通过";
                        break;
                    case 5:
                        state = "资质不通过";
                        break;
                }
                selectByProjectExport.setState(state);
                selectByProjectExportList.add(selectByProjectExport);
            }
            return selectByProjectExportList;
        }
    }


}
