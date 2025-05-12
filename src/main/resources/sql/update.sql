-- 新增项目信息
ALTER TABLE `project`
ADD COLUMN `project_compliance_conditions` varchar(2000) NULL AFTER `tenders_id`,
ADD COLUMN `project_scoring_requirements` varchar(2000) NULL AFTER `project_compliance_conditions`,
ADD COLUMN `project_qualification_conditions` varchar(2000) NULL AFTER `project_scoring_requirements`,
ADD COLUMN `project_overview` varchar(2000) NULL AFTER `project_qualification_conditions`,
ADD COLUMN `procurement_documents` varchar(255) NULL AFTER `bid_opening_time`,
ADD COLUMN `money` int(11) NULL AFTER `project_qualification_conditions`,
ADD COLUMN `member_id` bigint(20) NULL AFTER `project_overview`,
ADD COLUMN `senroll_start_date` datetime(0) NULL DEFAULT NULL AFTER `state`,
ADD COLUMN `enroll_end_date` datetime(0) NULL DEFAULT NULL AFTER `senroll_start_date`,
ADD COLUMN `answer_end_date` datetime(0) NULL DEFAULT NULL AFTER `enroll_end_date`;

-- 删除招标信息
ALTER TABLE `tenderee`
DROP COLUMN `senroll_start_date`,
DROP COLUMN `enroll_end_date`,
DROP COLUMN `answer_end_date`;

-- 调整投标报名
ALTER TABLE `project_register`
MODIFY COLUMN `bid_document_id` varchar(255) NULL DEFAULT NULL AFTER `member_id`,
MODIFY COLUMN `contract_imprint` varchar(255) NULL DEFAULT NULL AFTER `bid_document_id`,
MODIFY COLUMN `bid_evaluation_report` varchar(255) NULL DEFAULT NULL AFTER `win_bid_state`,
ADD COLUMN `payment_voucher` varchar(255) NULL AFTER `bid_evaluation_report`;