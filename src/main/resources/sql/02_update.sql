# 专家信息添加职称
ALTER TABLE `expert_info`
ADD COLUMN `position` varchar(255) NULL AFTER `professional_field`;