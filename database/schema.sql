# ************************************************************
# Sequel Pro SQL dump
# Version 5446
#
# https://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 8.0.19)
# Database: mcontract
# Generation Time: 2020-11-10 08:39:27 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table company
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `ward` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `street` varchar(500) NOT NULL,
  `fk_mst` int unsigned NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `company_fk` (`fk_mst`),
  CONSTRAINT `company_fk` FOREIGN KEY (`fk_mst`) REFERENCES `mst` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table company_user_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company_user_history`;

CREATE TABLE `company_user_history` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `fk_user` int unsigned NOT NULL,
  `fk_company` int unsigned NOT NULL,
  `fk_company_user_role` int unsigned NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `company_user_history_fk_company_user_role` (`fk_company_user_role`),
  KEY `fk_user` (`fk_user`),
  KEY `fk_company` (`fk_company`),
  CONSTRAINT `company_user_history_fk_company_user_role` FOREIGN KEY (`fk_company_user_role`) REFERENCES `company_user_role` (`id`),
  CONSTRAINT `company_user_history_ibfk_1` FOREIGN KEY (`fk_user`) REFERENCES `user` (`id`),
  CONSTRAINT `company_user_history_ibfk_2` FOREIGN KEY (`fk_company`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table company_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company_user_role`;

CREATE TABLE `company_user_role` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `company_user_role` WRITE;
/*!40000 ALTER TABLE `company_user_role` DISABLE KEYS */;

