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