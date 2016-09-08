CREATE TABLE Kayttaja (
	id INT AUTO_INCREMENT NOT NULL,
	sahkoposti VARCHAR(60) NOT NULL,
	etunimi VARCHAR(30) NOT NULL,
	sukunimi VARCHAR(30) NOT NULL,
	PRIMARY KEY (id, sahkoposti)
)Engine=InnoDB;

INSERT INTO Kayttaja (sahkoposti, etunimi, sukunimi) VALUES ('Nico', 'Nico', 'Hagelberg');
INSERT INTO Kayttaja (sahkoposti, etunimi, sukunimi) VALUES ('Jonni', 'Jonni', 'Aaltonen');
INSERT INTO Kayttaja (sahkoposti, etunimi, sukunimi) VALUES ('Eetu', 'Eetu', 'Halinen');
INSERT INTO Kayttaja (sahkoposti, etunimi, sukunimi) VALUES ('Matias', 'Matias', 'Hedenstam');
INSERT INTO Kayttaja (sahkoposti, etunimi, sukunimi) VALUES ('Pasi', 'Pasi', 'Pesonen');

CREATE TABLE Merkinta (
	id INT AUTO_INCREMENT NOT NULL,
	kayttaja_id INT NOT NULL,
	paivamaara TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	tunnit DECIMAL(4,2) NOT NULL,
	kuvaus TEXT NULL,
	PRIMARY KEY (id, kayttaja_id),
	FOREIGN KEY (kayttaja_id) REFERENCES Kayttaja(id)
)Engine=InnoDB;

INSERT INTO `Merkinta` (kayttaja_id, paivamaara, tunnit, kuvaus) VALUES
(1,'2016-08-25 11:52:00',1,'Koodasin lujaa prototyyppiä'),
(3,'2016-08-25 12:35:40',1,'Opiskelin Nicon lujaa koodausta'),
(1,'2016-08-25 16:47:58',1,'Protosivulle Skeleton boilerplate läpällä'),
(5,'2016-08-30 10:43:17',1,'Hengailin eclipse auki'),
(3,'2016-09-01 10:19:56',1,'Hieno layout drafti'),
(4,'2016-09-01 11:01:22',1,'Osallistuin dokumentointiin ja suunnitteluun'),
(1,'2016-09-01 14:36:54',1,'Kirjoitin softaan Slack-integraation'),
(5,'2016-09-01 09:05:53',1,'Miekin tutustuin koodiin'),
(5,'2016-09-01 11:56:15',3,'Projektisuunnitelma tjsp');