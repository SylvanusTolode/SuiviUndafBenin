/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.model.parent;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.persistence.EntityManager;

/**
 *
 * @author okap
 */
public class BaseBeanEntite implements Serializable{
    

    private final static long serialVersionUID = Long.MIN_VALUE;
    private Long version;
    
    private String encodeur = "Inconnu";

    private Date dateModif = new Date();

    private String id;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the nouvelleFiche
     */
    public boolean isNouvelleFiche() {
        return (getVersion() == null);
    }

    
    public void prePersistAction() {
//        dateModif = new Date();
//        if (StringUtils.isEmpty(encodeur)) {
//            encodeur = JSFUtility.getCurrentUserName();
//        }
    }

    public void preUpdateAction() {
//        dateModif = new Date();
//        if (!StringUtils.isEmpty(JSFUtility.getCurrentUserName())) {
//            encodeur = JSFUtility.getCurrentUserName();
//        }
    }

    public void postPersistAction() {
//        addToParentAfterPersist();
        //add2Many2OneProperties();
    }

    public void preRemoveAction() {
//        this.removeFromParentPriorToDelete();
        //removeFromMany2OneProperties();
    }

    public void postRemoveAction() {
    }

    public void postUpdateAction() {
        //add2Many2OneProperties();
    }

    // cette méthode doit être surchargée par cahque Classe ayant une relation O2O
    public void addOneSide(BaseBeanEntite bean, String targetProperty) {
    }

    ;
    
    public void deleteOneSide(BaseBeanEntite beanToDelete, String propertyFromToggledRow) {
    }

    public void addManySide(BaseBeanEntite mSideRelatedEntity, String m2oProperty) {
    }
    /*
     * ajouter un élément ? la relation O2M, M2M et O2O (côté RW pour le 02O). Cette méthode est appelée apr?s l'insertion de de 'entite' via le callback "postPersist".
     * Utiliser la méthode "get()" de la propriété plutôt que la propriété elle-m?me pour forcer un refresh par par la BD
     * @param entite Entité ? ajouter ? la relation.
     */

    public void addToManyRelation(BaseBeanEntite entite, String property) {
    }

    public void afterPersist() throws Exception {
    }

    public void removeManyRelation(BaseBeanEntite entite, String property) {
    }

    /*
     * ajouter un élément à la relation O2M, M2M et O2O (côté RW pour le 02O). Cette méthode est appelée après l'insertion de de 'entite' via le callback "postPersist".
     * Utiliser la méthode "get()" de la propriété plutôt que la propriété elle-même pour forcer un refresh par par la BD
     * @param entite Entité à ajouter à la relation.
     */
    public void addToManyRelation(BaseBeanEntite entite) {
    }

    public void updateOwnedM2MRelations(BaseBeanEntite entite, String opcode) {
    }
    /*
     * supprimer du cache toute relation O2M, M2M(côté RW) et O2O (côté RW). Cette méthode est appelée avant la suppression de 'entite' via le callback "preRemove".
     * Utiliser la propriété plutôt que la méthode "get()" correspondant à la propriété pour éviter un détour inutilement lourd par la BD
     * @param entite Entité à supprimer de la relation.
     */

    public void removeManyRelation(BaseBeanEntite entite) {
    }
    /*
     * Assigner le c?t? One de la relation OneTo-Many.
     * @param coteOneDelaRelationOne2Many entit? se trouvant du c?t? One. L'objet en cours (this) se trouve du
     * c?t? Many de la relation. Cette m?thode est appel?e ? partir de ControllerBaseBean:creerNouvellesFichesOne2ManyAction
     */

    public void addParent(Object coteOneDelaRelationOne2Many) {
    }

    public Map<String, Object> makeCriteriaQuery(EntityManager entityManager) {
        return null;
    }

    @Override
    public boolean equals(Object that) {
        if ((that == null) || (!(that instanceof BaseBeanEntite))) {
            return false;
        }
        BaseBeanEntite thatObject = (BaseBeanEntite) that;
        if (this == thatObject) {
            return true;
        }
        if ((thatObject.getId() == null)  ||(this.getId() == null)) {
            return false;
        }
        return getId().equals(thatObject.getId());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.version);
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     * @return the encodeur
     */
    public String getEncodeur() {
        return encodeur;
    }

    /**
     * @param encodeur the encodeur to set
     */
    public void setEncodeur(String encodeur) {
        this.encodeur = encodeur;
    }

    /**
     * @return the dateModif
     */
    public Date getDateModif() {
        return dateModif;
    }

    /**
     * @param dateModif the dateModif to set
     */
    public void setDateModif(Date dateModif) {
        this.dateModif = dateModif;
    }

    @Override
    public String toString() {
        return getId(); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
