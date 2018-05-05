/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.controller.converter;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uac.net.managerstock.model.parent.BaseBeanEntite;
/**
 *
 * @author okap
 */


@Named("convertisseurGenerique")
@FacesConverter("convertisseurGenerique")
public class ConvertisseurGenerique implements Converter, Serializable{
    
    @SuppressWarnings("unused")
    @PersistenceContext(unitName = "managerPU")
    private EntityManager entityManager;
    
     Logger logger ;
    
    @Override
    @SuppressWarnings("unused")
    public Object getAsObject(FacesContext fc, UIComponent uic, String id) {
        
        Class objectClass =  uic.getValueExpression("value").getType(FacesContext.getCurrentInstance().getELContext());
        
        if(id.isEmpty()){
            return null;
        }
        
        if(BaseBeanEntite.class.isAssignableFrom(objectClass)){
            Object entite = entityManager.find(objectClass, id);
            return entite;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unused")
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        
        if(o == null || o.equals(null) || !(o instanceof BaseBeanEntite) ){
            return "";
        }
        
        BaseBeanEntite entite = (BaseBeanEntite) o;
        return (entite.getId().isEmpty()) ? "" : entite.getId();
    }
    
}
