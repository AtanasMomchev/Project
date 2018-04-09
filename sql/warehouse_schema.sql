-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema warehouse
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema warehouse
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `warehouse` DEFAULT CHARACTER SET utf8 ;
USE `warehouse` ;

-- -----------------------------------------------------
-- Table `warehouse`.`lots`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `warehouse`.`lots` (
  `idLots` INT(11) NOT NULL AUTO_INCREMENT,
  `sizeLots` INT(11) NOT NULL,
  `weightCapacityLots` INT(11) NOT NULL,
  PRIMARY KEY (`idLots`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `warehouse`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `warehouse`.`products` (
  `nameProduct` VARCHAR(145) NOT NULL,
  `sizeProduct` INT(11) NOT NULL,
  `weightProduct` DECIMAL(11,0) NOT NULL,
  `priceProduct` DECIMAL(2,0) NOT NULL,
  PRIMARY KEY (`nameProduct`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `nameProduct_UNIQUE` ON `warehouse`.`products` (`nameProduct` ASC);


-- -----------------------------------------------------
-- Table `warehouse`.`lots_quantity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `warehouse`.`lots_quantity` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `product_name` VARCHAR(145) NOT NULL,
  `lot_id` INT(11) NOT NULL,
  `product_quantity` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_lot_id`
    FOREIGN KEY (`lot_id`)
    REFERENCES `warehouse`.`lots` (`idLots`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_product_name`
    FOREIGN KEY (`product_name`)
    REFERENCES `warehouse`.`products` (`nameProduct`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `FK_product_name_idx` ON `warehouse`.`lots_quantity` (`product_name` ASC);

CREATE INDEX `FK_lot_id_idx` ON `warehouse`.`lots_quantity` (`lot_id` ASC);


-- -----------------------------------------------------
-- Table `warehouse`.`history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `warehouse`.`history` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `product_name` VARCHAR(145) NOT NULL,
  `product_quantity` INT(11) NOT NULL,
  `operation` VARCHAR(6) NOT NULL,
  `date` DATETIME(6) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `product_name`
    FOREIGN KEY (`product_name`)
    REFERENCES `warehouse`.`lots_quantity` (`product_name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `id_UNIQUE` ON `warehouse`.`history` (`id` ASC);

CREATE INDEX `product_name_idx` ON `warehouse`.`history` (`product_name` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
