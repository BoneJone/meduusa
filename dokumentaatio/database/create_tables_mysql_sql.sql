CREATE TABLE Kayttajat (
	id INT AUTO_INCREMENT NOT NULL,
	sahkoposti VARCHAR(60) NOT NULL,
	etunimi VARCHAR(30) NOT NULL,
	sukunimi VARCHAR(30) NOT NULL,
	PRIMARY KEY (id, sahkoposti)
)Engine=InnoDB;

INSERT INTO `Kayttajat` VALUES (1,'Nico','Nico','Hagelberg');
INSERT INTO `Kayttajat` VALUES (2,'Jonni','Jonni','Aaltonen');
INSERT INTO `Kayttajat` VALUES (3,'Eetu','Eetu','Halinen');
INSERT INTO `Kayttajat` VALUES (4,'Matias','Matias','Hedenstam');
INSERT INTO `Kayttajat` VALUES (5,'Pasi','Pasi','Pesonen');

CREATE TABLE Projektit (
	id INT AUTO_INCREMENT NOT NULL,
	nimi VARCHAR(100) NOT NULL,
	kuvaus TEXT NULL,
	luontipaiva TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
)Engine=InnoDB;

INSERT INTO `Projektit` VALUES (1,'SP1 Tuntisovellus', 'SP1 Tuntisovelluksen webbisivun ja mobiiliappin koodausprojekti', '2016-08-25 08:00:00');

CREATE TABLE Roolit (
	id INT AUTO_INCREMENT NOT NULL,
	rooli VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
)Engine=InnoDB;

INSERT INTO `Roolit` VALUES (1,'siteadmin');
INSERT INTO `Roolit` VALUES (2,'projectadmin');
INSERT INTO `Roolit` VALUES (3,'projectuser');

CREATE TABLE Statukset (
	id INT AUTO_INCREMENT NOT NULL,
	status VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
)Engine=InnoDB;

INSERT INTO `Statukset` VALUES (1,'kutsuttu');
INSERT INTO `Statukset` VALUES (2,'aktiivinen');
INSERT INTO `Statukset` VALUES (3,'poistunut');

CREATE TABLE ProjektinJasenet (
	kayttaja_id INT NOT NULL,
	projekti_id INT NOT NULL,
	rooli_id INT NOT NULL,
	status_id INT NOT NULL,
	PRIMARY KEY (kayttaja_id, projekti_id),
	FOREIGN KEY (kayttaja_id) REFERENCES Kayttajat(id),
	FOREIGN KEY (projekti_id) REFERENCES Projektit(id),
	FOREIGN KEY (rooli_id) REFERENCES Roolit(id),
	FOREIGN KEY (status_id) REFERENCES Statukset(id)
)Engine=InnoDB;

INSERT INTO `ProjektinJasenet` VALUES (1,1,1,2);
INSERT INTO `ProjektinJasenet` VALUES (2,1,1,2);
INSERT INTO `ProjektinJasenet` VALUES (3,1,1,2);
INSERT INTO `ProjektinJasenet` VALUES (4,1,1,2);
INSERT INTO `ProjektinJasenet` VALUES (5,1,1,2);

CREATE TABLE Merkinnat (
	id INT AUTO_INCREMENT NOT NULL,
	kayttaja_id INT NOT NULL,
	projekti_id INT NOT NULL,
	paivamaara TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	tunnit DECIMAL(4,2) NOT NULL,
	kuvaus TEXT NULL,
	PRIMARY KEY (id, kayttaja_id),
	FOREIGN KEY (kayttaja_id) REFERENCES Kayttajat(id),
	FOREIGN KEY (projekti_id) REFERENCES Projektit(id)
)Engine=InnoDB;

