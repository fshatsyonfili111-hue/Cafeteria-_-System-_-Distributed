-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: cafeteria
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cached_students`
--

DROP TABLE IF EXISTS `cached_students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cached_students` (
  `id_card` varchar(20) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `department` varchar(50) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `student_type` varchar(20) DEFAULT 'Cafe',
  `app_password` varchar(100) DEFAULT '123456',
  `photo_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cached_students`
--

LOCK TABLES `cached_students` WRITE;
/*!40000 ALTER TABLE `cached_students` DISABLE KEYS */;
INSERT INTO `cached_students` VALUES ('09','aa bb','Information Technology',24,'male','Active','Cafe','123456','images/09.jpg'),('1','fila fish','Information Technology',24,'male','Active','Cafe','123','images/1.jpg'),('1100','haftom filimon','Information Technology',99999,'male','Active','Cafe','0987654321','images/1100.jpg'),('1112','aa bb','Information Technology',24,'male','Active','Cafe','123','images/1112.jpg'),('12','bb aa','Information Technology',24,'male','Active','Cafe','123','images/12.jpg'),('13','fila fish','Information Technology',NULL,NULL,'Active','Cafe','123','images/13.jpg'),('130300','Flimon Fshatsion Teklay','Information Technology',24,'male','Inactive','Cafe','123456','images/130300.jpg'),('130525','Elias Shumuye Tewele','Information Technology',25,'male','Active','Cafe','123456','images/130525.jpg'),('155','fila fish','Information Technology',24,'male','Inactive','Cafe','123456','images/155.jpg'),('5','Flimon Fshatsion Teklay','Information Technology',24,'male','Inactive','Cafe','123','images/5.jpg'),('67','fiyori mehari','Information Technology',24,'famale','Active','Cafe','123456','images/67.jpg'),('9','Flimon Fshatsion Teklay','Information Technology',23,'male','Active','Cafe','1212','images/9.jpg');
/*!40000 ALTER TABLE `cached_students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campus_messages`
--

DROP TABLE IF EXISTS `campus_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campus_messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `message_body` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campus_messages`
--

LOCK TABLES `campus_messages` WRITE;
/*!40000 ALTER TABLE `campus_messages` DISABLE KEYS */;
INSERT INTO `campus_messages` VALUES (4,'FFD','fgsdfghfhgw','2026-04-26 08:31:05'),(5,'gfd','dfgfgdsg','2026-04-26 12:49:17'),(6,'gfdfdgdfgfd','gf','2026-04-26 12:49:34'),(7,'fila','gfhi','2026-04-26 12:49:53'),(8,'filafila ','hi','2026-04-26 12:50:13'),(9,'today not  a time how its work ','fish\r\n','2026-04-30 23:54:33'),(10,'yared ','to day is holiday','2026-05-08 09:44:03');
/*!40000 ALTER TABLE `campus_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `face_templates`
--

DROP TABLE IF EXISTS `face_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `face_templates` (
  `id_card` varchar(20) NOT NULL,
  `encoding_data` text,
  PRIMARY KEY (`id_card`),
  CONSTRAINT `face_templates_ibfk_1` FOREIGN KEY (`id_card`) REFERENCES `cached_students` (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `face_templates`
--

LOCK TABLES `face_templates` WRITE;
/*!40000 ALTER TABLE `face_templates` DISABLE KEYS */;
INSERT INTO `face_templates` VALUES ('09','FEAT_09_1778233066305'),('1100','FEAT_1100_1778253291939'),('155','FEAT_155_1778233164082'),('5','FEAT_5_1777587495272'),('67','FEAT_67_1778241281316'),('9','FEAT_9_1778233240154');
/*!40000 ALTER TABLE `face_templates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_card` varchar(20) DEFAULT NULL,
  `meal_type` varchar(20) DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `comments` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `comment` text,
  `feedback_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `feedback_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (1,'5','Dinner',5,NULL,'2026-04-30 20:36:46','goood dinner','2026-04-30'),(2,'9','Breakfast',3,NULL,'2026-04-30 23:51:05','','2026-04-30'),(3,'9','Lunch',4,NULL,'2026-04-30 23:51:44','not good','2026-04-30'),(4,'5','Dinner',5,NULL,'2026-04-30 23:53:14','good','2026-04-30'),(5,'5','Lunch',2,NULL,'2026-04-30 23:53:51','bad','2026-04-30'),(6,'5','Lunch',3,NULL,'2026-05-01 00:03:25','gdgdhdhd','2026-04-30'),(7,'5','Breakfast',4,NULL,'2026-05-07 17:38:02','good  yared ','2026-05-07'),(8,'1112','Lunch',4,NULL,'2026-05-08 09:32:06','eila','2026-05-08'),(9,'1112','Dinner',3,NULL,'2026-05-08 11:57:10','not good','2026-05-08');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meal_records`
