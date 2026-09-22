-- Table for impiegati segreteria (added for gestore management functionality)
CREATE TABLE IF NOT EXISTS impiegati_segreteria (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);