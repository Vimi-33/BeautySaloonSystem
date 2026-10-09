CREATE TABLE `saloonmanagement`.`products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `product_type` VARCHAR(45) NOT NULL,
  `name` VARCHAR(125) NOT NULL,
  `brand` VARCHAR(80) NULL,
  `description` VARCHAR(500) NULL,
  `price` DECIMAL NOT NULL,
  `quantity` INT NOT NULL,
  `img_url` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));
