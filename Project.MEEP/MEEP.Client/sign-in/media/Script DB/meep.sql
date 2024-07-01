-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Lug 05, 2023 alle 17:15
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
(2, '2023-06-30', '                Ciaooo', 0, 10, '16:46:00.000000', '13:46:00.000000', 'defaultfoto.png', 'Gabriel', '0000000'),
(5, '2023-07-05', 'Esposizione di opere d\'arte con cena', 3, 12, '22:00:00.000000', '07:00:00.000000', 'defaultfoto.png', 'Mostra d\'Arte', '10006'),
(6, '2023-08-05', 'Party in piscina', 2, 10, '23:00:00.000000', '20:00:00.000000', 'drink5.jpg', 'Beach Party', 'BIAANT03'),
(7, '2023-08-05', 'Festa di compleanno', 4, 15, '23:00:00.000000', '19:00:00.000000', 'pizza3.jpg', 'Compleanno!', '10007'),
(8, '2023-07-25', 'Serata di pizzata gourmet', 0, 4, '22:00:00.000000', '19:30:00.000000', 'pizza2.jpg', 'Pizzata Gourmet!', '10007'),
(10, '2023-07-02', 'una serata all insegna dei grandi film italiani', 1, 10, '23:00:00.000000', '20:00:00.000000', 'cinema1.jpg', 'Venezia film festival', '10003'),
(11, '2023-11-09', 'ciao', 2, 4, '23:00:00.000000', '18:00:00.000000', 'copertina.jpg', 'prova1', 'BIAANT03');

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
(11, 10, 2);

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

-- --------------------------------------------------------

--
-- Struttura della tabella `partecipazione`
--

