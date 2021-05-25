package fr.tiavn.grst_services;

public class ReservationClass {
    private String reservation_id;
    private String service_id;
    private String client_id;
    private String client_pseudo;
    private String fournisseur_id;
    private String fournisseur_pseudo;
    private String description_service;
    private String date;
    private String periode;

    public ReservationClass(String reservation_id, String service_id, String client_id, String client_pseudo, String fournisseur_id, String fournisseur_pseudo, String description_service, String date, String periode) {
        this.reservation_id = reservation_id;
        this.service_id = service_id;
        this.client_id = client_id;
        this.client_pseudo = client_pseudo;
        this.fournisseur_id = fournisseur_id;
        this.fournisseur_pseudo = fournisseur_pseudo;
        this.description_service = description_service;
        this.date = date;
        this.periode = periode;
    }

    public ReservationClass(){
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_pseudo() {
        return client_pseudo;
    }

    public void setClient_pseudo(String client_pseudo) {
        this.client_pseudo = client_pseudo;
    }

    public String getFournisseur_id() {
        return fournisseur_id;
    }

    public void setFournisseur_id(String fournisseur_id) {
        this.fournisseur_id = fournisseur_id;
    }

    public String getFournisseur_pseudo() {
        return fournisseur_pseudo;
    }

    public void setFournisseur_pseudo(String fournisseur_pseudo) {
        this.fournisseur_pseudo = fournisseur_pseudo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getDescription_service() {
        return description_service;
    }

    public void setDescription_service(String description_service) {
        this.description_service = description_service;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }
}
