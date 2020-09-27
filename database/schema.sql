# ************************************************************
# Host: 127.0.0.1 (MySQL 8.0.19)
# Database: mcontract
# Generation Time: 2020-07-17 06:26:10 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table certificate_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `certificate_token`;

CREATE TABLE `certificate_token` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `private_key` varchar(1000) DEFAULT NULL,
  `public_key` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `fk_mst` int unsigned NOT NULL,
  `fk_email` int unsigned NOT NULL,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `certificate_token_fk_mst` (`fk_mst`),
  KEY `certificate_token_fk_email` (`fk_email`),
  CONSTRAINT `certificate_token_fk_email` FOREIGN KEY (`fk_email`) REFERENCES `email` (`id`),
  CONSTRAINT `certificate_token_fk_mst` FOREIGN KEY (`fk_mst`) REFERENCES `mst` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# Dump of table company
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `ward` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# Dump of table company_user_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company_user_history`;

CREATE TABLE `company_user_history` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `fk_company_user_role` int unsigned NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `company_user_history_fk_company_user_role` (`fk_company_user_role`),
  CONSTRAINT `company_user_history_fk_company_user_role` FOREIGN KEY (`fk_company_user_role`) REFERENCES `company_user_role` (`id`)
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `company_user_role` WRITE;
/*!40000 ALTER TABLE `company_user_role` DISABLE KEYS */;

INSERT INTO `company_user_role` (`name`)
VALUES
  ('admin'),
  ('inactivate');

/*!40000 ALTER TABLE `company_user_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table contract
# ------------------------------------------------------------

DROP TABLE IF EXISTS `contract`;

CREATE TABLE `contract` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `fk_user` int unsigned NOT NULL,
  `fk_company` int unsigned DEFAULT NULL,
  `fk_contract_status` int unsigned NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `contract_fk_user` (`fk_user`),
  KEY `contract_fk_company` (`fk_company`),
  KEY `contract_fk_contract_status` (`fk_contract_status`),
  CONSTRAINT `contract_fk_company` FOREIGN KEY (`fk_company`) REFERENCES `company` (`id`),
  CONSTRAINT `contract_fk_contract_status` FOREIGN KEY (`fk_contract_status`) REFERENCES `contract_status` (`id`),
  CONSTRAINT `contract_fk_user` FOREIGN KEY (`fk_user`) REFERENCES `user` (`id`)
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `contract_status` WRITE;
/*!40000 ALTER TABLE `contract_status` DISABLE KEYS */;

INSERT INTO `contract_status` (`name`)
VALUES
  ('waiting_for_approval'),
  ('waiting_for_signature'),
  ('signed'),
  ('approved'),
  ('cancelled');

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
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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

# Dump of table mst
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mst`;

CREATE TABLE `mst` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `mst` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `fk_company` int unsigned DEFAULT NULL,
  `fk_email` int unsigned DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mst_mst` (`mst`),
  KEY `mst_fk_company` (`fk_company`),
  KEY `mst_fk_email` (`fk_email`),
  CONSTRAINT `mst_fk_company` FOREIGN KEY (`fk_company`) REFERENCES `company` (`id`),
  CONSTRAINT `mst_fk_email` FOREIGN KEY (`fk_email`) REFERENCES `email` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `birthday` date NOT NULL,
  `cmnd` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `district` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `ward` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `date_of_registration` date NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `is_enabled` bit(1) NOT NULL DEFAULT b'1',
  `fk_company` int unsigned DEFAULT NULL,
  `fk_company_user_role` int unsigned DEFAULT NULL,
  `gender` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- Update 27/07/2020

ALTER TABLE user
CHANGE birthday date_of_brith DATE NOT NULL,
CHANGE address street VARCHAR(500) NOT NULL;

ALTER TABLE company
CHANGE address street VARCHAR(500) NOT NULL,
CHANGE website website VARCHAR(255) NULL;

ALTER TABLE company_user_history
	ADD fk_user INT unsigned NOT NULL,
    ADD fk_company INT unsigned NOT NULL;

