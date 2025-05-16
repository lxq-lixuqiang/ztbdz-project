# 调整投标方
ALTER TABLE `bidder_info`
MODIFY COLUMN `qualification_certificate_01` varchar(255) NULL DEFAULT NULL AFTER `bidder_code`,
MODIFY COLUMN `qualification_certificate_02` varchar(255) NULL DEFAULT NULL AFTER `qualification_certificate_01`,
MODIFY COLUMN `qualification_certificate_03` varchar(255) NULL DEFAULT NULL AFTER `qualification_certificate_02`,
MODIFY COLUMN `qualification_certificate_04` varchar(255) NULL DEFAULT NULL AFTER `qualification_certificate_03`,
MODIFY COLUMN `qualification_certificate_05` varchar(255) NULL DEFAULT NULL AFTER `qualification_certificate_04`;

# 调整评标标准
ALTER TABLE `evaluation_criteria`
ADD COLUMN `member_id` bigint(20) NULL AFTER `project_register_id`,
ADD COLUMN `review_type` int(11) NULL AFTER `member_id`,
ADD COLUMN `is_pass` int(11) NULL AFTER `review_type`,
ADD COLUMN `review_description` varchar(1000) NULL AFTER `is_pass`;

# 调整项目信息
ALTER TABLE `project`
ADD COLUMN `review_progress` int(11) NULL AFTER `answer_end_date`;

# 调整评标标准
ALTER TABLE `evaluation_criteria`
ADD COLUMN `deduction_reason` varchar(1000) NULL AFTER `review_type`;

# 项目信息添加报名费
ALTER TABLE `project`
ADD COLUMN `registration_fee` int(11) NULL AFTER `answer_end_date`;

# 投标报名添加报名费
ALTER TABLE `project_register`
ADD COLUMN `payment_money` int(11) NULL AFTER `num`;

# 项目信息添加评审结束时间
ALTER TABLE `project`
ADD COLUMN `review_end_date` datetime(0) NULL AFTER `review_progress`;

# 创建评审结果报告
CREATE TABLE `result_report`  (
  `id` bigint(20) NOT NULL,
  `member_id` bigint(20) NULL,
  `project_id` bigint(20) NULL,
  `state` int(11) NULL,
  `describe` varchar(1000) NULL,
  `result_report_id` varchar(255) NULL,
  `result_report` longtext NULL,
  `create_date` datetime(0) NULL,
  `update_date` datetime(0) NULL,
  PRIMARY KEY (`id`)
);

# 项目添加评审是否通过
ALTER TABLE `project`
ADD COLUMN `is_pass` int(11) NULL AFTER `state`;

# 投标报名修改中标金额类型
ALTER TABLE `project_register`
MODIFY COLUMN `bid_money` varchar(255) NULL DEFAULT NULL AFTER `payment_voucher`;