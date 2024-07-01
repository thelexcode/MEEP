-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Lug 14, 2023 alle 11:50
-- Versione del server: 10.4.28-MariaDB
-- Versione PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `meep`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `accompagnatore`
--

CREATE TABLE `accompagnatore` (
  `id` int(11) NOT NULL,
  `nominativo` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `accompagnatore`
--

INSERT INTO `accompagnatore` (`id`, `nominativo`, `email`) VALUES
(7, 'Armir Trupja', 'semeniuc.jobs@gmail.com');

-- --------------------------------------------------------

--
-- Struttura della tabella `evento`
--

CREATE TABLE `evento` (
  `codice_evento` int(11) NOT NULL,
  `data` date NOT NULL,
  `descrizione` varchar(255) NOT NULL,
  `max_accompagnatori` int(11) NOT NULL,
  `max_partecipanti` int(11) NOT NULL,
  `ora_fine` time(6) NOT NULL,
  `ora_inizio` time(6) NOT NULL,
  `thumbnail` varchar(100) DEFAULT NULL,
  `titolo` varchar(30) NOT NULL,
  `utente_admin` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `evento`
--

INSERT INTO `evento` (`codice_evento`, `data`, `descrizione`, `max_accompagnatori`, `max_partecipanti`, `ora_fine`, `ora_inizio`, `thumbnail`, `titolo`, `utente_admin`) VALUES
(5, '2023-08-05', 'Esposizione di opere d\'arte con cena', 3, 12, '22:00:00.000000', '07:00:00.000000', 'mostra.jpg', 'Mostra d\'Arte', '10006'),
(6, '2023-08-05', 'Party in piscina', 2, 10, '23:00:00.000000', '20:00:00.000000', 'drink5.jpg', 'Beach Party', '10003'),
(7, '2023-08-05', 'Festa di compleanno', 4, 15, '23:00:00.000000', '19:00:00.000000', 'compleanno.jpg', 'Compleanno!', '10007'),
(8, '2023-07-25', 'Serata di pizzata gourmet', 0, 4, '22:00:00.000000', '19:30:00.000000', 'pizza2.jpg', 'Pizzata Gourmet!', '10007'),
(10, '2023-08-02', 'una serata all insegna dei grandi film italiani', 1, 10, '23:00:00.000000', '20:00:00.000000', 'cinema1.jpg', 'Venezia film festival', '10003'),
(21, '2023-07-20', 'Dramma, azione, fantascienza o commedia                ', 3, 15, '21:33:00.000000', '19:33:00.000000', '21_serietv.jpg', 'Una Notte di Maratone TV', '0000000'),
(22, '2023-07-25', 'Unisciti a noi per una serata di puro intrattenimento', 2, 8, '16:33:00.000000', '07:33:00.000000', '22_videogiochi.jpg', 'Esplorando Mondi Virtuali ', '0000000'),
(23, '2023-07-16', 'A Tavola con i Personaggi', 2, 9, '20:43:00.000000', '16:43:00.000000', '23_cenatematica.jpg', 'Cena tematica !', '10006'),
(24, '2023-07-28', 'personaggi fantastici e  avventure indimenticabili.               ', 2, 15, '22:45:00.000000', '13:45:00.000000', '24_giochidiruolo.jpg', 'Serata giochi di ruolo ', '10008'),
(25, '2023-07-28', 'Una varietà di sapori e prelibatezze', 2, 15, '22:45:00.000000', '13:45:00.000000', '25_seratadegustazione.jpg', ' Sapori in un Viaggio Epico', '10008');

-- --------------------------------------------------------

--
-- Struttura stand-in per le viste `evento_partecipazioni`
-- (Vedi sotto per la vista effettiva)
--
CREATE TABLE `evento_partecipazioni` (
`codice_evento` int(11)
,`utente_admin` varchar(20)
,`titolo` varchar(30)
,`descrizione` varchar(255)
,`thumbnail` varchar(100)
,`data` date
,`ora_inizio` time(6)
,`ora_fine` time(6)
,`max_partecipanti` int(11)
,`max_accompagnatori` int(11)
,`n_partecipanti` bigint(21)
);

-- --------------------------------------------------------

--
-- Struttura della tabella `evento_tipologia`
--

CREATE TABLE `evento_tipologia` (
  `id` int(11) NOT NULL,
  `codice_evento` int(11) NOT NULL,
  `id_tipologia` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `evento_tipologia`
--

INSERT INTO `evento_tipologia` (`id`, `codice_evento`, `id_tipologia`) VALUES
(4, 5, 8),
(5, 7, 6),
(6, 7, 7),
(7, 7, 5),
(8, 6, 4),
(9, 8, 10),
(10, 8, 6),
(11, 10, 2),
(14, 21, 3),
(15, 22, 5),
(16, 23, 8),
(17, 24, 9),
(18, 25, 10);

-- --------------------------------------------------------

--
-- Struttura della tabella `indirizzo`
--

CREATE TABLE `indirizzo` (
  `cap` varchar(5) NOT NULL,
  `comune` varchar(50) NOT NULL,
  `provincia` varchar(2) NOT NULL,
  `via` varchar(50) NOT NULL,
  `matricola` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `indirizzo`
--

INSERT INTO `indirizzo` (`cap`, `comune`, `provincia`, `via`, `matricola`) VALUES
('00185', 'Roma', 'RM', 'Via Dante Alighieri 25', '0000000'),
('20121', 'Milano', 'MI', 'Via Roma 10', '10003'),
('10121', 'Torino', 'TO', 'Corso Vittorio Emanuele II  50', '10004'),
('40121', 'Bologna', 'BO', 'Via Garibaldi 15', '10005'),
('80132', 'Napoli', 'NA', 'Piazza del Plebiscito 5 ', '10006'),
('40124', 'Bologna ', 'BO', 'Piazza Maggiore  20', '10007'),
('20121', 'Milano', 'MI', 'Via Montenapoleone 15', '10008'),
('10124', 'Torino', 'TO', 'Via Giuseppe Verdi, 10', '10009'),
('12000', 'Genova', 'GE', 'VIA XX SETTEMBRE 20', 'BIAANT03');

-- --------------------------------------------------------

--
-- Struttura della tabella `partecipazione`
--

CREATE TABLE `partecipazione` (
  `id` bigint(20) NOT NULL,
  `codice_qr` text DEFAULT NULL,
  `codice_evento` int(11) NOT NULL,
  `codice_utente` varchar(20) NOT NULL,
  `id_accompagnatore` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `partecipazione`
--

INSERT INTO `partecipazione` (`id`, `codice_qr`, `codice_evento`, `codice_utente`, `id_accompagnatore`) VALUES
(42, 'iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAACO0lEQVR4Xu2XQW7cMAxFqZWPoZta9k11hC69kvof5SlmlADppihMiHAMhXwOoK9P2rH+N/HL5sy3sbApFjbFwqZY2BQLm+JfYM2Ifev9yufF4jBLvZLdYmGsK1VPnvq5rPRa7lIkrNpWd8ssLHfX5NSDQ6WAGElp0m59wmIFHWoC6OeVj5hYRwGTsVkYmgj+avLnY0bsXnq/yMbCXlHTlZupnTPrt0IYTKXGuev08bbdHpDVJ90ej3VePXrVMpwTTX0DzZlQGK9a6ZB9oajGBAuINT6cEKRpWF359PlsboMSDGPj6mXOXXx/nX5CmVgYewdufGBUGAfQh3ocTKHt7+bhVeMCDoZJkNPJhMOtuMmHLDghErbdjHi/XBbD8/bZCyGw3JhaIhDkdIA89TgY4SYvnXYed/OnSjDs0qeFfC5jU0rD5DxV348+BtY8n/50NEmwPRrmxna3c/QujqmvJ5NHwLx/PRJu59fUxdQSDFNGxqbKdbgUfg+H4W1Ov1nu3JnPJL+Y/OlY48SRorsBCifOyGqsQ2G9M7Uc4MT77XBpkkc5EEYcZvumqaV2HprQzikW1mz4uSYYeQCTN5doNvnDsRFtzKsNKca6+T99kbCx9+K9nF76mH9yFP8LcTB2reR4GTGrT5ygPPdYmG8fBTS19NrF26hEPiBWkEIAJj84dHX3p8kjYTfQT4CafFDHwnTj3OEJZ1yfFAzz2v4q7Ya9i8TZPk3+fOzHWNgUC5tiYVMsbIqFTfF/sN++ef04W8UJBgAAAABJRU5ErkJggg==', 7, '0000000', NULL),
(43, 'iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAACO0lEQVR4Xu2XQW7cMAxFqZWPoZta9k11hC69kvof5SlmlADppihMiHAMhXwOoK9P2rH+N/HL5sy3sbApFjbFwqZY2BQLm+JfYM2Ifev9yufF4jBLvZLdYmGsK1VPnvq5rPRa7lIkrNpWd8ssLHfX5NSDQ6WAGElp0m59wmIFHWoC6OeVj5hYRwGTsVkYmgj+avLnY0bsXnq/yMbCXlHTlZupnTPrt0IYTKXGuev08bbdHpDVJ90ej3VePXrVMpwTTX0DzZlQGK9a6ZB9oajGBAuINT6cEKRpWF359PlsboMSDGPj6mXOXXx/nX5CmVgYewdufGBUGAfQh3ocTKHt7+bhVeMCDoZJkNPJhMOtuMmHLDghErbdjHi/XBbD8/bZCyGw3JhaIhDkdIA89TgY4SYvnXYed/OnSjDs0qeFfC5jU0rD5DxV348+BtY8n/50NEmwPRrmxna3c/QujqmvJ5NHwLx/PRJu59fUxdQSDFNGxqbKdbgUfg+H4W1Ov1nu3JnPJL+Y/OlY48SRorsBCifOyGqsQ2G9M7Uc4MT77XBpkkc5EEYcZvumqaV2HprQzikW1mz4uSYYeQCTN5doNvnDsRFtzKsNKca6+T99kbCx9+K9nF76mH9yFP8LcTB2reR4GTGrT5ygPPdYmG8fBTS19NrF26hEPiBWkEIAJj84dHX3p8kjYTfQT4CafFDHwnTj3OEJZ1yfFAzz2v4q7Ya9i8TZPk3+fOzHWNgUC5tiYVMsbIqFTfF/sN++ef04W8UJBgAAAABJRU5ErkJggg==', 7, '0000000', 7);

-- --------------------------------------------------------

--
-- Struttura della tabella `tipologia`
--

CREATE TABLE `tipologia` (
  `id_tipologia` int(11) NOT NULL,
  `denominazione` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `tipologia`
--

INSERT INTO `tipologia` (`id_tipologia`, `denominazione`) VALUES
(2, 'Serata Cinema'),
(3, 'Serie TV'),
(4, 'Party'),
(5, 'Videogiochi'),
(6, 'Pizza'),
(7, 'Karaoke'),
(8, 'Cena tematica'),
(9, 'Gioco di ruolo'),
(10, 'Serata di degustazione');

-- --------------------------------------------------------

--
-- Struttura della tabella `user`
--

CREATE TABLE `user` (
  `matricola` varchar(20) NOT NULL,
  `biografia` varchar(255) NOT NULL,
  `cognome` varchar(50) NOT NULL,
  `data_nascita` datetime(6) NOT NULL,
  `email` varchar(50) NOT NULL,
  `foto_profilo` varchar(100) NOT NULL,
  `nome` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `username` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `user`
--

INSERT INTO `user` (`matricola`, `biografia`, `cognome`, `data_nascita`, `email`, `foto_profilo`, `nome`, `password`, `telefono`, `username`) VALUES
('0000000', 'Backend DEV', 'Semeniuc', '2023-06-29 11:12:59.000000', 'gabidenisa63@gmail.com', '0000000_Gabriel.jpg', 'Gabriel', 'f150d20ba4193568e8483206f0e8c70d38e210fd60f06dbe332b54b024cb5165', '3284767843', 'Gabriel'),
('10003', 'Let\'s meet up!\r\n', 'Rossi', '1993-06-20 16:31:55.000000', 'rossi.mario9@gmail.it', 'defaultfoto.jpg', 'Mario', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '+393333234426', 'MarioRoss'),
('10004', 'Let\'s meet up!', 'Michele', '1993-06-20 00:00:00.000000', 'armir.trupja@edu.itspiemonte.it', 'defaultfoto.jpg', 'Leone', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '+393231230021', 'ElProfessor'),
('10005', 'Let\'s meet up!', 'Bianchi', '1996-09-10 00:00:00.000000', 'luca@bianchi.it', 'defaultfoto.jpg', 'Luca', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '+393474280101', 'BlancoLuke'),
('10006', 'Let\'s meet up!', 'Trupja', '2002-10-26 00:00:00.000000', 'ale@bianchi.it', 'defaultfoto.jpg', 'Armir', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '+393367238028', 'Armir'),
('10007', 'Let\'s meet up!', 'Motal', '2002-11-02 00:00:00.000000', 'gabriel.semeniuc@edu.itspiemonte.it', 'defaultfoto.jpg', 'Matias', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '+393882236721', 'Mot123'),
('10008', 'Let\'s meet up!\r\n', 'Tamburano', '2023-06-16 10:59:43.000000', 'alessio.tamburrano@edu.itspiemonte.it', 'defaultfoto.jpg', 'Alessio', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '+393284767846', 'MrAle'),
('10009', 'Let\'s meet up!\r\n', 'Semeniuc', '2023-06-15 23:55:51.000000', 'gabidenisa62@yahoo.it', 'defaultfoto.jpg', 'Adrian', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '3284767843', 'Adrian66'),
('BIAANT03', 'Let\'s meet up!', 'Alberti', '2003-06-13 12:05:19.000000', 'antonella.alberti@edu.itspiemonte.it', 'BIAANT03_Antonella.png', 'Biagina Antonella', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '+39 3333333', NULL);

-- --------------------------------------------------------

--
-- Struttura per vista `evento_partecipazioni`
--
DROP TABLE IF EXISTS `evento_partecipazioni`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `evento_partecipazioni`  AS SELECT `e`.`codice_evento` AS `codice_evento`, `e`.`utente_admin` AS `utente_admin`, `e`.`titolo` AS `titolo`, `e`.`descrizione` AS `descrizione`, `e`.`thumbnail` AS `thumbnail`, `e`.`data` AS `data`, `e`.`ora_inizio` AS `ora_inizio`, `e`.`ora_fine` AS `ora_fine`, `e`.`max_partecipanti` AS `max_partecipanti`, `e`.`max_accompagnatori` AS `max_accompagnatori`, count(`p`.`codice_evento`) AS `n_partecipanti` FROM (`evento` `e` left join `partecipazione` `p` on(`p`.`codice_evento` = `e`.`codice_evento`)) GROUP BY `e`.`codice_evento` ;

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `accompagnatore`
--
ALTER TABLE `accompagnatore`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `evento`
--
ALTER TABLE `evento`
  ADD PRIMARY KEY (`codice_evento`),
  ADD KEY `FK6bkmrolcn9wdvg05pa2brxlkl` (`utente_admin`);

--
-- Indici per le tabelle `evento_tipologia`
--
ALTER TABLE `evento_tipologia`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKi0a404cijtrfry24m2jv1co7d` (`codice_evento`),
  ADD KEY `FK43jdtktet6pofv9epq6l3uebf` (`id_tipologia`);

--
-- Indici per le tabelle `indirizzo`
--
ALTER TABLE `indirizzo`
  ADD PRIMARY KEY (`matricola`);

--
-- Indici per le tabelle `partecipazione`
--
ALTER TABLE `partecipazione`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK2b6lsa727fidlxeko2l03lm1f` (`codice_evento`),
  ADD KEY `FKwheurnqy15w5e3hfvspmurup` (`codice_utente`),
  ADD KEY `FK3j7c3qmlkn6mr1kh4p15m6xwa` (`id_accompagnatore`);

--
-- Indici per le tabelle `tipologia`
--
ALTER TABLE `tipologia`
  ADD PRIMARY KEY (`id_tipologia`);

--
-- Indici per le tabelle `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`matricola`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `accompagnatore`
--
ALTER TABLE `accompagnatore`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT per la tabella `evento`
--
ALTER TABLE `evento`
  MODIFY `codice_evento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT per la tabella `evento_tipologia`
--
ALTER TABLE `evento_tipologia`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT per la tabella `partecipazione`
--
ALTER TABLE `partecipazione`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT per la tabella `tipologia`
--
ALTER TABLE `tipologia`
  MODIFY `id_tipologia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `evento`
--
ALTER TABLE `evento`
  ADD CONSTRAINT `FK6bkmrolcn9wdvg05pa2brxlkl` FOREIGN KEY (`utente_admin`) REFERENCES `user` (`matricola`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `evento_tipologia`
--
ALTER TABLE `evento_tipologia`
  ADD CONSTRAINT `FK43jdtktet6pofv9epq6l3uebf` FOREIGN KEY (`id_tipologia`) REFERENCES `tipologia` (`id_tipologia`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FKi0a404cijtrfry24m2jv1co7d` FOREIGN KEY (`codice_evento`) REFERENCES `evento` (`codice_evento`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `indirizzo`
--
ALTER TABLE `indirizzo`
  ADD CONSTRAINT `FKjc6leod42gbo41vbfjddtc7f2` FOREIGN KEY (`matricola`) REFERENCES `user` (`matricola`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `partecipazione`
--
ALTER TABLE `partecipazione`
  ADD CONSTRAINT `FK2b6lsa727fidlxeko2l03lm1f` FOREIGN KEY (`codice_evento`) REFERENCES `evento` (`codice_evento`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK3j7c3qmlkn6mr1kh4p15m6xwa` FOREIGN KEY (`id_accompagnatore`) REFERENCES `accompagnatore` (`id`),
  ADD CONSTRAINT `FKwheurnqy15w5e3hfvspmurup` FOREIGN KEY (`codice_utente`) REFERENCES `user` (`matricola`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
