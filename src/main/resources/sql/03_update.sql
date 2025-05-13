# 调整投标方
ALTER TABLE `bidder_info`
MODIFY COLUMN `qualification_certificate_01` varchar(255) NULL DEFAULT NULL AFTER `bidder_code`,
MODIFY COLUMN `qualification_certificate_02` varchar(255) NULL DEFAULT NULL AFTER `qualification_certificate_01`,
MODIFY COLUMN `qualification_certificate_03` varchar(255) NULL DEFAULT NULL AFTER `qualification_certificate_02`,
MODIFY COLUMN `qualification_certificate_04` varchar(255) NULL DEFAULT NULL AFTER `qualification_certificate_03`,
MODIFY COLUMN `qualification_certificate_05` varchar(255) NULL DEFAULT NULL AFTER `qualification_certificate_04`;