INSERT INTO `Merkinnat` VALUES (1,1,1,'2016-08-25 08:52:00',1.00,'Koodasin lujaa prototyyppiä');
INSERT INTO `Merkinnat` VALUES (2,3,1,'2016-08-25 09:35:40',1.00,'Opiskelin Nicon lujaa koodausta');
INSERT INTO `Merkinnat` VALUES (3,1,1,'2016-08-25 13:47:58',1.00,'Protosivulle Skeleton boilerplate läpällä');
INSERT INTO `Merkinnat` VALUES (4,5,1,'2016-08-30 07:43:17',1.00,'Hengailin eclipse auki');
INSERT INTO `Merkinnat` VALUES (5,3,1,'2016-09-01 07:19:56',1.00,'Hieno layout drafti');
INSERT INTO `Merkinnat` VALUES (6,4,1,'2016-09-01 08:01:22',1.00,'Osallistuin dokumentointiin ja suunnitteluun');
INSERT INTO `Merkinnat` VALUES (7,1,1,'2016-09-01 11:36:54',1.00,'Kirjoitin softaan Slack-integraation');
INSERT INTO `Merkinnat` VALUES (8,5,1,'2016-09-01 06:05:53',1.00,'Miekin tutustuin koodiin');
INSERT INTO `Merkinnat` VALUES (9,5,1,'2016-09-01 08:56:15',3.00,'Projektisuunnitelma tjsp');
INSERT INTO `Merkinnat` VALUES (10,1,1,'2016-09-07 16:10:39',2.50,'Projekti Maven-projektiksi + yleistä koodausta');
INSERT INTO `Merkinnat` VALUES (11,1,1,'2016-09-07 18:00:11',1.50,'Käyttäjäkohtainen tietojen haku ja näyttö');
INSERT INTO `Merkinnat` VALUES (12,5,1,'2016-09-08 07:47:29',1.00,'Git-suunnitelma');
INSERT INTO `Merkinnat` VALUES (13,5,1,'2016-09-08 08:42:55',1.00,'Kaikenlaista hölmöilyä');
INSERT INTO `Merkinnat` VALUES (14,1,1,'2016-09-08 09:23:01',1.00,'Git ohje ja opastus, suunnittelu, coachaus');
INSERT INTO `Merkinnat` VALUES (15,4,1,'2016-09-08 09:23:20',1.00,'Tutustuin koodiin ja lisäsin poistonapin front-endiin');
INSERT INTO `Merkinnat` VALUES (16,3,1,'2016-09-08 09:27:58',2.00,'Android sovelluksen alustusta');
INSERT INTO `Merkinnat` VALUES (17,2,1,'2016-09-08 09:28:46',1.00,'Tuijotti React Native koodeja ja shelliä');
INSERT INTO `Merkinnat` VALUES (18,1,1,'2016-09-08 14:55:27',2.00,'Poistotoiminto tehty ja kommentoitu koodia');
INSERT INTO `Merkinnat` VALUES (19,1,1,'2016-09-09 14:29:17',1.25,'Ilmoitusten näyttö ja mahdollisuus valita alle 1h työtä');
INSERT INTO `Merkinnat` VALUES (20,1,1,'2016-09-13 07:18:53',0.75,'Sprint suunnittelu ja tiimin coachaus');
INSERT INTO `Merkinnat` VALUES (21,2,1,'2016-09-13 07:43:59',1.25,'Perehdyin Skeletoncss ja tein virhe ilmoitus sivun');
INSERT INTO `Merkinnat` VALUES (22,4,1,'2016-09-13 07:44:41',1.50,'Kirjoittelin testitapausten tyyppisiä');
INSERT INTO `Merkinnat` VALUES (23,5,1,'2016-09-13 07:49:08',1.50,'Opettelin gitiä, siirsin sql -datan erillisiin filuihin ja kaikkee sellaista.');
INSERT INTO `Merkinnat` VALUES (24,3,1,'2016-09-13 08:42:56',3.00,'React-Native opiskelua & meduusatuntikirjaus sovelluksen koodausta');
INSERT INTO `Merkinnat` VALUES (25,1,1,'2016-09-15 08:50:44',2.50,'Pasin kanssa JDBC-templaten käyttöönottoa');
INSERT INTO `Merkinnat` VALUES (26,5,1,'2016-09-15 08:50:56',3.00,'Koodasin lujaa');
INSERT INTO `Merkinnat` VALUES (27,4,1,'2016-09-15 09:01:18',2.00,'Osallistuin jdbc muutokseen Nikon ja Pasin\r\nkanssa ');
INSERT INTO `Merkinnat` VALUES (28,1,1,'2016-09-15 17:07:03',1.50,'JDBC-muutoksen viimeistely kontrolleriin, daoon ja .jsp');
INSERT INTO `Merkinnat` VALUES (29,5,1,'2016-09-20 08:27:49',2.50,'Dokumentointi, koodin läpikäynti, vanhojen luokkien poisto');
INSERT INTO `Merkinnat` VALUES (30,1,1,'2016-09-20 12:43:38',0.50,'Käyttötapauskuvaukset, koodin läpikäynti');
INSERT INTO `Merkinnat` VALUES (31,3,1,'2016-09-21 14:30:18',2.00,'Mobiilisovellus tutkimusmatkailua');
INSERT INTO `Merkinnat` VALUES (32,5,1,'2016-09-21 17:35:42',1.00,'System.out.printit pois ja tilalle loggeri.');
INSERT INTO `Merkinnat` VALUES (33,4,1,'2016-09-22 06:00:30',1.00,'Dokumentointii tiistailta');
INSERT INTO `Merkinnat` VALUES (34,1,1,'2016-09-22 07:31:13',0.50,'Sprint review demotus');
INSERT INTO `Merkinnat` VALUES (35,5,1,'2016-09-22 08:00:57',0.25,'Punotukset pois virhesivulta');
INSERT INTO `Merkinnat` VALUES (36,5,1,'2016-09-23 18:39:30',1.50,'Tein demograafit plot.lyllä.');
INSERT INTO `Merkinnat` VALUES (37,1,1,'2016-09-24 12:33:28',0.75,'Plot.ly tutustumista ja pientä ulkoasun hiomista');
INSERT INTO `Merkinnat` VALUES (38,1,1,'2016-09-27 08:23:12',2.25,'Spring MVC:tä käyttöön');
INSERT INTO `Merkinnat` VALUES (39,3,1,'2016-09-27 08:49:18',2.50,'Front-end suunnittelua ja koodausta Bulmantulalla');
INSERT INTO `Merkinnat` VALUES (40,2,1,'2016-09-27 08:49:46',2.50,'Front-end suunnittelua ja toteutusta bullmantulalle. Login page myös.');
INSERT INTO `Merkinnat` VALUES (41,4,1,'2016-09-27 08:49:57',2.00,'Osallistuin Spring MCV  vaihtoon');
INSERT INTO `Merkinnat` VALUES (42,5,1,'2016-09-27 09:11:36',2.25,'Spring MVC:tä käyttöön');
INSERT INTO `Merkinnat` VALUES (43,1,1,'2016-09-29 07:46:22',2.00,'Spring MVC käyttöönoton viimeistely');
INSERT INTO `Merkinnat` VALUES (44,5,1,'2016-09-29 07:47:27',2.00,'Spring MVC käyttöönoton viimeistely');
INSERT INTO `Merkinnat` VALUES (45,4,1,'2016-09-29 08:01:02',2.00,'Spring MVC käyttöönoton viimeistely');
INSERT INTO `Merkinnat` VALUES (46,5,1,'2016-09-29 09:10:28',1.00,'Plot.ly:n testailua ja Nicon koodauksen ihailua.');
INSERT INTO `Merkinnat` VALUES (47,2,1,'2016-09-29 09:21:14',3.25,'Front-end suunnittelua ');
INSERT INTO `Merkinnat` VALUES (48,3,1,'2016-09-29 10:08:30',4.00,'Bulma front-endia');
INSERT INTO `Merkinnat` VALUES (53,1,1,'2016-10-02 10:46:12',8.00,'React Native appin eka versio');
