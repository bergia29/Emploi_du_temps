-- Création de la base de données
CREATE DATABASE IF NOT EXISTS PlanningCampus;
USE PlanningCampus;

-- Suppression des tables si elles existent déjà
DROP TABLE IF EXISTS note_etudiant;
DROP TABLE IF EXISTS etudiant_cours;
DROP TABLE IF EXISTS cours;
DROP TABLE IF EXISTS salle;
DROP TABLE IF EXISTS Seance;
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

-- Création de la table seance
CREATE TABLE Seance (
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
    id_seance INT,
    id_salle INT,
    groupe VARCHAR(50), -- Groupe d'étudiants assigné au cours
    FOREIGN KEY (id_enseignant) REFERENCES enseignant(id) ON DELETE SET NULL,
    FOREIGN KEY (id_seance) REFERENCES seance(id) ON DELETE CASCADE,
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

-- Utilisateurs
INSERT INTO utilisateur (nom, prenom, email, motDePasse, type) VALUES
('Morin', 'Mirika', 'mirika.morin@admin.com', 'admin321', 'admin'),
('Grimm', 'Elias', 'elias.grimm@isepteach.com', 'teach001', 'enseignant'),
('Zhang', 'Wei', 'wei.zhang@isepteach.com', 'teach002', 'enseignant'),
('Okafor', 'Chinasa', 'chinasa.okafor@isepteach.com', 'teach003', 'enseignant'),
('Moretti', 'Giulia', 'giulia.moretti@isepteach.com', 'teach004', 'enseignant'),
('Diallo', 'Amadou', 'amadou.diallo@isepstud.com', 'stud123', 'etudiant'),
('Tremblay', 'Léa', 'lea.tremblay@isepstud.com', 'stud123', 'etudiant'),
('Garcia', 'Mateo', 'mateo.garcia@isepstud.com', 'stud123', 'etudiant'),
('Rossi', 'Chiara', 'chiara.rossi@isepstud.com', 'stud123', 'etudiant'),
('Kim', 'Soojin', 'soojin.kim@isepstud.com', 'stud123', 'etudiant'),
('Kouadio', 'Yao', 'yao.kouadio@isepstud.com', 'stud123', 'etudiant'),
('Müller', 'Lena', 'lena.mueller@isepstud.com', 'stud123', 'etudiant'),
('Tanaka', 'Haruto', 'haruto.tanaka@isepstud.com', 'stud123', 'etudiant'),
('Smith', 'Emily', 'emily.smith@isepstud.com', 'stud123', 'etudiant'),
('Nguyen', 'Bao', 'bao.nguyen@isepstud.com', 'stud123', 'etudiant'),
('Fernandez', 'Lucia', 'lucia.fernandez@isepstud.com', 'stud123', 'etudiant'),
('Patel', 'Raj', 'raj.patel@isepstud.com', 'stud123', 'etudiant'),
('Ali', 'Zainab', 'zainab.ali@isepstud.com', 'stud123', 'etudiant'),
('Dufour', 'Nicolas', 'nicolas.dufour@isepstud.com', 'stud123', 'etudiant'),
('Jensen', 'Freja', 'freja.jensen@isepstud.com', 'stud123', 'etudiant');

-- Admin
INSERT INTO administrateur (id) VALUES (1);

-- Enseignants
INSERT INTO enseignant (id, datePriseFonction) VALUES
(2, '2019-01-05'),
(3, '2021-09-10'),
(4, '2020-06-22'),
(5, '2022-03-15');

-- Matières
INSERT INTO matiere_enseignant (id_enseignant, matiere) VALUES
(2, 'Programmation Web'),
(2, 'Design UX'),
(3, 'Cybersécurité'),
(4, 'Réseaux Informatiques'),
(5, 'Intelligence Artificielle');

-- Étudiants (groupes)
INSERT INTO etudiant (id, groupe) VALUES
(6, 'Aube Rouge'), (7, 'Aube Rouge'), (8, 'Aube Rouge'),
(9, 'Orchidée Bleue'), (10, 'Orchidée Bleue'), (11, 'Orchidée Bleue'),
(12, 'Tempête Jaune'), (13, 'Tempête Jaune'), (14, 'Tempête Jaune'),
(15, 'Lynx Vert'), (16, 'Lynx Vert'), (17, 'Lynx Vert'),
(18, 'Aube Rouge'), (19, 'Orchidée Bleue'), (20, 'Lynx Vert');

-- Salles
INSERT INTO salle (nom, capacite, localisation, equipements) VALUES
('Omega1', 35, 'Bloc Omega - Rdc', 'Projecteur, Tableau numérique'),
('Nova2', 40, 'Bâtiment Nova - Étage 1', 'VR, Table tactile'),
('HorizonX', 25, 'Tour Horizon - Niveau 2', 'Audio HD, Tableau blanc'),
('TerraLab', 30, 'Lab Terra - Sous-sol', 'PCs gamer, Routeurs Cisco'),
('AulaMax', 50, 'Aile Centrale', 'Double écran, pupitre enseignant');

-- Seances
INSERT INTO Seance (jour, heureDebut, heureFin, semaine) VALUES
('Lundi', '08:00:00', '10:00:00', 5),
('Lundi', '10:15:00', '12:15:00', 5),
('Mardi', '13:00:00', '15:00:00', 5),
('Mercredi', '09:00:00', '11:00:00', 5),
('Jeudi', '14:00:00', '16:00:00', 5),
('Vendredi', '11:00:00', '13:00:00', 5);

-- Cours
INSERT INTO cours (matiere, id_enseignant, id_seance, id_salle, groupe) VALUES
('Programmation Web', 2, 1, 1, 'Aube Rouge'),
('Design UX', 2, 2, 1, 'Orchidée Bleue'),
('Cybersécurité', 3, 3, 2, 'Tempête Jaune'),
('Réseaux Informatiques', 4, 4, 3, 'Lynx Vert'),
('Intelligence Artificielle', 5, 5, 4, 'Orchidée Bleue'),
('Cybersécurité', 3, 6, 5, 'Aube Rouge');

-- Association étudiants-cours (extrait pour 20 étudiants)
INSERT INTO etudiant_cours (id_etudiant, id_cours) VALUES
(6, 1), (7, 1), (8, 1), (18, 1), (6, 6), (18, 6),
(9, 2), (10, 2), (11, 2), (19, 2), (15, 4), (16, 4), (17, 4), (20, 4),
(12, 3), (13, 3), (14, 3),
(9, 5), (10, 5), (11, 5), (19, 5);

-- Notes (aléatoires mais crédibles)
INSERT INTO note_etudiant (id_etudiant, id_cours, note) VALUES
(6, 1, 14.5), (7, 1, 16.0), (8, 1, 12.0), (18, 1, 15.0),
(6, 6, 17.5), (18, 6, 13.5),
(9, 2, 18.0), (10, 2, 17.5), (11, 2, 15.5), (19, 2, 16.0),
(12, 3, 14.0), (13, 3, 14.5), (14, 3, 13.0),
(15, 4, 15.0), (16, 4, 16.5), (17, 4, 14.0), (20, 4, 12.5),
(9, 5, 17.0), (10, 5, 16.8), (11, 5, 15.5), (19, 5, 14.8);

-- Notifications
INSERT INTO notification (contenu, id_destinataire, id_expediteur, date, lue) VALUES
('Changement de salle pour Réseaux Informatiques – nouvelle salle: HorizonX', 15, 1, '2025-05-22 09:00:00', false),
('Le cours de Design UX du jeudi est avancé à 10h.', 9, 1, '2025-05-21 14:15:00', true),
('Un contrôle surprise aura lieu mardi prochain pour le cours de Cybersécurité.', 12, 1, '2025-05-20 13:30:00', false),
('Mise à jour des notes de Programmation Web disponible.', 6, 1, '2025-05-19 18:00:00', true),
('Nouvelle ressource ajoutée dans le cours d''IA.', 10, 1, '2025-05-22 10:00:00', false);
