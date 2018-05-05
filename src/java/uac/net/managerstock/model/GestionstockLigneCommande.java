/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.model;

import java.io.Serializable;
import uac.net.managerstock.model.parent.BaseBeanEntite;

/**
 *
 * @author okapi
 */
public class GestionstockLigneCommande extends BaseBeanEntite implements Serializable{
    
    private static final long serialVersionUID = Long.MIN_VALUE;
    
    private GestionstockFacture facture;
    
    private GestionstockProduit produit;
    
    private int quantiteCommande;

    public GestionstockFacture getFacture() {
        return facture;
    }

    public void setFacture(GestionstockFacture facture) {
        this.facture = facture;
    }

  

    public GestionstockProduit getProduit() {
        return produit;
    }

    public void setProduit(GestionstockProduit produit) {
        this.produit = produit;
    }

   

    /**
     * @return the quantiteCommande
     */
    public int getQuantiteCommande() {
        return quantiteCommande;
    }

    /**
     * @param quantiteCommande the quantiteCommande to set
     */
    public void setQuantiteCommande(int quantiteCommande) {
        this.quantiteCommande = quantiteCommande;
    }
    
       
    
    
}
