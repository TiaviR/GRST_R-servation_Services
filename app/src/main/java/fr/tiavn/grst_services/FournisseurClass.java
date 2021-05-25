package fr.tiavn.grst_services;

public class FournisseurClass {
    private String fournisseur_id;
    private String nom_fournisseur;
    private String prenom_fournisseur;
    private String email_fournisseur;
    private String pseudo_fournisseur;
    private String mdp_fournisseur;
    private String imageProfil;
    //private String type_service;

    public FournisseurClass(){
    }

    public FournisseurClass(String fournisseur_id, String nom_fournisseur, String prenom_fournisseur, String email_fournisseur, String pseudo_fournisseur, String mdp_fournisseur/*, String type_service*/, String imageProfil) {
        this.fournisseur_id = fournisseur_id;
        this.nom_fournisseur = nom_fournisseur;
        this.prenom_fournisseur = prenom_fournisseur;
        this.email_fournisseur = email_fournisseur;
        this.pseudo_fournisseur = pseudo_fournisseur;
        this.mdp_fournisseur = mdp_fournisseur;
        this.imageProfil = imageProfil;
        //this.type_service = type_service;
    }

    public String getFournisseur_id() {
        return fournisseur_id;
    }

    public String getNom_fournisseur() {
        return nom_fournisseur;
    }

    public String getPrenom_fournisseur() {
        return prenom_fournisseur;
    }

    public String getEmail_fournisseur() {
        return email_fournisseur;
    }

    public String getPseudo_fournisseur() {
        return pseudo_fournisseur;
    }

    public String getMdp_fournisseur() {
        return mdp_fournisseur;
    }

    public String getImageProfil() {return imageProfil;}
    /*
    public String getType_service() {
        return type_service;
    }
     */
}