CREATE TABLE `partecipazione` (
  `id` bigint(20) NOT NULL,
  `codice_qr` text DEFAULT NULL,
  `codice_evento` int(11) NOT NULL,
  `codice_utente` varchar(20) NOT NULL,
  `is_accompagnatore` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `partecipazione`
--

INSERT INTO `partecipazione` (`id`, `codice_qr`, `codice_evento`, `codice_utente`, `is_accompagnatore`) VALUES
(16, 'iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAACGUlEQVR4Xu2YO27DQAxEKbhw6SPsUXQ06Wg6io+Q0oVhZma4zmdjICkDzLKhvPvkgiCHpCL/Ym8xnry0iQ02scEmNtjEBvv/2BG0lnucbhHrnnm74FinsTliemyZjzjT5R3u2JbPOzssdI+44WTNJRm+XO4IpjMGd0IGHZH3uFxjmxguKnxx5rEzxkfcM4PawbPXlWWDlfb2uH24H9prgz1th+gyUpJgBaybG8bUUVtmLcE9TkokKo4nRqPospbYjIghgx7MLk8MGcQiCmYQXIluPDTNOWIM2O1yXbOPK2pNuRNr+hs3DI04WUvB9nNlBklxFlaWFMcOg8acec+LKzNK+aQohiWWXP5ajStB+sxDJBKzyxA7QqM9R9hqy3Ic9PGuJybRXSXBCp/qTAVmiT2XHN0HexIKbOUvlpslxn6MLpSSGm6EvKfUMK0MMfRj9R0MbupCvNBL36caH6xPJjslWIN+lRS7s8wNo6qolnqk+hgnCfbEVEQImFbBmlp0XbQjViNssDsLg9RwFRwXARtM3bmJplNlHYiglNgQ68a+o0SqKG6srK+J5IMpRPzS2CNVmw/dt2ZkhG18bMdz64NLYFTiVnd2GNfgVlMLehJpvhRSHGMM3blrDBx/DeOKH6b5DStP5ZOa9ItEcsD42OrDGkwFtiaVuOnADmOw+PU59QW278Zb/XLEfrWJDTaxwSY22MQGM8LeAQtA+YHSR3XTAAAAAElFTkSuQmCC', 5, '0000000', b'0'),
(17, 'iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAACRklEQVR4Xu2XQW7sIBBE2ysfg5vC+KY+wl+ygtRr0PwZFCnZRFFatCwPNI8F1QV4rH8n/tma+TQ2tsTGltjYEhtbYmNL/ATWjCj9ziePnf1xKn2TpREI86SAUpXnXbrIu8yhSNjNqCclxcOSGpcmmuWIGEmzo6bL1TjiYqWmDoAglycjYh2TW3IdtLtT89KXORQIMyJLgfeHbCxsRjUZ++BkTn6O/R8JgyEIy09XZTuLLzzq4vZg2MEvPu/cR10APteUWJgABBnXLnuZI8vc9uYGiINVHVmcVNmv3WfXRjcS1pGiUHo3+ehWQ5ZYmPzc5tpdEzxP42JuLMxQ46DitzQY4pjv8dfSR8BON7bn+7TBPM0YCoW5vdEEk8+uTH6+HVwRMJJJ72y8DXsL5n0xHgtj/1J0czX0mKWO7WNhAMlFuA+fZd54Rhis4+1R/US7o4kyivwiSAys8EPp5fOHuTj+vnw8DNa4djG5MYTDM2JImfUy+vOYsW2bG/sBjD6XDDC/OgJhdO+MsRHk8K8Lt8GbySNghpkFZJfFvOiaK00K44EwjmgtP3UvfUaE+6hoYm+6BcC0dvzchtVrGhOHLMEw6VA6Hugc1xiemOLEwUYgQifPYVXT0Of14AqANV99Nv710ECHGzUMOBRGe7gaTbSR8zQ8o7GwsXz274XJp0TmmZBYntX378an4SNiXn0/vjAAd3E4TC9hqbkOpaY2pPBGKIxyU/fGnSuTI46U6csHxt/HvoyNLbGxJTa2xMaW2NgSv4N9ABRPfay7hM6MAAAAAElFTkSuQmCC', 7, 'BIAANT03', b'0'),
(18, 'iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAACRklEQVR4Xu2XQW7sIBBE2ysfg5vC+KY+wl+ygtRr0PwZFCnZRFFatCwPNI8F1QV4rH8n/tma+TQ2tsTGltjYEhtbYmNL/ATWjCj9ziePnf1xKn2TpREI86SAUpXnXbrIu8yhSNjNqCclxcOSGpcmmuWIGEmzo6bL1TjiYqWmDoAglycjYh2TW3IdtLtT89KXORQIMyJLgfeHbCxsRjUZ++BkTn6O/R8JgyEIy09XZTuLLzzq4vZg2MEvPu/cR10APteUWJgABBnXLnuZI8vc9uYGiINVHVmcVNmv3WfXRjcS1pGiUHo3+ehWQ5ZYmPzc5tpdEzxP42JuLMxQ46DitzQY4pjv8dfSR8BON7bn+7TBPM0YCoW5vdEEk8+uTH6+HVwRMJJJ72y8DXsL5n0xHgtj/1J0czX0mKWO7WNhAMlFuA+fZd54Rhis4+1R/US7o4kyivwiSAys8EPp5fOHuTj+vnw8DNa4djG5MYTDM2JImfUy+vOYsW2bG/sBjD6XDDC/OgJhdO+MsRHk8K8Lt8GbySNghpkFZJfFvOiaK00K44EwjmgtP3UvfUaE+6hoYm+6BcC0dvzchtVrGhOHLMEw6VA6Hugc1xiemOLEwUYgQifPYVXT0Of14AqANV99Nv710ECHGzUMOBRGe7gaTbSR8zQ8o7GwsXz274XJp0TmmZBYntX378an4SNiXn0/vjAAd3E4TC9hqbkOpaY2pPBGKIxyU/fGnSuTI46U6csHxt/HvoyNLbGxJTa2xMaW2NgSv4N9ABRPfay7hM6MAAAAAElFTkSuQmCC', 7, 'BIAANT03', b'1'),
(19, 'iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAACRklEQVR4Xu2XQW7sIBBE2ysfg5vC+KY+wl+ygtRr0PwZFCnZRFFatCwPNI8F1QV4rH8n/tma+TQ2tsTGltjYEhtbYmNL/ATWjCj9ziePnf1xKn2TpREI86SAUpXnXbrIu8yhSNjNqCclxcOSGpcmmuWIGEmzo6bL1TjiYqWmDoAglycjYh2TW3IdtLtT89KXORQIMyJLgfeHbCxsRjUZ++BkTn6O/R8JgyEIy09XZTuLLzzq4vZg2MEvPu/cR10APteUWJgABBnXLnuZI8vc9uYGiINVHVmcVNmv3WfXRjcS1pGiUHo3+ehWQ5ZYmPzc5tpdEzxP42JuLMxQ46DitzQY4pjv8dfSR8BON7bn+7TBPM0YCoW5vdEEk8+uTH6+HVwRMJJJ72y8DXsL5n0xHgtj/1J0czX0mKWO7WNhAMlFuA+fZd54Rhis4+1R/US7o4kyivwiSAys8EPp5fOHuTj+vnw8DNa4djG5MYTDM2JImfUy+vOYsW2bG/sBjD6XDDC/OgJhdO+MsRHk8K8Lt8GbySNghpkFZJfFvOiaK00K44EwjmgtP3UvfUaE+6hoYm+6BcC0dvzchtVrGhOHLMEw6VA6Hugc1xiemOLEwUYgQifPYVXT0Of14AqANV99Nv710ECHGzUMOBRGe7gaTbSR8zQ8o7GwsXz274XJp0TmmZBYntX378an4SNiXn0/vjAAd3E4TC9hqbkOpaY2pPBGKIxyU/fGnSuTI46U6csHxt/HvoyNLbGxJTa2xMaW2NgSv4N9ABRPfay7hM6MAAAAAElFTkSuQmCC', 7, 'BIAANT03', b'1'),
(20, 'iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAACO0lEQVR4Xu2XQW71IAyEzSrH4KbwclOO0GVWuDMDTVX6S383VRULC0XE+XhShsHOM/9JvNma+WdsbImNLbGxJTa2xMaW+A2sG6N6KweHHf46kG7MchIIUxJAvZDntTrIVuejSFjjUyUhxcsyJicWmpWIGJNm6cqn1EhxsXplJ0BBTiUjYk6TW5YOON25a+vrfBQIM0aBAl8Hs7GwGZfB2ImVOauOfT4Jg1EQvn4+Lx5n8JUDt3R7MAyvDwY+d/YjzOVzLAmHUZDRdnmWWbJMtjcZIAyGGO/e1Xb9QgVj4bJxGwnDRkuTorbrTmUqnYBkKKyjDVmWJqxXL2MFG7av+pkw2FSAe92gwZwTXhzyeAz7XuZHxb2EySTbx8KyDm9LakZ9un0tXAEwV6UCrGFGe0ufr203AqYvKCiATTepgWGWx/JIGEWQFAW7P241uSMMNklKAREM4hSZ3LgwFDbsTT/jCH80JmbWz+PHYzB50R92ASA5EOlbz3o61g8/+d048g1AxS3XRsNcnQgjXRQkucihTCysw9gHBUGyH2TSVKlV/UocDA6nCNm19YUi3L0pHqaqJUFgAGbk+R4OM+nQjc0I4hRTTHHiYCO6CRjF6qLt+1q4Ho91vX3BoZ4icHCiYx4K4xw+b2lcp9unw2Nhw+TAWKjrh0SmWh0Qu3ef13zeho+H3WXZstMAIONhuExBoEO9ch9SaBIK43Zb4fnlWa5aAmV8+cB4Pvbf2NgSG1tiY0tsbImNLfE32Du5pWwYu14zXAAAAABJRU5ErkJggg==', 7, 'BIAANT03', b'0');

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
('0000000', 'Let\'s Meet up!', 'Prova', '2023-06-29 11:12:59.000000', 'gabidenisa62@yahoo.it', '', 'Test', '9a720f48dcd51638a622406a857196b5155f388b02644c210a5dd560c523a436', '3284767843', 'Prova12345'),
('10003', 'ciaoo', 'Rossi', '1993-06-20 16:31:55.000000', 'rossi.mario9@gmail.it', 'defaultprofilo.png', 'Mario', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '+393333234426', NULL),
('10004', 'Let\'s meet up!', 'Trupja', '1993-06-20 00:00:00.000000', 'armir.trupja@edu.itspiemonte.it', 'defaultprofilo.png', 'Armir', '263f0dde07011bb9f35dc42d7a92ba51b03fbad7a34bc34e6d8760349376731a', '+393231230021', 'Armirrrrrr'),
('10005', 'Let\'s meet up!', 'Bianchi', '1996-09-10 00:00:00.000000', 'luca@bianchi.it', 'defaultprofilo.png', 'Luca', 'ff8bd36a066c4214aaaec94574e68986df88eb091e4a70a14b870a947dfb98d3', '+393474280101', 'BlancoLuke'),
('10006', 'Let\'s meet up!', 'Trupja', '2002-10-26 00:00:00.000000', 'ale@bianchi.it', 'defaultprofilo.png', 'Armir', '999c8d874b9c413e04c1acd4b09d049e703d927b44d389648af131a1abd1e933', '+393367238028', 'Armirrr'),
('10007', 'Let\'s meet up!', 'Semeniuc', '2002-11-02 00:00:00.000000', 'gabriel.semeniuc@edu.itspiemonte.it', 'defaultprofilo.png', 'Gabriel', 'fc1519f330c43be537a6209b8628413f74e41c921764e74bcca3eab105f56e17', '+393882236721', 'Gabriel12345'),
('12345678', 'Ciao', 'Tamburano', '2023-06-16 10:59:43.000000', 'alessio.tamburrano@edu.itspiemonte.it', '', 'Alessio', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '', NULL),
('123456789', 'Ciaoo', 'Semeniuc', '2023-06-15 23:55:51.000000', 'gabidenisa63@gmail.com', '', 'Adrian', '9a720f48dcd51638a622406a857196b5155f388b02644c210a5dd560c523a436', '3284767843', 'Adrian66'),
('BIAANT03', 'Let\'s Meet up!', 'Alberti', '2003-06-13 12:05:19.000000', 'antonella.alberti@edu.itspiemonte.it', 'defaultfoto.jpg', 'Biagina Antonella', '4db6ae07d3b2f1c7c51e234f05b47e95e527860aedf6e78338a55bd31ceff5ab', '+39 999999', 'YAYAY');

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
  ADD KEY `FKwheurnqy15w5e3hfvspmurup` (`codice_utente`);

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
-- AUTO_INCREMENT per la tabella `evento`
--
ALTER TABLE `evento`
  MODIFY `codice_evento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT per la tabella `evento_tipologia`
--
ALTER TABLE `evento_tipologia`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT per la tabella `partecipazione`
--
ALTER TABLE `partecipazione`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

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
  ADD CONSTRAINT `FKwheurnqy15w5e3hfvspmurup` FOREIGN KEY (`codice_utente`) REFERENCES `user` (`matricola`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
