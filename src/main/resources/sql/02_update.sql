# 专家信息添加职称
ALTER TABLE `expert_info`
ADD COLUMN `position` varchar(255) NULL AFTER `professional_field`;

# 专家信息调整类型的长度
ALTER TABLE `expert_info`
MODIFY COLUMN `expert_type` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `expert_state`;