INSERT INTO `company_user_role` (`id`, `name`, `created_at`, `updated_at`)
VALUES
	(1,'admin','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(2,'inactivate','2020-11-10 14:01:53','2020-11-10 14:01:53');

/*!40000 ALTER TABLE `company_user_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table contract
# ------------------------------------------------------------

DROP TABLE IF EXISTS `contract`;

CREATE TABLE `contract` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `description` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `fk_user` int unsigned NOT NULL,
  `fk_mst` int unsigned NOT NULL,
  `fk_file` int unsigned NOT NULL,
  `fk_contract_status` int unsigned NOT NULL,
  `fk_contract_message` int unsigned DEFAULT NULL,
  `expiry_date_signed` date DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `contract_fk_user` (`fk_user`),
  KEY `contract_fk_contract_status` (`fk_contract_status`),
  KEY `contract_fk` (`fk_mst`),
  KEY `contract_fk_1` (`fk_file`),
  KEY `fk_contract_message` (`fk_contract_message`),
  CONSTRAINT `contract_fk` FOREIGN KEY (`fk_mst`) REFERENCES `mst` (`id`),
  CONSTRAINT `contract_fk_1` FOREIGN KEY (`fk_file`) REFERENCES `files` (`id`),
  CONSTRAINT `contract_fk_contract_status` FOREIGN KEY (`fk_contract_status`) REFERENCES `contract_status` (`id`),
  CONSTRAINT `contract_fk_user` FOREIGN KEY (`fk_user`) REFERENCES `user` (`id`),
  CONSTRAINT `contract_ibfk_1` FOREIGN KEY (`fk_contract_message`) REFERENCES `contract_message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table contract_message
# ------------------------------------------------------------

DROP TABLE IF EXISTS `contract_message`;

CREATE TABLE `contract_message` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `fk_contract_user` int unsigned NOT NULL,
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_contract_user` (`fk_contract_user`),
  CONSTRAINT `contract_message_ibfk_1` FOREIGN KEY (`fk_contract_user`) REFERENCES `contract_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table contract_status
# ------------------------------------------------------------

DROP TABLE IF EXISTS `contract_status`;

CREATE TABLE `contract_status` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `contract_status` WRITE;
/*!40000 ALTER TABLE `contract_status` DISABLE KEYS */;

INSERT INTO `contract_status` (`id`, `name`, `created_at`, `updated_at`)
VALUES
	(1,'waiting_for_approval','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(2,'waiting_for_signature','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(3,'signed','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(4,'approved','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(5,'cancelled','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(6,'draft','2020-08-24 22:57:44','2020-08-24 22:57:44'),
	(7,'invalid_cert','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(8,'invalid_algorithm','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(9,'invalid_signature','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(10,'expired_certificate','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(11,'revoked_certificate','2020-11-10 14:01:53','2020-11-10 14:01:53'),
	(12,'mismatch_tax_code','2020-11-10 14:01:53','2020-11-10 14:01:53');

/*!40000 ALTER TABLE `contract_status` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table contract_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `contract_user`;

CREATE TABLE `contract_user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `fk_contract` int unsigned NOT NULL,
  `fk_contract_status` int unsigned NOT NULL,
  `fk_contract_user_role` int unsigned NOT NULL,
  `fk_email` int unsigned NOT NULL,
  `fk_mst` int unsigned NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `contract_user_fk_contract` (`fk_contract`),
  KEY `contract_user_fk_contract_status` (`fk_contract_status`),
  KEY `contract_user_fk_contract_user_role` (`fk_contract_user_role`),
  KEY `contract_user_fk_email` (`fk_email`),
  KEY `contract_user_fk_mst` (`fk_mst`),
  CONSTRAINT `contract_user_fk_contract` FOREIGN KEY (`fk_contract`) REFERENCES `contract` (`id`),
  CONSTRAINT `contract_user_fk_contract_status` FOREIGN KEY (`fk_contract_status`) REFERENCES `contract_status` (`id`),
  CONSTRAINT `contract_user_fk_contract_user_role` FOREIGN KEY (`fk_contract_user_role`) REFERENCES `contract_user_role` (`id`),
  CONSTRAINT `contract_user_fk_email` FOREIGN KEY (`fk_email`) REFERENCES `email` (`id`),
  CONSTRAINT `contract_user_fk_mst` FOREIGN KEY (`fk_mst`) REFERENCES `mst` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table contract_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `contract_user_role`;

CREATE TABLE `contract_user_role` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `contract_user_role` WRITE;
/*!40000 ALTER TABLE `contract_user_role` DISABLE KEYS */;

INSERT INTO `contract_user_role` (`id`, `name`, `created_at`, `updated_at`)
VALUES
	(1,'creater','2020-06-27 15:21:46','2020-06-27 15:33:56'),
	(2,'signer','2020-06-27 15:34:00','2020-06-27 15:34:07'),
	(3,'approver','2020-06-27 15:34:48','2020-06-27 15:34:48');

/*!40000 ALTER TABLE `contract_user_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table email
# ------------------------------------------------------------

DROP TABLE IF EXISTS `email`;

CREATE TABLE `email` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `fk_user` int unsigned DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`),
  KEY `email_fk_user` (`fk_user`),
  CONSTRAINT `email_fk_user` FOREIGN KEY (`fk_user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table email_verify_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `email_verify_token`;

CREATE TABLE `email_verify_token` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `expiry` datetime NOT NULL,
  `fk_user` int unsigned NOT NULL,
  `is_active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table files
# ------------------------------------------------------------

DROP TABLE IF EXISTS `files`;

CREATE TABLE `files` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `key_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `files_upload_path_uindex` (`key_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table mst
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mst`;

CREATE TABLE `mst` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `mst` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mst_mst` (`mst`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `date_of_birth` date NOT NULL,
  `cmnd` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `district` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `ward` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `street` varchar(500) NOT NULL,
  `cmnd_issue_place` varchar(255) NOT NULL,
  `date_of_registration` date NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `gender` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `fullname` varchar(500) NOT NULL,
  `cmnd_issue_date` date NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `company_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `company_website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fk_company` int unsigned DEFAULT NULL,
  `fk_company_user_role` int unsigned DEFAULT NULL,
  `is_enabled` bit(1) NOT NULL DEFAULT b'1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- DVHCVN

DROP TABLE IF EXISTS `dvhc_cities`;
DROP TABLE IF EXISTS `dvhc_districts`;
DROP TABLE IF EXISTS `dvhc_wards`;

CREATE TABLE `dvhc_cities` (
  `id` int unsigned NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `dvhc_districts` (
  `id` int unsigned NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fk_dvhc_city` int unsigned NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `dvhc_districts_fk_cities` (`fk_dvhc_city`),
  CONSTRAINT `dvhc_districts_fk_cities` FOREIGN KEY (`fk_dvhc_city`) REFERENCES `dvhc_cities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `dvhc_wards` (
  `id` int unsigned NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fk_dvhc_district` int unsigned NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `dvhc_wards_fk_districts` (`fk_dvhc_district`),
  CONSTRAINT `dvhc_wards_fk_districts` FOREIGN KEY (`fk_dvhc_district`) REFERENCES `dvhc_districts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- For data dump of dvhc, refer to file ./dvhcvn.sql


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
