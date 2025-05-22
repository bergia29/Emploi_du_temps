package fr.isep.algo.gestionemploisdutemps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
    private static final String FILE_PATH = "data/utilisateurs.json";

    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String role;

    public Utilisateur() {}

    public Utilisateur(int idUtilisateur, String nom, String prenom, String email, String motDePasse, String role) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    /** Vérifie si l'email est valide (format regex simple) */
    public static boolean emailValide(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }

    /** Vérifie si l'email existe déjà dans le fichier JSON */
    public static boolean emailExistant(String email) {
        Gson gson = new Gson();
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Utilisateur>>() {}.getType();
            utilisateurs = gson.fromJson(reader, listType);
            if (utilisateurs == null) utilisateurs = new ArrayList<>();
        } catch (IOException e) {
            // fichier non trouvé = pas d'utilisateurs => email pas existant
            return false;
        }
        for (Utilisateur u : utilisateurs) {
            if (u.email.equalsIgnoreCase(email)) return true;
        }
        return false;
    }

    /** Hash le mot de passe avec SHA-256 */
    public static String hacherMotDePasse(String motDePasse) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(motDePasse.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Sauvegarde l'utilisateur dans le fichier JSON après validation */
    public boolean sauvegarderDansJson() {
        if (!emailValide(this.email)) {
            System.out.println("❌ Format d'email invalide.");
            return false;
        }
        if (emailExistant(this.email)) {
            System.out.println("❌ Cet email est déjà utilisé.");
            return false;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Utilisateur>>() {}.getType();
            utilisateurs = gson.fromJson(reader, listType);
            if (utilisateurs == null) utilisateurs = new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Fichier introuvable, création d'un nouveau.");
        }

        // Trouver le plus grand ID existant
        int maxId = 0;
        for (Utilisateur u : utilisateurs) {
            if (u.getIdUtilisateur() > maxId) {
                maxId = u.getIdUtilisateur();
            }
        }

        // Attribuer un nouvel ID = maxId + 1
        this.idUtilisateur = maxId + 1;

        this.motDePasse = hacherMotDePasse(this.motDePasse);
        utilisateurs.add(this);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(utilisateurs, writer);
            System.out.println("✅ Utilisateur enregistré dans le fichier JSON !");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static Utilisateur seConnecter(String email, String motDePasse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                System.out.println("Aucun utilisateur trouvé.");
                return null;
            }

            List<Utilisateur> utilisateurs = mapper.readValue(file, new TypeReference<List<Utilisateur>>() {});
            String motDePasseHache = hacherMotDePasse(motDePasse);

            for (Utilisateur u : utilisateurs) {
                if (u.email.equals(email) && u.motDePasse.equals(motDePasse)) {
                    System.out.println("Connexion réussie : " + u.nom + " " + u.prenom);
                    return u;
                }
            }
            System.out.println("Email ou mot de passe incorrect.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Getters et setters

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
