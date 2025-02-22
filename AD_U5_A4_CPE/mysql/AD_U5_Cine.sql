-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 51.210.43.140    Database: AD_U5_Cine
-- ------------------------------------------------------
-- Server version	8.0.28-0ubuntu0.20.04.3

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
-- Table structure for table `Director`
--

USE Cine;

DROP TABLE IF EXISTS `Director`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Director` (
  `idDirector` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `nacionalidad` varchar(10) DEFAULT NULL,
  `anyoNacimiento` int NOT NULL,
  PRIMARY KEY (`idDirector`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Director`
--

LOCK TABLES `Director` WRITE;
/*!40000 ALTER TABLE `Director` DISABLE KEYS */;
INSERT INTO `Director` VALUES (1,'George Lucas','EEUU',1944),(2,'JJ Abrams','EEUU',1966),(4,'Rian Johnson','EEUU',1973),(5,'Ron Howard','EEUU',1954),(6,'Gareth Edwards','EEUU',1975),(39,'pepe',NULL,1987);
/*!40000 ALTER TABLE `Director` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pelicula`
--

DROP TABLE IF EXISTS `Pelicula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Pelicula` (
  `idPelicula` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(45) NOT NULL,
  `anyo` varchar(45) NOT NULL,
  `idDirector` int DEFAULT NULL,
  PRIMARY KEY (`idPelicula`),
  KEY `Director_Peli_fk_1` (`idDirector`),
  CONSTRAINT `Director_Peli_fk_1` FOREIGN KEY (`idDirector`) REFERENCES `Director` (`idDirector`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pelicula`
--

LOCK TABLES `Pelicula` WRITE;
/*!40000 ALTER TABLE `Pelicula` DISABLE KEYS */;
INSERT INTO `Pelicula` VALUES (1,'La Amenaza Fantasma','1999',1),(2,'El Ataque de los Clones','2002',1),(3,'La Venganza de los Sith','2005',1),(4,'Una Nueva Esperanza','1977',1),(5,'El Imperio Contraataca','1980',1),(6,'El Retorno del Jedi','1984',1),(7,'El Despertar de la Fuerza','2015',2),(8,'Los Últimos Jedi','2017',4),(9,'Rogue One: A Star Wars Story','2016',6),(10,'Solo: A Star Wars Story','2019',5),(11,'El Ascenso de Skywalker','2018',2),(12,'Indiana Jones: Raiders of the Lost ARk','1981',NULL),(13,'Indiana Jones: The Temple of Doom','1984',NULL);
/*!40000 ALTER TABLE `Pelicula` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-01 19:21:57
