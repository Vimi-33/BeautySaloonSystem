CREATE TABLE `saloonmanagement`.`staff` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `full_name` VARCHAR(255) NULL,
  `email` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(100) NULL,
  `role` VARCHAR(105) NULL,
  `status` VARCHAR(45) NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`));
