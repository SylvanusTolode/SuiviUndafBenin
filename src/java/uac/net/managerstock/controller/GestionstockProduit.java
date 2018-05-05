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
@Table(name = "gestionstock_produit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionstockProduit.findAll", query = "SELECT g FROM GestionstockProduit g")
    , @NamedQuery(name = "GestionstockProduit.findByCode", query = "SELECT g FROM GestionstockProduit g WHERE g.code = :code")
    , @NamedQuery(name = "GestionstockProduit.findByDateModif", query = "SELECT g FROM GestionstockProduit g WHERE g.dateModif = :dateModif")
    , @NamedQuery(name = "GestionstockProduit.findByEncodeur", query = "SELECT g FROM GestionstockProduit g WHERE g.encodeur = :encodeur")
    , @NamedQuery(name = "GestionstockProduit.findByLibelle", query = "SELECT g FROM GestionstockProduit g WHERE g.libelle = :libelle")})
public class GestionstockProduit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_modif")
    @Temporal(TemporalType.DATE)
    private Date dateModif;
    @Size(max = 80)
    @Column(name = "encodeur")
    private String encodeur;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "libelle")
    private String libelle;
    @Lob
    @Size(max = 16777215)
    @Column(name = "version")
    private String version;
    @OneToMany(mappedBy = "produit")
    private List<GestionstockLigneCommande> gestionstockLigneCommandeList;

    public GestionstockProduit() {
    }

    public GestionstockProduit(String code) {
        this.code = code;
    }

    public GestionstockProduit(String code, Date dateModif, String libelle) {
        this.code = code;
        this.dateModif = dateModif;
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GestionstockProduit)) {
            return false;
        }
        GestionstockProduit other = (GestionstockProduit) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uac.net.managerstock.controller.GestionstockProduit[ code=" + code + " ]";
    }
    
}
