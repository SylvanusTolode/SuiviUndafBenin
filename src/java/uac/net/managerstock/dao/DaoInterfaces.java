/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import uac.net.managerstock.model.parent.BaseBeanEntite;
/**
 *
 * @author okapi
 */
public interface DaoInterfaces {
    
    public int getCount(CriteriaQuery<Long> criteriaQuery);

    public List executeCriteriaQuery(CriteriaQuery criteriaQuery, int firstResult, int pageSize, int maxFetchDepth, Map params) throws Exception;

    public List executeCriteriaQuery(CriteriaQuery criteriaQuery, int maxFetchDepth) throws Exception;

    public Object executeCriteriaQuerySingle(CriteriaQuery criteriaQuery, int maxFetchDepth, Map params) throws Exception;

    public Metamodel getMetamodel();

    public CriteriaBuilder getCriteriaBuilder();

    public <T extends BaseBeanEntite> void saveUnlessExists(List<T> entities) throws Exception;

    public Number lookupCountDetached(String sql, Map<String, Object> params) throws Exception;

    public Object lookupSingleDetached(String sql, Map<String, Object> params) throws Exception;

    public <T extends BaseBeanEntite> T lookupSingleDetached(String sql,
            Map<String, Object> params, Class<T> classz) throws Exception;

    public List lookupHavingSqlQueryDetachedAny(String sql,
            Map<String, Object> params, Class classz) throws Exception;

    public <T extends BaseBeanEntite> List<? extends BaseBeanEntite> lookupHavingSqlQuery(String sql,
            Map<String, Object> params, Class<T> classz) throws Exception;

    public <T extends BaseBeanEntite> List<? extends BaseBeanEntite> lookupHavingSqlQueryDetached(String sql,
            Map<String, Object> params, Class<T> classz) throws Exception;

    public void flush() throws Exception;

    public <T extends BaseBeanEntite> List<? extends BaseBeanEntite> getResultList(Query persistenceQuery, Class<T> klassz) throws Exception;

    public <T extends BaseBeanEntite> void save(T entity) throws Exception;

    public void saveArray(Object[] entities) throws Exception;

    public void refresh(BaseBeanEntite entity) throws Exception;

    public <T extends BaseBeanEntite> void delete(T entity) throws Exception;

    public <T extends BaseBeanEntite> void delete(T[] tab) throws Exception;

    public <T extends BaseBeanEntite> void delete(List<T> entities) throws Exception;

    public <T extends BaseBeanEntite> T update(T entity) throws Exception;

    public <T extends BaseBeanEntite> void detach(T entity) throws Exception;

    public <T extends BaseBeanEntite> void updateList(List<T> entities) throws Exception;

    public <T extends BaseBeanEntite> void update(T[] entities) throws Exception;

    public <T extends BaseBeanEntite> T findById(Class<T> classz, String id) throws Exception;

    public <T extends BaseBeanEntite> List<T> doNativeStatement(String sql, Map<Integer, String> params, Class<T> classz)
            throws Exception;

    public <T> T getReference(Class<T> klazz, String id) throws Exception;

    public <T extends Number> T lookupNumberHavingSqlQueryDetached(String sql,
            Map<String, Object> params) throws Exception;

    public <T extends BaseBeanEntite> List<T> lazyLookup(
            String sql,
            Map<String, Object> params,
            int first,
            int pageSize) throws Exception;
}