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
public class GestionstockProduit extends BaseBeanEntite implements Serializable {
    
    private static final long serialVersionUID = Long.MIN_VALUE;
    
    private String libelle;
    
    private List<GestionstockLigneCommande> commanders = new ArrayList<>();

    /**
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @param libelle the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<GestionstockLigneCommande> getCommanders() {
        return commanders;
    }

    public void setCommanders(List<GestionstockLigneCommande> commanders) {
        this.commanders = commanders;
    }

   

    

    
    
         
    
}
