SELECT idCalendarioLezioni, dataInizio, CorsoidCorso
FROM CalendarioLezioni;

SELECT idClasse, capienza
FROM Classe;
SELECT idCliente, nome, cognome, dataNascita, email, numeroTelefono
FROM Cliente;
SELECT idCorso, linguaCorso, livelloCorso, numeroMaxPartecipanti, costo, DocenteidDocente, numeroIscritti
FROM Corso;
SELECT idCredenziali, username, password, ClienteidCliente
FROM Credenziali;
SELECT idDocente, nome, cognome
FROM Docente;
SELECT idIscrizione, dataIscrizione, annoAccademico, ClienteidCliente, CorsoidCorso, ClasseidClasse
FROM Iscrizione;
SELECT idLezione, dataLezione, orarioInizio, CalendarioLezionidCalendarioLezioni
FROM Lezione;
SELECT idPagamento, importo, statoPagamento, IscrizioneidIscrizione
FROM Pagamento;



-- INSERT
INSERT INTO CalendarioLezioni
(idCalendarioLezioni,
 dataInizio,
 CorsoidCorso)
VALUES
    (?,
     ?,
     ?);
INSERT INTO Classe
(idClasse,
 capienza)
VALUES
    (?,
     ?);
INSERT INTO Cliente
(idCliente,
 nome,
 cognome,
 dataNascita,
 email,
 numeroTelefono)
VALUES
    (?,
     ?,
     ?,
     ?,
     ?,
     ?);
INSERT INTO Corso
(idCorso,
 linguaCorso,
 livelloCorso,
 numeroMaxPartecipanti,
 costo,
 DocenteidDocente,
 numeroIscritti)
VALUES
    (?,
     ?,
     ?,
     ?,
     ?,
     ?,
     ?);
INSERT INTO Credenziali
(idCredenziali,
 username,
 password,
 ClienteidCliente)
VALUES
    (?,
     ?,
     ?,
     ?);
INSERT INTO Docente
(idDocente,
 nome,
 cognome)
VALUES
    (?,
     ?,
     ?);
INSERT INTO Iscrizione
(idIscrizione,
 dataIscrizione,
 annoAccademico,
 ClienteidCliente,
 CorsoidCorso,
 ClasseidClasse)
VALUES
    (?,
     ?,
     ?,
     ?,
     ?,
     ?);
INSERT INTO Lezione
(idLezione,
 dataLezione,
 orarioInizio,
 CalendarioLezionidCalendarioLezioni)
VALUES
    (?,
     ?,
     ?,
     ?);
INSERT INTO Pagamento
(idPagamento,
 importo,
 statoPagamento,
 IscrizioneidIscrizione)
VALUES
    (?,
     ?,
     ?,
     ?);

-- UPLocalDate
UPLocalDate CalendarioLezioni SET
                             dataInizio = ?,
                             CorsoidCorso = ?
WHERE
    idCalendarioLezioni = ?;
UPLocalDate Classe SET
    capienza = ?
WHERE
    idClasse = ?;
UPLocalDate Cliente SET
                   nome = ?,
                   cognome = ?,
                   dataNascita = ?,
                   email = ?,
                   numeroTelefono = ?
WHERE
    idCliente = ?;
UPLocalDate Corso SET
                 linguaCorso = ?,
                 livelloCorso = ?,
                 numeroMaxPartecipanti = ?,
                 costo = ?,
                 DocenteidDocente = ?,
                 numeroIscritti = ?
WHERE
    idCorso = ?;
UPLocalDate Credenziali SET
                       username = ?,
                       password = ?,
                       ClienteidCliente = ?
WHERE
    idCredenziali = ?;
UPLocalDate Docente SET
                   nome = ?,
                   cognome = ?
WHERE
    idDocente = ?;
UPLocalDate Iscrizione SET
                      dataIscrizione = ?,
                      annoAccademico = ?,
                      ClienteidCliente = ?,
                      CorsoidCorso = ?,
                      ClasseidClasse = ?
WHERE
    idIscrizione = ?;
UPLocalDate Lezione SET
                   dataLezione = ?,
                   orarioInizio = ?,
                   CalendarioLezionidCalendarioLezioni = ?
WHERE
    idLezione = ?;
UPLocalDate Pagamento SET
                     importo = ?,
                     statoPagamento = ?,
                     IscrizioneidIscrizione = ?
WHERE
    idPagamento = ?;

-- DELETE
DELETE FROM CalendarioLezioni
WHERE idCalendarioLezioni = ?;
DELETE FROM Classe
WHERE idClasse = ?;
DELETE FROM Cliente
WHERE idCliente = ?;
DELETE FROM Corso
WHERE idCorso = ?;
DELETE FROM Credenziali
WHERE idCredenziali = ?;
DELETE FROM Docente
WHERE idDocente = ?;
DELETE FROM Iscrizione
WHERE idIscrizione = ?;
DELETE FROM Lezione
WHERE idLezione = ?;
DELETE FROM Pagamento
WHERE idPagamento = ?;
