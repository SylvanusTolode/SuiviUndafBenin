/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import uac.net.managerstock.model.parent.BaseBeanEntite;

/**
 *
 * @author okapi
 */
public class GestionstockClient extends BaseBeanEntite implements Serializable {
    private static final long serialVersionUID = Long.MIN_VALUE;
    
    private String nom;
    
    private String prenom;
    
    private String telephone;
    
    private String email;
    
    private String adresse;
    
    private String sexe;
    
    private List<GestionstockFacture> factures = new ArrayList<>();

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    
    
    
    /**
     * @param sexe the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public List<GestionstockFacture> getFactures() {
        return factures;
    }

    public void setFactures(List<GestionstockFacture> factures) {
        this.factures = factures;
    }

   

    

    @Override
    public String toString() {
           return nom + " "+ prenom +" " + " [ "+ getId() +" ]";
//   
//        return nom + " "+ prenom +" "+ telephone + " [ "+ getId() +" ]";
//        return getId();
    }
    
    
    
    
    
    
}
