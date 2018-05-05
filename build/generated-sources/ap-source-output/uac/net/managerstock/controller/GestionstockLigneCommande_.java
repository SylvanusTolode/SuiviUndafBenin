package uac.net.managerstock.controller;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import uac.net.managerstock.controller.GestionstockFacture;
import uac.net.managerstock.controller.GestionstockProduit;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-02T18:27:08")
@StaticMetamodel(GestionstockLigneCommande.class)
public class GestionstockLigneCommande_ { 

    public static volatile SingularAttribute<GestionstockLigneCommande, GestionstockProduit> produit;
    public static volatile SingularAttribute<GestionstockLigneCommande, BigInteger> quantiteCommande;
    public static volatile SingularAttribute<GestionstockLigneCommande, GestionstockFacture> facture;
    public static volatile SingularAttribute<GestionstockLigneCommande, String> id;
    public static volatile SingularAttribute<GestionstockLigneCommande, Date> dateModif;
    public static volatile SingularAttribute<GestionstockLigneCommande, String> encodeur;
    public static volatile SingularAttribute<GestionstockLigneCommande, String> version;

}