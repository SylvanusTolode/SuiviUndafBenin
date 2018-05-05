package uac.net.managerstock.controller;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import uac.net.managerstock.controller.GestionstockLigneCommande;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-02T18:27:08")
@StaticMetamodel(GestionstockFacture.class)
public class GestionstockFacture_ { 

    public static volatile ListAttribute<GestionstockFacture, GestionstockLigneCommande> gestionstockLigneCommandeList;
    public static volatile SingularAttribute<GestionstockFacture, Date> dateFacture;
    public static volatile SingularAttribute<GestionstockFacture, String> client;
    public static volatile SingularAttribute<GestionstockFacture, Date> dateModif;
    public static volatile SingularAttribute<GestionstockFacture, String> encodeur;
    public static volatile SingularAttribute<GestionstockFacture, String> version;
    public static volatile SingularAttribute<GestionstockFacture, String> numFacture;

}