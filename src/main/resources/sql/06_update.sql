# 专家添加银行名称和银行卡号
ALTER TABLE `expert_info`
ADD COLUMN `bank_name` varchar(255) NULL AFTER `not_check_show`,
ADD COLUMN `bank_card` varchar(255) NULL AFTER `bank_name`;