package uac.net.managerstock.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import uac.net.managerstock.model.GestionstockFacture;
import uac.net.managerstock.model.parent.BaseBeanEntite_;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-02T18:27:08")
@StaticMetamodel(GestionstockClient.class)
public class GestionstockClient_ extends BaseBeanEntite_ {

    public static volatile SingularAttribute<GestionstockClient, String> adresse;
    public static volatile SingularAttribute<GestionstockClient, String> telephone;
    public static volatile SingularAttribute<GestionstockClient, String> sexe;
    public static volatile ListAttribute<GestionstockClient, GestionstockFacture> factures;
    public static volatile SingularAttribute<GestionstockClient, String> nom;
    public static volatile SingularAttribute<GestionstockClient, String> prenom;
    public static volatile SingularAttribute<GestionstockClient, String> email;

}