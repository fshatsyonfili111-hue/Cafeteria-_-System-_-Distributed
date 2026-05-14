-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: registrar_db
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
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `id_card` varchar(20) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `department` varchar(50) DEFAULT NULL,
  `faculty` varchar(50) DEFAULT NULL,
  `academic_year` int DEFAULT NULL,
  `status` varchar(10) DEFAULT 'Active',
  `admission_date` date DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `photo_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('09','aa','bb',24,'male','Information Technology','computing ',3,'Active','2023-02-02','2004-02-04','images/09.jpg'),('1','fila','fish',24,'male','Information Technology','computing ',3,'Active','2023-02-02','2004-02-04','images/1.jpg'),('1100','haftom','filimon',99999,'male','Information Technology','computing ',3,'Active','2023-02-02','2004-02-04','images/1100.jpg'),('1112','aa','bb',24,'male','Information Technology','computing ',3,'Active','2023-02-02','2004-02-04','images/1112.jpg'),('12','bb','aa',24,'male','Information Technology','computing ',3,'Active','2023-02-02','2004-02-04','images/12.jpg'),('13','fila','fish',24,'male','Information Technology','computing ',3,'Active','2023-02-02','2004-02-04','images/13.jpg'),('130300','Flimon','Fshatsion Teklay',24,'male','Information Technology','computing ',3,'Inactive','2023-02-02','2004-02-04','images/130300.jpg'),('130525','Elias','Shumuye Tewele',25,'male','Information Technology','computing ',2,'Active','2023-02-02','2004-02-04','images/130525.jpg'),('155','fila','fish',24,'male','Information Technology','computing ',3,'Inactive','2023-02-02','2004-02-04','images/155.jpg'),('5','Flimon','Fshatsion Teklay',24,'male','Information Technology','computing ',3,'Inactive','2023-02-02','2004-02-04','images/5.jpg'),('67','fiyori','mehari',24,'famale','Information Technology','computing ',3,'Active','2023-02-02','2004-02-04','images/67.jpg'),('9','Flimon','Fshatsion Teklay',23,'male','Information Technology','computing ',3,'Active','2023-02-02','2004-02-04','images/9.jpg');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-14 11:32:54
