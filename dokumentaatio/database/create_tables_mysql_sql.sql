CREATE TABLE Kayttajat (
	id INT AUTO_INCREMENT NOT NULL,
	sahkoposti VARCHAR(60) NOT NULL,
	etunimi VARCHAR(30) NOT NULL,
	sukunimi VARCHAR(30) NOT NULL,
	PRIMARY KEY (id, sahkoposti)
)Engine=InnoDB;

CREATE TABLE Projektit (
	id INT AUTO_INCREMENT NOT NULL,
	nimi VARCHAR(100) NOT NULL,
	kuvaus TEXT NULL,
	luontipaiva TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
)Engine=InnoDB;

CREATE TABLE Roolit (
	id INT AUTO_INCREMENT NOT NULL,
	rooli VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
)Engine=InnoDB;

CREATE TABLE Statukset (
	id INT AUTO_INCREMENT NOT NULL,
	status VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
)Engine=InnoDB;

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
