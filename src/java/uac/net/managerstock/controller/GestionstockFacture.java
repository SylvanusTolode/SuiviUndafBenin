/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author martial
 */
@Entity
@Table(name = "gestionstock_facture")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionstockFacture.findAll", query = "SELECT g FROM GestionstockFacture g")
    , @NamedQuery(name = "GestionstockFacture.findByNumFacture", query = "SELECT g FROM GestionstockFacture g WHERE g.numFacture = :numFacture")
    , @NamedQuery(name = "GestionstockFacture.findByDateFacture", query = "SELECT g FROM GestionstockFacture g WHERE g.dateFacture = :dateFacture")
    , @NamedQuery(name = "GestionstockFacture.findByDateModif", query = "SELECT g FROM GestionstockFacture g WHERE g.dateModif = :dateModif")
    , @NamedQuery(name = "GestionstockFacture.findByEncodeur", query = "SELECT g FROM GestionstockFacture g WHERE g.encodeur = :encodeur")
    , @NamedQuery(name = "GestionstockFacture.findByClient", query = "SELECT g FROM GestionstockFacture g WHERE g.client = :client")})
public class GestionstockFacture implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "num_facture")
    private String numFacture;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_facture")
    @Temporal(TemporalType.DATE)
    private Date dateFacture;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_modif")
    @Temporal(TemporalType.DATE)
    private Date dateModif;
    @Size(max = 80)
    @Column(name = "encodeur")
    private String encodeur;
    @Lob
    @Size(max = 16777215)
    @Column(name = "version")
    private String version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "client")
    private String client;
    @OneToMany(mappedBy = "facture")
    private List<GestionstockLigneCommande> gestionstockLigneCommandeList;

    public GestionstockFacture() {
    }

    public GestionstockFacture(String numFacture) {
        this.numFacture = numFacture;
    }

    public GestionstockFacture(String numFacture, Date dateFacture, Date dateModif, String client) {
        this.numFacture = numFacture;
        this.dateFacture = dateFacture;
        this.dateModif = dateModif;
        this.client = client;
    }

    public String getNumFacture() {
        return numFacture;
    }

    public void setNumFacture(String numFacture) {
        this.numFacture = numFacture;
    }

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @XmlTransient
    public List<GestionstockLigneCommande> getGestionstockLigneCommandeList() {
        return gestionstockLigneCommandeList;
    }

    public void setGestionstockLigneCommandeList(List<GestionstockLigneCommande> gestionstockLigneCommandeList) {
        this.gestionstockLigneCommandeList = gestionstockLigneCommandeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numFacture != null ? numFacture.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GestionstockFacture)) {
            return false;
        }
        GestionstockFacture other = (GestionstockFacture) object;
        if ((this.numFacture == null && other.numFacture != null) || (this.numFacture != null && !this.numFacture.equals(other.numFacture))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uac.net.managerstock.controller.GestionstockFacture[ numFacture=" + numFacture + " ]";
    }
    
}
