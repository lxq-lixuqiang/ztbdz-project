# 项目信息添加审核状态
ALTER TABLE `project`
ADD COLUMN `is_audit` int(11) NULL AFTER `is_pass`;

# 投标报名添加是否开具发票
ALTER TABLE `project_register`
ADD COLUMN `is_invoice` int(11) NULL AFTER `bid_money`;

# 调整招标公告
ALTER TABLE `tenderee_inform`
CHANGE COLUMN `project_name` `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `tenderee_id`,
ADD COLUMN `content` longtext NULL AFTER `is_public`,
ADD COLUMN `tenderee_inform_type` varchar(255) NULL AFTER `content`,
ADD COLUMN `supplementary_content` varchar(255) NULL AFTER `tenderee_info_type`;

# 项目信息添加不通过原因
ALTER TABLE `project`
ADD COLUMN `not_pass_info` varchar(255) NULL AFTER `is_pass`;

# 投标报名添加不通过原因
ALTER TABLE `project_register`
ADD COLUMN `not_pass_info` varchar(255) NULL AFTER `state`;