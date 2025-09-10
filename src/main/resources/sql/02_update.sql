ALTER TABLE `project`
MODIFY COLUMN `money` double(11, 2) NULL DEFAULT NULL AFTER `member_id`;

ALTER TABLE `review_info`
ADD COLUMN `expert_leader` bigint(20) NULL AFTER `spare_expert`;

ALTER TABLE `result_report`
ADD COLUMN `result_html` longtext NULL AFTER `result_report`;

ALTER TABLE `result_report`
CHANGE COLUMN `describe` `description` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `state`;