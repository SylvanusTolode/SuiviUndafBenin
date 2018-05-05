/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.controller;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "gestionstock_client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionstockClient.findAll", query = "SELECT g FROM GestionstockClient g")
    , @NamedQuery(name = "GestionstockClient.findByCode", query = "SELECT g FROM GestionstockClient g WHERE g.code = :code")
    , @NamedQuery(name = "GestionstockClient.findByAdresse", query = "SELECT g FROM GestionstockClient g WHERE g.adresse = :adresse")
    , @NamedQuery(name = "GestionstockClient.findByDateModif", query = "SELECT g FROM GestionstockClient g WHERE g.dateModif = :dateModif")
    , @NamedQuery(name = "GestionstockClient.findBySiteWeb", query = "SELECT g FROM GestionstockClient g WHERE g.siteWeb = :siteWeb")
    , @NamedQuery(name = "GestionstockClient.findByEncodeur", query = "SELECT g FROM GestionstockClient g WHERE g.encodeur = :encodeur")
    , @NamedQuery(name = "GestionstockClient.findByNomClient", query = "SELECT g FROM GestionstockClient g WHERE g.nomClient = :nomClient")
    , @NamedQuery(name = "GestionstockClient.findByPrenomClient", query = "SELECT g FROM GestionstockClient g WHERE g.prenomClient = :prenomClient")
    , @NamedQuery(name = "GestionstockClient.findBySexe", query = "SELECT g FROM GestionstockClient g WHERE g.sexe = :sexe")
    , @NamedQuery(name = "GestionstockClient.findByTelephone", query = "SELECT g FROM GestionstockClient g WHERE g.telephone = :telephone")})
public class GestionstockClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 17)
    @Column(name = "adresse")
    private String adresse;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_modif")
    @Temporal(TemporalType.DATE)
    private Date dateModif;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "site_web")
    private String siteWeb;
    @Size(max = 80)
    @Column(name = "encodeur")
    private String encodeur;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "nom_client")
    private String nomClient;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "prenom_client")
    private String prenomClient;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "sexe")
    private String sexe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 17)
    @Column(name = "telephone")
    private String telephone;
    @Lob
    @Size(max = 16777215)
    @Column(name = "version")
    private String version;

    public GestionstockClient() {
    }

    public GestionstockClient(String code) {
        this.code = code;
    }

    public GestionstockClient(String code, String adresse, Date dateModif, String siteWeb, String nomClient, String prenomClient, String sexe, String telephone) {
        this.code = code;
        this.adresse = adresse;
        this.dateModif = dateModif;
        this.siteWeb = siteWeb;
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.sexe = sexe;
        this.telephone = telephone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getDateModif() {
        return dateModif;
    }

    public void setDateModif(Date dateModif) {
        this.dateModif = dateModif;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getEncodeur() {
        return encodeur;
    }

    public void setEncodeur(String encodeur) {
        this.encodeur = encodeur;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
        if (!(object instanceof GestionstockClient)) {
            return false;
        }
        GestionstockClient other = (GestionstockClient) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uac.net.managerstock.controller.GestionstockClient[ code=" + code + " ]";
    }
    
}
