ALTER TABLE `bidder_info`
ADD COLUMN `submit_date` datetime(0) NULL AFTER `check_date`;
ALTER TABLE `account`
ADD COLUMN `remark` varchar(2000) NULL AFTER `account_code_file_id`;