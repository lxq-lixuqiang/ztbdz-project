ALTER TABLE `project`
ADD COLUMN `project_type` varchar(255) NULL AFTER `registration_fee`,
ADD COLUMN `old_suppliers` varchar(5000) NULL AFTER `project_method`;