package fr.tiavn.grst_services;

public class UtilisateurClass {
    private String utilisateur_id;
    private String utilisateur_nom;
    private String utilisateur_prenom;
    private String utilisateur_email;
    private String utilisateur_mdp;
    private String utilisateur_pseudo;
    private String imageProfil;

    private UtilisateurClass(){
    }

    public UtilisateurClass(String utilisateur_id, String utilisateur_nom, String utilisateur_prenom, String utilisateur_email, String utilisateur_mdp, String utilisateur_pseudo, String imageProfil) {
        this.utilisateur_id = utilisateur_id;
        this.utilisateur_nom = utilisateur_nom;
        this.utilisateur_prenom = utilisateur_prenom;
        this.utilisateur_email = utilisateur_email;
        this.utilisateur_mdp = utilisateur_mdp;
        this.utilisateur_pseudo = utilisateur_pseudo;
        this.imageProfil = imageProfil;
    }

    public String getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(String utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public String getUtilisateur_nom() {
        return utilisateur_nom;
    }

    public void setUtilisateur_nom(String utilisateur_nom) {
        this.utilisateur_nom = utilisateur_nom;
    }

    public String getUtilisateur_prenom() {
        return utilisateur_prenom;
    }

    public void setUtilisateur_prenom(String utilisateur_prenom) {
        this.utilisateur_prenom = utilisateur_prenom;
    }

    public String getUtilisateur_email() {
        return utilisateur_email;
    }

    public void setUtilisateur_email(String utilisateur_email) {
        this.utilisateur_email = utilisateur_email;
    }

    public String getUtilisateur_mdp() {
        return utilisateur_mdp;
    }

    public void setUtilisateur_mdp(String utilisateur_mdp) {
        this.utilisateur_mdp = utilisateur_mdp;
    }

    public String getUtilisateur_pseudo() {
        return utilisateur_pseudo;
    }

    public void setUtilisateur_pseudo(String utilisateur_pseudo) {
        this.utilisateur_pseudo = utilisateur_pseudo;
    }

    public String getImageProfil() { return imageProfil; }

    public void setImageProfil(String imageProfil) {
        this.imageProfil = imageProfil;
    }
}
