ALTER TABLE `project`
ADD COLUMN `description_file` varchar(255) NULL AFTER `procurement_documents`;

INSERT INTO `menu_authorize`(`id`, `name`, `sign`, `path`, `url`, `create_date`, `update_date`, `ext1`, `ext2`, `ext3`, `ext4`, `ext5`) VALUES (-996220136613920919, '澄清页面', 'clarify', NULL, '/clarify.html', '2025-06-09 10:45:10', '2025-06-09 10:45:10', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `role_related_authorize`(`role_id`, `menu_authorize_id`) VALUES (-2280461216064585690, -996220136613920919);
INSERT INTO `role_related_authorize`(`role_id`, `menu_authorize_id`) VALUES (5695793173735621193, -996220136613920919);

ALTER TABLE `tenderee_inform`
ADD COLUMN `association_id` bigint(20) NULL AFTER `tenderee_id`;

ALTER TABLE `tenderee_inform`
ADD COLUMN `project_id` bigint(20) NULL AFTER `tenderee_id`;

ALTER TABLE `tenderee_inform`
ADD COLUMN `reply_content` varchar(255) NULL AFTER `is_public`,
ADD COLUMN `reply_date` datetime(0) NULL AFTER `reply_content`;
