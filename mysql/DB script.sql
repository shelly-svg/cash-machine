-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema cash_machine
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema cash_machine
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cash_machine
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cash_machine` DEFAULT CHARACTER SET utf8 ;
USE `cash_machine` ;

-- -----------------------------------------------------
-- Table `cash_machine`.`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cash_machine`.`category` ;

CREATE TABLE IF NOT EXISTS `cash_machine`.`category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(45) NOT NULL,
  `name_en` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `cash_machine`.`delivery`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cash_machine`.`delivery` ;

CREATE TABLE IF NOT EXISTS `cash_machine`.`delivery` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(255) NOT NULL,
  `name_en` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `cash_machine`.`product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cash_machine`.`product` ;

CREATE TABLE IF NOT EXISTS `cash_machine`.`product` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(100) NOT NULL,
  `name_en` VARCHAR(100) NOT NULL,
  `code` VARCHAR(128) NOT NULL,
  `price` DECIMAL(9,2) UNSIGNED NOT NULL,
  `amount` INT UNSIGNED NOT NULL,
  `weight` DECIMAL(8,3) NOT NULL,
  `description_ru` TEXT NULL DEFAULT NULL,
  `description_en` TEXT NULL DEFAULT NULL,
  `category_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_product_category1_idx` (`category_id` ASC) VISIBLE,
  INDEX `idx_product_price_amount` (`price` ASC, `amount` ASC) VISIBLE,
  CONSTRAINT `fk_product_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `cash_machine`.`category` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 55
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `cash_machine`.`receipt_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cash_machine`.`receipt_status` ;

CREATE TABLE IF NOT EXISTS `cash_machine`.`receipt_status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(45) NOT NULL,
  `name_en` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `cash_machine`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cash_machine`.`role` ;

CREATE TABLE IF NOT EXISTS `cash_machine`.`role` (
  `id` INT NOT NULL,
  `name_ru` VARCHAR(45) NOT NULL,
  `name_en` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `cash_machine`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cash_machine`.`user` ;

CREATE TABLE IF NOT EXISTS `cash_machine`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(16) NOT NULL,
  `password` VARCHAR(256) NULL DEFAULT NULL,
  `salt` VARCHAR(50) NOT NULL,
  `first_name` VARCHAR(45) NULL DEFAULT NULL,
  `last_name` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(50) NOT NULL,
  `locale_name` VARCHAR(45) NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  INDEX `fk_user_role1_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `cash_machine`.`role` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `cash_machine`.`receipt`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cash_machine`.`receipt` ;

CREATE TABLE IF NOT EXISTS `cash_machine`.`receipt` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name_ru` VARCHAR(45) NOT NULL,
  `name_en` VARCHAR(45) NOT NULL,
  `address_ru` VARCHAR(150) NOT NULL,
  `address_en` VARCHAR(150) NULL DEFAULT NULL,
  `description_ru` VARCHAR(255) NULL DEFAULT NULL,
  `description_en` VARCHAR(255) NULL DEFAULT NULL,
  `phone_number` VARCHAR(15) NOT NULL,
  `delivery_id` INT NOT NULL,
  `receipt_status_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_receipt_delivery1_idx` (`delivery_id` ASC) VISIBLE,
  INDEX `fk_receipt_receipt_status1_idx` (`receipt_status_id` ASC) VISIBLE,
  INDEX `fk_receipt_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_receipt_delivery1`
    FOREIGN KEY (`delivery_id`)
    REFERENCES `cash_machine`.`delivery` (`id`),
  CONSTRAINT `fk_receipt_receipt_status1`
    FOREIGN KEY (`receipt_status_id`)
    REFERENCES `cash_machine`.`receipt_status` (`id`),
  CONSTRAINT `fk_receipt_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `cash_machine`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 63
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `cash_machine`.`receipt_has_product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cash_machine`.`receipt_has_product` ;

CREATE TABLE IF NOT EXISTS `cash_machine`.`receipt_has_product` (
  `receipt_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  `amount` INT UNSIGNED NOT NULL,
  `price` DECIMAL(9,2) UNSIGNED NOT NULL,
  PRIMARY KEY (`receipt_id`, `product_id`),
  INDEX `fk_receipt_has_product_product1_idx` (`product_id` ASC) VISIBLE,
  INDEX `fk_receipt_has_product_receipt1_idx` (`receipt_id` ASC) VISIBLE,
  CONSTRAINT `fk_receipt_has_product_product1`
    FOREIGN KEY (`product_id`)
    REFERENCES `cash_machine`.`product` (`id`),
  CONSTRAINT `fk_receipt_has_product_receipt1`
    FOREIGN KEY (`receipt_id`)
    REFERENCES `cash_machine`.`receipt` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `cash_machine`.`user_details`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cash_machine`.`user_details` ;

CREATE TABLE IF NOT EXISTS `cash_machine`.`user_details` (
  `user_id` INT NOT NULL,
  `salt` VARCHAR(50) NOT NULL,
  `code` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_user_details_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `cash_machine`.`user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
