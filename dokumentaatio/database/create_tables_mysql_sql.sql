CREATE TABLE Kayttaja (
	id INT AUTO_INCREMENT NOT NULL,
	sahkoposti VARCHAR(60) NOT NULL,
	etunimi VARCHAR(30) NOT NULL,
	sukunimi VARCHAR(30) NOT NULL,
	PRIMARY KEY (id, sahkoposti)
)Engine=InnoDB;

CREATE TABLE Merkinta (
	id INT AUTO_INCREMENT NOT NULL,
	kayttaja_id INT NOT NULL,
	paivamaara TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	tunnit DECIMAL(4,2) NOT NULL,
	kuvaus TEXT NULL,
	PRIMARY KEY (id, kayttaja_id),
	FOREIGN KEY (kayttaja_id) REFERENCES Kayttaja(id)
)Engine=InnoDB;