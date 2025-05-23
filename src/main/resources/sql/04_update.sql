# 专家信息添加是否组长
ALTER TABLE `expert_info`
ADD COLUMN `is_admin` int(11) NULL AFTER `is_check`;