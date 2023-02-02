CREATE TABLE `ligue` (
  `id_ligue` int(11) AUTO_INCREMENT,
  `nom_ligue` varchar(255) DEFAULT NULL,
 PRIMARY KEY (`id_ligue`)
) 
CREATE TABLE `employe` (
  `id_employe` INT(11)  AUTO_INCREMENT,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
`mail` varchar(255) DEFAULT NULL,
  `date_arrivee` DATE,
  `date_depart` DATE,
  `habilitation` INT(11),
  `id_ligue` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id_employee`)
) ;


ALTER TABLE `employe` ADD FOREIGN KEY  (`id_ligue`) REFERENCES `ligue` (`id_ligue`);
