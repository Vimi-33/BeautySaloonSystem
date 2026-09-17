CREATE TABLE `saloonmanagement`.`appointments` (
  `id` BIGINT NOT NULL,
  `appointment_type` VARCHAR(45) NOT NULL,
  `customer_id` BIGINT NOT NULL,
  `service_name` VARCHAR(255) NOT NULL,
  `appointment_date_time` DATETIME NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `notes` TEXT NULL,
  `dedicated_stylist` VARCHAR(255) NULL,
  `price` DOUBLE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_appointment_customer_idx` (`customer_id` ASC) VISIBLE,
  CONSTRAINT `fk_appointment_customer`
    FOREIGN KEY (`customer_id`)
    REFERENCES `saloonmanagement`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);