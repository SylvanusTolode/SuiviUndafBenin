package uac.net.managerstock.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import uac.net.managerstock.model.GestionstockClient;
import uac.net.managerstock.model.GestionstockLigneCommande;
import uac.net.managerstock.model.parent.BaseBeanEntite_;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-02T18:27:08")
@StaticMetamodel(GestionstockFacture.class)
public class GestionstockFacture_ extends BaseBeanEntite_ {

    public static volatile ListAttribute<GestionstockFacture, GestionstockLigneCommande> commanders;
    public static volatile SingularAttribute<GestionstockFacture, Date> dateFacture;
    public static volatile SingularAttribute<GestionstockFacture, GestionstockClient> client;

}