/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import uac.net.managerstock.model.parent.BaseBeanEntite;

/**
 *
 * @author okapi
 */
public class GestionstockFacture extends BaseBeanEntite implements Serializable {
    
    private static final long serialVersionUID = Long.MIN_VALUE;
    
    private Date dateFacture;
    
    private GestionstockClient client;
    
    private List<GestionstockLigneCommande> commanders = new ArrayList<>();

    /**
     * @return the dateCommande
     */
    public Date getDateCommande() {
        return dateFacture;
    }

    /**
     * @param dateCommande the dateCommande to set
     */
    public void setDateCommande(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public GestionstockClient getClient() {
        return client;
    }

    public void setClient(GestionstockClient client) {
        this.client = client;
    }

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public List<GestionstockLigneCommande> getCommanders() {
        return commanders;
    }

    public void setCommanders(List<GestionstockLigneCommande> commanders) {
        this.commanders = commanders;
    }

   

  


  

    @Override
    public String toString() {
        return getId(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
