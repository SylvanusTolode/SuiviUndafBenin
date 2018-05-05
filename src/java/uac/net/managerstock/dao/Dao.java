/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.dao;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.UserTransaction;
import uac.net.managerstock.dao.DaoInterfaces;
import uac.net.managerstock.model.parent.BaseBeanEntite;

/**
 *
 * @author okapi
 */


@Stateless


public class Dao implements DaoInterfaces {

    @PersistenceContext(unitName = "managerPU")
    private EntityManager entityManager;
    @Resource
    private UserTransaction tx;

    @Override
    public int getCount(CriteriaQuery<Long> criteriaQuery) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List executeCriteriaQuery(CriteriaQuery criteriaQuery, int firstResult, int pageSize, int maxFetchDepth, Map params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List executeCriteriaQuery(CriteriaQuery criteriaQuery, int maxFetchDepth) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object executeCriteriaQuerySingle(CriteriaQuery criteriaQuery, int maxFetchDepth, Map params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Metamodel getMetamodel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> void saveUnlessExists(List<T> entities) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Number lookupCountDetached(String sql, Map<String, Object> params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object lookupSingleDetached(String sql, Map<String, Object> params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> T lookupSingleDetached(String sql, Map<String, Object> params, Class<T> classz) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List lookupHavingSqlQueryDetachedAny(String sql, Map<String, Object> params, Class classz) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> List<? extends BaseBeanEntite> lookupHavingSqlQuery(String sql, Map<String, Object> params, Class<T> classz) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> List<? extends BaseBeanEntite> lookupHavingSqlQueryDetached(String sql, Map<String, Object> params, Class<T> classz) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveArray(Object[] entities) throws Exception {
        for (Object obj : entities) {
            if (!(obj instanceof BaseBeanEntite)) {
                throw new Exception(
                        "classe d'objet inattendue; on attend EntiteBaseBean, on re?oit "
                        + obj.getClass().getName());
            }
            save((BaseBeanEntite) obj);
            //entityManager.persist(obj);
        }
    }

    @Override
    public void refresh(BaseBeanEntite entity) throws Exception {
        if (entityManager.contains(entity)) {
            entityManager.refresh(entity);
        }
    }

    @Override

    public <T extends BaseBeanEntite> void save(T entity) throws Exception {

        
        
        if (tx == null) {
            throw new Exception("Cannot get a valid transaction from the EntityManager");
        }
        tx.begin();
        entityManager.persist(entity);
        tx.commit();

    }

//    @Override
//    
//    public <T extends BaseBeanEntite> T update(T entity) throws Exception {
//        boolean alreadyPersisted = entityManager.contains(entity);
//        if (!alreadyPersisted) {
//            BaseBeanEntite persisted = entityManager.find(entity.getClass(), entity.getId());
//            if (persisted == null) {
//                throw new Exception("Une fiche doit être persistée avant sa modification dans la BD; l'entité '" + entity.toString() + "' ne peut être persistée");
//            }
//
//            if (persisted.getVersion() > entity.getVersion()) {
//                // by doing this, we chose the above "a" workaround
//                entity.setVersion(persisted.getVersion());
//            }
//        }
//        tx.begin();
//        T mergedEntity = entityManager.merge(entity);
//        tx.commit();
//        return mergedEntity;
//    }
//
//    @Override
//    public <T extends BaseBeanEntite> void delete(T entity) throws Exception {
//        if (!entityManager.contains(entity)) {
//            BaseBeanEntite persisted = entityManager.find(entity.getClass(), entity.getId());
//            if (persisted == null) {
//                throw new Exception("La fiche '" + entity.toString() + "' doit être persistée avant qu'elle ne soit supprimée; impossible de la persister.. Veuillez vérifier qu'elle existe toujours");
//            }
////            entityManager.joinTransaction();
//            tx.begin();
//            entityManager.remove(entityManager.merge(entity));
//            tx.commit();
//        } else {
////            entityManager.joinTransaction();
//            tx.begin();
//            entityManager.remove(entity);
//            tx.commit();
//        }
//
//    }

    @Override
    public <T extends BaseBeanEntite> void delete(T[] tab) throws Exception {
        for (T entity : tab) {
            delete(entity);
        }
    }

    @Override
    // @AuditDataInterceptor(operation = "remove")
    public <T extends BaseBeanEntite> void delete(List< T> entities) throws Exception {
        for (T entity : entities) {
            delete(entity);
        }
    }

    public <T extends BaseBeanEntite> T update2(T entity) throws Exception {
        return update(entity);
    }

    public void clear() throws Exception {
        entityManager.clear();
    }

//    @Override
//    public <T extends BaseBeanEntite> void detach(T entity) throws Exception {
//        // persist it before merging
//        BaseBeanEntite foundObj = entityManager.find(entity.getClass(), entity.getId());
//        entityManager.detach(entity);
//    }

    @Override
    public <T extends BaseBeanEntite> void update(T[] entities) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> T findById(Class<T> classz, String id) throws Exception {
        return entityManager.find(classz, id);
    }

    @Override
    public <T extends BaseBeanEntite> List<T> doNativeStatement(String sql, Map<Integer, String> params, Class<T> classz) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T getReference(Class<T> klazz, String id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends Number> T lookupNumberHavingSqlQueryDetached(String sql, Map<String, Object> params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> List<T> lazyLookup(String sql, Map<String, Object> params, int first, int pageSize) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void persist(Object object) {
        entityManager.persist(object);
    }

    @Override
    public void flush() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> List<? extends BaseBeanEntite> getResultList(Query persistenceQuery, Class<T> klassz) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> void updateList(List<T> entities) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> void delete(T entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> T update(T entity) throws Exception {
        
        
        
//        if (tx == null) {
//            throw new Exception("Cannot get a valid transaction from the EntityManager");
//        }
//        tx.begin();
//        entityManager.(entity);
        tx.commit();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends BaseBeanEntite> void detach(T entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
