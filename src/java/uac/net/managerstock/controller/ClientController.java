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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.bilima.okapiUtils.utils.JSFUtility;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.deltaspike.core.util.StringUtils;
import uac.net.managerstock.controller.parent.ControllerParent;
import uac.net.managerstock.model.GestionstockClient;

/**
 *
 * @author okapi
 */
@Named("clientController")
@ViewAccessScoped

public class ClientController extends ControllerParent implements Serializable {

    private static final long serialVersionUID = Long.MIN_VALUE;
    @PersistenceContext(unitName = "managerPU")
    private EntityManager entityManager;
    private GestionstockClient phiche;
    private List<GestionstockClient> clients = new ArrayList<>();
    
    private GestionstockClient clientSelection;

    @Override
    @PostConstruct
    public void setInfoVueEtEntite() {
        setNomCompletDeLaClasse("uac.net.managerstock.model.GestionClient");
//        creerNouvelleFicheAction("uac.net.managerstock.model.GestionClient");
//        phiche = new GestionstockClient();
        getTousLesclients();
    }

    public List<GestionstockClient> getTousLesclients() {
        Query query = entityManager.createQuery("select c from GestionstockClient c");
        clients = query.getResultList();
        return clients;
    }

    public List<GestionstockClient> getClients() {
        return clients;
    }

    public void setClients(List<GestionstockClient> clients) {
        this.clients = clients;
    }

    public GestionstockClient getPhiche() {
        return phiche;
    }

    public void setPhiche(GestionstockClient phiche) {
        this.phiche = phiche;
    }

    public GestionstockClient getClientSelection() {
        return clientSelection;
    }

    public void setClientSelection(GestionstockClient clientSelection) {
        this.clientSelection = clientSelection;
    }
    
    
    

    public String sauveClient() {
//        phiche = new GestionstockProduit();
        phiche.setNom(phiche.getNom());
        phiche.setPrenom(phiche.getPrenom());
        phiche.setEmail(phiche.getEmail());
        phiche.setAdresse(phiche.getAdresse());
        phiche.setTelephone(phiche.getTelephone());
        phiche.setSexe("MF");

//        try {
//            if (this.findById(phiche.getClass(), phiche.getId()) != null) {
//                JSFUtility.addInfoMessage("Contrôle", "Cet enregistrement existe d�j�.");
//                update(phiche);
//                phiche = new GestionstockClient();
//                return null;
//            }
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//            return null;
//        }
        try {
            _save(phiche);
            System.out.println("Pris par sauve");
            phiche = new GestionstockClient();

        } catch (Exception e) {
            System.out.println("Pris par ici");
//            phiche.setVersion(null);
//            phiche.setId(null);
            phiche = new GestionstockClient();
            JSFUtility.queueException(e);
            return null;
        }

        return null;

    }
    
    
      @SuppressWarnings("unchecked")
    public List<GestionstockClient> suggestionSurNomDeFamille(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return null;
        }
        
         
        String sql = "SELECT c FROM GestionstockClient c "
                + "WHERE UPPER(c.nom) LIKE :prefix";
        
        Query query = entityManager.createQuery(sql);
        query.setParameter("prefix", prefix.toUpperCase() + "%");
        List<GestionstockClient> liste = query.getResultList();
       
        
        return liste;
    }

    

    public void save() {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("OK : " + "TOLODE" + " " + "text2"));
    }
}