--

DROP TABLE IF EXISTS `meal_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal_records` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_card` varchar(20) DEFAULT NULL,
  `meal_type` varchar(20) DEFAULT NULL,
  `meal_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_card` (`id_card`),
  CONSTRAINT `meal_records_ibfk_1` FOREIGN KEY (`id_card`) REFERENCES `cached_students` (`id_card`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meal_records`
--

LOCK TABLES `meal_records` WRITE;
/*!40000 ALTER TABLE `meal_records` DISABLE KEYS */;
INSERT INTO `meal_records` VALUES (1,'09','Lunch','2026-05-08'),(2,'67','Lunch','2026-05-08');
/*!40000 ALTER TABLE `meal_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_schedule`
--

DROP TABLE IF EXISTS `menu_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_schedule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `day_of_week` varchar(10) DEFAULT NULL,
  `meal_type` varchar(20) DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `menu_name` varchar(100) DEFAULT NULL,
  `description` text,
  `menu_photo` varchar(255) DEFAULT NULL,
  `is_override` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_schedule`
--

LOCK TABLES `menu_schedule` WRITE;
/*!40000 ALTER TABLE `menu_schedule` DISABLE KEYS */;
INSERT INTO `menu_schedule` VALUES (1,'Monday','Breakfast','07:00:00','08:00:00',NULL,'[Miyazya 25] tea and bread ','images/menu_1777579111372.jpg',0),(5,'Monday','Lunch','11:30:00','13:00:00',NULL,'msr wet','images/menu_1778236156482.jpg',0),(6,'Monday','Dinner','17:30:00','19:00:00',NULL,'beyeaynetu','images/menu_1777579620756.jpg',0),(7,'Tuesday','Breakfast','07:01:00','08:01:00',NULL,'1 bread + rice + tea','images/menu_1778236823736.jpg',0),(8,'Tuesday','Lunch','23:30:00','07:00:00',NULL,'shro wet','images/menu_1778236184816.jpg',0),(9,'Tuesday','Dinner','17:30:00','01:15:00',NULL,'shro wet','images/menu_1778235362428.jpg',0),(10,'Wednesday','Breakfast','07:00:00','08:00:00',NULL,'1 bread + tea','images/menu_1778235973323.jpg',0),(11,'Wednesday','Lunch','05:30:00','19:00:00',NULL,'dench wet','images/menu_1778235557996.jpg',0),(12,'Wednesday','Dinner','17:30:00','01:00:00',NULL,'msr wet','images/menu_1778235624395.jpg',0),(13,'Thursday','Breakfast','07:00:00','08:00:00',NULL,'1 bread + rice + tea','images/menu_1778235724463.jpg',0),(14,'Thursday','Lunch','11:30:00','13:00:00',NULL,'msr wet','images/menu_1778235801852.jpg',0),(15,'Thursday','Dinner','23:30:00','01:00:00',NULL,'dnch wet','images/menu_1778235854153.jpg',0),(16,'Friday','Breakfast','07:00:00','08:00:00',NULL,'1 bread + tea ','images/menu_1778235932411.jpg',0),(17,'Friday','Lunch','05:00:00','19:00:00',NULL,'dnch wet','images/menu_1778236026739.jpg',0),(18,'Friday','Dinner','08:00:00','10:00:00',NULL,'shro wet','images/menu_1778236087171.jpg',0),(19,'Saturday','Breakfast','07:00:00','20:00:00',NULL,'1 bread + tea + rice','images/menu_1778236258270.jpg',0),(20,'Saturday','Lunch','11:30:00','19:00:00',NULL,'key wet','images/menu_1778236492179.jpg',0),(21,'Monday','Dinner','17:30:00','07:36:00',NULL,'msr wet','images/menu_1778236596393.jpg',0),(22,'Sunday','Breakfast','07:00:00','20:00:00',NULL,'1 bread + tea + frfr','images/menu_1778236665901.jpg',0),(23,'Sunday','Lunch','11:30:00','19:00:00',NULL,'shro wet','images/menu_1778236726246.jpg',0),(24,'Sunday','Dinner','17:30:00','07:00:00',NULL,'key wet','images/menu_1778236772878.jpg',0);
/*!40000 ALTER TABLE `menu_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff_accounts`
--

DROP TABLE IF EXISTS `staff_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_accounts`
--

LOCK TABLES `staff_accounts` WRITE;
/*!40000 ALTER TABLE `staff_accounts` DISABLE KEYS */;
INSERT INTO `staff_accounts` VALUES (4,'cafe','123','Manager'),(5,'staff','123','Operator');
/*!40000 ALTER TABLE `staff_accounts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-14 11:32:02
