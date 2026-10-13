CREATE TABLE `saloonmanagement`.`feedbacks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `comment` TEXT NOT NULL,
  `rating` INT NOT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_feedback_user_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_feedback_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `saloonmanagement`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
