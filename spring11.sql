CREATE DATABASE  IF NOT EXISTS `todo` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `todo`;
DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(100) NOT NULL,
  `status` int(11) NOT NULL,
  `password` varchar(100),
  `secretinfo` varchar(100),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT  IGNORE INTO `task` VALUES (1,'aaa',1,'1111','aaaa'),(2,'bbb',2,'1111','aaaa'),(3,'ccc',0,'1111','aaaa'),(4,'ddd',1,'1111','aaaa'),(5,'eee',2,'1111','aaaa'),(6,'fff',0,'1111','aaaa'),(7,'ggg',1,'1111','aaaa'),(8,'hhh',2,'1111','aaaa'),(9,'jjj',0,'1111','aaaa'),(10,'kkk',1,'1111','aaaa'),(11,'lll',2,'1111','aaaa'),(12,'mmm',0,'1111','aaaa'),(13,'nnn',1,'1111','aaaa'),(14,'ooo',2,'1111','aaaa'),(15,'ppp',0,'1111','aaaa');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;