-- Sample data for SistemaScuolaLingue
-- This file is optional and can be used for initial data loading

-- Insert sample courses
INSERT INTO EntityCorso (id, linguaCorso, livelloCorso, costo, numeroMassimoPartecipanti) VALUES
(1, 'Inglese', 'A1', 100.00, 20),
(2, 'Inglese', 'A2', 120.00, 20),
(3, 'Inglese', 'B1', 150.00, 20),
(4, 'Spagnolo', 'A1', 100.00, 20),
(5, 'Francese', 'A1', 100.00, 20)
ON CONFLICT (id) DO NOTHING;

-- Insert sample docenti (teachers)
INSERT INTO EntityDocente (id, nome, cognome, email, telefono) VALUES
(1, 'Maria', 'Rossi', 'm.rossi@example.com', '3331234567'),
(2, 'Giovanni', 'Bianchi', 'g.bianchi@example.com', '3339876543'),
(3, 'Laura', 'Verdi', 'l.verdi@example.com', '3335551234')
ON CONFLICT (id) DO NOTHING;

-- Insert sample classi (classes)
INSERT INTO EntityClasse (id, idCorso, idDocente, capienza, numeroIscritti) VALUES
(1, 1, 1, 20, 5),
(2, 2, 2, 20, 8),
(3, 3, 3, 20, 3),
(4, 4, 1, 20, 0),
(5, 5, 2, 20, 0)
ON CONFLICT (id) DO NOTHING;