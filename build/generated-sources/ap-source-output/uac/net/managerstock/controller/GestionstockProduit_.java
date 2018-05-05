package uac.net.managerstock.controller;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import uac.net.managerstock.controller.GestionstockLigneCommande;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-02T18:27:08")
@StaticMetamodel(GestionstockProduit.class)
public class GestionstockProduit_ { 

    public static volatile SingularAttribute<GestionstockProduit, String> code;
    public static volatile ListAttribute<GestionstockProduit, GestionstockLigneCommande> gestionstockLigneCommandeList;
    public static volatile SingularAttribute<GestionstockProduit, String> libelle;
    public static volatile SingularAttribute<GestionstockProduit, Date> dateModif;
    public static volatile SingularAttribute<GestionstockProduit, String> encodeur;
    public static volatile SingularAttribute<GestionstockProduit, String> version;

}