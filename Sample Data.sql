-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: mybnb
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `amenities`
--

DROP TABLE IF EXISTS `amenities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amenities` (
  `idamenities` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `amount` int DEFAULT '0',
  PRIMARY KEY (`idamenities`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amenities`
--

LOCK TABLES `amenities` WRITE;
/*!40000 ALTER TABLE `amenities` DISABLE KEYS */;
INSERT INTO `amenities` VALUES (1,'Wifi',12),(2,'Washer',0),(3,'Air conditioning',3),(4,'Dedicated workspace',0),(5,'Hair dryer',0),(6,'Kitchen',2),(7,'Dryer',0),(8,'Heating',3),(9,'TV',5),(10,'Iron',0),(11,'Pool',2),(12,'Free parking',2),(13,'Crib',0),(14,'BBQ grill',2),(15,'Indoor fireplace',0),(16,'Hot tub',1),(17,'EV charger',0),(18,'Gym',1),(19,'Breakfast',0),(20,'Smoking allowed',1),(21,'Beachfront',1),(22,'Waterfront',0),(23,'Ski-in/ski-out',0),(24,'Smoke alarm',0),(25,'Carbon monoxide alarm',0);
/*!40000 ALTER TABLE `amenities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `idbookings` int NOT NULL AUTO_INCREMENT,
  `idlistings` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `status` int DEFAULT NULL,
  `total_cost` double DEFAULT NULL,
  PRIMARY KEY (`idbookings`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (1,3,'1995-07-28','1995-07-28',2,0),(2,3,'1995-07-29','1995-07-29',2,0),(3,3,'1995-07-27','1995-07-30',3,0),(4,4,'1995-07-28','1995-07-29',3,0),(5,4,'1995-07-30','1995-08-01',2,0),(6,7,'1911-04-05','1912-04-15',4,450),(7,3,'2024-01-03','2024-01-05',1,100),(8,8,'2024-07-12','2024-07-15',1,240),(9,8,'2024-06-05','2024-06-11',1,480),(10,12,'2024-02-01','2024-02-15',1,840),(11,12,'2024-01-08','2024-01-09',1,60),(12,10,'2024-09-02','2024-09-08',1,330);
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `iduser` int NOT NULL,
  `idbookings` int NOT NULL,
  PRIMARY KEY (`iduser`,`idbookings`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (4,1),(4,2),(4,5),(5,3),(6,4),(9,6),(10,7),(13,11),(14,9),(14,10),(14,12),(15,8);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `idcomment` int NOT NULL AUTO_INCREMENT,
  `commenter` int NOT NULL,
  `commentee` int NOT NULL,
  `rating` double DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idcomment`,`commenter`,`commentee`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,9,8,5,'Smart Guy'),(2,6,7,1,'It\'s On!');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `has`
--

DROP TABLE IF EXISTS `has`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `has` (
  `idlistings` int NOT NULL,
  `idamenities` int NOT NULL,
  PRIMARY KEY (`idlistings`,`idamenities`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `has`
--

LOCK TABLES `has` WRITE;
/*!40000 ALTER TABLE `has` DISABLE KEYS */;
INSERT INTO `has` VALUES (1,1),(1,8),(1,9),(2,1),(2,11),(3,1),(3,9),(3,11),(4,1),(4,12),(5,1),(5,9),(5,14),(6,1),(6,18),(7,1),(7,9),(7,21),(8,1),(8,8),(8,12),(9,1),(9,8),(9,16),(9,20),(10,1),(10,3),(10,6),(11,1),(11,3),(12,1),(12,3),(12,6),(12,9),(12,14);
/*!40000 ALTER TABLE `has` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hosts`
--

DROP TABLE IF EXISTS `hosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hosts` (
  `iduser` int NOT NULL,
  `idlistings` int NOT NULL,
  PRIMARY KEY (`iduser`,`idlistings`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hosts`
--

LOCK TABLES `hosts` WRITE;
/*!40000 ALTER TABLE `hosts` DISABLE KEYS */;
INSERT INTO `hosts` VALUES (2,1),(2,5),(3,6),(7,2),(7,3),(7,4),(8,7),(11,8),(11,9),(11,11),(12,10),(12,12);
/*!40000 ALTER TABLE `hosts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listings`
--

DROP TABLE IF EXISTS `listings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listings` (
  `idlistings` int NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `longi` double DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `price_per_day` double DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `unavailable_dates` json DEFAULT NULL,
  PRIMARY KEY (`idlistings`),
  UNIQUE KEY `address_UNIQUE` (`address`),
  UNIQUE KEY `lat_UNIQUE` (`lat`),
  UNIQUE KEY `long_UNIQUE` (`longi`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listings`
--

LOCK TABLES `listings` WRITE;
/*!40000 ALTER TABLE `listings` DISABLE KEYS */;
INSERT INTO `listings` VALUES (1,'House','1 Toronto St',12.3456,12.3457,'A1B2C3','Toronto','CA',50,'2024-01-01','2024-12-31','[\"2024-01-02\"]'),(2,'Apartment','5 CN Tower St',12.3457,12.3455,'A1B2C4','Toronto','CA',50,'2024-01-01','2024-12-01','[\"2024-12-25\", \"2024-12-26\"]'),(3,'House','1 Empire St',15.2346,11.1256,'B1C3Z4','NYC','US',70,'2024-01-01','2024-12-01','[]'),(4,'Apartment','5 Stature Blvd',15.2348,11.1255,'B1C3Z6','NYC','US',70,'2024-01-01','2024-12-01','[]'),(5,'House','2 Seattle St',23.5649,29.8843,'H1Y7Q2','Seattle','US',60,'2024-01-01','2024-12-01','[]'),(6,'Apartment','5 Seattle Blvd',23.5647,29.8842,'H1Y7Q8','Seattle','US',60,'2024-01-01','2024-12-01','[]'),(7,'Townhouse','221A Baker St',37.8235,42.5673,'L1O2N3','London','UK',45,'2024-01-01','2024-12-01','[]'),(8,'House','135 Sicily St',37.4867,15.0579,'D2B3F7','Catania','IT',80,'2024-01-01','2024-12-01','[]'),(9,'House','155 Romeo Blvd',37.5662,17.2537,'D2B3G9','Florence','IT',80,'2024-01-01','2024-12-01','[]'),(10,'House','233 Effile St',40.1234,20.2356,'C3N6M8','Paris','FR',55,'2024-01-01','2024-12-01','[]'),(11,'Apartment','19 Franco Cir',42.5884,20.1899,'J8K9L3','Barcelona','ES',40,'2024-01-01','2024-12-01','[]'),(12,'Townhouse','280 West Blvd',40.1229,20.2348,'A6M7C3','Berlin','DE',60,'2024-01-01','2024-12-01','[]');
/*!40000 ALTER TABLE `listings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `idreview` int NOT NULL AUTO_INCREMENT,
  `iduser` int NOT NULL,
  `idlistings` int NOT NULL,
  `rating` double DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idreview`,`iduser`,`idlistings`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,5,3,4,'Good House'),(2,4,4,2,'Bad Apartment'),(3,6,4,1,'Terrible Apartment'),(4,4,3,3,'OK House'),(5,9,7,5,'Great Townhouse');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `iduser` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `SIN` int DEFAULT NULL,
  `occupation` varchar(255) DEFAULT NULL,
  `credit_number` bigint DEFAULT NULL,
  `credit_password` varchar(255) DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `email_UNIQUE` (`username`),
  UNIQUE KEY `SIN_UNIQUE` (`SIN`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'admin'),(2,'Geller1','2222','Ross Geller','123 friend st','1980-03-24',9802,'palentologist',NULL,NULL,'host'),(3,'Green1','3333','Rachel Green','321 friend st','1985-04-22',1567,'Sales',NULL,NULL,'host'),(4,'Bing1','4444','Chandler Bing','231 friend st','1980-08-10',8852,'Transponder',131,'313','renter'),(5,'Trib1','5555','Joey Tribbiany','213 friend st','1981-03-03',4457,'Actor',525,'252','renter'),(6,'Geller2','6666','Monica Geller','132 friend st','1985-02-16',8848,'Chef',383,'838','renter'),(7,'Buffay1','7777','Pheobe Buffay','312 friend st','1982-09-17',5768,'Massuse',NULL,NULL,'host'),(8,'Holmes1','8888','Sherlock Holmes','221B Baker St','1888-03-15',2210,'Detective',NULL,NULL,'host'),(9,'Watson1','9999','John Watson','221B Baker St','1887-06-02',3355,'Doctor',181,'818','renter'),(10,'Solo1','1010','Hans Solo','123 Space Blvd','1977-05-25',2456,'Smuggler',727,'272','renter'),(11,'Don1','1111','Vito Corleone','542 Mafia Circle','1891-12-07',1546,'Godfather',NULL,NULL,'host'),(12,'Blaine1','1212','Rick Blaine','765 Cafe Blvd','1892-08-01',3722,'Cafe Owner',NULL,NULL,'host'),(13,'McClane1','1313','John McClane','288 Hard St','1948-05-23',5321,'Police Officer',919,'191','renter'),(14,'Bond1','1414','James Bond','455 No Blvd','1912-03-25',6792,'Spy',393,'939','renter'),(15,'Jones1','1515','Indiana Jones','818 Indy St','1911-02-02',6558,'Archaeologist',737,'373','renter');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-07 22:01:32
