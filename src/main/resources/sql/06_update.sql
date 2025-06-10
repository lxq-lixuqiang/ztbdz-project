# 专家添加银行名称和银行卡号
ALTER TABLE `expert_info`
ADD COLUMN `bank_name` varchar(255) NULL AFTER `not_check_show`,
ADD COLUMN `bank_card` varchar(255) NULL AFTER `bank_name`;

# 评审添加选中专家和备选专家
ALTER TABLE `review_info`
ADD COLUMN `select_expert` varchar(2000) NULL AFTER `hide_account`,
ADD COLUMN `spare_expert` varchar(1000) NULL AFTER `select_expert`;


# 评审添加备选数量
ALTER TABLE `review_info`
ADD COLUMN `spare_number` int(11) NULL AFTER `number`;