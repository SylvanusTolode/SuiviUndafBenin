/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.controller;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author martial
 */
@Entity
@Table(name = "gestionstock_ligne_commande")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionstockLigneCommande.findAll", query = "SELECT g FROM GestionstockLigneCommande g")
    , @NamedQuery(name = "GestionstockLigneCommande.findById", query = "SELECT g FROM GestionstockLigneCommande g WHERE g.id = :id")
    , @NamedQuery(name = "GestionstockLigneCommande.findByDateModif", query = "SELECT g FROM GestionstockLigneCommande g WHERE g.dateModif = :dateModif")
    , @NamedQuery(name = "GestionstockLigneCommande.findByEncodeur", query = "SELECT g FROM GestionstockLigneCommande g WHERE g.encodeur = :encodeur")
    , @NamedQuery(name = "GestionstockLigneCommande.findByQuantiteCommande", query = "SELECT g FROM GestionstockLigneCommande g WHERE g.quantiteCommande = :quantiteCommande")})
public class GestionstockLigneCommande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_modif")
    @Temporal(TemporalType.DATE)
    private Date dateModif;
    @Size(max = 80)
    @Column(name = "encodeur")
    private String encodeur;
    @Column(name = "quantite_commande")
    private BigInteger quantiteCommande;
    @Lob
    @Size(max = 16777215)
    @Column(name = "version")
    private String version;
    @JoinColumn(name = "facture", referencedColumnName = "num_facture")
    @ManyToOne
    private GestionstockFacture facture;
    @JoinColumn(name = "produit", referencedColumnName = "code")
    @ManyToOne
    private GestionstockProduit produit;

    public GestionstockLigneCommande() {
    }

    public GestionstockLigneCommande(String id) {
        this.id = id;
    }

    public GestionstockLigneCommande(String id, Date dateModif) {
        this.id = id;
        this.dateModif = dateModif;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateModif() {
        return dateModif;
    }

    public void setDateModif(Date dateModif) {
        this.dateModif = dateModif;
    }

    public String getEncodeur() {
        return encodeur;
    }

    public void setEncodeur(String encodeur) {
        this.encodeur = encodeur;
    }

    public BigInteger getQuantiteCommande() {
        return quantiteCommande;
    }

    public void setQuantiteCommande(BigInteger quantiteCommande) {
        this.quantiteCommande = quantiteCommande;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GestionstockLigneCommande)) {
            return false;
        }
        GestionstockLigneCommande other = (GestionstockLigneCommande) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uac.net.managerstock.controller.GestionstockLigneCommande[ id=" + id + " ]";
    }
    
}