create table email_verify_token
(
	id int unsigned auto_increment,
	token varchar(255) not null,
	expiry datetime null,
	fk_user int unsigned not null,
	constraint email_verify_token_pk
		primary key (id)
);

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

ALTER TABLE user ADD fullname VARCHAR(500) NOT NULL;

ALTER TABLE user ADD cmnd_issue_date DATE NOT NULL,
CHANGE place cmnd_issue_place VARCHAR(255) NOT NULL;

CREATE TABLE files
(
	id INT unsigned auto_increment,
	original_filename VARCHAR(255) NOT NULL,
	upload_path VARCHAR(500) NOT NULL,
	content_type VARCHAR(255) NOT NULL,
	uploaded_by INT unsigned NOT NULL,
	uploaded_at datetime DEFAULT current_timestamp NOT NULL,
	CONSTRAINT files_pk
		PRIMARY KEY (id),
	CONSTRAINT files_user_id_fk
		FOREIGN KEY (uploaded_by) REFERENCES user (id)
);

CREATE UNIQUE index files_upload_path_uindex
	ON files (upload_path);

--  add column 'mst' in company table
ALTER TABLE company ADD fk_mst INT UNSIGNED NULL;
ALTER TABLE company ADD CONSTRAINT company_fk FOREIGN KEY (fk_mst) REFERENCES mst(id);

--  drop column 'company' in mst table
ALTER TABLE mst DROP FOREIGN KEY mst_fk_company;
ALTER TABLE mst DROP COLUMN fk_company;

-- drop column 'company', add column 'mst' in contract
ALTER TABLE contract DROP FOREIGN KEY contract_fk_company;
ALTER TABLE contract DROP COLUMN fk_company;
ALTER TABLE contract ADD fk_mst INT UNSIGNED NULL;
ALTER TABLE contract ADD CONSTRAINT contract_fk FOREIGN KEY (fk_mst) REFERENCES mst(id);

-- create table contract_history to store history when send contract between 2 person
DROP TABLE IF EXISTS `contract_message`;
CREATE TABLE `contract_message` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `fk_contract` int unsigned NOT NULL,
    `fk_mail` int unsigned NOT NULL,
    `message` varchar(500) DEFAULT NULL,
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `contract_message_fk` (`fk_contract`),
    KEY `contract_message_fk_1` (`fk_mail`),
    CONSTRAINT `contract_message_fk` FOREIGN KEY (`fk_contract`) REFERENCES `contract` (`id`),
    CONSTRAINT `contract_message_fk_1` FOREIGN KEY (`fk_mail`) REFERENCES `email` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--  Auto-generated SQL script #202008242257
INSERT INTO contract_status (name,created_at,updated_at) VALUES ('draft','2020-08-24 22:57:44','2020-08-24 22:57:44');
ALTER TABLE contract ADD bookmark_star BOOL DEFAULT false NOT NULL;
ALTER TABLE contract ADD expiry_date_signed DATE DEFAULT NULL;
--  Auto-generated SQL script #202008242307
INSERT INTO contract_status (name) VALUES ('invalid_cert');
INSERT INTO contract_status (name) VALUES ('invalid_algorithm');
INSERT INTO contract_status (name) VALUES ('invalid_signature');
INSERT INTO contract_status (name) VALUES ('expired_certificate');
INSERT INTO contract_status (name) VALUES ('revoked_certificate');
INSERT INTO contract_status (name) VALUES ('mismatch_tax_code');

ALTER TABLE `user` CHANGE date_of_brith date_of_birth date NOT NULL;
ALTER TABLE contract ADD fk_contract_message INT UNSIGNED NULL;
ALTER TABLE contract ADD fk_file INT UNSIGNED NULL;
ALTER TABLE contract ADD CONSTRAINT contract_fk_1 FOREIGN KEY (fk_file) REFERENCES files(id);

ALTER TABLE email_verify_token ADD is_active bit(1) NOT NULL DEFAULT b'1';
