-- Création de la base de données
CREATE DATABASE IF NOT EXISTS PlanningCampus;
USE PlanningCampus;

-- Suppression des tables si elles existent déjà
DROP TABLE IF EXISTS note_etudiant;
DROP TABLE IF EXISTS etudiant_cours;
DROP TABLE IF EXISTS cours;
DROP TABLE IF EXISTS salle;
DROP TABLE IF EXISTS horaire;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS etudiant;
DROP TABLE IF EXISTS matiere_enseignant;
DROP TABLE IF EXISTS enseignant;
DROP TABLE IF EXISTS administrateur;
DROP TABLE IF EXISTS utilisateur;

-- Création de la table utilisateur
CREATE TABLE utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    motDePasse VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL -- 'admin', 'enseignant', 'etudiant'
);

-- Création de la table administrateur
CREATE TABLE administrateur (
    id INT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- Création de la table enseignant
CREATE TABLE enseignant (
    id INT PRIMARY KEY,
    datePriseFonction DATE,
    FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- Création de la table matiere_enseignant
CREATE TABLE matiere_enseignant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_enseignant INT,
    matiere VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_enseignant) REFERENCES enseignant(id) ON DELETE CASCADE
);

-- Création de la table etudiant
CREATE TABLE etudiant (
    id INT PRIMARY KEY,
    groupe VARCHAR(50),
    FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- Création de la table salle
CREATE TABLE salle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    capacite INT NOT NULL,
    localisation VARCHAR(100) DEFAULT 'Non spécifiée',
    equipements TEXT
);

-- Création de la table horaire
CREATE TABLE horaire (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jour VARCHAR(20) NOT NULL,
    heureDebut TIME NOT NULL,
    heureFin TIME NOT NULL,
    semaine INT NOT NULL,
    date DATE DEFAULT NULL
);

-- Création de la table cours
CREATE TABLE cours (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matiere VARCHAR(100) NOT NULL,
    id_enseignant INT,
    id_horaire INT,
    id_salle INT,
    groupe VARCHAR(50), -- Groupe d'étudiants assigné au cours
    FOREIGN KEY (id_enseignant) REFERENCES enseignant(id) ON DELETE SET NULL,
    FOREIGN KEY (id_horaire) REFERENCES horaire(id) ON DELETE CASCADE,
    FOREIGN KEY (id_salle) REFERENCES salle(id) ON DELETE SET NULL
);

-- Création de la table etudiant_cours (relation many-to-many)
CREATE TABLE etudiant_cours (
    id_etudiant INT,
    id_cours INT,
    PRIMARY KEY (id_etudiant, id_cours),
    FOREIGN KEY (id_etudiant) REFERENCES etudiant(id) ON DELETE CASCADE,
    FOREIGN KEY (id_cours) REFERENCES cours(id) ON DELETE CASCADE
);

-- Création de la table note_etudiant
CREATE TABLE note_etudiant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_etudiant INT,
    id_cours INT,
    note FLOAT NOT NULL,
    FOREIGN KEY (id_etudiant) REFERENCES etudiant(id) ON DELETE CASCADE,
    FOREIGN KEY (id_cours) REFERENCES cours(id) ON DELETE CASCADE
);

-- Création de la table notification
CREATE TABLE notification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    contenu TEXT NOT NULL,
    id_destinataire INT,
    id_expediteur INT DEFAULT NULL,
    date DATETIME NOT NULL,
    lue BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_destinataire) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (id_expediteur) REFERENCES utilisateur(id) ON DELETE SET NULL
);

-- Insertion de données de test
-- Utilisateurs
INSERT INTO utilisateur (nom, prenom, email, motDePasse, type) VALUES
('Admin', 'System', 'admin@timeplanner.com', 'admin123', 'admin'),
('Dupont', 'Jean', 'jean.dupont@timeplanner.com', 'enseignant123', 'enseignant'),
('Martin', 'Sophie', 'sophie.martin@timeplanner.com', 'enseignant123', 'enseignant'),
('Dubois', 'Pierre', 'pierre.dubois@timeplanner.com', 'etudiant123', 'etudiant'),
('Leroy', 'Marie', 'marie.leroy@timeplanner.com', 'etudiant123', 'etudiant'),
('Garcia', 'Lucas', 'lucas.garcia@timeplanner.com', 'etudiant123', 'etudiant');

-- Administrateur
INSERT INTO administrateur (id) VALUES (1);

-- Enseignants
INSERT INTO enseignant (id, datePriseFonction) VALUES
(2, '2020-09-01'),
(3, '2018-09-01');

-- Matières des enseignants
INSERT INTO matiere_enseignant (id_enseignant, matiere) VALUES
(2, 'Algorithmique'),
(2, 'Programmation Java'),
(3, 'Base de données'),
(3, 'UML');

-- Étudiants
INSERT INTO etudiant (id, groupe) VALUES
(4, 'Groupe A'),
(5, 'Groupe A'),
(6, 'Groupe B');

-- Salles
INSERT INTO salle (nom, capacite, localisation, equipements) VALUES
('A101', 30, 'Bâtiment A, 1er étage', 'Vidéoprojecteur,Tableau blanc,PC'),
('A102', 25, 'Bâtiment A, 1er étage', 'Vidéoprojecteur,Tableau noir'),
('B201', 40, 'Bâtiment B, 2ème étage', 'Vidéoprojecteur,PC,Équipement réseau'),
('B202', 20, 'Bâtiment B, 2ème étage', 'Tableau blanc,PC');

-- Horaires
INSERT INTO horaire (jour, heureDebut, heureFin, semaine) VALUES
('Lundi', '08:30:00', '10:30:00', 1),
('Lundi', '10:45:00', '12:45:00', 1),
('Mardi', '08:30:00', '10:30:00', 1),
('Mardi', '10:45:00', '12:45:00', 1),
('Mercredi', '08:30:00', '10:30:00', 1),
('Mercredi', '10:45:00', '12:45:00', 1);

-- Cours
INSERT INTO cours (matiere, id_enseignant, id_horaire, id_salle, groupe) VALUES
('Algorithmique', 2, 1, 1, 'Groupe A'),
('Programmation Java', 2, 3, 2, 'Groupe A'),
('Base de données', 3, 2, 3, 'Groupe A'),
('UML', 3, 4, 4, 'Groupe B');

-- Association étudiants-cours
INSERT INTO etudiant_cours (id_etudiant, id_cours) VALUES
(4, 1), (4, 2), (4, 3),
(5, 1), (5, 2), (5, 3),
(6, 4);

-- Notes
INSERT INTO note_etudiant (id_etudiant, id_cours, note) VALUES
(4, 1, 15.5),
(4, 2, 14.0),
(5, 1, 16.0),
(5, 2, 13.5),
(6, 4, 17.5);

-- Notifications
INSERT INTO notification (contenu, id_destinataire, id_expediteur, date, lue) VALUES
('Cours d''Algorithmique déplacé en salle A102', 4, 1, '2025-05-15 10:30:00', false),
('Cours d''Algorithmique déplacé en salle A102', 5, 1, '2025-05-15 10:30:00', false),
('Cours d''Algorithmique déplacé en salle A102', 6, 1, '2025-05-15 10:30:00', false),
('Réunion pédagogique le 20 mai à 14h', 2, 1, '2025-05-14 09:00:00', true),
('Réunion pédagogique le 20 mai à 14h', 3, 1, '2025-05-14 09:00:00', false);