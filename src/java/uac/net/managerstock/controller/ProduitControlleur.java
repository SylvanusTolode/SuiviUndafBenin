/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.bilima.okapiUtils.utils.JSFUtility;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.eclipse.persistence.exceptions.DatabaseException;
import uac.net.managerstock.controller.parent.ControllerParent;
import uac.net.managerstock.model.GestionstockClient;
import uac.net.managerstock.model.GestionstockProduit;

/**
 *
 * @author okapi
 */
@Named("produitControlleur")
@ViewAccessScoped

public class ProduitControlleur extends ControllerParent implements Serializable {

    private static final long serialVersionUID = Long.MIN_VALUE;
    @PersistenceContext(unitName = "managerPU")
    private EntityManager entityManager;
    private List<GestionstockProduit> produits = new ArrayList<>();
    private GestionstockProduit phiche;

    public GestionstockProduit getPhiche() {
        return phiche;
    }

    public void setPhiche(GestionstockProduit Fiche) {
        this.phiche = Fiche;
    }

    @Override
    @PostConstruct
    public void setInfoVueEtEntite() {
//        setNomCompletDeLaClasse("uac.net.managerstock.GestionstockProduit");
//        creerNouvelleFicheAction("uac.net.managerstock.GestionstockProduit");
        phiche = new GestionstockProduit();
        getTousLesProduits();
    }

    public List<GestionstockProduit> getTousLesProduits() {
        Query query = entityManager.createQuery("select p from GestionstockProduit p");
        produits = query.getResultList();
        return produits;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<GestionstockProduit> getProduits() {
        return produits;
    }

    public void setProduits(List<GestionstockProduit> produits) {
        this.produits = produits;
    }

    
     public String initialize() {
        phiche = new GestionstockProduit();
       
        return null;

    }
 
    
    
    
    public String sauveProduit() {
//        phiche = new GestionstockProduit();
        phiche.setId(phiche.getId());
        phiche.setLibelle(phiche.getLibelle());
        try {
            if (this.findById(phiche.getClass(), phiche.getId()) != null) {
                JSFUtility.addInfoMessage("Contr�le", "Cet enregistrement existe d�j�.");
                update(phiche);
                phiche = new GestionstockProduit();
                return null;
            }
        } catch (Exception e) {
            JSFUtility.queueException(e);
            return null;
        }
        try {
            _save(phiche);
            System.out.println("Pris par sauve");
            phiche = new GestionstockProduit();

        } catch (Exception e) {
            System.out.println("Pris par ici");
//            phiche.setVersion(null);
//            phiche.setId(null);
            phiche = new GestionstockProduit();
            JSFUtility.queueException(e);
            return null;
        }

        return null;

    }

}
