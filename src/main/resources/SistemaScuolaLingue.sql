CREATE TABLE CalendarioLezioni (
                                   idCalendarioLezioni SERIAL NOT NULL,
                                   dataInizio LocalDate NOT NULL CHECK ((EXTRACT(MONTH FROM dataInizio)) > 9),
                                   CorsoidCorso int4 NOT NULL,
                                   PRIMARY KEY (idCalendarioLezioni)
);

CREATE TABLE Classe (
                        idClasse SERIAL NOT NULL,
                        capienza int4 NOT NULL,
                        PRIMARY KEY (idClasse)
);
CREATE TABLE Cliente (
                         idCliente      SERIAL NOT NULL,
                         nome           varchar(50) NOT NULL,
                         cognome        varchar(50) NOT NULL,
                         dataNascita    LocalDate NOT NULL,
                         email          varchar(100) NOT NULL UNIQUE CHECK(email LIKE '%@%.%'),
                         numeroTelefono varchar(20) CHECK(numeroTelefono ~ '^\+?[0-9 ]+$'),
                         PRIMARY KEY (idCliente));
CREATE TABLE Corso (
                       idCorso               SERIAL NOT NULL,
                       linguaCorso           varchar(30) NOT NULL,
                       livelloCorso          varchar(10) NOT NULL,
                       numeroMaxPartecipanti int4 NOT NULL,
                       costo                 numeric(8, 2) NOT NULL,
                       DocenteidDocente      int4 NOT NULL,
                       numeroIscritti        int4 NOT NULL,
                       PRIMARY KEY (idCorso));

CREATE TABLE Credenziali (
                             idCredenziali    SERIAL NOT NULL,
                             username         varchar(50) NOT NULL UNIQUE,
                             password         varchar(255) NOT NULL,
                             ClienteidCliente int4 NOT NULL,
                             PRIMARY KEY (idCredenziali));
CREATE TABLE Docente (
                         idDocente SERIAL NOT NULL,
                         nome      varchar(60) NOT NULL,
                         cognome   int4 NOT NULL,
                         PRIMARY KEY (idDocente));
CREATE TABLE Iscrizione (
                            idIscrizione     SERIAL NOT NULL,
                            dataIscrizione   LocalDate NOT NULL,
                            annoAccademico   varchar(9) DEFAULT (EXTRACT(YEAR FROM CURRENT_LocalDate)) NOT NULL,
                            ClienteidCliente int4 NOT NULL,
                            CorsoidCorso  int4 NOT NULL,
                            ClasseidClasse   int4,
                            PRIMARY KEY (idIscrizione));
CREATE TABLE Lezione (
                         idLezione SERIAL NOT NULL,
                         dataLezione LocalDate NOT NULL CHECK (EXTRACT(MONTH FROM dataLezione) >= 10),
                         orarioInizio time NOT NULL CHECK(orarioInizio <= '20:00:00' AND orarioInizio >= '10:00:00'),
                         CalendarioLezionidCalendarioLezioni int4 NOT NULL,
                         PRIMARY KEY (idLezione)
);
CREATE TABLE Pagamento (
                           idPagamento            SERIAL NOT NULL,
                           importo                numeric(8, 2) NOT NULL,
                           statoPagamento         int4 NOT NULL,
                           IscrizioneidIscrizione int4 NOT NULL,
                           PRIMARY KEY (idPagamento));

CREATE INDEX Credenziali_idCredenziali
    ON Credenziali (idCredenziali);
ALTER TABLE Lezione ADD CONSTRAINT FKLezione976396 FOREIGN KEY (CalendarioLezionidCalendarioLezioni) REFERENCES CalendarioLezioni (idCalendarioLezioni);
ALTER TABLE Iscrizione ADD CONSTRAINT FKIscrizione220196 FOREIGN KEY (ClasseidClasse) REFERENCES Classe (idClasse);
ALTER TABLE CalendarioLezioni ADD CONSTRAINT FKCalendario564959 FOREIGN KEY (CorsoidCorso) REFERENCES Corso (idCorso);
ALTER TABLE Corso ADD CONSTRAINT FKCorso380059 FOREIGN KEY (DocenteidDocente) REFERENCES Docente (idDocente);
ALTER TABLE Iscrizione ADD CONSTRAINT FKIscrizione566173 FOREIGN KEY (CorsoidCorso) REFERENCES Corso (idCorso);
ALTER TABLE Pagamento ADD CONSTRAINT FKPagamento971393 FOREIGN KEY (IscrizioneidIscrizione) REFERENCES Iscrizione (idIscrizione);
ALTER TABLE Iscrizione ADD CONSTRAINT FKIscrizione492030 FOREIGN KEY (ClienteidCliente) REFERENCES Cliente (idCliente);
ALTER TABLE Credenziali ADD CONSTRAINT FKCredenzial396955 FOREIGN KEY (ClienteidCliente) REFERENCES Cliente (idCliente);



