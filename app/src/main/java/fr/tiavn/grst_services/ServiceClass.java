package fr.tiavn.grst_services;

import java.util.ArrayList;

public class ServiceClass {
    private String service_id = "";
    private String fournisseur_id ="";
    private String type_service = "";
    private String image_type_service = "";
    private String image_profil = "";
    private String pseudo_user = "";
    private String description_service = "";
    private double note_moyenne = 0.0;
    private int nb_notes = 0;
    private String cout_horaire = "";
    private boolean favori = false;
    private ArrayList<Integer> notes;

    public ServiceClass(){
    }

    public ServiceClass(String service_id, String fournisseur_id, String type_service, String image_type_service, String image_profil, String pseudo_user, String description_service, double note_moyenne, int nb_notes, String cout_horaire, boolean favori) {
        this.service_id = service_id;
        this.fournisseur_id = fournisseur_id;
        this.type_service = type_service;
        this.image_type_service = image_type_service;
        this.image_profil = image_profil;
        this.pseudo_user = pseudo_user;
        this.description_service = description_service;
        this.note_moyenne = note_moyenne;
        this.nb_notes = nb_notes;
        this.cout_horaire = cout_horaire;
        this.favori = favori;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public void setFournisseur_id(String fournisseur_id) {
        this.fournisseur_id = fournisseur_id;
    }

    public void setType_service(String type_service) {
        this.type_service = type_service;
    }

    public void setImage_type_service(String image_type_service) {
        this.image_type_service = image_type_service;
    }

    public void setImage_profil(String image_profil) {
        this.image_profil = image_profil;
    }

    public void setPseudo_user(String pseudo_user) {
        this.pseudo_user = pseudo_user;
    }

    public void setDescription_service(String description_service) {
        this.description_service = description_service;
    }

    public void setNote_moyenne(double note_moyenne) {
        this.note_moyenne = note_moyenne;
    }

    public void setNb_notes(int nb_notes) {
        this.nb_notes = nb_notes;
    }

    public void setCout_horaire(String cout_horaire) {
        this.cout_horaire = cout_horaire;
    }

    public void setNotes(ArrayList<Integer> notes) {
        this.notes = notes;
    }

    public String getService_id() {
        return service_id;
    }

    public String getFournisseur_id() { return fournisseur_id;}

    public String getType_service(){
        return type_service;
    }

    public String getImage_type_service() {
        return image_type_service;
    }

    public String getImage_profil() {
        return image_profil;
    }

    public String getPseudo_user() {
        return pseudo_user;
    }

    public String getDescription_service() {
        return description_service;
    }

    public double getNote_moyenne() {
        return note_moyenne;
    }

    public int getNb_notes() {
        return nb_notes;
    }

    public String getCout_horaire() {
        return cout_horaire;
    }

    public boolean getFavori(){
        return favori;
    }

    public void setFavori(boolean estFav) {
        favori = estFav;
    }

    public ArrayList<Integer> getNotes() {
        return notes;
    }

    public double moyenne(){
        int add = 0;
        double moy = 0.0;

        for (Integer note : notes){
            add += note;
        }

        moy = add / notes.size();
        return moy;
    }

    public void addListNote(int newNote){
        notes.add(newNote);
    }
}
