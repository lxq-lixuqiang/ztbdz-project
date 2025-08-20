-- 增加 发票类型 区分 专票和普票
ALTER TABLE `project_register`
ADD COLUMN `invoice_type` varchar(255) NULL AFTER `is_invoice`;