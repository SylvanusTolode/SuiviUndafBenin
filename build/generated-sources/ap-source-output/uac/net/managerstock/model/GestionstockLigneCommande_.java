package uac.net.managerstock.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import uac.net.managerstock.model.GestionstockFacture;
import uac.net.managerstock.model.GestionstockProduit;
import uac.net.managerstock.model.parent.BaseBeanEntite_;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-02T18:27:08")
@StaticMetamodel(GestionstockLigneCommande.class)
public class GestionstockLigneCommande_ extends BaseBeanEntite_ {

    public static volatile SingularAttribute<GestionstockLigneCommande, GestionstockProduit> produit;
    public static volatile SingularAttribute<GestionstockLigneCommande, Integer> quantiteCommande;
    public static volatile SingularAttribute<GestionstockLigneCommande, GestionstockFacture> facture;

}