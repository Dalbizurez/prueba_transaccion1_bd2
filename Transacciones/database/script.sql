-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema transacciones
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `transacciones` ;

-- -----------------------------------------------------
-- Schema transacciones
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `transacciones` DEFAULT CHARACTER SET utf8 ;
USE `transacciones` ;

-- -----------------------------------------------------
-- Table `transacciones`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `transacciones`.`Cliente` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `apellido` VARCHAR(45) NULL,
  `direccion` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `transacciones`.`Telefono`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `transacciones`.`Telefono` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `numero` VARCHAR(45) NULL,
  `Cliente_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Telefono_Cliente_idx` (`Cliente_id` ASC) VISIBLE,
  CONSTRAINT `fk_Telefono_Cliente`
    FOREIGN KEY (`Cliente_id`)
    REFERENCES `transacciones`.`Cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
