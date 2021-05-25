package fr.tiavn.grst_services;

public class DomaineMetier {
    //private int domaine_metier_id = 0;
    private String domaine_metier_image = "";
    private String domaine_metier_nom = "";

    public DomaineMetier(String domaine_metier_image, String domaine_metier_nom) {
        this.domaine_metier_image = domaine_metier_image;
        this.domaine_metier_nom = domaine_metier_nom;
    }

    public String getDomaine_metier_image() {
        return domaine_metier_image;
    }

    public String getDomaine_metier_nom() {
        return domaine_metier_nom;
    }
}
