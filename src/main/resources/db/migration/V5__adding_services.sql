CREATE TABLE `saloonmanagement`.`services` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `category` VARCHAR(100) NULL,
  `description` VARCHAR(500) NULL,
  `duration_minutes` INT NOT NULL,
  `price` DECIMAL NOT NULL,
  PRIMARY KEY (`id`));
