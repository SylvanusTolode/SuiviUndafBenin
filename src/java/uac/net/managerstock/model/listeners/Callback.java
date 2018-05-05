/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.model.listeners;

import javax.persistence.PostPersist;
import uac.net.managerstock.model.parent.BaseBeanEntite;



/**
 *
 * @author edmond.mulemangabo@uclouvain.be
 */
public class Callback {



    public void prePersistAction(Object obj) {
        ((BaseBeanEntite) obj).prePersistAction();
    }

    @PostPersist
    public void postPersistAction(Object obj) {
        if (!(obj instanceof BaseBeanEntite)) {
            return;
        }
        ((BaseBeanEntite) obj).postPersistAction();

    }

    public void preUpdateAction(Object obj) {
        ((BaseBeanEntite) obj).preUpdateAction();
    }

    public void postUpdateAction(Object obj) {
        if (!(obj instanceof BaseBeanEntite)) {
            return;
        }
        ((BaseBeanEntite) obj).postUpdateAction();
    }

    public void preRemoveAction(Object obj) {
        if (!(obj instanceof BaseBeanEntite)) {
            return;
        }
        ((BaseBeanEntite) obj).preRemoveAction();
    }

    public void postRemoveAction(Object obj) {
        if (!(obj instanceof BaseBeanEntite)) {
            return;
        }
        ((BaseBeanEntite) obj).postRemoveAction();
    }
}
