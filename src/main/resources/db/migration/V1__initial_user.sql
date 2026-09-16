CREATE TABLE `saloonmanagement`.`users` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_type` VARCHAR(20) NOT NULL,
  `full_name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `loyalty_points` INT NULL DEFAULT 0,
  `membership_tier` VARCHAR(20) NULL DEFAULT 'SILVER',
  `total_spent` DECIMAL NULL DEFAULT 0.00,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);