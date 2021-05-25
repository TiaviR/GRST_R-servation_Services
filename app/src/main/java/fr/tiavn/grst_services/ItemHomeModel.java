package fr.tiavn.grst_services;

import java.util.ArrayList;

public class ItemHomeModel {
    private String imageURL = "";
    private String imageProfil = "";
    private String pseudo = "";
    private String descriptionService = "";
    private double noteMoyenne = 0.0;
    private int nbNotes = 0;
    private String prixMinimum = "";
    private ArrayList<Integer> notes;

    public ItemHomeModel(){}

    public ItemHomeModel(String imageURL, String imageProfil, String pseudo, String descriptionService, double noteMoyenne, int nbNotes, String prixMinimum) {
        this.imageURL = imageURL;
        this.imageProfil = imageProfil;
        this.pseudo = pseudo;
        this.descriptionService = descriptionService;
        this.noteMoyenne = noteMoyenne;
        this.nbNotes = nbNotes;
        this.prixMinimum = prixMinimum;
    }

    public String getImageURL() { return imageURL; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public String getImageProfil() { return imageProfil; }

    public void setImageProfil(String imageProfil) { this.imageProfil = imageProfil; }

    public String getPseudo() { return pseudo; }

    public void setPseudo(String pseudo) { this.pseudo = pseudo; }

    public String getDescriptionService() { return descriptionService; }

    public void setDescriptionService(String descriptionService) { this.descriptionService = descriptionService; }

    public double getNoteMoyenne() { return noteMoyenne; }

    public void setNoteMoyenne(double noteMoyenne) { this.noteMoyenne = noteMoyenne; }

    public int getNbNotes() { return nbNotes; }

    public void setNbNotes(int nbNotes) { this.nbNotes = nbNotes; }

    public String getPrixMinimum() { return prixMinimum; }

    public void setPrixMinimum(String prixMinimum) { this.prixMinimum = prixMinimum; }

    public double moyenne(ArrayList<Integer> notes){
        int add = 0;
        double moy = 0.0;

        for (Integer note : notes){
            add += note;
        }

        moy = add / notes.size();

        return moy;
    }
}
