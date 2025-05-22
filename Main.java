package fr.isep.algo.gestionemploisdutemps;

public class Main {
    public static void main(String[] args) {
        //Test admin
        Administrateur administrateur1 = new Administrateur(50,"Navalingame","Cindia","cindia.navalingame@eleve.isep.fr","Test","Admin");
        System.out.println(administrateur1.toString());


        //Test etudiant
        Etudiant etudiant1 = new Etudiant(50,"Djidonou","Charnelle","test@eleve.isep.fr","mot",75342);
        System.out.println(etudiant1.toString()+etudiant1.getNom());
    }
}
