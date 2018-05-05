/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.net.managerstock.controller.parent;

import java.awt.event.ActionEvent;
import java.io.*;
import java.text.DecimalFormat;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletContext;
import javax.validation.ValidationException;
import net.bilima.okapiUtils.utils.AcademicYear;
import net.bilima.okapiUtils.utils.JSFUtility;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.PieChartModel;
import uac.net.managerstock.dao.Dao;
import uac.net.managerstock.model.GestionstockClient;
import uac.net.managerstock.model.GestionstockProduit;
import uac.net.managerstock.model.parent.BaseBeanEntite;

/**
 *
 * @author edmond.mulemangabo@uclouvain.be
 */
public abstract class ControllerParent extends Dao implements Serializable {

    private static final long serialVersionUID = Long.MIN_VALUE;

    private static final String MAIN_ENTITY = "mainEntity";
    private static final String O2M_ENTITY = "o2mEntity";
    private static int TAILLEMAXNOUVELLESFICHES = 1;
    private String sortColumn = null;
    private boolean sortAscending = true;
    private Integer tableBlockSize = new Integer(10);
    private String reference = null;
    private String currentReference = null;    // search properties >>>
    private String currentSearchCriteria = "*";    // search condition
    // private AcademicYear academicYear = new AcademicYear();
    private Date dateRangeStart;
    private Date dateRangeEnd;
    private String many2OneNavigationDestination;
    // search conditions
    private String searchValue;
    private Map<String, String> selectedMap;
    private boolean toggleSelectAll = Boolean.FALSE;
    private Integer subRowCount = new Integer(10);
    private boolean subSortAscending = true;
    private String subSortColumn = null;
    private boolean subToggleSelectAll = Boolean.FALSE;
    private String sqlPattern;
    private String many2OnePropertyTobeEdited;
    private Map<String, Map<String, Float>> fraisRetrocedes = new HashMap<String, Map<String, Float>>();

    //private List<EtudiantEntite> etudiants;
    //protected Logger logger = Logger.getLogger(this.getClass().getName());
    // private ProprietesApp proprietesApp = null;
//    protected @Inject
//    @Default
//    Dao dao;
//    @inject
//    protected Dao dao;
    protected String nomCompletDeLaClasse; // initialis? par la classe fille par @PostConstruct
    protected String racineVue; // initialis? par la classe fille par @PostConstruct
    // protected List<BaseBeanEntite> fiches = null;
//    protected BilimaLazyDataModel fiches = null;
//    private OkapiLazyModel<? extends BaseBeanEntite> okapiLazyModel = null;
    // propri�t�s utilis�es dans le composant "autoComplete"
    
//    protected RetrocessionAvance avanceSelection;
//    private RetrocessionOrgane organeSelection;

    protected int montantRetrocessionFraisMedicaux = 0;
    protected float montantRetrocessionFraisInscriptionRectorat = 0;
    protected float montantRetrocessionFraisFormationRectorat = 0;
    protected float montantRetrocessionFraisInscriptionCS = 0;
    protected float montantRetrocessionFraisFormationCS = 0;
    protected float montantRetrocessionFraisInscriptionBU = 0;
    protected float montantRetrocessionFraisFormationBU = 0;
    protected float montantRetrocessionFraisInscriptionEtablissement = 0;
    protected float montantRetrocessionFraisFormationEtablissement = 0;

    private BaseBeanEntite ficheSelectionnee;
    //private BaseBeanEntite ficheSelectionneeO2M;
    private GestionstockProduit[] ficheselect;
    protected BaseBeanEntite selection;
    private BaseBeanEntite[] selections;
    private BaseBeanEntite[] selectionsO2M;
    //protected BaseBeanEntite[] fichesSelectionnees;
    protected BaseBeanEntite[] fichesSelectionneesO2M;
    protected BaseBeanEntite elementEnCoursDeCreationOuDeModification;
    private String id = null;
    protected boolean ecritureOk = Boolean.FALSE; // il est positionn? sur true quand une sauvegarde en BD a r?ussi
    private String clefPrimaire = null;
//    protected CartesianChartModel chartModel = null;
    protected String chartDialogHeader = null;
    private BaseBeanEntite visitedEntity;
    private BaseBeanEntite ficheASupprimer;
    private BaseBeanEntite ficheBranche = null;
    private BaseBeanEntite toggledRow = null;
    private BaseBeanEntite toggledRow_O2M = null;
//    private TreeNode selectedNode;
//    private StreamedContent logMigration;
    protected String opCodeCsv = "test";
    private String nomDeFamille;
    private String prenom;
    private String prefixePrenom;
    private String cpiIdPourParcours;
    protected Date date1;
    protected Date date2;
    private String selecteurProfil = "fiche"; // choisir entre fiche|carrieres|documents|diplomes|fonctions|decorations|sanctions
    protected GestionstockClient nouvelleFiche; // utilis�e pour la cr�ation directe d'une fiche : voir script nouveau.xhtml
    private BaseBeanEntite nouvelleFiche_M2O;
    private List<? extends BaseBeanEntite> filteredRows = null;

    @SuppressWarnings("unused")
    @PersistenceContext(unitName = "managerPU")
    private EntityManager entityManager;
//    @Inject
//    protected DaoInterfaces dao;
//    @inject
//    protected DaoInterfaces dao;
//    @Inject
//    protected transient Logger logger;
    //private UploadedFile uploadedFile;

    // private List<EtudiantEntite> etudiants;
    private Date currentDateTime = new Date();
    private String fmtDate = "dd/MM/yyyy";
//    private UploadedFile fichierCsv;
    private String etablissement = "";
    private String specialite = "";
    private Map<String, Map<String, Integer>> fraisDetailEtranger = new HashMap<String, Map<String, Integer>>();
    private Map<String, Map<String, Integer>> fraisDetailBeninois = new HashMap<String, Map<String, Integer>>();
    private PieChartModel pieModel;
    private PieChartModel pieModelEtranger;
    private PieChartModel pieModelBeninois;
    private int montantMedicauxTotal;
    private int montantPercuTotalBeninois;
    private int tauxDroitsBeninois;
    private int montantInscriptionTotalBeninois;
    private int montantFormationTotalBeninois;
    private int montantMedicauxTotalBeninois;
    private int tauxDroitsEtranger;
    private int montantMedicauxTotalEtranger;
    private int montantInscriptionTotalEtranger;
    private int montantFormationTotalEtranger;

    private Map< String, Map<String, Float>> mapApresRetrocessionBeninois = new HashMap<String, Map<String, Float>>();
    private Map< String, Map<String, Float>> mapApresRetrocessionEtranger = new HashMap<String, Map<String, Float>>();
//    public void handleFileUploadListener(FileUploadEvent event) {
//        fichierCsv = event.getFile();
//    }

    public void attacherResultat(File fichierAttache) throws IOException {
        if (fichierAttache.length() <= 0) {
            return;
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext xtContext = fc.getExternalContext();
        xtContext.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        xtContext.setResponseContentType("application/zip"); // Check http://www.w3schools.com/media/media_mimeref.asp for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
        String disposition = "attachment; filename=\"autorisation.zip\"";
        xtContext.setResponseHeader("Content-Disposition", disposition);
        xtContext.setResponseContentLength((int) fichierAttache.length());
        try (
                FileInputStream fileIn = new FileInputStream(fichierAttache)) {
            byte[] outputByte = new byte[4096];
            //copy binary to ServletOutputStream
            OutputStream servletOutputStream = xtContext.getResponseOutputStream();
            while (fileIn.read(outputByte, 0, 4096) != -1) {
                servletOutputStream.write(outputByte, 0, 4096);
            }
            servletOutputStream.flush();
        }
        fc.renderResponse();
        fc.responseComplete();
    }

    /**
     *
     * @param valeur NE , NEEN, NELE, NEVERS
     * @return une des valeurs suivantes : ne , neEn, neLe, neVers
     */
    public String traiterCommentaireDateNaissance(String valeur) {
        /*
         * le parser de superCSV assure que "commentaireDateNaissance" contient la bonne valeur
         * � ceci pr�s que le minuscule/majuscule peut ne pas avoir �t� respect�. Il faut donc
         * transformer la valeur pour s'assurer que le minuscule/majuscule est respect�.
         */
        String commentaireDateNaissance = valeur.toUpperCase();
        if (commentaireDateNaissance.equals(JSFUtility.NAISSANCE_EN.toUpperCase())) {
            commentaireDateNaissance = JSFUtility.NAISSANCE_EN;
        } else if (commentaireDateNaissance.equals(JSFUtility.NAISSANCE_LE.toUpperCase())) {
            commentaireDateNaissance = JSFUtility.NAISSANCE_LE;
        } else if (commentaireDateNaissance.equals(JSFUtility.NAISSANCE_NE.toUpperCase())) {
            commentaireDateNaissance = JSFUtility.NAISSANCE_NE;
        } else if (commentaireDateNaissance.equals(JSFUtility.NAISSANCE_VERS.toUpperCase())) {
            commentaireDateNaissance = JSFUtility.NAISSANCE_VERS;
        }
        return commentaireDateNaissance;
    }

    /**
     * Trouver le chemin absolu d'un chemin relatif au contexte de l'application
     * Okapi.
     *
     * @param relativePath le chemin relatif.
     * @return le chemin absolu correspondant.
     */
    public String getAbsolutePath(String relativePath) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        return servletContext.getRealPath("/") + relativePath;
    }

    public void anneeAcademiqueLISTENER() {
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void refreshCache() throws Exception {
        entityManager.getEntityManagerFactory().getCache().evictAll();
    }

    /*
     * initialiser la propri�t� nouvelleFiche.
     * @param classFqnName nom complet de la classe Entit� � cr�er
     * @return null
     */
    public String creerNouvelleFicheAction(String classFqnName) {
        try {
            
            Class classz = Class.forName(classFqnName);
            nouvelleFiche =  (GestionstockClient) classz.newInstance();
            
            System.out.println("OK");
            
        } catch (Exception e) {
            System.out.println("Non n OK");
            JSFUtility.queueException(e);
        }
        return null;
    }

    
    
     
     
     
     
//    public String supprimerSelectionsAction(String type, String listProperty) {
//        try {
//            if (type.equals(listTypeMain)) {
//                if (selections == null || selections.length == 0) {
//                    JSFUtility.addWarnMessage("Veuilsauvegeraderlez s�lectionner les fiches � supprimer.");
//                    return null;
//                }
//                this.delete(selections);
//                refreshCache(); // EbM 13 10 04
//                if (okapiLazyModel != null && okapiLazyModel.getRowCount() > 0) {
//                    for (BaseBeanEntite element : selections) {
//                        element.removeFromMany2OneProperties(); // EbM 2013 09 27
//                        if (((List) okapiLazyModel.getWrappedData()).contains(element)) {
//                            ((List) okapiLazyModel.getWrappedData()).remove(element);
//                            if (toggledRow != null && element.getId().equals(toggledRow.getId())) {
//                                toggledRow = null;
//                            }
//                        }
//                    }
//                    okapiLazyModel.setRowCount(okapiLazyModel.getRowCount() - selections.length);
//                }
//            } else if (type.equals(listTypeO2M)) {
//                if (selection == null) {
//                    JSFUtility.addWarnMessage("Veuillez s�lectionner la fiche � supprimer.");
//                    return null;
//                }
////                BaseBeanEntite beanToDelete = selection;
////                if (!entityManager.contains(selection)) {
////                    beanToDelete = this.findById(selection.getClass(), selection.getId());
////                }
////                if (beanToDelete == null) {
////                    JSFUtility.addErrorMessage("La fiche '" + selection.toString() + "' doit �tre persist�e avant qu'elle ne soit supprim�e; impossible de la persister.");
////                    return null;
////                }
//                _remove(selection);
//                toggledRow.removeManyRelation(selection, listProperty);
//            }
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//            return null;
//        }
//        JSFUtility.addInfoMessage("ok !");
//        return null;
//    }

    /*
     * v�rifier que l'objet en cours d'�dition est du type donn�.
     * @param simpleName nom du type; c'est le nom abr�g� d'une classe Entit�.
     * @param type : "nouvelleFiche" pour une nouvelle Entit� principale, "nouvelleFiche_M2O" pour
     * une nouvelle Entit� d'une liste O2M, "toggledRow" pour une fiche principale en �dition,
     * "toggledRow_O2M" pour une fiche d'une liste O2M en �dition.
     * @return TRUE si l'objet en cours d'�dition est du type donn�, FALSE dans le cas contraire.
     */
    public boolean assertToggled(String simpleName, String type) {
        boolean retval = Boolean.FALSE;
        switch (type) {
            case "nouvelleFiche":
                if (getNouvelleFiche() != null && getNouvelleFiche().getClass().getSimpleName().equals(simpleName)) {
                    retval = Boolean.TRUE;
                }
                break;
            case "nouvelleFiche_M2O":
                if (getNouvelleFiche_M2O() != null && getNouvelleFiche_M2O().getClass().getSimpleName().equals(simpleName)) {
                    retval = Boolean.TRUE;
                }
                break;
            case "toggledRow":
                BaseBeanEntite e = getToggledRow();
                if (e != null && e.getClass().getSimpleName().equals(simpleName)) {
                    retval = Boolean.TRUE;
                }
                break;
            case "toggledRow_O2M":
                if (getToggledRow_O2M() != null && getToggledRow_O2M().getClass().getSimpleName().equals(simpleName)) {
                    retval = Boolean.TRUE;
                }
                break;
            default:
                break;
        }
        return retval;
    }

    public <P extends BaseBeanEntite> boolean foundById(Class<P> classz, String id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<P> root = countQuery.from(classz);
        Predicate conditions = builder.equal(root.<String>get("id"), id);
        countQuery.where(conditions);
        countQuery.select(builder.count(root));
        TypedQuery<Long> tq = entityManager.createQuery(countQuery);
        try {
            int count = tq.getSingleResult().intValue();
            return (count > 0);
        } catch (NoResultException nre) {
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

//        HSSFCellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setFillBackgroundColor((short)0xaa0708);
//        cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
//        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);

            cell.setCellStyle(cellStyle);
        }
    }

    /*
     * cr�er une nouvelle fiche O2O ayant 'toggledRow' du c�t� ma�tre de la relation One2One. La sauvegarde
     * se fera automatiquement en sauvegardant l'objet 'toggeldRow'
     * @param entityClassName nom complet de la classe entit� de la nouvelle fiche
     * @param propertyFromToggledRow nom de la propri�t� O2O de 'toggledRow' qui contiendra la nouvelle fiche
     * @return script de retour, null si on change pas de script.
     */
    public String nouvelleFiche_O2O_action(String entityClassName, String masterTargetProperty, String slaveTargetProperty) {
        if (toggledRow == null) {
            JSFUtility.addErrorMessage("La fiche repr�sentant le c�t� One de la relation est inexistante.");
            return null;
        }
        try {
            if (!JSFUtility.propertyExists(toggledRow, masterTargetProperty)) {
                JSFUtility.addErrorMessage(
                        "Property '" + masterTargetProperty + "' is not found in class '" + toggledRow.getClass().getName() + "'");
                return null;
            }
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        try {
            // cr�er une instance de la nouvelle fiche
            Class classz = Class.forName(entityClassName);
            try {
                if (!JSFUtility.propertyExists(classz, slaveTargetProperty)) {
                    JSFUtility.addErrorMessage(
                            "Property '" + slaveTargetProperty + "' is not found in class '" + classz.getName() + "'");
                    return null;
                }
            } catch (Exception e) {
                JSFUtility.queueException(e);
            }
            BaseBeanEntite bean = (BaseBeanEntite) classz.newInstance();
            bean.addOneSide(toggledRow, slaveTargetProperty);
            // mettre <toggledRow> du c�t� "M" de la relation M2O
            toggledRow.addOneSide(bean, masterTargetProperty);

        } catch (Exception exc) {
            JSFUtility.queueException(exc);
        }
        return null;
    }

    public String delete_O2O_action(BaseBeanEntite beanToDelete, String propertyFromToggledRow) {
        if (toggledRow == null) {
            JSFUtility.addErrorMessage("La fiche repr�sentant le c�t� One de la relation est inexistante.");
            return null;
        }
        try {
            if (!JSFUtility.propertyExists(toggledRow, propertyFromToggledRow)) {
                JSFUtility.addErrorMessage(
                        "Property '" + propertyFromToggledRow + "' is not found in class '" + toggledRow.getClass().getName() + "'");
                return null;
            }
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        try {
            _remove(beanToDelete);
            //refresh(toggledRow);
            toggledRow.deleteOneSide(beanToDelete, propertyFromToggledRow);
//            _save(toggledRow); // this will actually result in a merge operation
        } catch (Exception exc) {
            JSFUtility.queueException(exc);
        }
        return null;
    }

    /*
     * cr�er une nouvelle fiche M2O ayant toggledRow_O2M du c�t� One de la relation.
     * @param entityClassName nom complet de la classe entit� de la nouvelle fiche
     * @param m2oProperty nom de la propri�t� M2O de toggledRow_O2M qui contiendra <toggledRow>
     * @return script de retour, null si on change pas de script.
     */
    public String nouvelleFiche_M2O_action(String entityClassName, String propertyFromToggledRow) {
        if (toggledRow == null) {
            JSFUtility.addErrorMessage("La fiche repr�sentant le c�t� One de la relation est inexistante.");
            return null;
        }
        try {
            // cr�er une instance de la nouvelle fiche
            Class classz = Class.forName(entityClassName);
            try {
                if (!JSFUtility.propertyExists(classz, propertyFromToggledRow)) {
                    JSFUtility.addErrorMessage(
                            "Property '" + propertyFromToggledRow + "' is not found in class '" + classz.getName() + "'");
                    return null;
                }
            } catch (Exception e) {
                JSFUtility.queueException(e);
                return null;
            }
            nouvelleFiche_M2O = (BaseBeanEntite) classz.newInstance();
            // mettre <toggledRow> du c�t� "M" de la relation M2O
            nouvelleFiche_M2O.addManySide(toggledRow, propertyFromToggledRow);
        } catch (Exception exc) {
            JSFUtility.queueException(exc);
        }
        return null;
    }

    public BaseBeanEntite getVisitedEntity() {
        if (visitedEntity == null) {
            try {
                Class classz = Class.forName(nomCompletDeLaClasse);
                Object obj = classz.newInstance();
                if (obj instanceof BaseBeanEntite) {
                    visitedEntity = (BaseBeanEntite) classz.newInstance();
                } else {
                    visitedEntity = null;
                }
            } catch (ClassNotFoundException cnfe) {
                visitedEntity = null;
            } catch (InstantiationException ie) {
                visitedEntity = null;
            } catch (IllegalAccessException iae) {
                visitedEntity = null;
            }
        }
        return visitedEntity;
    }

    public void setVisitedEntity(BaseBeanEntite visitedEntity) {
        this.visitedEntity = visitedEntity;
    }

    public GestionstockClient getNouvelleFiche() {
        if (nouvelleFiche != null) {
            return nouvelleFiche;
        }
        try {
            Class classz = Class.forName(nomCompletDeLaClasse);
            nouvelleFiche = (GestionstockClient) classz.newInstance();
            return nouvelleFiche;
        } catch (Exception exc) {
            JSFUtility.queueException(exc);
        }
        return null;
    }

    /*
     * Cette m�thode sert � homog�n�iser les sp�cifaction du composant dlgFormFooter.xhtml.
     * La seule action qu'elle prend, c'est appeler la m�thode sauvegarderNouvelleFicheAction().
     * @param listeO2M non utilis�
     * @param m2oProperty
     */
    public String sauvegarderNouvelleFicheAction(String listeO2M, String m2oProperty) {
        return sauvegarderNouvelleFicheAction();
    }

//    public Map<String, Map<String, Float>> getFraisRepartisParSpecialite(String etablissement, String specialite) {
//        if (etablissement.isEmpty() || specialite.isEmpty()) {
//            return null;
//        }
//
//        Map<String, List<RetrocessionComptabilite>> comptabilites = getComptabiliteParTypeFraisInscription(etablissement);
//        Map< String, Map<String, Float>> results = new HashMap<String, Map<String, Float>>();
//        if (comptabilites == null) {
//            return new HashMap<String, Map<String, Float>>();
//        }
//        RetrocessionIndicateurRepartition indicateur = getIndicateurRepartition();
//        int fraisMedical = getFraisVisite().intValue();
//        Map<String, Float> cleFraisFormationClassique = cleRepartionFormationSuivantType("CLASSIQUE");
//        Map<String, Float> cleFraisFormationProfessionnelle = cleRepartionFormationSuivantType("PROFESSIONNEL");
//        Map<String, Float> cleFraisFormationContinue = cleRepartionFormationSuivantType("CONTINUE");
//
//        Map<String, Float> mapFraisFormationClassique = new HashMap<String, Float>();
//        Map<String, Float> mapFraisFormationProfessionnelle = new HashMap<String, Float>();
//        Map<String, Float> mapFraisInscription = new HashMap<String, Float>();
//
//        for (Map.Entry<String, List<RetrocessionComptabilite>> elt : comptabilites.entrySet()) {
//            String typeFraisInscription = elt.getKey();
//            double montantInscriptionAPartager = 0;
//            double montantFormationClassiqueAPartager = 0;
//            double montantFormationProfessionnelleAPartager = 0;
//            double montantFormationContinueAPartager = 0;
//            List<RetrocessionComptabilite> valeurs = elt.getValue();
//            montantRetrocessionFraisMedicaux += fraisMedical * valeurs.size();
//
//            Map<String, Float> cleFraisInscription = cleRepartionInscriptionSuivantMontant(typeFraisInscription);
//            double fraisInscription = Double.parseDouble(typeFraisInscription);
//            for (RetrocessionComptabilite compt : valeurs) {
//
//                double tmp = compt.getMontantPayer() - fraisInscription;
//                if (compt.getMontantPayer() >= fraisInscription) {
//                    montantInscriptionAPartager = fraisInscription - fraisMedical;
//                } else {
//                    tmp = compt.getMontantPayer();
//                }
//
//                if (compt.getTypeFormation().getId().toUpperCase().equals("CLASSIQUE")) {
//                    montantFormationClassiqueAPartager += tmp;
//                }
//
//                if (compt.getTypeFormation().getId().toUpperCase().equals("PROFESSIONNEL")) {
//                    montantFormationProfessionnelleAPartager += tmp;
//                }
//
//                if (compt.getTypeFormation().getId().toUpperCase().equals("CONTINUE")) {
//                    montantFormationContinueAPartager += tmp;
//                }
//            }
//
//            if (montantInscriptionAPartager > 0) {
//                for (Map.Entry<String, Float> key : cleFraisInscription.entrySet()) {
//                    float taux = key.getValue();
//                    String organe = key.getKey();
//
//                    if (mapFraisInscription.get(organe) != null) {
//                        float tampon = (float) (mapFraisInscription.get(organe) + taux * montantInscriptionAPartager);
//                        mapFraisInscription.put(organe, tampon);
//                    } else {
//                        float tampon = (float) (taux * montantInscriptionAPartager);
//                        mapFraisInscription.put(organe, tampon);
//                    }
//                }
//            }
//
//            if (montantFormationClassiqueAPartager > 0) {
//                for (Map.Entry<String, Float> key : cleFraisFormationClassique.entrySet()) {
//                    float taux = key.getValue();
//                    String organe = key.getKey();
//
//                    if (mapFraisFormationClassique.get(organe) != null) {
//                        float tampon = (float) (mapFraisFormationClassique.get(organe) + taux * montantFormationClassiqueAPartager);
//                        mapFraisFormationClassique.put(organe, tampon);
//                    } else {
//                        float tampon = (float) (taux * montantFormationClassiqueAPartager);
//                        mapFraisFormationClassique.put(organe, tampon);
//                    }
//                }
//            }
//            if (montantFormationProfessionnelleAPartager > 0) {
//                for (Map.Entry<String, Float> key : cleFraisFormationProfessionnelle.entrySet()) {
//                    float taux = key.getValue();
//                    String organe = key.getKey();
//
//                    if (mapFraisFormationProfessionnelle.get(organe) != null) {
//                        float tampon = (float) (mapFraisFormationProfessionnelle.get(organe) + taux * montantFormationProfessionnelleAPartager);
//                        mapFraisFormationProfessionnelle.put(organe, tampon);
//                    } else {
//                        float tampon = (float) (taux * montantFormationProfessionnelleAPartager);
//                        mapFraisFormationProfessionnelle.put(organe, tampon);
//                    }
//                }
//            }
//        }
//
//        /*
//         Reconstitution des montants retroc�d�s par organe et par type de montant (frais inscription, formation clssique, formation professionnelle)
//         */
//        for (Map.Entry<String, Float> elt : mapFraisInscription.entrySet()) {
//            String key = elt.getKey();
//            float valeur = elt.getValue();
//
//            if (results.get(key) != null) {
//                Map<String, Float> tamp = results.get(key);
//                tamp.put("fraisInscription", valeur);
//                results.put(key, tamp);
//            } else {
//                Map<String, Float> tamp = new HashMap<String, Float>();
//                tamp.put("fraisInscription", valeur);
//                results.put(key, tamp);
//            }
//        }
//        for (Map.Entry<String, Float> elt : mapFraisFormationClassique.entrySet()) {
//            String key = elt.getKey();
//            float valeur = elt.getValue();
//
//            if (results.get(key) != null) {
//                Map<String, Float> tamp = results.get(key);
//                tamp.put("fraisFormationClassique", valeur);
//                results.put(key, tamp);
//            } else {
//                Map<String, Float> tamp = new HashMap<String, Float>();
//                tamp.put("fraisFormationClassique", valeur);
//                results.put(key, tamp);
//            }
//        }
//
//        for (Map.Entry<String, Float> elt : mapFraisFormationProfessionnelle.entrySet()) {
//            String key = elt.getKey();
//            float valeur = elt.getValue();
//
//            if (results.get(key) != null) {
//                Map<String, Float> tamp = results.get(key);
//                tamp.put("fraisFormationProfessionnelle", valeur);
//                results.put(key, tamp);
//            } else {
//                Map<String, Float> tamp = new HashMap<String, Float>();
//                tamp.put("fraisFormationProfessionnelle", valeur);
//                results.put(key, tamp);
//            }
//        }
//
//        return results;
//    }
  

    public String getDateCourante() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(Calendar.getInstance().getTime());
    }

  
//    @Named("toutesLesNationalites")
//    @ToutesLesFichesQualifier
//    public List<RetrocessionNomenclatureNationalite> getToutesLesNationalites() {
//        Query query = entityManager.createQuery("select o from RetrocessionNomenclatureNationalite o");
//        List<RetrocessionNomenclatureNationalite> answers = query.getResultList();
//        return answers;
//    }
//
//    @Named("tousLesTypesFormation")
//    @ToutesLesFichesQualifier
//    public List<RetrocessionTypeFormation> getTousLesTypesFormation() {
//        Query query = entityManager.createQuery("select o from RetrocessionTypeFormation o");
//        List<RetrocessionTypeFormation> answers = query.getResultList();
//        return answers;
//    }
//
//    @Named("tousLesTypesFrais")
//    @ToutesLesFichesQualifier
//    public List<RetrocessionTypeFrais> getTousLesTypesFrais() {
//        Query query = entityManager.createQuery("select o from RetrocessionTypeFrais o");
//        List<RetrocessionTypeFrais> answers = query.getResultList();
//        return answers;
//    }
//
//    @Named("tousLesParametres")
//    @TousLesParametresQualifier
//    public List<RetrocessionParametre> getTousLesParametres() {
//        Query query = entityManager.createQuery("select o from RetrocessionParametre o");
//        List<RetrocessionParametre> answers = query.getResultList();
//        return answers;
//    }
//
//    @Named("toutesLesClesRepartitionFraisFormation")
//    @ToutesLesClesRepartitionQualifier
//    public List<RetrocessionCleRepartitionFraisFormation> getToutesLesClesRepartitionFraisFormation() {
//        Query query = entityManager.createQuery("select o from RetrocessionCleRepartitionFraisFormation o");
//        List<RetrocessionCleRepartitionFraisFormation> answers = query.getResultList();
//        return answers;
//    }
//
//    @Named("toutesLesClesRepartition")
//    @ToutesLesClesRepartitionQualifier
//    public List<RetrocessionCleRepartitionFraisInscription> getToutesLesClesRepartition() {
//        Query query = entityManager.createQuery("select o from RetrocessionCleRepartitionFraisInscription o");
//        List<RetrocessionCleRepartitionFraisInscription> answers = query.getResultList();
//        return answers;
//    }
//
//    @Named("tousLesIndicateursRepartition")
//    @TousLesIndicateursRepartitionQualifier
//    public List<RetrocessionIndicateurRepartition> getTousLesIndicateursRepartition() {
//        Query query = entityManager.createQuery("select o from RetrocessionIndicateurRepartition o");
//        List<RetrocessionIndicateurRepartition> answers = query.getResultList();
//        return answers;
//    }
//
//    @Named("tousLesOrganes")
//    @TousLesOrganesQualifier
//    public List<RetrocessionOrgane> getTousLesOrganes() {
//        Query query = entityManager.createQuery("select o from RetrocessionOrgane o");
//        List<RetrocessionOrgane> answers = query.getResultList();
//        return answers;
//    }
//
//    @Named("tousLesEtablissements")
//    @ToutesLesFichesQualifier
//    public List<String> getTousLesEtablissements() {
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<String> query = builder.createQuery(String.class
//        );
//        Root<RetrocessionInscriptionEtudiant> root = query.from(RetrocessionInscriptionEtudiant.class);
//
//        query.select(root.<String>get("etablissement"))
//                .distinct(true).orderBy(builder.asc(root.<String>get("etablissement")));
//        TypedQuery<String> tpQ = entityManager.createQuery(query);
//        List<String> answ = tpQ.getResultList();
//        return answ;
//    }
//
//    @Named("toutesLesSpecialites")
//    @ToutesLesFichesQualifier
//    public List<String> getSpecialitesViaEtablissement() {
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<String> query = builder.createQuery(String.class
//        );
//        Root<RetrocessionInscriptionEtudiant> root = query.from(RetrocessionInscriptionEtudiant.class);
//        Predicate condition = builder.equal(root.<String>get("etablissement"), etablissement);
//
//        query.select(root.<String>get("specialite"))
//                .distinct(true).where(condition);
//        TypedQuery<String> tpQ = entityManager.createQuery(query);
//        List<String> answ = tpQ.getResultList();
//        return answ;
//    }
//
//    @Named("toutesLesAnneesAcademiques")
//    @ToutesLesAnneesAcademiquesQualifier
//    public List<RetrocessionAnneeAcademique> getToutesLesAnneesAcademiques() {
//        Query query = entityManager.createQuery("select a from RetrocessionAnneeAcademique a order by a.id desc");
//        List<RetrocessionAnneeAcademique> answers = query.getResultList();
//        return answers;
//    }
    /*
     * Cette m�thode sert � homog�n�iser les sp�cifaction du composant dlgFormFooter.xhtml.
     * La seule action qu'elle prend, c'est appeler la m�thode sauvegarderNouvelleFicheAction().
     * @param listeO2M non utilis�
     * @param m2oProperty
     */

    public String sauvegarderNouvelleFicheActionBis() {

        return sauvegarderNouvelleFicheAction();
    }

    public void sauvegarderN0ouvelleFicheActionBi() {
        sauvegarderNouvelleFicheAction();
    }

    public String goProfil() {
        return "profil.jsf?faces-redirect=true";
    }

    public String sauvegarderNouvelleFicheAction() {

        if (nouvelleFiche == null) {
            creerNouvelleFicheAction("uac.net.managerstock.GestionstockProduit");
        
            System.out.println("OK");
            return null;
            
        }
        if (nouvelleFiche.isNouvelleFiche() && nouvelleFiche.getId() != null) {
            try {

                if (this.findById(nouvelleFiche.getClass(), nouvelleFiche.getId()) != null) {
                    JSFUtility.addInfoMessage("Contr�le", "Cet enregistrement existe d�j�.");
                    return null;
                }
            } catch (Exception e) {
                JSFUtility.queueException(e);
                return null;
            }
        }
        try {

            boolean newRecord = nouvelleFiche.isNouvelleFiche();
            
            try {
                _save(nouvelleFiche);

            } catch (Exception e) {
                if (newRecord && ((e instanceof DatabaseException) || (e.getCause() instanceof DatabaseException))) {
                    nouvelleFiche.setVersion(null);
                    nouvelleFiche.setId(null);
                }
                JSFUtility.queueException(e);
                return null;
            }
            JSFUtility.addMessage("Message", "Op�ration r�ussie !!!", FacesMessage.SEVERITY_INFO);

        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        try {

            /*
             La ligne suivante est ajout�e en vue de pouvoir encha�ner les cr�ations sans
             �tre tenu d'appeler la m�thode creerNouvelleFiche
             */
            nouvelleFiche = nouvelleFiche.getClass().newInstance();
        } catch (Exception e) {

        }

        return null;
    }

    public String getEffectifTotalBeninois() {
        int effectifTotal = 0;
        if (fraisDetailBeninois.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailBeninois.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("effectif")) {
                    effectifTotal += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(effectifTotal);
    }

    public String getTauxDroitsTotalBeninois() {
        tauxDroitsBeninois = 0;
        if (fraisDetailBeninois.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailBeninois.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantPayer")) {
                    tauxDroitsBeninois += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(tauxDroitsBeninois);
    }

    public String getMontantPercuTotalBeninois() {
        montantPercuTotalBeninois = 0;
        if (fraisDetailBeninois.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailBeninois.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantPercu")) {
                    montantPercuTotalBeninois += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(montantPercuTotalBeninois);
    }

    public String getMontantMedicauxTotalBeninois() {
        montantMedicauxTotalBeninois = 0;
        if (fraisDetailBeninois.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailBeninois.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantMedicaux")) {
                    montantMedicauxTotalBeninois += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(montantMedicauxTotalBeninois);
    }

    public String getMontantInscriptionTotalBeninois() {
        montantInscriptionTotalBeninois = 0;
        if (fraisDetailBeninois.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailBeninois.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantInscription")) {
                    montantInscriptionTotalBeninois += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(montantInscriptionTotalBeninois);
    }

    public String getMontantFormationTotalBeninois() {
        montantFormationTotalBeninois = 0;
        if (fraisDetailBeninois.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailBeninois.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantFormation")) {
                    montantFormationTotalBeninois += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(montantFormationTotalBeninois);
    }

    public Map<String, Integer> getMontantApresRetocessionEtranger() {
        if (mapApresRetrocessionEtranger.size() == 0) {
            new HashMap<String, Integer>();
        }

        Map<String, Integer> results = new HashMap<>();
        for (Map.Entry<String, Map<String, Float>> elt : mapApresRetrocessionEtranger.entrySet()) {
            int montant = 0;
            String organe = elt.getKey();
            for (Map.Entry<String, Float> tmp : elt.getValue().entrySet()) {
                String key = tmp.getKey();
                montant += tmp.getValue();
            }

            results.put(organe, montant);
        }

        return results;
    }
    
    public Map<String, Integer> getMontantApresRetocessionBeninois() {
        if (mapApresRetrocessionBeninois.size() == 0) {
            new HashMap<String, Integer>();
        }

        Map<String, Integer> results = new HashMap<>();
        for (Map.Entry<String, Map<String, Float>> elt : mapApresRetrocessionBeninois.entrySet()) {
            int montant = 0;
            String organe = elt.getKey();
            for (Map.Entry<String, Float> tmp : elt.getValue().entrySet()) {
                String key = tmp.getKey();
                montant += tmp.getValue();
            }

            results.put(organe, montant);
        }

        return results;
    }
    
    public Map<String, Integer> getMontantApresRetocession() {
        if (fraisRetrocedes.size() == 0) {
            new HashMap<String, Integer>();
        }

        Map<String, Integer> results = new HashMap<>();
        for (Map.Entry<String, Map<String, Float>> elt : fraisRetrocedes.entrySet()) {
            int montant = 0;
            String organe = elt.getKey();
            for (Map.Entry<String, Float> tmp : elt.getValue().entrySet()) {
                String key = tmp.getKey();
                montant += tmp.getValue();
            }

            results.put(organe, montant);
        }

        return results;
    }

    public PieChartModel getPieModel() {
        pieModel = new PieChartModel();
        for (Map.Entry<String, Integer> elt : getMontantApresRetocession().entrySet()) {
            if (elt.getKey().equalsIgnoreCase("Etablissements")) {
                pieModel.set(etablissement, elt.getValue());
                continue;
            }
            pieModel.set(elt.getKey(), elt.getValue());
        }
        return pieModel;
    }

    public PieChartModel getPieModelBeninois() {
        pieModelBeninois = new PieChartModel();

//        for (Map.Entry<String, Integer> elt : getMontantApresRetocessionBeninois().entrySet()) {
//            if (elt.getKey().equalsIgnoreCase("Etablissements")) {
//                pieModel.set(etablissement, elt.getValue());
//                continue;
//            }
//            pieModel.set(elt.getKey(), elt.getValue());
//        }
        pieModelBeninois.set("Droits de formation", montantFormationTotalBeninois);
        pieModelBeninois.set("Droits d'inscription", montantInscriptionTotalBeninois);
        pieModelBeninois.set("Visite m�dicale", montantMedicauxTotalBeninois);

        return pieModelBeninois;
    }

    public PieChartModel getPieModelEtranger() {
        pieModelEtranger = new PieChartModel();

        pieModelEtranger.set("Droits de formation", montantFormationTotalEtranger);
        pieModelEtranger.set("Droits d'inscription", montantInscriptionTotalEtranger);
        pieModelEtranger.set("Visite m�dicale", montantMedicauxTotalEtranger);
//        for (Map.Entry<String, Integer> elt : getMontantApresRetocessionEtranger().entrySet()) {
//            if (elt.getKey().equalsIgnoreCase("Etablissements")) {
//                pieModel.set(etablissement, elt.getValue());
//                continue;
//            }
//            pieModel.set(elt.getKey(), elt.getValue());
//        }
        return pieModelEtranger;
    }

    public String getEffectifTotalEtranger() {
        int effectifTotal = 0;
        if (fraisDetailEtranger.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailEtranger.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("effectif")) {
                    effectifTotal += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(effectifTotal);
    }

    public String getTauxDroitsTotalEtranger() {
        tauxDroitsEtranger = 0;
        if (fraisDetailEtranger.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailEtranger.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantPayer")) {
                    tauxDroitsEtranger += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(tauxDroitsEtranger);
    }

    public String getMontantPercuTotalEtranger() {
        int montantPercuTotal = 0;
        if (fraisDetailEtranger.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailEtranger.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantPercu")) {
                    montantPercuTotal += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(montantPercuTotal);
    }

    public Map<String, Map<String, Float>> getMapApresRetrocessionBeninois() {
        return mapApresRetrocessionBeninois;
    }

    public Map<String, Map<String, Float>> getMapApresRetrocessionEtranger() {
        return mapApresRetrocessionEtranger;
    }

    
    public String getMontantMedicauxTotalEtranger() {
        montantMedicauxTotalEtranger = 0;
        if (fraisDetailEtranger.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailEtranger.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantMedicaux")) {
                    montantMedicauxTotalEtranger += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(montantMedicauxTotalEtranger);
    }

    public String getMontantInscriptionTotalEtranger() {
        montantInscriptionTotalEtranger = 0;
        if (fraisDetailEtranger.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailEtranger.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantInscription")) {
                    montantInscriptionTotalEtranger += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(montantInscriptionTotalEtranger);
    }

    public String getMontantFormationTotalEtranger() {
        montantFormationTotalEtranger = 0;
        if (fraisDetailEtranger.size() == 0) {
            return "0";
        }

        for (Map.Entry<String, Map<String, Integer>> tmp : fraisDetailEtranger.entrySet()) {
            String cle = tmp.getKey();
            for (Map.Entry<String, Integer> elt : tmp.getValue().entrySet()) {
                if (elt.getKey().equalsIgnoreCase("montantFormation")) {
                    montantFormationTotalEtranger += elt.getValue().intValue();
                    break;
                }
            }
        }
        return new DecimalFormat("#,##0").format(montantFormationTotalEtranger);
    }
    
    public List<Map.Entry<String, Map<String, Float>>> getRetrocessionEtranger() {
        if (etablissement.isEmpty()) {
            return new ArrayList<Map.Entry<String, Map<String, Float>>>();
        }
        if (mapApresRetrocessionEtranger.size() == 0) {
            return new ArrayList<Map.Entry<String, Map<String, Float>>>();
        }

        Set<Map.Entry<String, Map<String, Float>>> retro = mapApresRetrocessionEtranger.entrySet();
        return new ArrayList<Map.Entry<String, Map<String, Float>>>(retro);
    }

    public List<Map.Entry<String, Map<String, Float>>> getRetrocessionBeninois() {
        if (etablissement.isEmpty()) {
            return new ArrayList<Map.Entry<String, Map<String, Float>>>();
        }
        if (mapApresRetrocessionBeninois.size() == 0) {
            return new ArrayList<Map.Entry<String, Map<String, Float>>>();
        }

        Set<Map.Entry<String, Map<String, Float>>> retro = mapApresRetrocessionBeninois.entrySet();
        return new ArrayList<Map.Entry<String, Map<String, Float>>>(retro);
    }

    
    public String action() {
        return null;
    }

    /*
     * @param dummy not used but required by the dlgFormFooter.xhtml component
     */

    public String instanceNouvelleFicheAction(String dummy) {
        if (nouvelleFiche == null) {
            JSFUtility.addMessage("La fiche pr�c�dente sur laquelle doit �tre bas�e la nouvelle est inexistante.");
            return null;
        }
        try {
            nouvelleFiche = (GestionstockClient) nouvelleFiche.getClass().newInstance();
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        return null;
    }

    public String instanceNouvelle_M2O_action(String m2oProperty) {
        if (nouvelleFiche_M2O == null) {
            JSFUtility.addMessage("La fiche M2O pr�c�dente sur laquelle doit �tre bas�e la nouvelle est inexistante.");
            return null;
        }
        try {
            nouvelleFiche_M2O = (BaseBeanEntite) nouvelleFiche_M2O.getClass().newInstance();
            nouvelleFiche_M2O.addManySide(toggledRow, m2oProperty);
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        return null;
    }

    /*
     * sauvegarder la nouvelle fiche M2O
     * @param listeO2M propri�t� "O2M" de <toggledRow> qui contient la r�f�rence � <nouvelleFiche_M2O>
     * @param m2oProperty propri�t� "M2O" de <nouvelleFiche_M2O> qui contient la r�f�rence � <toggledRow>
     */
    public String sauvegarderNouvelle_M2O_action(String listeO2M, String m2oProperty) {
        if (nouvelleFiche_M2O == null) {
            JSFUtility.addErrorMessage("La fiche � sauvegarder n'est pas d�finie.");
            return null;
        }
        try {
            if (!JSFUtility.propertyExists(nouvelleFiche_M2O, m2oProperty)) {
                JSFUtility.addErrorMessage(
                        "Property '" + m2oProperty + "' is not found in class '" + nouvelleFiche_M2O.getClass().getName() + "'");
                return null;
            }
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        //BaseBeanEntite parent = getPremiereFicheSelectionnee();
        if (toggledRow == null) {
            JSFUtility.addErrorMessage("Vous voulez sauvegarder une fiche branche dont la fiche parente n'est pas d�finie; v�rifiez qu'une fiche parente est s�lectionn�e.");
            return null;
        }
        try {
//            String s = (utilisateurCourant == null) ? "Inconnu" : utilisateurCourant.getNomComplet();
//            if (s.length() > 32) {
//                s = s.substring(0, 31);
//            }
//            nouvelleFiche_M2O.setEncodeur(s);
//            nouvelleFiche_M2O.setDateModif(new Date());
            boolean newRecord = nouvelleFiche_M2O.isNouvelleFiche();
            try {
                try {
                    _save(nouvelleFiche_M2O);
                    // refresh(nouvelleFiche_M2O); // EbM 130929
                    toggledRow.addToManyRelation(nouvelleFiche_M2O, listeO2M);
                } catch (Exception e1) {
                    // roll it back if an Exception occurred
                    // toggledRow.removeManyRelation(nouvelleFiche_M2O, listeO2M);
                    JSFUtility.queueException(e1);
                    return null;

                }

            } catch (Exception e) {
                if (newRecord && ((e instanceof DatabaseException) || (e.getCause() instanceof DatabaseException))) {
                    nouvelleFiche_M2O.setVersion(null);
                    nouvelleFiche_M2O.setId(null);
                }
                JSFUtility.queueException(e);
                return null;
            }
            JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        return null;
    }

    public void nullActionListener(ActionEvent evt) {
    }

//    public String gotoToggledRowAction(String viewPrefix, String entityType) {
//        BaseBeanEntite entityToGoTo = null;
//        if (entityType.equals(listTypeO2M)) {
//            entityToGoTo = toggledRow_O2M;
//        } else if (entityType.equals(listTypeMain)) {
//            entityToGoTo = toggledRow;
//        }
//        if (entityToGoTo == null) {
//            JSFUtility.addErrorMessage("Aucune fiche n'est s�lectionn�e par syst�me de 'toggle'");
//            return null;
//        }
//        return viewPrefix + "?faces-redirect=true&clefPrimaire=" + entityToGoTo.getId();
//    }
//    public String deleteToggledRowAction(String entityType, String listToDeleteFrom) {
//        BaseBeanEntite entityToDelete = null;
//        if (entityType.equals(listTypeO2M)) {
//            try {
//                if (!JSFUtility.propertyExists(toggledRow, listToDeleteFrom)) {
//                    JSFUtility.addErrorMessage(
//                            "Property '" + listToDeleteFrom + "' is not found in class '" + toggledRow.getClass().getName() + "'");
//                    return null;
//                }
//            } catch (Exception e) {
//                JSFUtility.queueException(e);
//            }
//            entityToDelete = toggledRow_O2M;
//        } else if (entityType.equals(listTypeMain)) {
//            entityToDelete = toggledRow;
//        }
//
//        if (entityToDelete == null) {
//            JSFUtility.addErrorMessage("Aucune fiche n'est s�lectionn�e par syst�me de 'toggle'");
//            return null;
//        }
//        try {
//            _remove(entityToDelete);
//            if (entityType.equals(listTypeO2M)) {
//                toggledRow.removeManyRelation(entityToDelete, listToDeleteFrom);
//                /*
//                 * Not updating toggledRow will make the deleted entity show up again following a search
//                 * unless you restart your session.
//                 */
//                toggledRow = this.update(toggledRow); // do this to force immediate update
//                toggledRow_O2M = null;
//            } else if (entityType.equals(listTypeMain)) {
//                if (okapiLazyModel != null && okapiLazyModel.getRowCount() > 0 && ((List) okapiLazyModel.getWrappedData()).contains(entityToDelete)) {
//                    ((List) okapiLazyModel.getWrappedData()).remove(entityToDelete);
//                    okapiLazyModel.setRowCount(okapiLazyModel.getRowCount() - 1);
//                }
//                toggledRow = null;
//            }
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//        }
//        return null;
//    }

    /*
     * marquer la fiche cible (toggledRow) du composant p:rowExpand pour pr�parer l'op�ration
     * saveToggledRowAction
     */
//    public void onRowToggle(ToggleEvent event) {
//        if (event.getVisibility() == Visibility.VISIBLE) {
//            toggledRow = (BaseBeanEntite) event.getData();
//        }
//    }
//
//    /*
//     * marquer la fiche cible (toggledRow_M2O) du composant p:rowExpand pour pr�parer l'op�ration
//     * saveToggledRowAction
//     */
//    public void onRowToggle_M2O(ToggleEvent event) {
//        if (event.getVisibility() == Visibility.VISIBLE) {
//            toggledRow_O2M = (BaseBeanEntite) event.getData();
//        }
//    }

    /*
     * sauvegarer la fiche "toggledRow" la fiche "toggledRow" marqu�e par la
     * m�thode "onRowToggle"
     //     */
////    public String saveToggledRowAction(String entityType) {
////        BaseBeanEntite entityToSave = null;
////        if (entityType.equals(listTypeO2M)) {
////            entityToSave = toggledRow_O2M;
////        } else if (entityType.equals(listTypeMain)) {
////            entityToSave = toggledRow;
////        }
////        if (entityToSave == null) {
////            JSFUtility.addErrorMessage("No toggled row to save; please, make sure you toggled one.");
////            return null;
////        }
////        try {
////            boolean newRecord = entityToSave.isNouvelleFiche();
////            try {
////                BaseBeanEntite updatedBean = _save(entityToSave);
////                if (entityType.equals(listTypeO2M)) {
////                    toggledRow_O2M = updatedBean;
////                } else if (entityType.equals(listTypeMain)) {
////                    toggledRow = updatedBean;
////                }
////                //toggledRow = update(entityToSave);
////
////                //refresh(entityToSave);
////            } catch (Exception e) {
////                if (newRecord && ((e instanceof DatabaseException) || (e.getCause() instanceof DatabaseException))) {
////                    entityToSave.setVersion(null);
////                }
////                JSFUtility.queueException(e);
////                return null;
////            }
////            JSFUtility.addInfoMessage("ok !");
////        } catch (Exception e) {
////            JSFUtility.queueException(e);
////        }
////        return null;
////    }
    private void _remove(BaseBeanEntite recordToDelete) throws Exception {
        BaseBeanEntite entityToDelete = recordToDelete;
        if (!entityManager.contains(entityToDelete)) {
            entityToDelete = this.findById(recordToDelete.getClass(), recordToDelete.getId());
        }
        if (entityToDelete == null) {
            throw new Exception("La fiche '" + selection.toString() + "' doit �tre persist�e avant qu'elle ne soit supprim�e; impossible de la persister.");
        }
        this.delete(entityToDelete);
        refreshCache(); // EbM 13 10 04
    }

    /*
     * sauvegarder une fiche : save si elle est nouvelle, upadte si elle n'est pas nouvelle.
     * @param record fiche � sauvegarder
     * @return TRUE si la fiche est nouvelle, FALSE si la fiche ,n'est pas nouvelle.
     */
    public BaseBeanEntite _save(BaseBeanEntite record) throws Exception {

        try {
            if (record.isNouvelleFiche()) {
                try {
                    //record.beforePersist();
                    this.save(record);
                    this.refreshCache(); // EbM 13 10 04
                    record.afterPersist();
                } catch (Exception e) {
                    record.setVersion(null);
                    record.setId(null);
                    throw e;
                }
                return record;
            } else {
                BaseBeanEntite retval = this.update(record);
                this.refreshCache(); // EbM 13 10 04
                return retval;
            }
        } catch (ValidationException e) {
            throw new Exception(JSFUtility.getBundleEntry("db_validation"));
        }
    }

    public void addM2M(BaseBeanEntite m2mEntite, BaseBeanEntite owner, String listToAddTo) throws Exception {
        // ajouter le r�le si l'utilisateur ne l'a pas encore
        try {
            if (!JSFUtility.propertyExists(owner, listToAddTo)) {
                throw new Exception(
                        "Property '" + listToAddTo + "' is not found in class '" + owner.getClass().getName() + "'");
            }
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        owner.addToManyRelation(m2mEntite, listToAddTo);
        sauvegarderFiche(owner);
    }

    public String removeM2M(BaseBeanEntite m2mEntite) {
        if (m2mEntite == null) {
            JSFUtility.addErrorMessage("S�lectionnez une fiche s.v.p. !");
            return null;
        }
        getSelection().removeManyRelation(m2mEntite);
        try {
            sauvegarderFicheSelectionneeAction();
            this.refresh(m2mEntite);
            // getSelection().updateOwnedM2MRelations(m2mEntite, "remove");
        } catch (Exception e) {
            getSelection().addToManyRelation(m2mEntite);
            JSFUtility.queueException(e);
        }
        return null;
    }

    /*
     * parcours acad�mique de l'�tudiant s�lectionn� sous forme d'arbre
     */
//    public TreeNode getParcoursAcademiqueTree() {
//
//        // retourner si le parcours avait d�j� �t� calcul�
//        if (toggledRow instanceof EtudiantEntite && ((EtudiantEntite) toggledRow).getParcoursAcademique() != null) {
//            return ((EtudiantEntite) toggledRow).getParcoursAcademique();
//        } else if (toggledRow instanceof InscriptionDansAnneeEtudeEntite && ((InscriptionDansAnneeEtudeEntite) toggledRow).getParcoursAcademique() != null) {
//            return ((InscriptionDansAnneeEtudeEntite) toggledRow).getParcoursAcademique();
//        }
//
//        /**
//         * si cpiIdPourParcours est NULL, le d�duire de toggledRow si :
//         * toggledRow est de type EtudiantEntite ou
//         * InscriptionDansAnneeEtudeEntite, les 2 seules classes qui peuvent
//         * d�clencher la m�thode getParcoursAcademiqueTree()
//         *
//         */
//        if (StringUtils.isEmpty(cpiIdPourParcours)) {
//            if (toggledRow == null) {
//                return new DefaultTreeNode("root", null);
//            }
//            if (toggledRow instanceof EtudiantEntite) {
//                cpiIdPourParcours = ((EtudiantEntite) toggledRow).getCodePreInscription().getId();
//            } else if (toggledRow instanceof InscriptionDansAnneeEtudeEntite) {
//                cpiIdPourParcours = ((InscriptionDansAnneeEtudeEntite) toggledRow).getAutorisation().getCodePreInscription().getId();
//            } else {
//                return new DefaultTreeNode("root", null);
//            }
//        }
//
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<InscriptionDansAnneeEtudeEntite> cq = builder.createQuery(InscriptionDansAnneeEtudeEntite.class
//        );
//        Root<InscriptionDansAnneeEtudeEntite> idaeRoot = cq.from(InscriptionDansAnneeEtudeEntite.class);
//        Predicate conditions = builder.equal(idaeRoot
//                .<AutorisationEntite>get("autorisation")
//                .<CodePreInscriptionEntite>get("codePreInscription")
//                .<String>get("id"),
//                cpiIdPourParcours);
//
//        cq.select(idaeRoot)
//                .where(conditions)
//                .orderBy(builder.desc(idaeRoot.<AutorisationEntite>get("autorisation").<AnneeAcademiqueEntite>get("anneeAutorisation").<String>get("id")));
//        List<InscriptionDansAnneeEtudeEntite> idaes;
//
//        try {
//            idaes = entityManager.createQuery(cq).getResultList();
//            if (idaes == null || idaes.isEmpty()) {
//                return new DefaultTreeNode("root", null);
//            }
//        } catch (Exception e1) {
//            JSFUtility.queueException(e1);
//            return new DefaultTreeNode("root", null);
//        }
//
//        try {
//            // construire l'arbre
//            Map<String, TreeNode> mapAnac = new HashMap<>(3);
//            TreeNode root = new DefaultTreeNode("root", null);
//            for (InscriptionDansAnneeEtudeEntite idae : idaes) {
//                AutorisationEntite auto = idae.getAutorisation();
//                if (!mapAnac.containsKey(auto.getAnneeAutorisation().getId())) {
//                    TreeNode racineAnacNode = new DefaultTreeNode(
//                            "racineAnac",
//                            new BilimaTreeNodeObject(auto.getAnneeAutorisation().getLibelle(), auto.getAnneeAutorisation().getId()),
//                            root);
//                    mapAnac.put(auto.getAnneeAutorisation().getId(), racineAnacNode);
//                }
//                TreeNode racineIdae = new DefaultTreeNode(
//                        "racineIdae",
//                        new BilimaTreeNodeObject(auto.getAnneeEtude().getId(), "-"),
//                        mapAnac.get(auto.getAnneeAutorisation().getId()));
//                TreeNode racineFicheIdae = new DefaultTreeNode(
//                        "racineFicheIdae",
//                        new BilimaTreeNodeObject("fiche", "-"),
//                        racineIdae);
//                TreeNode ficheIdae = new DefaultTreeNode(
//                        "ficheIdae",
//                        new BilimaTreeNodeObject("-", idae),
//                        racineFicheIdae);
//
//                // comptabilit�
//                TreeNode racineComptabilite = new DefaultTreeNode(
//                        "racineComptabilite",
//                        new BilimaTreeNodeObject("comptabilit�s", "-"),
//                        racineIdae);
//                CriteriaQuery<ComptabiliteEntite> comptaCQ = builder.createQuery(ComptabiliteEntite.class);
//                Root<ComptabiliteEntite> comptaRoot = comptaCQ.from(ComptabiliteEntite.class);
//                conditions = builder.equal(
//                        comptaRoot.<InscriptionDansAnneeEtudeEntite>get("inscriptionDansAnneeEtude").<String>get("id"),
//                        idae.getId());
//                comptaCQ.select(comptaRoot)
//                        .where(conditions)
//                        .orderBy(builder.desc(comptaRoot.<Date>get("datePaiement")));
//                List<ComptabiliteEntite> comptes;
//                try {
//                    comptes = entityManager.createQuery(comptaCQ).getResultList();
//                } catch (Exception e2) {
//                    JSFUtility.queueException(e2);
//                    return new DefaultTreeNode("root", null);
//                }
//
//                if (comptes != null && (!comptes.isEmpty())) {
//                    for (ComptabiliteEntite compte : comptes) {
//                        TreeNode tn = new DefaultTreeNode(
//                                "ficheComptabilite",
//                                new BilimaTreeNodeObject("paiement", compte),
//                                racineComptabilite);
//                    }
//                }
//
//                // exon�rations
//                TreeNode racineExoneration = new DefaultTreeNode(
//                        "racineExoneration",
//                        new BilimaTreeNodeObject("exon�rations", "-"),
//                        racineIdae);
//                CriteriaQuery<ExonerationEntite> exoCQ = builder.createQuery(ExonerationEntite.class);
//                Root<ExonerationEntite> exonRoot = exoCQ.from(ExonerationEntite.class);
//                conditions = builder.equal(
//                        exonRoot.<InscriptionDansAnneeEtudeEntite>get("inscriptionDansAnneeEtude").<String>get("id"),
//                        idae.getId());
//                exoCQ.select(exonRoot).where(conditions);
//                List<ExonerationEntite> exonerations;
//                try {
//                    exonerations = entityManager.createQuery(exoCQ).getResultList();
//                } catch (Exception e2) {
//                    JSFUtility.queueException(e2);
//                    return new DefaultTreeNode("root", null);
//                }
//
//                if (exonerations != null && (!exonerations.isEmpty())) {
//                    for (ExonerationEntite exoneration : exonerations) {
//                        TreeNode tn = new DefaultTreeNode(
//                                "ficheExoneration",
//                                new BilimaTreeNodeObject(exoneration.getStatut().getId(), exoneration),
//                                racineExoneration);
//                    }
//                }
//
//                // d�cisions de jury
//                TreeNode racineDecisionJury = new DefaultTreeNode(
//                        "racineDecisionJury",
//                        new BilimaTreeNodeObject("d�cisions de jury", "-"),
//                        racineIdae);
//                NomenclatureVerdictEntite verdict = idae.getVerdict();
//                if (!verdict.getId().equalsIgnoreCase(JSFUtility.VERDICT_NON_LMD)) {
//                    TreeNode tn = new DefaultTreeNode(
//                            "ficheDecisionJury",
//                            new BilimaTreeNodeObject("verdict", idae.getVerdict().getId()/*verdict.getId()*/),
//                            racineIdae/*racineDecisionJury*/);
//                } else {
//                    // ancien syst�me
//                    CriteriaQuery<DecisionJuryEntite> djCQ = builder.createQuery(DecisionJuryEntite.class);
//                    Root<DecisionJuryEntite> djRoot = djCQ.from(DecisionJuryEntite.class);
//                    conditions = builder.equal(
//                            djRoot.<InscriptionDansAnneeEtudeEntite>get("inscriptionDansAnneeEtude").<String>get("id"),
//                            idae.getId());
//                    djCQ.select(djRoot).where(conditions).orderBy(builder.desc(djRoot.<Integer>get("semestre")));
//                    List<DecisionJuryEntite> decisionJurys;
//                    try {
//                        decisionJurys = entityManager.createQuery(djCQ).getResultList();
//                    } catch (Exception e2) {
//                        JSFUtility.queueException(e2);
//                        return new DefaultTreeNode("root", null);
//                    }
//
//                    if (decisionJurys != null && (!decisionJurys.isEmpty())) {
//                        for (DecisionJuryEntite decisionJury : decisionJurys) {
//                            TreeNode tn = new DefaultTreeNode(
//                                    "ficheDecisionJury",
//                                    new BilimaTreeNodeObject(" semestre " + decisionJury.getSemestre(), decisionJury.getVerdict().getId()/*decisionJury*/),
//                                    racineDecisionJury);
//                        }
//                    }
//                }
//
//                // sanctions
//                TreeNode racineSanction = new DefaultTreeNode(
//                        "racineSanction",
//                        new BilimaTreeNodeObject("sanctions", "-"),
//                        racineIdae);
//
//                CriteriaQuery<SanctionEntite> sanctionCQ = builder.createQuery(SanctionEntite.class);
//                Root<SanctionEntite> sanctionRoot = sanctionCQ.from(SanctionEntite.class);
//                conditions = builder.equal(
//                        sanctionRoot.<EtudiantEntite>get("etudiant").<CodePreInscriptionEntite>get("codePreInscription").<String>get("id"),
//                        cpiIdPourParcours);
//                sanctionCQ.select(sanctionRoot).where(conditions).orderBy(builder.desc(sanctionRoot.<Date>get("dateDebut")));
//                List<SanctionEntite> sanctions;
//                try {
//                    sanctions = entityManager.createQuery(sanctionCQ).getResultList();
//                } catch (Exception e2) {
//                    JSFUtility.queueException(e2);
//                    return new DefaultTreeNode("root", null);
//                }
//
//                if (sanctions != null && (!sanctions.isEmpty())) {
//                    for (SanctionEntite sanction : sanctions) {
//                        TreeNode tn = new DefaultTreeNode(
//                                "ficheSanction",
//                                new BilimaTreeNodeObject(sanction.getNiveauExclusion(), sanction),
//                                racineSanction);
//                    }
//                }
//
//            }
//            // keep this value to avoid parsing again and again; see above
//            if (toggledRow instanceof InscriptionDansAnneeEtudeEntite) {
//                ((InscriptionDansAnneeEtudeEntite) toggledRow).setParcoursAcademique(root);
//            } else if (toggledRow instanceof EtudiantEntite) {
//                ((EtudiantEntite) toggledRow).setParcoursAcademique(root);
//            }
//            return root;
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//        }
//
//        return new DefaultTreeNode(
//                "root", null);
//    }
//    private String retrouverLogo(String etablissementId) {
//        BilimaFilenameFilter filter = new BilimaFilenameFilter(etablissementId.toLowerCase() + ".", "startsWith");
//        String repertoireLogo = proprieteApplication.getRepertoireLogo();
//        File fichierRep = new File(repertoireLogo);
//        // liste des fichiers pr?sents dans le r?pertoire en tenant compte du filtre
//        String[] fichiers = fichierRep.list(filter);
//        if (fichiers == null || fichiers.length == 0) {
//            return null;
//        }
//        // prendre le 1er fichier qui commence par "<etablissementId>."
//        for (String nomFichier : fichiers) {
//            File f = new File(repertoireLogo + "/" + nomFichier);
//            if (f.exists() && f.isFile()) {
//                return f.getAbsolutePath();
//            }
//        }
//        return null;
//    }
//    private String retrouverLogoUac() {
//        BilimaFilenameFilter filter = new BilimaFilenameFilter("logoUac.", "startsWith");
//        String repertoireLogo = proprieteApplication.getRepertoireLogo();//proprietesApp.getLogo().getValeur();
//        File fichierRep = new File(repertoireLogo);
//        // liste des fichiers pr?sents dans le r?pertoire en tenant compte du filtre
//        String[] fichiers = fichierRep.list(filter);
//        if (fichiers == null || fichiers.length == 0) {
//            return null;
//        }
//        // prendre le 1er fichier qui commence par "<etablissementId>."
//        for (String nomFichier : fichiers) {
//            File f = new File(repertoireLogo + "/" + nomFichier);
//            if (f.exists() && f.isFile()) {
//                return f.getAbsolutePath();
//            }
//        }
//        return null;
//    }
    public String profileAction(String selecteurProfil) {
        this.selecteurProfil = selecteurProfil;
        nouvelleFiche_M2O = null;
        return "profil?faces-redirect=true";
    }

    /*
     * retrouver une Entit� par le nom de sa classe et son ID
     * @param nomEntite nom de la classe de l'Entit�
     * @param id l'ID de la fiche � rechercher
     * @return la fiche , null si elle n'existe pas
     */
    public BaseBeanEntite findEntite(String nomEntite, String id) throws Exception {
        // si nomEntite est un nom complet, le rendre court
        String q = "SELECT r "
                + "FROM " + _nomCourt(nomEntite) + " r "
                + "WHERE "
                + "UPPER(r.id) = :id ";
        HashMap map = new HashMap<>(1);
        map.put("id", id.toUpperCase());
        BaseBeanEntite ent = (BaseBeanEntite) this.lookupSingleDetached(q, map);
        return ent;
    }

    /*
     * retrouver une Entit� par le nom de sa classe et son ID
     * @param nomEntite nom de la classe de l'Entit�
     * @param id l'ID de la fiche � rechercher
     * @return la fiche , null si elle n'existe pas
     */
    public BaseBeanEntite findEntiteByPK(String nomEntite, String pk, Object pkValue) throws Exception {
        // si nomEntite est un nom complet, le rendre court
        String q = "SELECT r "
                + "FROM " + _nomCourt(nomEntite) + " r "
                + "WHERE "
                + "r." + pk + "= :pkValue ";
        HashMap map = new HashMap<>(1);
        map.put("pkValue", pkValue);
        BaseBeanEntite ent = (BaseBeanEntite) this.lookupSingleDetached(q, map);
        return ent;
    }


    /*
     * cette listener servant � prendre des photos.
     * Cette m�thode doit �tre red�finie par les beans o� il y a capture de photo par une cam�ra.
     * @param event objet transmis par Primefaces.
     */
//    public void handleFileUpload_XX(FileUploadEvent event) {
//    }
    public String nullAction() {
//        System.err.println("test ok !" + toggledRow.getId());
         System.out.println("test ok !" );
        return null;

    }

    /*
     * assigner l'ann�e acad�mqiue de r�f�rence pour le Staff et pour les autres.
     */
//    public void postConstruct() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Principal principal = context.getExternalContext().getUserPrincipal();
////        if (principal != null) {
////            utilisateurCourant = new Utilisateur();
////            utilisateurCourant.setIdentifiant(principal.getName());
////            utilisateurCourant.setNom("");
////            utilisateurCourant.setPrenom("");
////            utilisateurCourant.setSessionEndormie(Boolean.FALSE);
////        }
//        // proprietesApp = ApplicationController.retrieveProprietesApp();
//        // d�duire l'ann�e d'inscription par d�faut
//        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//        // ann�e de r�fe�rence = ann�e courante + 1
//        String anneeAcadId = proprieteApplication.getAnac().getId();
//        AnneeAcademiqueEntite anacStaff = proprieteApplication.getAnacStaff();
//        String sql = "SELECT r FROM AnneeAcademiqueEntite r "
//                + "WHERE r.id = :anacId";
//        Map<String, Object> map = new HashMap<>(2);
//        map.put("anacId", anneeAcadId);
//        if (anacStaff != null) {
//            map.put("anacStaffId", anacStaff.getId());
//            sql = "SELECT r FROM AnneeAcademiqueEntite r "
//                    + "WHERE r.id = :anacId OR r.id = :anacStaffId";
//
//        }
//        try {
//            List<AnneeAcademiqueEntite> l = (List<AnneeAcademiqueEntite>) this.lookupHavingSqlQueryDetached(sql, map, AnneeAcademiqueEntite.class
//            );
//            if (l
//                    != null && (!l.isEmpty())) {
//                for (AnneeAcademiqueEntite anac : l) {
//                    if (anac.getId().equals(anneeAcadId)) {
//                        anacDeReferencePourLesInscriptions = anac;
//                    } else if (anacStaff != null && anac.getId().equals(anacStaff.getId())) { // paranoia ?
//                        anacDeReferencePourStaff = anac;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//        }
//    }
    private void _saveIfNotExists(BaseBeanEntite nouvelleEntite) throws Exception {
        if (!StringUtils.isEmpty(nouvelleEntite.getId())) {
            BaseBeanEntite bean = this.findById(nouvelleEntite.getClass(), nouvelleEntite.getId());
            if (bean != null) {
                throw new Exception(
                        "Impossible de sauvergarder la fiche '" + nouvelleEntite.getId() + "' de classe '" + nouvelleEntite.getClass().getName()
                        + "'; sa clef primaire est d�j� attribu�e");
            }
        }
        this.save(nouvelleEntite);
    }

    public String sauvegarderFicheSelectionneeAction() {
        if (selection == null) {
//            JSFUtility.queueFmtString("msg_ses�lectionner", null, FacesMessage.SEVERITY_WARN);
            return null;
        }
        try {
//            String s = (utilisateurCourant == null) ? "Inconnu" : utilisateurCourant.getNomComplet();
//            if (s.length() > 32) {
//                s = s.substring(0, 31);
//            }
//            selection.setEncodeur(s);
//            selection.setDateModif(new Date());
            boolean newRecord = selection.isNouvelleFiche();
            try {
                _save(selection);

            } catch (Exception e) {
                if (newRecord && ((e instanceof DatabaseException) || (e.getCause() instanceof DatabaseException))) {
                    selection.setVersion(null);
                }
//                JSFUtility.queueException(e);
                return null;
            }
//            JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
//            JSFUtility.queueException(e);
        }
        return null;
    }

    /*
     * supprimer la 1�re fiche s�lectionn�e.
     * D'o� qu'elle soit appel�e, cette action retourne vers resultats.xhtml
     */
    @SuppressWarnings("unchecked")
    public String supprimerFicheSelectionneeAction() {
        try {
            if (selection == null) {
//                JSFUtility.queueFmtString("msg_ses�lectionner", null, FacesMessage.SEVERITY_WARN);
                return null;
            }

            _remove(selection);

        } catch (Exception e) {
//            JSFUtility.queueException(e);
        }
        return /*"search?faces-redirect=true"*/ null;
    }

    /*
     * supprimer la 1�re fiche s�lectionn�e.
     * D'o� qu'elle soit appel�e, cette action retourne vers resultats.xhtml
     */
    @SuppressWarnings("unchecked")
    public String supprimerFicheSelectionneeAction(BaseBeanEntite entite) {
        try {
            if (entite == null) {
                JSFUtility.queueFmtString("msg_ses�lectionner", null, FacesMessage.SEVERITY_WARN);
                return null;
            }
            // delete(selection);

            boolean persisted = entityManager.contains(entite);

            BaseBeanEntite bean = entityManager.find(entite.getClass(), entite.getId());
            if (bean == null) {
                JSFUtility.addErrorMessage("La fiche '" + entite.toString() + "' doit �tre persist�e avant qu'elle ne soit supprim�e; impossible de la persister.");
                return null;
            }
            _remove(bean);
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        return /*"search?faces-redirect=true"*/ null;
    }

    public String visiterFicheBrancheAction(BaseBeanEntite ficheBranche) {
        this.ficheBranche = ficheBranche;
        return null;
    }

    public String getFicheBrancheClassName() {
        if (ficheBranche == null) {
            return "";
        }
        return ficheBranche.getClass().getSimpleName();
    }

    public void rafraichirPremiereFicheSelectionneeActionListener(ActionEvent actionEvent) {
        if (selection == null) {
            return;
        }
        entityManager.refresh(selection);
    }

    public void annulerFicheCibleActionListener(ActionEvent actionEvent) {
        ficheASupprimer = null;
    }

    /*
     * supprimer la fiche marqu�e par ciblerFicheASupprimerAction(BaseBeanEntite ficheASupprimer).
     * Cet �l�ment devrait �tre membre d'une liste O2M de l'�l�ment retourn� par getPremiereFicheSelectionnee().
     * La relation est cens�e �tre bi-directionnelle. il faut donc une suppression bi-directionnelle.
     * @param actionEvent param�tre transmis automatiquement par le syst�me graphique
     */
    public void supprimerFicheCibleActionListener(ActionEvent actionEvent) {
        if (ficheASupprimer == null) {
            return;
        }
        try {
            if (selection != null) {
                // 1. supprimer l'�l�ment de la liste dans l'�l�ment parent
                //bean.supprimerDeLaListeOne2Many(new Object[]{ficheASupprimer});
                //2. supprimer l'�l�ment lui-m�me
                BaseBeanEntite bean = entityManager.find(ficheASupprimer.getClass(), ficheASupprimer.getId());
                if (bean == null) {
                    JSFUtility.addErrorMessage("La fiche '" + ficheASupprimer.toString() + "' doit �tre persist�e avant qu'elle ne soit supprim�e; impossible de la persister.");
                    return;
                }
                //entityManager.merge(ficheASupprimer);
                _remove(bean);
                // ficheASupprimer.removeFromParentPriorToDelete();  // EbM 2013 09 27
                // r�initialiser la variable
                // ficheASupprimer = null; // remettre la cible � supprimer � null
            }
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
    }

    /*
     * marquer la fiche qui sera supprim�e par supprimerFicheCibleActionListener(ActionEvent actionEvent).
     * @param ficheASupprimer fiche � marquer
     * @return page de retour, null si on reste sur la m�me page
     */
    public String ciblerFicheASupprimerAction(BaseBeanEntite ficheASupprimer) {
        this.ficheASupprimer = ficheASupprimer;
        return null;
    }

    public String nouvelleFicheAction() {
        try {
            Class classz = Class.forName(nomCompletDeLaClasse);
            visitedEntity = (BaseBeanEntite) classz.newInstance();
        } catch (Exception exc) {
//            JSFUtility.queueException(exc);
        }
        return null;
    }

    public String nouvelleFicheRacineAction(String entityClassName) {
        try {
            Class classz = Class.forName(entityClassName);
            visitedEntity = (BaseBeanEntite) classz.newInstance();
        } catch (Exception exc) {
            JSFUtility.queueException(exc);
        }
        return null;
    }

//    public void rowUnselectListener(UnselectEvent ev) {
//        // fichesSelectionnees = null;// identifiant jobiste : 7:8726
//    }
//
//    public void rowSelectListener(SelectEvent ev) {
//        UIComponent comp = ev.getComponent();
//        UIComponent xx = comp.findComponent(":id-resultats:xx");
//        UIComponent yy = comp.findComponent(":yy");
//        UIComponent form = comp.findComponent(":id-resultats");
//        UIComponent fAction = comp.findComponent(":f-action");
//        Object obj = ev.getObject();
//    }
    public String gotoResultatsAction() {
        return racineVue + "/resultats?faces-redirect=true";
    }

//    public void resetStatsListener(ActionEvent ae) {
//        chartModel = null;
//        chartDialogHeader = null;
//    }
    // rafra?chir la liste apr?s cr?ation ou modification
    public <T extends BaseBeanEntite> void ajouterALaListe(T e) {
    }

    /*
     * renvoie le nom de ce javaBean
     */
    public String getBeanSimpleName() {
        return getClass().getSimpleName();
    }

    public String navAvecClefPrimaireAction(String clefPrimaire, String nav) {
        return nav + "?faces-redirect=true&clefPrimaire=" + clefPrimaire;
    }

    /*
     * M?thode d?clench?e ? partir d'un formulaire de recherhe sur clef primaire.
     * @param entiteFullClassName nom de la classe de l'entit? recherch?e
     * @return vue "r?sultats"
     */
    @SuppressWarnings("unchecked")
//    public String searchByIdAction() {
//        if (StringUtils.isEmpty(clefPrimaire)) {
////            JSFUtility.addErrorMessage("La clef primaire est obligatoire si vous faites une recherche sur cette base.");
//            return null;
//        }
//        if (StringUtils.isEmpty(nomCompletDeLaClasse)) {
////            JSFUtility.addErrorMessage("Le nom complet de la classe Entite est vide; pr�venez l'�quipement de d�veloppement");
//            return null;
//        }
//        try {
//            okapiLazyModel = ((OkapiLazyModel<? extends BaseBeanEntite>) this.new OkapiLazyModel(Class.forName(nomCompletDeLaClasse)));
//
//            // common (eg main result and count result)
////            Map<String, Object> paramsKeyAndValue = new HashMap<>(3);
////            paramsKeyAndValue.put("id", clefPrimaire);
////            getOkapiLazyModel().getLazyLoad().setParams(paramsKeyAndValue);
//            // main result
//            CriteriaBuilder builder = getOkapiLazyModel().getLazyLoad().getResultBuilder();
//            Root<? extends BaseBeanEntite> resultRoot = getOkapiLazyModel().getLazyLoad().getResultRoot();
////            ParameterExpression<String> pkParam = builder.parameter(String.class, "id");
//            Predicate conditions = builder.equal(resultRoot.get("id"), clefPrimaire);
//            getOkapiLazyModel().getLazyLoad().setResultConditions(conditions);
//
//            // count result
//            builder = getOkapiLazyModel().getLazyLoad().getCountBuilder();
//            Root<? extends BaseBeanEntite> countRoot = getOkapiLazyModel().getLazyLoad().getCountRoot();
////            pkParam = builder.parameter(String.class, "id");
//            conditions = builder.equal(countRoot.get("id"), clefPrimaire);
//            getOkapiLazyModel().getLazyLoad().setCountConditions(conditions);
//
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//            return null;
//        }
//        return "resultats.jsf?faces-redirect=true";
//    }

    /*
     * M?thode d?clench?e ? partir d'un formulaire de recherhe sur clef primaire.
     * @param entiteFullClassName nom de la classe de l'entit? recherch?e
     * @return vue "r?sultats"
     */
//    @SuppressWarnings("unchecked")
//    public String rechercheParClefPrimaireAction() {
//        if (StringUtils.isEmpty(clefPrimaire)) {
//            JSFUtility.addErrorMessage("La clef primaire est obligatoire si vous faites une recherche sur cette base.");
//            return null;
//        }
//        if (StringUtils.isEmpty(nomCompletDeLaClasse)) {
//            JSFUtility.addErrorMessage("Le nom complet de la classe Entite est vide; pr�venez l'�quipement de d�veloppement");
//            return null;
//        }
//        String sql = "SELECT r FROM " + _nomCourt(nomCompletDeLaClasse) + " r WHERE UPPER(r.id) = :clefPrimaire ";
//        Map map = new HashMap(1);
//        map.put("clefPrimaire", clefPrimaire.toUpperCase());
//        try {
//            fiches = this.new BilimaLazyDataModel(sql, map);
////            Class classz = Class.forName(nomCompletDeLaClasse);
////            List<BaseBeanEntite> l = this.lookupHavingSqlQueryDetached(sql, map, classz);
////            fiches = new EntiteBeanDataModel(l);
//            // beginConversation();
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//            return null;
//        }
//        return "resultats?faces-redirect=true";
//    }
    public String emptyFichesSelectionneesO2MAndNavigateAction(String nav) {
        fichesSelectionneesO2M = null;
        return racineVue + "/" + nav + "?faces-redirect=true";
    }

    public List<Object> autoCompleteSurBaseDeId(String prefix) {
        String q = "SELECT r "
                + "FROM " + _nomCourt(nomCompletDeLaClasse) + " r "
                + "WHERE "
                + "UPPER(r.id) LIKE :id ";
        HashMap map = new HashMap<>(1);
        map.put("id", prefix.toUpperCase() + "%");
        try {
            List<Object> l = this.lookupHavingSqlQueryDetached(q, map, (Class<BaseBeanEntite>) Class.forName(nomCompletDeLaClasse));
            return l;
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public BaseBeanEntite rechercherEntiteParIdEtNomDeClasse(String id, String nomCompletDeClasse) throws Exception {
        Class classz = Class.forName(nomCompletDeClasse);
        classz.getSimpleName();
        String sql = "SELECT r FROM " + classz.getSimpleName() + " r WHERE UPPER(r.id) = :id";
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", id.toUpperCase());
        try {
            List<BaseBeanEntite> list = this.lookupHavingSqlQueryDetached(sql, map, classz);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        } catch (Exception e) {
//            JSFUtility.queueException(e);
        }
        return null;
    }

    public void passwordListener(ValueChangeEvent vce) {
        String clearText = (String) ((UIInput) vce.getComponent()).getValue();
        try {
            String encypted = JSFUtility.md5Hex(clearText);
            ((UIInput) vce.getComponent()).setValue(encypted);
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
    }

    public void modifierFicheSelectionneeAction() {
        try {
            if (selection == null) {
                JSFUtility.addErrorMessage("Veuillez s�lectionner une et une seule fiche pour la modification.");
                elementEnCoursDeCreationOuDeModification = null;
                return;
            }
            elementEnCoursDeCreationOuDeModification = selection;
            //JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            JSFUtility.queueException(e);
            elementEnCoursDeCreationOuDeModification = null;
        }
    }

    public void modifierFicheSelectionneeListener() {
        try {
            if (selection == null) {
                JSFUtility.addErrorMessage("Veuillez s�lectionner une et une seule fiche pour la modification.");
                elementEnCoursDeCreationOuDeModification = null;
                return;
            }
            elementEnCoursDeCreationOuDeModification = selection;
            //JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            JSFUtility.queueException(e);
            elementEnCoursDeCreationOuDeModification = null;
        }
    }

    public void mettreAJourFicheSelectionneeListener(ActionEvent ev) {
        if (this.getNombreFichesSelectionnees() != 1) {
            JSFUtility.addErrorMessage("S�lectionnez et une seule fiche");
            return;
        }
        //BaseBeanEntite entite = this.getPremiereFicheSelectionnee();
        try {
            // il s'agit d'une modification de fiche existante; s'assurer qu'elle est persist�e
            BaseBeanEntite bean = this.findById(selection.getClass(), selection.getId());
            if (bean == null) {
                JSFUtility.addErrorMessage("La fiche " + selection.toString() + " ne peut �tre persist�e alors qu'elle doit �tre modifi�e.");
                return;
            }
            this.update(bean);
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
    }

    public String creerElementO2MAction(String nomCompletDeLaClasseEntite) {
        if (selection == null) {
            JSFUtility.addErrorMessage("Aucun enregistrement n'est s?lectionn?; il faut pr?venir l'?quipe de d?veloppement.");
            elementEnCoursDeCreationOuDeModification = null;
            return null;
        }
        try {
            Class classz = Class.forName(nomCompletDeLaClasseEntite);
            elementEnCoursDeCreationOuDeModification = (BaseBeanEntite) classz.newInstance();
            elementEnCoursDeCreationOuDeModification.addParent(selection);
        } catch (Exception e) {
            JSFUtility.queueException(e);
            elementEnCoursDeCreationOuDeModification = null;
        }
        return null;
    }

    public String modifierElementO2MAction() {
        if (fichesSelectionneesO2M == null || fichesSelectionneesO2M.length != 1) {
            JSFUtility.addErrorMessage("Veuillez s�lectionner un et un seul �l�ment  ? modifier.");
            elementEnCoursDeCreationOuDeModification = null;
            return null;
        }
        elementEnCoursDeCreationOuDeModification = fichesSelectionneesO2M[0];
        JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
        return null;
    }

    public String supprimerFicheActionBaseBeanEntite(BaseBeanEntite fiche) {
        if (fiche == null) {
            return null;
        }
        try {
            _remove(fiche);
            JSFUtility.addInfoMessage("ok !");
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        return null;
    }

    /*
     * cette m�thode est appel�e uniquement par la page des Etudiants pour
     * une inscription � une sp�cialit� non encore valid�e.
     * @param fiche fiche � supprimer
     * @return page de retour
     */
    public String supprimerFicheAction(BaseBeanEntite fiche) {
        if (fiche == null) {
            return null;
        }
        try {
            _remove(fiche);
            JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        return "search.jsf?faces-redirect=true";
    }

    /*
     * Cette m�thode action cr�e ou modifie une fiche; elle est g�n�ralement utilis�e pour sauvegarder une fiche
     * qui est dans une relation O2O avec la fiche s�lectionn�e.
     * @param fiche fiche ? sauvegarder; elle est cens�e ?tre en relation O2O avec la fiche paire , c?d la fiche
     * produite par getPremiereFicheSelectionnee()
     * @return null
     */
    public String sauvegarderFicheAction(BaseBeanEntite fiche) {
        if (fiche == null) {
            JSFUtility.addErrorMessage("La fiche ? sauvegarder n'est pas d�finie.");
            return null;
        }
//        BaseBeanEntite parent = getPremiereFicheSelectionnee();
//        if (parent == null) {
//            JSFUtility.addErrorMessage("Vous voulez sauvegarder une fiche branche dont la fiche parente n'est pas d�finie; v�rifiez qu'une fiche parente est s�lectionn�e.");
//            return null;
//        }
        try {
//            String s = (utilisateurCourant == null) ? "Inconnu" : utilisateurCourant.getNomComplet();
//            if (s.length() > 32) {
//                s = s.substring(0, 31);
//            }
//            fiche.setEncodeur(s);
//            fiche.setDateModif(new Date());
            _save(fiche);

            JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
            // JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        return null;
    }

    /*
     * Cette m�thode action cr�e ou modifie une fiche; elle est g�n�ralement utilis�e pour sauvegarder une fiche
     * qui est dans une relation O2O avec la fiche s�lectionn�e.
     * @param fiche fiche ? sauvegarder; elle est cens�e ?tre en relation O2O avec la fiche paire , c?d la fiche
     * produite par getPremiereFicheSelectionnee()
     */
    public void sauvegarderFiche(BaseBeanEntite fiche) throws Exception {
        if (fiche == null) {
            JSFUtility.addErrorMessage("La fiche ? sauvegarder n'est pas d�finie.");
            return;
        }

        _save(fiche);
    }

    public String visiterEntiteAction(BaseBeanEntite visitedEntity) {
        this.visitedEntity = visitedEntity;
        return null;
    }

    public String resetVisitedEntityAction() {
        setVisitedEntity(null);
        return null;
    }

    public void resetVisitedEntity() {
        visitedEntity = null;
    }

    public String saveVisitedEntityAction() {
        if (visitedEntity == null) {
            return null;
        }
        try {

            boolean newRecord = visitedEntity.isNouvelleFiche();
            _save(visitedEntity);
            if (newRecord) {
                visitedEntity = (BaseBeanEntite) visitedEntity.getClass().newInstance();
            } else {
                this.refresh(visitedEntity); // 2012 12 28
            }
            // visitedEntity = null; // reset this properties as the save succeds
            JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
        return null;
    }

    public void sauvegarderFicheSelectionneeListener(ActionEvent ae) {
        if (selection == null) {
            JSFUtility.addErrorMessage("La variable repr&#233;sentant la fiche &#224; enregistrer est nulle; pr&#233;venez l'&#233;quipe de d&#233;veloppement.");
            return;
        }
        elementEnCoursDeCreationOuDeModification = selection;
        sauvegarderElementEnCoursDeCreationOuDeModificationListener(null);
    }

    public void sauvegarderElementEnCoursDeCreationOuDeModificationListener(ActionEvent ae) {
        try {

            if (elementEnCoursDeCreationOuDeModification == null) {
                JSFUtility.addErrorMessage("La variable repr&#233;sentant la fiche &#224; enregistrer est nulle; pr&#233;venez l'&#233;quipe de d&#233;veloppement.");
                return;
            }
//            String s = (utilisateurCourant == null) ? "Inconnu" : utilisateurCourant.getNomComplet();
//            if (s.length() > 32) {
//                s = s.substring(0, 31);
//            }
//            elementEnCoursDeCreationOuDeModification.setEncodeur(s);
//            elementEnCoursDeCreationOuDeModification.setDateModif(new Date());
            if (elementEnCoursDeCreationOuDeModification.isNouvelleFiche()) {
                // il 'agit d'une nouvelle fiche
                _saveIfNotExists(elementEnCoursDeCreationOuDeModification);
                this.ajouterALaListe(elementEnCoursDeCreationOuDeModification);
                elementEnCoursDeCreationOuDeModification = (BaseBeanEntite) elementEnCoursDeCreationOuDeModification.getClass().newInstance();
            } else {
                // il s'agit d'une modification de fiche existante; s'assurer qu'elle est persist�e
                BaseBeanEntite bean = this.findById(elementEnCoursDeCreationOuDeModification.getClass(), elementEnCoursDeCreationOuDeModification.getId());
                if (bean == null) {
                    JSFUtility.addErrorMessage("La fiche " + elementEnCoursDeCreationOuDeModification.toString() + " ne peut �tre persist�e alors qu'elle doit �tre modifi�e.");
                    return;
                }
                this.update(elementEnCoursDeCreationOuDeModification);
            }
            elementEnCoursDeCreationOuDeModification = null;
            JSFUtility.addInfoMessage("ok !");
            // JSFUtility.queueFmtString("ok", null, FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            JSFUtility.queueException(e);
        }
    }

    public void resetElementEnCoursDeCreationOuDeModificationListener(ActionEvent ae) {
        elementEnCoursDeCreationOuDeModification = null;
    }
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 2010 12 14

    public BaseBeanEntite getNullObject() {
        return null;
    }

//    public void dlgFermetureListener(CloseEvent event) {
//        // ecritureOk = Boolean.FALSE;
//    }
    public String activerEntiteParRechercheAction(String id, String nav) {
        if (StringUtils.isEmpty(id)) {
            JSFUtility.addErrorMessage("Le Id de la fiche recherch�e est nul; veuillez en informer l'�quipe de d�veloppement.");
            return null;
        }
        return nav + "?faces-redirect=true&id=" + id;
    }

    /*
     * rechercher une entit? connissant son Id.
     * @param classEntite nom complet de la classe entit?
     * @return page de vue ? afficher
     */
//    @SuppressWarnings("unchecked")
//    public String rechercherParIdAction() {
//        if (id == null) {
//            JSFUtility.addErrorMessage("L'id recherch� est nul; pr�venez l'�quipe de d�veloppement.");
//            return null;
//        }
//        String sql = "SELECT r FROM " + _nomCourt(nomCompletDeLaClasse) + " r WHERE r.id = :id ";
//        Map map = new HashMap(1);
//        map.put("id", id);
//        try {
//            fiches = this.new BilimaLazyDataModel(sql, map);
////            Class classz = Class.forName(nomCompletDeLaClasse);
////            List<BaseBeanEntite> l = this.lookupHavingSqlQueryDetached(sql, map, classz);
////            fiches = new EntiteBeanDataModel(l);
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//            return null;
//        }
//        return "resultats?faces-redirect=true";
//    }

    /*
     * rechercher une entit? connissant son Id.
     * @param classEntite nom complet de la classe entit?
     * @return page de vue ? afficher
     */
    @SuppressWarnings("unchecked")
//    protected BilimaLazyDataModel rechercherTypedQuery(TypedQuery tp) {
//        try {
//            return this.new BilimaLazyDataModel(tp);
////            Class classz = Class.forName(nomCompletDeLaClasse);
////            List<BaseBeanEntite> l = this.lookupHavingSqlQueryDetached(sql, map, classz);
////            fiches = new EntiteBeanDataModel(l);
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//        }
//        return null;
//    }
//
//    protected BilimaLazyDataModel rechercherParCriteriaQuery(Map<String, Object> mainAndCountParams) throws Exception {
//        return this.new BilimaLazyDataModel(mainAndCountParams);
//    }

//    protected BilimaLazyDataModel rechercherParCriteriaQuery(Map<String, Object> mainAndCountParams, boolean lazyLoad) throws Exception {
//        return this.new BilimaLazyDataModel(mainAndCountParams, lazyLoad);
//    }

    /*
     * rechercher une entit? connissant son Id.
     * @param classEntite nom complet de la classe entit?
     * @return page de vue ? afficher
     */
//    @SuppressWarnings("unchecked")
//    protected BilimaLazyDataModel rechercherParIdEtClass(String id, String nomCourtDeLaClasse) throws Exception {
//        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(nomCourtDeLaClasse)) {
//            throw new Exception("L'id recherch� et/ou le nom de la classe sont nuls; pr�venez l'�quipe de d�veloppement.");
//        }
//        String sql = "SELECT r FROM " + nomCourtDeLaClasse + " r WHERE r.id = :id ";
//        Map map = new HashMap(1);
//        map.put("id", id);
//        return this.new BilimaLazyDataModel(sql, map);
//    }

    /*
     * rechercher une entit? connissant son Id.
     * @param classEntite nom complet de la classe entit?
     * @return page de vue ? afficher
     */
//    @SuppressWarnings("unchecked")
//    protected BilimaLazyDataModel rechercherParSqlEtMap(String sql, Map<String, Object> map) throws Exception {
//        if (StringUtils.isEmpty(sql)) {
//            throw new Exception("Le query SQL est null; pr�venez l'�quipe de d�veloppement.");
//        }
//        return this.new BilimaLazyDataModel(sql, map);
//    }

    /*
     * refresh selected items
     */
    public void refreshActionListener(ActionEvent actionEvent) {
        if (!this.isSelectionActive()) {
            return;
        }
        try {
            this.refresh(selection);
        } catch (Exception exc) {
            JSFUtility.queueException(exc);
        }
    }

//    public String getIdDerniereAnneeAcademique() {
//        String sql = "SELECT r FROM AnneeAcademiqueEntite r "
//                + "ORDER By r.id DESC";
//        try {
//            @SuppressWarnings("unchecked")
//            List<AnneeAcademiqueEntite> list
//                    = (List<AnneeAcademiqueEntite>) this.lookupHavingSqlQueryDetached(sql, null, AnneeAcademiqueEntite.class
//                    );
//            if (list
//                    == null | list.isEmpty()) {
//                return null;
//            }
//
//            return list.get(
//                    0).getId();
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//            return null;
//        }
//    }
    abstract public void setInfoVueEtEntite();

    public void assignerSourcePourMany2Many(BaseBeanEntite entite) {
    }

    public String annulerAction() {
        return goHomeAction();
    }

    public List<SelectItem> getEuropeanCountries() {
        List<SelectItem> list = new ArrayList<>();
        list.add(new SelectItem("AT", "Austria"));
        list.add(new SelectItem("BE", "Belgium"));
        list.add(new SelectItem("BG", "Bulgaria"));
        list.add(new SelectItem("CY", "Cyprus"));
        list.add(new SelectItem("CZ", "Czech Republic"));
        list.add(new SelectItem("DE", "Germany"));
        list.add(new SelectItem("DK", "Denmark"));
        list.add(new SelectItem("EE", "Estonia"));
        list.add(new SelectItem("ES", "Spain"));
        list.add(new SelectItem("FI", "Finland"));
        list.add(new SelectItem("FR", "France"));
        list.add(new SelectItem("GB", "The United Kingdom"));
        list.add(new SelectItem("GR", "Greece"));
        list.add(new SelectItem("HU", "Hungary"));
        list.add(new SelectItem("IE", "Ireland"));
        list.add(new SelectItem("IS", "Iceland"));
        list.add(new SelectItem("IT", "Italy"));
        list.add(new SelectItem("LI", "Liechtenstein"));
        list.add(new SelectItem("LU", "Luxembourg"));
        list.add(new SelectItem("LT", "Lithuania"));
        list.add(new SelectItem("LV", "Latvia"));
        list.add(new SelectItem("MT", "Malta"));
        list.add(new SelectItem("NL", "The Netherlands"));
        list.add(new SelectItem("NO", "Norway"));
        list.add(new SelectItem("PL", "Poland"));
        list.add(new SelectItem("PT", "Portugal"));
        list.add(new SelectItem("RO", "Romania"));
        list.add(new SelectItem("SI", "Slovenia"));
        list.add(new SelectItem("SK", "Slovakia"));
        list.add(new SelectItem("SE", "Sweden"));
        return list;
    }

    public boolean preUpdate() {
        return Boolean.TRUE;
    }

    public void initPatternBasedSelectors() {
        // should be redefined in child class if necessary
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public Integer getTableBlockSize() {
        return tableBlockSize;

    }

    public void setTableBlockSize(Integer tableBlockSize) {
        this.tableBlockSize = tableBlockSize;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean getSortAscending() {
        return sortAscending;
    }

    public boolean isSortAscending() {
        return sortAscending;

    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;

    }

    public String getReference() {
        return reference;

    }

    public void setReference(String reference) {
        this.reference = reference;

    }

    public String getCurrentReference() {
        return currentReference;

    }

    public void setCurrentReference(String currentReference) {
        this.currentReference = currentReference;

    }

    public String getCurrentSearchCriteria() {
        return currentSearchCriteria;

    }

    public void setCurrentSearchCriteria(String currentSearchCriteria) {
        this.currentSearchCriteria = currentSearchCriteria;

    }

    public String sqlDate2Euro(java.sql.Date sqldate) {
        // input format = yyyy-MM-dd; output : dd/MM/yyyy
        String[] s = sqldate.toString().split("-");

        return s[2] + "/" + s[1] + "/" + s[0];

    }

    public Date getDateRangeStart() {
        if (dateRangeStart == null) {
            AcademicYear ay = new AcademicYear();
            dateRangeStart = ay.getRangeStart();

        }
        return dateRangeStart;

    }

    public void setDateRangeStart(Date dateRangeStart) {
        this.dateRangeStart = dateRangeStart;

    }

    public Date getDateRangeEnd() {
        if (dateRangeEnd == null) {
            AcademicYear ay = new AcademicYear();
            dateRangeEnd = ay.getRangeEnd();

        }
        return dateRangeEnd;

    }

    public void setDateRangeEnd(Date dateRangeEnd) {
        this.dateRangeEnd = dateRangeEnd;

    }

    public String getMany2OneNavigationDestination() {
        return many2OneNavigationDestination;

    }

    public void setMany2OneNavigationDestination(String many2OneNavigationDestination) {
        this.many2OneNavigationDestination = many2OneNavigationDestination;

    }

    public Map<String, String> getSelectedMap() {
        return selectedMap;

    }

    public void setSelectedMap(Map<String, String> selectedMap) {
        this.selectedMap = selectedMap;

    }

    public boolean isToggleSelectAll() {
        return toggleSelectAll;

    }

    public void setToggleSelectAll(boolean toggleSelectAll) {
        this.toggleSelectAll = toggleSelectAll;

    }

    public String getSqlPattern() {
        return sqlPattern;

    }

    public void setSqlPattern(String sqlPattern) {
        this.sqlPattern = sqlPattern;

    }

    public void processPostPersist() {
    }

    public String getMany2OnePropertyTobeEdited() {
        return many2OnePropertyTobeEdited;

    }

    public void setMany2OnePropertyTobeEdited(String many2OnePropertyTobeEdited) {
        this.many2OnePropertyTobeEdited = many2OnePropertyTobeEdited;

    }

    /**
     * @return the searchValue
     */
    public String getSearchValue() {
        return searchValue;

    }

    /**
     * @param searchValue the searchValue to set
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;

    }

    /**
     * @return the subToggleSelectAll
     */
    public boolean isSubToggleSelectAll() {
        return subToggleSelectAll;

    }

    /**
     * @param subToggleSelectAll the subToggleSelectAll to set
     */
    public void setSubToggleSelectAll(boolean subToggleSelectAll) {
        this.subToggleSelectAll = subToggleSelectAll;

    }

    /**
     * @return the subRowCount
     */
    public Integer getSubRowCount() {
        return subRowCount;

    }

    /**
     * @param subRowCount the subRowCount to set
     */
    public void setSubRowCount(Integer subRowCount) {
        this.subRowCount = subRowCount;

    }

    /**
     * @return the subSortAscending
     */
    public boolean isSubSortAscending() {
        return subSortAscending;

    }

    /**
     * @param subSortAscending the subSortAscending to set
     */
    public void setSubSortAscending(boolean subSortAscending) {
        this.subSortAscending = subSortAscending;

    }

    /**
     * @return the subSortColumn
     */
    public String getSubSortColumn() {
        return subSortColumn;

    }

    /**
     * @param subSortColumn the subSortColumn to set
     */
    public void setSubSortColumn(String subSortColumn) {
        this.subSortColumn = subSortColumn;

    }

    /*
     * @return script appelant (view) sans son extension; null si le script est introuvable
     */
    public String getExtensionLessViewId() {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot != null) {
            String viewName = viewRoot.getViewId();
            if (!StringUtils.isEmpty(viewName)) {
                int idx = viewName.lastIndexOf('.');
                if (idx > 0) {
                    viewName = viewName.substring(0, idx);
                }
            }
            return viewName;
        }
        return null;
    }

    public String searchAction(String navTo, String convId) {
        return navTo + "?faces-redirect=true";
    }

    public String searchAction() {
        return this.racineVue + "/search.jsf?faces-redirect=true";
    }

    public String cancelAction() {
        String viewId = getExtensionLessViewId();
        return (!StringUtils.isEmpty(viewId)) ? viewId + "?faces-redirect=true" : null;
    }

    public void validateurIdentifiantListener(ActionEvent ae) {
        UIInput input = (UIInput) ae.getSource();//ae.getComponent();
        String value = (String) input.getValue();

        if (StringUtils.isEmpty(value) || value.length() < 2) {
//            JSFUtility.queueFmtString("validationIdentifiant", null, FacesMessage.SEVERITY_ERROR);
            return;
        }
        Pattern p = Pattern.compile("[a-z][a-z0-9_.][a-z0-9_.]*");
        Matcher m = p.matcher(value);
        boolean b = m.matches();
        if (!b) {
//            JSFUtility.queueFmtString("validationIdentifiant", null, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * @return the racineVue
     */
    public String getRacineVue() {
        return racineVue;
    }

    public void setRacineVue(String racineVue) {
        this.racineVue = racineVue;
    }

    /**
     * @return the nomCompletDeLaClasse
     */
    public String getNomCompletDeLaClasse() {
        return nomCompletDeLaClasse;
    }

    /**
     * @param nomCompletDeLaClasse the nomCompletDeLaClasse to set
     */
    public void setNomCompletDeLaClasse(String nomCompletDeLaClasse) {
        this.nomCompletDeLaClasse = nomCompletDeLaClasse;
    }

    private String _nomCourt(String fqcn) {
        if (StringUtils.isEmpty(fqcn)) {
            return fqcn; // malformed class name
        }
        int lastDotIdx = fqcn.lastIndexOf(".");
        if (lastDotIdx < 0) {
            return fqcn;
        }
        int len = fqcn.length();
        if (lastDotIdx >= (len - 2)) {
            return fqcn; // malformed class name
        }
        return fqcn.substring(lastDotIdx + 1);
    }

    /**
     * @param fiches the fiches to set
     */
//    @SuppressWarnings("unchecked")
//    public void setFiches(BilimaLazyDataModel fiches) {
//        this.fiches = fiches;
//    }
//
//    /**
//     * @return the fiches
//     */
//    @SuppressWarnings("unchecked")
//    public BilimaLazyDataModel getFiches() {
//        return fiches;
////        if (fiches == null) {
////            return (List<BaseBeanEntite>) new ArrayList<BaseBeanEntite>(1);
////        }
////        return (List<BaseBeanEntite>) fiches;
//    }
//    public int getNombreDeFiches() {
//        if (fiches == null || fiches.getRowCount() < 1) {
//            return 0;
//        }
//        return fiches.getRowCount();
//    }
    /**
     * @return the ficheSelectionnee
     */
    public BaseBeanEntite getFicheSelectionnee() {
        return ficheSelectionnee;
    }

//    public <T extends BaseBeanEntite> String toggleSelectionAction(T ficheSelectionnee) {
//        if (ficheSelectionnee != null) {
//            if (ficheSelectionnee.isSelected()) {
//                setFicheSelectionnee(null);
//            } else {
//                setFicheSelectionnee(ficheSelectionnee);
//            }
//            ficheSelectionnee.setSelected(!ficheSelectionnee.isSelected());
//        }
//        return null;
//    }
    /**
     * @param ficheSelectionnee the ficheSelectionnee to set
     */
    public <T extends BaseBeanEntite> void setFicheSelectionnee(T ficheSelectionnee) {
//        if (ficheSelectionnee == null) {
//            fichesSelectionnees = null;
//        } else {
//            fichesSelectionnees = (BaseBeanEntite[])Array.newInstance(ficheSelectionnee.getClass(), 1);
//            selection = ficheSelectionnee;
//        }
        selection = ficheSelectionnee;
    }

    public BaseBeanEntite getPremiereFicheSelectionnee() {
        return selection;
    }

    private void setPremiereFicheSelectionnee(BaseBeanEntite eb) {
        selection = eb;
    }

    public boolean isSelectionActive() {
        return (selection != null);
    }

    public int getNombreFichesSelectionnees() {
        return (selection == null) ? 0 : 1;
    }

    /**
     * @param fichesSelectionnees the fichesSelectionnees to set
     */
//    public <T extends BaseBeanEntite> void setFichesSelectionnees(T[] fichesSelectionnees) {
//        this.fichesSelectionnees = fichesSelectionnees;
//    }
    public String goHomeAction() {
        return "/index?faces-redirect=true";
    }

    // v?rifier qu'il y a un et un seul �l�ment dans "fichesSelectionnees"
    public String gotoProfilAction() {
        if (selection == null) {
//            JSFUtility.addErrorMessage("Veuillez s�lectionner un et un seul �l�ment pour l'affichage du profil");
            return null;//getExtensionLessViewId() +"?faces-redirect=true";
        }
        return racineVue + "/profil?faces-redirect=true";
    }

//    public String annulerOne2ManyAction() {
//        fiches = null;
//        selection = null;
//        String s = annulerOne2ManyDialogAction();
//        return null;
//    }
    public String annulerOne2ManyDialogAction() {
        String viewId = getExtensionLessViewId();
        return viewId + "?faces-redirect=true";
    }

    /*
     * retourne ? l'?cran des r?sultats, c?d ? l'?cran r?sultant de la recherche.
     * Cette action est d?clench?e ? partir de la page 'profil'
     * @param nouvellePage page ? afficher au retour.
     * @return page ? afficher au retour.
     */
    public String retourVersEcranResultatsAction(String nouvellePage) {
        selection = null;
        return nouvellePage + "?faces-redirect=true";
    }

    /*
     * retourne ? l'?cran 'profil'.
     * Cette action est d?clench?e ? partir de la page 'one2Many'
     * @param nouvellePage page ? afficher au retour.
     * @return page ? afficher au retour.
     */
    public String retourVersEcranProfilAction(String nouvellePage) {
        return nouvellePage + "?faces-redirect=true";
    }

    /*
     * retourne ? l'?cran de recherche.
     * Cette action est d?clench?e ? partir de la page 'resultats'
     * @param nouvellePage page ? afficher au retour.
     * @return page ? afficher au retour.
     */
    public String retourVersEcranRechercheAction(String nouvellePage) {
        return nouvellePage + "?faces-redirect=true";
    }

//    @SuppressWarnings({"unchecked", "unchecked"})
//    public String findAllAction() {
//        try {
//            okapiLazyModel = ((OkapiLazyModel<? extends BaseBeanEntite>) this.new OkapiLazyModel(Class.forName(nomCompletDeLaClasse)));
//            return "resultats?faces-redirect=true";
//        } catch (Exception e) {
////            JSFUtility.queueException(e);
//        }
//        return null;
//    }
//    @SuppressWarnings({"unchecked", "unchecked"})
//    public String tousSearchAction() {
//        try {
//            Class classeEntite = Class.forName(nomCompletDeLaClasse);
//            BaseBeanEntite newEntity = (BaseBeanEntite) classeEntite.newInstance();
//            Map<String, Object> mainAndCountParams = newEntity.makeCriteriaQuery(entityManager);
//            if (mainAndCountParams == null) {
//                fiches = new BilimaLazyDataModel();
//            } else {
//                fiches = rechercherParCriteriaQuery(mainAndCountParams);
//            }
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//        }
//
//        return "resultats?faces-redirect=true";
//    }
    public String gotoProfilActionListener(ActionEvent ae) {
        if (ficheSelectionnee == null) {
            return null;
        }
        return "/views/instutionAcademique/etablissement/profil?faces-redirect=true";
    }

//    public String rowSelectNavigateToProfilListener(SelectEvent event) {
//        // proprietaireDuProfilCourant = (BaseBeanEntite) event.getObject();
//        return "profil?faces-redirect=true";
//    }
    public String modifierFichesSelectionneesAction() {
        try {
            if (selection == null) {
                throw (new Exception("Veuillez s�lectionner une et une seule fiche pour la modification."));
            }
        } catch (Exception e) {
//            JSFUtility.queueException(e);
            return null;
        }
        return null;
    }

    public String visionnerAvantSuppressionAction() {
        try {
            if (selection == null) {
                throw (new Exception("Veuillez s�lectionner au moins une fiche"));
            }
        } catch (Exception e) {
            JSFUtility.queueException(e);
            return null;
        }
        return racineVue + "/multiPurpose?faces-redirect=true";
    }

    @SuppressWarnings("unchecked")
    public String supprimerFichesSelectionneesAction() {
        return supprimerFicheSelectionneeAction();
    }

    // Les m?thodes suvantes peuvent ?tre rd?finies par le <X>Controller.
    // Elles sont utilis?es dans le d?corateur "ValiderFichesAVantEcritureDecortor"
    // Elles retournent false si la validation n'a pas r?ussi, true si elle a r?ussi.
    public boolean validerSauverNouvellesFiches() {
        return Boolean.TRUE;
    }

    public boolean validerSauverNouvellesFichesOne2Many() {
        return Boolean.TRUE;
    }

    public boolean validerSauverFichesModifiees() {
        return Boolean.TRUE;
    }

    public boolean validerSauverFichesModifieesOne2Many() {
        return Boolean.TRUE;
    }

    public boolean validerSupprimerFichesSelectionnees() {
        return Boolean.TRUE;
    }

    public boolean validerSupprimerDefinitivementFichesSelectionneesOne2Many() {
        return Boolean.TRUE;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the ecritureOk
     */
    public boolean isEcritureOk() {
        return ecritureOk;
    }

    /**
     * @param ecritureOk the ecritureOk to set
     */
    public void setEcritureOk(boolean ecritureOk) {
        this.ecritureOk = ecritureOk;
    }

    /**
     * @return the fichesSelectionneesO2M
     */
    public BaseBeanEntite[] getFichesSelectionneesO2M() {
        return fichesSelectionneesO2M;
    }

    /**
     * @param fichesSelectionneesO2M the fichesSelectionneesO2M to set
     */
    public void setFichesSelectionneesO2M(BaseBeanEntite[] fichesSelectionneesO2M) {
        this.fichesSelectionneesO2M = fichesSelectionneesO2M;
    }

    /**
     * @return the elementEnCoursDeCreationOuDeModification
     */
    public BaseBeanEntite getElementEnCoursDeCreationOuDeModification() {
        return elementEnCoursDeCreationOuDeModification;
    }

    /**
     * @param elementEnCoursDeCreationOuDeModification the
     * elementEnCoursDeCreationOuDeModification to set
     */
    public void setElementEnCoursDeCreationOuDeModification(BaseBeanEntite elementEnCoursDeCreationOuDeModification) {
        this.elementEnCoursDeCreationOuDeModification = elementEnCoursDeCreationOuDeModification;
    }

    /**
     * @return the clefPrimaire
     */
    public String getClefPrimaire() {
        return clefPrimaire;
    }

    /**
     * @param clefPrimaire the clefPrimaire to set
     */
    public void setClefPrimaire(String clefPrimaire) {
        this.clefPrimaire = clefPrimaire;
    }

    /**
     * @return the chartModel
     */
//    public CartesianChartModel getChartModel() {
//        return chartModel;
//    }
//
//    /**
//     * @param chartModel the chartModel to set
//     */
//    public void setChartModel(CartesianChartModel chartModel) {
//        this.chartModel = chartModel;
//    }
    /**
     * @return the chartDialogHeader
     */
    public String getChartDialogHeader() {
        return chartDialogHeader;
    }

    /**
     * @param chartDialogHeader the chartDialogHeader to set
     */
    public void setChartDialogHeader(String chartDialogHeader) {
        this.chartDialogHeader = chartDialogHeader;
    }

//    /**
//     * @return the visitedEntity
//     */
//    public BaseBeanEntite getVisitedEntity() {
//        return visitedEntity;
//    }
//    /**
//     * @param visitedEntity the visitedEntity to set
//     */
//    public void setVisitedEntity(BaseBeanEntite visitedEntity) {
//        this.visitedEntity = visitedEntity;
//    }
    public String getVisitedEntityClassName() {
        if (visitedEntity == null) {
            return "";
        }
        return visitedEntity.getClass().getSimpleName();
    }

    /**
     * @return the ficheBranche
     */
    public BaseBeanEntite getFicheBranche() {
        return ficheBranche;
    }

    /**
     * @param ficheBranche the ficheBranche to set
     */
    public void setFicheBranche(BaseBeanEntite ficheBranche) {
        this.ficheBranche = ficheBranche;
    }

    /**
     * @return the selectedNode
     */
//    public TreeNode getSelectedNode() {
//        return selectedNode;
//    }
//
//    /**
//     * @param selectedNode the selectedNode to set
//     */
//    public void setSelectedNode(TreeNode selectedNode) {
//        this.selectedNode = selectedNode;
//    }
//
//    public BilimaLazyDataModel initialiserDataModel(String sql, Map<String, Object> params) throws Exception {
//        return this.new BilimaLazyDataModel(sql, params);
//    }
    /**
     * @return the selection
     */
    public BaseBeanEntite getSelection() {
        return selection;
    }

    /**
     * @param selection the selection to set
     */
    public void setSelection(BaseBeanEntite selection) {
        this.selection = selection;
    }

    /**
     * @return the selecteurProfil
     */
    public String getSelecteurProfil() {
        return selecteurProfil;
    }

    /**
     * @param selecteurProfil the selecteurProfil to set
     */
    public void setSelecteurProfil(String selecteurProfil) {
        this.selecteurProfil = selecteurProfil;
    }

    /**
     * @return the cpiIdPourParcours
     */
    public String getCpiIdPourParcours() {
        return cpiIdPourParcours;
    }

    /**
     * @param cpiIdPourParcours the cpiIdPourParcours to set
     */
    public void setCpiIdPourParcours(String cpiIdPourParcours) {
        this.cpiIdPourParcours = cpiIdPourParcours;
    }

    /**
     * @return the opCodeCsv
     */
    public String getOpCodeCsv() {
        return opCodeCsv;
    }

    /**
     * @param opCodeCsv the opCodeCsv to set
     */
    public void setOpCodeCsv(String opCodeCsv) {
        this.opCodeCsv = opCodeCsv;
    }

    /**
     * @return the toggledRow
     */
    public BaseBeanEntite getToggledRow() {
        return toggledRow;
    }

    /**
     * @param toggledRow the toggledRow to set
     */
    public void setToggledRow(BaseBeanEntite toggledRow) {
        this.toggledRow = toggledRow;
    }

    /**
     * @return the nomDeFamille
     */
    public String getNomDeFamille() {
        return nomDeFamille;
    }

    /**
     * @param nomDeFamille the nomDeFamille to set
     */
    public void setNomDeFamille(String nomDeFamille) {
        this.nomDeFamille = nomDeFamille;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the toggledRow_O2M
     */
    public BaseBeanEntite getToggledRow_O2M() {
        return toggledRow_O2M;
    }

    /**
     * @param toggledRow_O2M the toggledRow_O2M to set
     */
    public void setToggledRow_O2M(BaseBeanEntite toggledRow_O2M) {
        this.toggledRow_O2M = toggledRow_O2M;
    }

    /**
     * @return the okapiLazyModel
     */
//    public OkapiLazyModel<? extends BaseBeanEntite> getOkapiLazyModel() {
////        return okapiLazyModel;
//        try {
//            return okapiLazyModel = ((OkapiLazyModel<? extends BaseBeanEntite>) this.new OkapiLazyModel(Class.forName(nomCompletDeLaClasse)));
////            return "resultats?faces-redirect=true";
//        } catch (Exception e) {
//            JSFUtility.queueException(e);
//        }
//        return null;
//
//    }
    /**
     * @param okapiLazyModel the okapiLazyModel to set
     */
//    public void setOkapiLazyModel(OkapiLazyModel<? extends BaseBeanEntite> okapiLazyModel) {
//        this.okapiLazyModel = okapiLazyModel;
//    }
    /**
     * @return the nouvelleFiche_M2O
     */
    public BaseBeanEntite getNouvelleFiche_M2O() {
        return nouvelleFiche_M2O;
    }

    public void setNouvelleFiche(GestionstockClient nouvelleFiche) {
        this.nouvelleFiche = nouvelleFiche;
    }

    /**
     * @param nouvelleFiche_M2O the nouvelleFiche_M2O to set
     */
    public void setNouvelleFiche_M2O(BaseBeanEntite nouvelleFiche_M2O) {
        this.nouvelleFiche_M2O = nouvelleFiche_M2O;
    }

    /**
     * @return the filteredRows
     */
    public List<? extends BaseBeanEntite> getFilteredRows() {
        return filteredRows;
    }

    /**
     * @param filteredRows the filteredRows to set
     */
    public void setFilteredRows(List<? extends BaseBeanEntite> filteredRows) {
        this.filteredRows = filteredRows;
    }

    /**
     * @return the prefixePrenom
     */
    public String getPrefixePrenom() {
        return prefixePrenom;
    }

    /**
     * @param prefixePrenom the prefixePrenom to set
     */
    public void setPrefixePrenom(String prefixePrenom) {
        this.prefixePrenom = prefixePrenom;
    }

    /**
     * @return the selections
     */
    public BaseBeanEntite[] getSelections() {
        return selections;
    }

    /**
     * @param selections the selections to set
     */
    public void setSelections(BaseBeanEntite[] selections) {
        this.selections = selections;
    }

    /**
     * @return the selectionsO2M
     */
    public BaseBeanEntite[] getSelectionsO2M() {
        return selectionsO2M;
    }

    /**
     * @param selectionsO2M the selectionsO2M to set
     */
    public void setSelectionsO2M(BaseBeanEntite[] selectionsO2M) {
        this.selectionsO2M = selectionsO2M;
    }
    
    
    
    

    public GestionstockProduit[] getFicheselect() {
        return ficheselect;
    }

    public void setFicheselect(GestionstockProduit[] ficheselect) {
        this.ficheselect = ficheselect;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * @return the currentDateTime
     */
    public Date getCurrentDateTime() {
        return currentDateTime;
    }

    /**
     * @param currentDateTime the currentDateTime to set
     */
    public void setCurrentDateTime(Date currentDateTime) {
        this.currentDateTime = currentDateTime;

    }

    /**
     * @return the fmtDate
     */
    public String getFmtDate() {
        return fmtDate;
    }

    /**
     * @param fmtDate the fmtDate to set
     */
    public void setFmtDate(String fmtDate) {
        this.fmtDate = fmtDate;
    }

    /**
     * @return the fichierCsv
     */
//    public UploadedFile getFichierCsv() {
//        return fichierCsv;
//    }
//
//    /**
//     * @param fichierCsv the fichierCsv to set
//     */
//    public void setFichierCsv(UploadedFile fichierCsv) {
//        this.fichierCsv = fichierCsv;
//    }
   
    
    
    
//    /**
//     * @return the organeSelection
//     */
//    public RetrocessionOrgane getOrganeSelection() {
//        return organeSelection;
//    }
//
//    /**
//     * @param organeSelection the organeSelection to set
//     */
//    public void setOrganeSelection(RetrocessionOrgane organeSelection) {
//        this.organeSelection = organeSelection;
//    }

    
    
    
    
    
    
    
    
    
    /**
     * @return the etablissement
     */
    public String getEtablissement() {
        return etablissement;
    }

    /**
     * @param etablissement the etablissement to set
     */
    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;

    }

//    /**
//     * @return the etudiantPourPhoto
//     */
//    public EtudiantEntite getEtudiantPourPhoto() {
//        return etudiantPourPhoto;
//    }
//
//    /**
//     * @param etudiantPourPhoto the etudiantPourPhoto to set
//     */
//    public void setEtudiantPourPhoto(EtudiantEntite etudiantPourPhoto) {
//        this.etudiantPourPhoto = etudiantPourPhoto;
//    }
//
//    /**
//     * @return the uploadedFile
//     */
//    public UploadedFile getUploadedFile() {
//        return uploadedFile;
//    }
//
//    /**
//     * @param uploadedFile the uploadedFile to set
//     */
//    public void setUploadedFile(UploadedFile uploadedFile) {
//        this.uploadedFile = uploadedFile;
//    }
//    public class BilimaLazyDataModel<T extends BaseBeanEntite> extends LazyDataModel<T> implements Serializable {
//
//        static final long serialVersionUID = 1L;
//        private final int DS_NONE = 1;
//        private final int DS_LOCAL = 2;
//        private final int DS_CRITERIAQUERY = 3;
//        private final int DS_CQ_SINGLERESULT = 4;
//        private int dataSourceType = DS_NONE;
//        //boolean localDataSource = Boolean.FALSE;
//        private Map<String, Object> mainAndCountParams = null;
//        private long numberOfRecords = 0;
//        private CriteriaBuilder builder;
//        //private TypedQuery query;
//        private List<T> singleResult = null;
//        private CriteriaQuery cq;
//        private TypedQuery<T> typedQuery;
//        private Root<T> root;
//        private Root<T> countRoot;
//        private Predicate originalConditions = null;
//        private Predicate originalCountConditions = null;
//        private Map<String, Predicate> foreignKeyPredicates = null;
//        private Map<String, Predicate> foreignKeyCountPredicates = null;
//        //private Map<String, ParameterExpression> foreignKeyParameters;
//        private Map<String, Path<String>> foreignKeyPathes = null;
//        // private Map<String, Path<String>> foreignKeyCountPathes;
////            private CriteriaBuilder countBuilder;
//        private CriteriaQuery countCriteriaQuery;
//        private Map<String, Object> paramsKeyAndValue = null;
//        private boolean lazyLoad = Boolean.TRUE;
//
//        private BilimaLazyDataModel() {
//            super();
//        }
//
//        private BilimaLazyDataModel(TypedQuery<T> tp) {
//            super();
//            typedQuery = tp;
//            dataSourceType = DS_CQ_SINGLERESULT;
//        }
//
//        private BilimaLazyDataModel(String sql, Map<String, Object> params) throws Exception {
//            super();
//            Query query = entityManager.createQuery(sql);
//            // EbM++ 2012 12 07
//            // query.setHint("eclipselink.cache-usage", "DoNotCheckCache");
//            //query.setHint("eclipselink.maintain-cache", "false");
//            if (params != null) {
//                Iterator<String> iter = params.keySet().iterator();
//                while (iter.hasNext()) {
//                    String p = iter.next();
//                    query.setParameter(p, params.get(p));
//                }
//            }
//            List<BaseBeanEntite> data = query.getResultList();
//            if (data != null) {
//                this.setRowCount(data.size());
//            }
//            this.setWrappedData(data);
//            dataSourceType = DS_LOCAL;//Boolean.TRUE;
//        }
//
//        private BilimaLazyDataModel(Map<String, Object> mainAndCountParams) throws Exception {
//            super();
//            dataSourceType = DS_CRITERIAQUERY;
//            this.mainAndCountParams = mainAndCountParams;
//            _init();
//
//            List<T> data = loadUsingCriteriaQuery(0, 20/*Integer.MAX_VALUE*/, null, SortOrder.ASCENDING, null);
//
////            TypedQuery<T> query = entityManager.createQuery(cq);
////            for (Map.Entry<String,Object> entry : paramsKeyAndValue.entrySet()) {
////                query.setParameter(entry.getKey(), entry.getValue());
////            }
////            List<T> data = query.getResultList();
//            if (data != null) {
//                numberOfRecords = (data != null) ? data.size() : 0;
//            }
//            setRowCount((int) numberOfRecords);
//            this.setWrappedData(data);
//        }
//
//        private BilimaLazyDataModel(Map<String, Object> mainAndCountParams, boolean lazyLoad) throws Exception {
//            super();
//            dataSourceType = DS_CRITERIAQUERY;
//            this.mainAndCountParams = mainAndCountParams;
//            this.lazyLoad = lazyLoad;
//            _init();
//
//            List<T> data = loadUsingCriteriaQuery(0, 20/*Integer.MAX_VALUE*/, null, SortOrder.ASCENDING, null);
//
////            TypedQuery<T> query = entityManager.createQuery(cq);
////            for (Map.Entry<String,Object> entry : paramsKeyAndValue.entrySet()) {
////                query.setParameter(entry.getKey(), entry.getValue());
////            }
////            List<T> data = query.getResultList();
//            if (data != null) {
//                numberOfRecords = (data != null) ? data.size() : 0;
//            }
//            setRowCount((int) numberOfRecords);
//            this.setWrappedData(data);
//        }
//
//        private void _init() {
//            builder = (CriteriaBuilder) mainAndCountParams.get(JSFUtility.BUILDER);
//            // query = (TypedQuery) mainAndCountParams.get("mainTypedQuery");
//            cq = (CriteriaQuery) mainAndCountParams.get(JSFUtility.RECORD_CQ);
//            typedQuery = (TypedQuery<T>) mainAndCountParams.get("mainTypedQuery");
//            root = (Root<T>) mainAndCountParams.get(JSFUtility.RECORD_ROOT);
//            countRoot = (Root<T>) mainAndCountParams.get(JSFUtility.COUNT_ROOT);
//            originalConditions = (Predicate) mainAndCountParams.get(JSFUtility.RECORD_CONDITIONS);
//            originalCountConditions = (Predicate) mainAndCountParams.get(JSFUtility.COUNT_CONDITIONS);
//            foreignKeyPredicates = (Map<String, Predicate>) mainAndCountParams.get(JSFUtility.RECORD_FK_PREDICATES);
//            foreignKeyCountPredicates = (Map<String, Predicate>) mainAndCountParams.get(JSFUtility.COUNT_FK_PREDICATES);
//            //foreignKeyParameters = (Map<String, ParameterExpression>) mainAndCountParams.get(JSFUtility.RECORD_FK_PREDICATES);
//            foreignKeyPathes = (Map<String, Path<String>>) mainAndCountParams.get(JSFUtility.FK_PATHES);
//            //foreignKeyCountPathes = (Map<String, Path<String>>) mainAndCountParams.get("mainForeignKeyCountPathes");
//            countCriteriaQuery = (CriteriaQuery) mainAndCountParams.get(JSFUtility.COUNT_CQ);
//            paramsKeyAndValue = (Map<String, Object>) mainAndCountParams.get(JSFUtility.PARAMS_KEY_AND_VALUE);
////            // placer les valeurs des ParameterExpression dans "query"; pour le compteur, ce sera fait plus tard.
////            for (Map.Entry<String,Object> entry : paramsKeyAndValue.entrySet()) {
////                query.setParameter(entry.getKey(), entry.getValue());
////            }
//        }
//
//        public boolean isEmpty() {
//            return (getRowCount() < 1);
//        }
//
//        public void add(BaseBeanEntite e) {
//            if (getRowCount() == 0) {
//                List l = new ArrayList(1);
//                setWrappedData(l);
//            }
//            ((List) getWrappedData()).add(e);
//            this.setRowCount(getRowCount() + 1);
//        }
//
////        @Override
////        public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object/*EbM-5*/> filters) {
////            try {
////                if (dataSourceType == DS_LOCAL) {
////                    // aucun traitement n'est fait pour le moment.
////                    // System.out.println("EbM - ControllerBaseBean:load() : going to execute loadImportedDataSource()");
////                    return loadImportedDataSource(first, pageSize, sortField, sortOrder, filters);
////                } else if (dataSourceType == DS_CRITERIAQUERY) {
////                    return loadUsingCriteriaQuery(first, pageSize, sortField, sortOrder, filters);
////                } else if (dataSourceType == DS_CQ_SINGLERESULT) {
////                    if (singleResult == null) {
////                        T record = typedQuery.getSingleResult();
////                        singleResult = new ArrayList<>(1);
////                        singleResult.add(record);
////                        setRowCount(1);
////                    }
////                    return singleResult;
////                    // return loadSingleResult();
////                }
////                return loadAll(first, pageSize, sortField, sortOrder, filters);
////            } catch (Exception e) {
////                JSFUtility.queueException(e);
////            }
////            return null;
////        }
//        private List<T> loadImportedDataSource(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object/*EbM-5*/> filters) {
//            List<T> l = (List<T>) getWrappedData();
//            return l;
//        }
//
//        /*
//         * rechercher toutes les fiches.
//         */
//        private List<T> loadUsingCriteriaQuery(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object/*EbM-5*/> filters) throws Exception {
//            List<T> data;
//            Set<Parameter<?>> paramsCQ = cq.getParameters();
//            if (paramsCQ != null && (!paramsCQ.isEmpty())) {
//                for (Iterator<Parameter<?>> iter = paramsCQ.iterator(); iter.hasNext();) {
//                    Parameter<?> entry = iter.next();
//                    if (paramsKeyAndValue == null
//                            || (!paramsKeyAndValue.containsKey(entry.getName())) /*|| paramsKeyAndValue.get(entry.getName()) == null
//                             || StringUtils.isEmpty(paramsKeyAndValue.get(entry.getName()).toString())*/) {
//                        iter.remove(); // using paramsCQ.remove(entry) would lead to a ConcurrentModificationException
//                    }
//                }
//                // paramsCQ = cq.getParameters();
//            }
//
//            //sort
//            if (!StringUtils.isEmpty(sortField)) {
//                if (foreignKeyPathes != null && foreignKeyPathes.containsKey(sortField)) {
//                    if (sortOrder == SortOrder.ASCENDING) {
//                        cq.orderBy(builder.asc(foreignKeyPathes.get(sortField)));
//                    } else {
//                        cq.orderBy(builder.desc(foreignKeyPathes.get(sortField)));
//                    }
//                } else if (sortField.indexOf('.') < 0) {
//                    if (sortOrder == SortOrder.ASCENDING) {
//                        cq.orderBy(builder.asc(root.get(sortField)));
//                    } else {
//                        cq.orderBy(builder.desc(root.get(sortField)));
//                    }
//                }
//                // else
//                // clef étrangère non prévue dans le filtrage : voir action dans AutorisationController
//            }
//
//            List<Predicate> conditions = new ArrayList<>();
//            if (originalConditions != null) {
//                conditions.add(originalConditions);
//            }
//
//            List<Predicate> countConditions = new ArrayList<>();
//            if (originalCountConditions != null) {
//                countConditions.add(originalCountConditions);
//            }
//
//            // filters
//            Map<String, Object> filterMap = new HashMap<>(); // need by Count
//            Map<String, String> filterParameterMap = null;
//            if (filters != null && (!filters.isEmpty())) {
//                filterParameterMap = new HashMap<>(filters.size());
//                for (Map.Entry<String, Object/*EbM-5*/> entry : filters.entrySet()) {
//                    String filterProperty = entry.getKey();
//                    String filterValue = (String) entry.getValue();
//                    if (StringUtils.isEmpty(entry.getKey()) || StringUtils.isEmpty((String) entry.getValue())) {
//                        continue;
//                    }
//
//                    if (foreignKeyPredicates != null && foreignKeyPredicates.containsKey(filterProperty)) {
//                        // this is a foreign key
//                        conditions.add(foreignKeyPredicates.get(filterProperty));
//                        //foundPredicates.put(filterProperty, foreignKeyPredicates.get(filterProperty)); // conserver l'adresse de ce Predicate pour l'enlever plus tard.
//                        countConditions.add(foreignKeyCountPredicates.get(filterProperty));
//                        filterParameterMap.put(filterProperty, filterValue.trim().toUpperCase());
//                        //query.setParameter(filterProperty, filterValue.trim().toUpperCase()); / !!!!
//                        filterMap.put(filterProperty, filterValue); //needed later by Count
//                    } else if (filterProperty.indexOf('.') >= 0) {
//                        // unhandled foreign key
//                        continue;
//                    } else {
//                        // at this stage, we've got a local property
//                        Path p = root.<String>get(filterProperty);
//                        Predicate predicate;
//                        String cname = p.getJavaType().getName();
//                        if (!cname.equals("Date")) {
//                            predicate = builder.like(builder.upper(p), filterValue.toUpperCase());
//                        } else {
//                            // Date
//                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                            try {
//                                predicate = builder.equal(builder.upper(p), sdf.parse(filterValue.toUpperCase()));
//                            } catch (Exception e) {
//                                predicate = builder.like(builder.upper(p), filterValue.toUpperCase()); // default predicate
//                            }
//                        }
//                        conditions.add(predicate);
//                        //foundPredicates.put(filterProperty, predicate); // conserver l'adresse de ce Predicate pour l'enlever plus tard.
//                        p = countRoot.<String>get(filterProperty);
//                        predicate = builder.like(builder.upper(p), filterValue.toUpperCase());
//                        //foundCountPredicates.put(filterProperty, predicate);
//                        countConditions.add(predicate);
//                    }
//                }
//            }
//
//            if (conditions != null && (!conditions.isEmpty())) {
//                Predicate finalCondition = null;
//                if (conditions.size() == 1) {
//                    finalCondition = conditions.get(0);
//                } else if (conditions.size() > 1) {
//                    finalCondition = builder.and(conditions.toArray(new Predicate[conditions.size()]));
//                }
//                if (finalCondition != null) {
//                    cq.where(finalCondition);
//                }
//            } else {
//                Predicate p = builder.equal(root.get("id"), root.get("id"));
//                cq.where(p);
//            }
//
//            if (countConditions != null && (!countConditions.isEmpty())) {
//                Predicate finalCondition = null;
//                if (countConditions.size() == 1) {
//                    finalCondition = countConditions.get(0);
//                } else if (countConditions.size() > 1) {
//                    finalCondition = builder.and(countConditions.toArray(new Predicate[countConditions.size()]));
//                }
//                if (finalCondition != null) {
//                    countCriteriaQuery.where(finalCondition);
//                }
//            } else {
//                Predicate p = builder.equal(root.get("id"), root.get("id"));
//                countCriteriaQuery.where(p);
//            }
//
//            // Note that this will get a previous Query if any; we need to reset some data including parameters if any
//            TypedQuery<T> query = typedQuery != null ? typedQuery : entityManager.createQuery(cq);
//            if (paramsKeyAndValue != null) {
////                Set<Parameter<?>> params = query.getParameters();
////                if (params != null && (!params.isEmpty())) {
////                    for (Parameter<?> entry : params) {
////                        // don't reset paramsKeyAndValue; we need them
////                        if (!paramsKeyAndValue.containsKey(entry.getName())) {
////                            query.setParameter(entry.getName(), "%");
////                        }
////                    }
////                }
//                for (Map.Entry<String, Object> entry : paramsKeyAndValue.entrySet()) {
//                    query.setParameter(entry.getKey(), entry.getValue());
//                    //                try {
//                    //                    // if the parameter doesn't exist, an IllegalArgumentException exception should be thrown, which not the case in EclipseLink 2.4.1 !
//                    //                    Parameter<?> p = query.getParameter(entry.getKey());
//                    //                } catch (Exception IllegalArgumentException) {
//                    //                    // This exception means that the parameter doesn't exist; so create it
//                    //                    query.setParameter(entry.getKey(), entry.getValue());
//                    //                }
//                }
//            }
//            if (filterParameterMap != null) {
//                for (Map.Entry<String, String> entry : filterParameterMap.entrySet()) {
//                    query.setParameter(entry.getKey(), entry.getValue());
//                }
//            }
//            if (lazyLoad) {
//                query.setFirstResult(first); // 0 assumed if not set
//                query.setMaxResults(pageSize); // Integer.MAX_VALUE assumed if not set
//            }
//            data = query.getResultList();
//            long count = JSFUtility.getRecordCount(entityManager, countCriteriaQuery, paramsKeyAndValue, filterMap);
//            setRowCount((int) count);
//            int dataSize = (data == null) ? 0 : data.size();
//
//            //paginate
//            if (dataSize > pageSize) {
//                // paranoia ?
//                /*
//                 * paranoia ? The above statement "data = executeCriteriaQuery(cq, first, pageSize, -1, null);"
//                 * grants that dataSise will never be greater than pageSize
//                 */
//                try {
//                    data = data.subList(first, first + pageSize);
//                } catch (IndexOutOfBoundsException e) {
//                    data = data.subList(first, first + (dataSize % pageSize));
//                }
//            }
//
////            if (data != null) {
////                data = (List<T>)new LinkedList<T>(data); // this makes the RO list RW
////            }
//            return data;
//        }
//
//        /*
//         * rechercher toutes les fiches.
//         */
//        private List<T> loadAll(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object/*EbM-5*/> filters) throws Exception {
//            Class rootClassz = null;
//            try {
//                rootClassz = Class.forName(nomCompletDeLaClasse);
//            } catch (ClassNotFoundException cnfe) {
//                JSFUtility.queueException(cnfe);
//            }
//            List<T> data;//new ArrayList<Account>();
//
//            // Criteria
//            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//            CriteriaQuery cQuery = cb.createQuery();
//            // From
//            Root<T> myRoot = cQuery.from(rootClassz);
//            cQuery.select(myRoot);
//
//            //sort
//            if (sortField != null) {
//                try {
//                    String[] joins = sortField.split("\\.");
//                    if (joins.length == 1) {
//                        // level 1
//                        if (joins.length == 1) {
//                            if (sortOrder == SortOrder.ASCENDING) {
//                                cQuery.orderBy(cb.asc(myRoot.get(sortField)));
//                            } else {
//                                cQuery.orderBy(cb.desc(myRoot.get(sortField)));
//                            }
//                        }
//                    } else if (joins.length == 2) {
//                        // level 2
//                        Join<T, T> join = myRoot.join(joins[0]);
//                        if (sortOrder == SortOrder.ASCENDING) {
//                            cQuery.orderBy(cb.asc(join.get(joins[1])));
//                        } else {
//                            cQuery.orderBy(cb.desc(join.get(joins[1])));
//                        }
//                    }
//                } catch (Exception exc) {
//                    // it's assumed that either the property is not found or it's too far (beyond 2 levesl) to be reached
//                }
//            }
//
//            // filters
//            Predicate[] predicatesArray = this.handleFilters(myRoot, cb, filters);
//            if (predicatesArray != null) {
//                cQuery.where(predicatesArray);
//            }
//            data = this.executeCriteriaQuery(cQuery, first, pageSize, -1, null);
//
//            // row count
//            CriteriaBuilder cb2 = entityManager.getCriteriaBuilder();
//            CriteriaQuery<Long> countQuery = cb2.createQuery(Long.class);
//            Root<T> cRoot = countQuery.from(rootClassz);
//            countQuery.select(cb2.count(cRoot));//cb2.count(cRoot));
//            predicatesArray = handleFilters(cRoot, cb2, filters);
//            if (predicatesArray != null) {
//                countQuery.where(predicatesArray);
//            }
//            Query query = entityManager.createQuery(countQuery);
//            int count = ((Long) query.getSingleResult()).intValue();
//            setRowCount(count);
//            int dataSize = (data == null) ? 0 : data.size();
//
//            //paginate
//            if (dataSize > pageSize) {
//                // paranoia ?
//                /*
//                 * paranoia ? The above statement "data = executeCriteriaQuery(cQuery, first, pageSize, -1, null);"
//                 * grants that dataSise will never be greater than pageSize
//                 */
//                try {
//                    data = data.subList(first, first + pageSize);
//                } catch (IndexOutOfBoundsException e) {
//                    data = data.subList(first, first + (dataSize % pageSize));
//                }
//            }
//
////            if (data != null) {
////                data = (List<T>)new LinkedList<T>(data); // this makes the RO list RW
////            }
//            return data;
//        }
//
//        /*
//         * rechercher toutes les fiches.
//         */
////        private List<EntiteBaseBean> loadAll_previous(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) throws Exception {
////            Class classz = null;
////            try {
////                classz = Class.forName(nomCompletDeLaClasse);
////            } catch (ClassNotFoundException cnfe) {
////                JSFUtility.queueException(cnfe);
////            }
////            List<EntiteBaseBean> data;
////
////            CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
////            CriteriaQuery accountQuery = criteriaBuilder.createQuery(classz);
////            Root<EntiteBaseBean> from = accountQuery.from(classz);
////            List<Predicate> predicates = new ArrayList<Predicate>();
////            data = executeCriteriaQuery(accountQuery, first, pageSize, -1, null);
////            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
////            countQuery.select(criteriaBuilder.count(countQuery.from(classz)));
////            countQuery.where(predicates.toArray(new Predicate[predicates.size()]));
////            Long rowCount = (Long) executeCriteriaQuerySingle(countQuery, -1, null);
////            setRowCount(rowCount.intValue());
////            if (data != null) {
////                data = new LinkedList<EntiteBaseBean>(data); // this makes the RO list RW
////            }
////            if (data != null && ((filters != null && (!filters.isEmpty())) || (sortField != null))) {
////                data = sortAndFilter(data, sortField, sortOrder, filters);
////               int dataSize = data.size();
////                this.setRowCount(dataSize);
////                if (dataSize > pageSize) {
////                    try {
////                        return data.subList(first, first + pageSize);
////                    } catch (IndexOutOfBoundsException e) {
////                        return data.subList(first, first + (dataSize % pageSize));
////                    }
////                }
////            }
////            return data;
////        }
//        private Predicate[] handleFilters(Root<T> root, CriteriaBuilder cb, Map<String, Object/*EbM-5*/> filters) {
//            // filters
//            if (filters == null || filters.isEmpty()) {
//                return null;
//            }
//            List<Predicate> predicates = new ArrayList<>();
//            for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
//                String filterProperty = it.next(); // table column name = field name
//                String filterValue = (String) filters.get(filterProperty);
//                Path p;
//                String[] joins = filterProperty.split("\\.");
//                if (joins.length > 1) {
//                    continue; // no multi-level filter property is accepted because it'd be too costly
//                }
//
////                if (joins.length > 1) {
////                    Join<T,T> join = root.join(joins[0]);
////                    try {
////                        p = join.<String>get(joins[1]);
////                    } catch (Exception exc) {
////                        // it's assumed that either the property is not found or it's too far to be reached
////                        continue;
////                    }
////                    predicates.add(cb.like(cb.upper(p), filterValue.toUpperCase() + "%")/*predicate*/);
////                } else {
//                try {
//                    p = root.<String>get(filterProperty);
//                } catch (Exception exc) {
//                    // it's assumed that either the property is not found or it's too far to be reached
//                    continue;
//                }
//                predicates.add(cb.like(cb.upper(p), filterValue.toUpperCase() + "%")/*predicate*/);
////                }
//            }
//            if (!predicates.isEmpty()) {
//                return predicates.toArray(new Predicate[predicates.size()]);
//            }
//            return null;
//        }
//
//        public List sortAndFilter(List dataSource, String sortField, SortOrder sortOrder, Map<String, String> filters) {
//            List data = new ArrayList();
//            if (filters != null && (!filters.isEmpty())) {
//                //filter
//                for (Object entite : dataSource) {
//                    boolean match = Boolean.TRUE;
//                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
//                        try {
//                            String filterProperty = it.next();
//                            String filterValue = filters.get(filterProperty);
//
//                            String methodName = "get" + filterProperty.substring(0, 1).toUpperCase() + filterProperty.substring(1);
//                            Method getMethod = entite.getClass().getDeclaredMethod(methodName);
//                            Object obj = getMethod.invoke(entite, (Object[]) null);
//                            String fieldValue = obj.toString();
//                            // String fieldValue = String.valueOf(entite.getClass().getField(filterProperty).get(entite));
//
//                            if (filterValue == null || fieldValue.startsWith(filterValue)) {
//                                match = Boolean.TRUE;
//                            } else {
//                                match = Boolean.FALSE;
//                                break;
//                            }
//                        } catch (Exception e) {
//                            match = Boolean.FALSE;
//                        }
//                    }
//                    if (match) {
//                        data.add(entite);
//                    }
//                }
//            } else {
//                data = dataSource;
//            }
//
//            if (sortField != null) {
//                if (data == null || data.isEmpty()) {
//                    // System.out.println("EbM - ControllerBaseBean:sortAndFilter() : data is null or empty");
//                }
//                // System.out.println("EbM - ControllerBaseBean:sortAndFilter() : going to sort and filter : " + sortField + "," + sortOrder.toString());
//                Collections.sort(data, new LazySorter(sortField, sortOrder));
//                // System.out.println("EbM - ControllerBaseBean:sortAndFilter() : done");
//            }
//            return data;
//        }
//        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//
//        @Override
//        public Object getRowKey(BaseBeanEntite entite) {
//            return entite.getId();
//        }
//
//        @Override
//        public T getRowData(String rowKey) {
//            List<T> entites = (List<T>) this.getWrappedData();
//
//            for (T entite : entites) {
//                if (entite.getId().equals(rowKey)) {
//                    return entite;
//                }
//            }
//
//            return null;
//        }
//
//        private List<T> executeCriteriaQuery(CriteriaQuery cQuery, int first, int pageSize, int i, Object object) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        }
//    }
//
//    public class OkapiLazyModel<T extends BaseBeanEntite> extends LazyDataModel<T> implements Serializable {
//
//        private static final long serialVersionUID = Long.MIN_VALUE;
//        private ControllerParent.OkapiLazyModel.LazyLoad lazyLoad;
//        private boolean singleResult = Boolean.FALSE;
//
//        public OkapiLazyModel(Class<T> classz) {
//            lazyLoad = new ControllerParent.OkapiLazyModel.LazyLoad(classz/*, entityManager*/);
//        }
//
//        public OkapiLazyModel(Class<T> queryClass, Class<T> rootClass) {
//            lazyLoad = new ControllerParent.OkapiLazyModel.LazyLoad(queryClass, rootClass/*, entityManager*/);
//        }
//
//        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> LazyLoad
//        /**
//         *
//         * @author Edmond B. Mulemangabo <edmond.mulemangabo@uclouvain.be>
//         */
//        public class LazyLoad<T extends BaseBeanEntite> implements Serializable {
//
//            private static final long serialVersionUID = Long.MIN_VALUE;
//
//            private Class<T> queryClass = null;
//            private Class<T> rootClass = null;
//            private CriteriaBuilder resultBuilder;
//            private CriteriaBuilder countBuilder;
//            private Root<T> countRoot;
//            private Root<T> resultRoot;
//            private CriteriaQuery<Long> countQuery;
//            private CriteriaQuery<T> resultQuery;
//            private Predicate resultPermanentPredicate;
//            private Predicate countPermanentPredicate;
//            //private Map<String, Object> params;
//            private Map<String, Path<String>> resultForeignKeyMap = new HashMap<>(1);
//            private Map<String, Path<String>> countForeignKeyMap = new HashMap<>(1);
//
//            public LazyLoad(/*EntityManager entityManager*/) {
//                // this.entityManager = entityManager;
//                _init();
//            }
//
//            public LazyLoad(Class<T> classz/*, EntityManager entityManager*/) {
//                //this.entityManager = entityManager;
//                queryClass = rootClass = classz;
//                _init();
//            }
//
//            public LazyLoad(Class<T> queryClass, Class<T> rootClass/*, EntityManager entityManager*/) {
//                //this.entityManager = entityManager;
//                this.queryClass = queryClass;
//                this.rootClass = rootClass;
//                _init();
//            }
//
////            public void postConstruct() {
////                resultBuilder = entityManager.getCriteriaBuilder();
////                countBuilder = entityManager.getCriteriaBuilder();
////                resultPermanentPredicate = resultBuilder.equal(resultBuilder.literal(1), 1); // get all records by default
////
////                resultQuery = resultBuilder.createQuery(queryClass);
////                resultRoot = resultQuery.from(rootClass);
////                resultQuery.select(resultRoot);
////
////                countQuery = countBuilder.createQuery(Long.class);
////                countRoot = countQuery.from(queryClass);
////                countQuery.select(countBuilder.count(countRoot));
////            }
//            private void _init() {
//                resultBuilder = entityManager.getCriteriaBuilder();
//                countBuilder = entityManager.getCriteriaBuilder();
//                resultPermanentPredicate = resultBuilder.equal(resultBuilder.literal(1), 1); // get all records by default
//
//                resultQuery = resultBuilder.createQuery(queryClass);
//                resultRoot = resultQuery.from(rootClass);
//                resultQuery.select(resultRoot);
//
//                countQuery = countBuilder.createQuery(Long.class);
//                countRoot = countQuery.from(queryClass);
//                countQuery.select(countBuilder.count(countRoot));
//            }
//
//            private Predicate getFilterCondition(/*Map<String, Path<String>> foreignKeyMap,*/CriteriaBuilder builder, Root<T> entityObj, Map<String, String> filters) {
//                Predicate filterCondition = builder.conjunction();
//                String wildCard = "%";
//                for (Map.Entry<String, String> filter : filters.entrySet()) {
//                    String value = wildCard + filter.getValue() + wildCard;
//                    if (!StringUtils.isEmpty(filter.getValue())) {
//                        if (filter.getKey().indexOf((int) '.') > 0) {
//                            String[] tokens = filter.getKey().split("\\.");
//                            Path<? extends BaseBeanEntite> entityPath = entityObj.get(tokens[0]);
//                            for (int i = 1; i < tokens.length - 1; i++) {
//                                entityPath = entityPath.get(tokens[i]);
//                            }
//                            Path<String> pathValue = entityPath.get(tokens[tokens.length - 1]);
//                            filterCondition = builder.and(filterCondition, builder.like(pathValue, value));
//                        } else {
//                            javax.persistence.criteria.Path<String> path = entityObj.get(filter.getKey());
//                            filterCondition = builder.and(filterCondition, builder.like(path, value));
//                        }
////                    else if (foreignKeyMap.containsKey(filter.getKey())) {
////                        filterCondition = builder.and(filterCondition, builder.like(foreignKeyMap.get(filter.getKey()), value));
////                    }
//                    }
//                }
//                return filterCondition;
//            }
//
//            public int count(Map<String, String> filters) {
//                //CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//                Predicate condition = countPermanentPredicate;
//                if (filters != null && (!filters.isEmpty())) {
//                    Predicate filterCondition = getFilterCondition(/*countForeignKeyMap, */countBuilder, countRoot, filters);
//                    if (filterCondition != null) {
//                        condition = (condition != null)
//                                ? countBuilder.and(condition, filterCondition)
//                                : filterCondition;
//                    }
//                }
//                if (condition != null) {
//                    countQuery.where(condition);
//                }
//                //countQuery.select(countBuilder.count(countRoot));
//                TypedQuery<Long> tq = entityManager.createQuery(countQuery);
//                //tq.setHint("javax.persistence.cache.storeMode", "REFRESH"); // EbM 2013 10 02
////        if (params != null && (!params.isEmpty())) {
////            for (String key : params.keySet()) {
////                tq.setParameter(key, params.get(key));
////            }
////        }
//                return tq.getSingleResult().intValue();
//            }
//
//            public List<T> getResultList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
//                //CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//
//                Predicate condition = resultPermanentPredicate;
//                if (filters != null && (!filters.isEmpty())) {
//                    Predicate filterCondition = getFilterCondition(/*resultForeignKeyMap, */resultBuilder, resultRoot, filters);
//                    if (filterCondition != null) {
//                        condition = (condition != null)
//                                ? resultBuilder.and(condition, filterCondition)
//                                : filterCondition;
//                    }
//                }
////        if (condition != null) {
////            resultQuery.where(condition);
////        }
//                resultQuery.where(condition);
//                // resultQuery.select(resultRoot);
//                if (!StringUtils.isEmpty(sortField)) {
//                    if (sortField.indexOf((int) '.') > 0) {
//                        String[] tokens = sortField.split("\\.");
//                        Path<? extends BaseBeanEntite> entityPath = resultRoot.get(tokens[0]);
//                        for (int i = 1; i < tokens.length - 1; i++) {
//                            entityPath = entityPath.get(tokens[i]);
//                        }
//                        Path<String> pathValue = entityPath.get(tokens[tokens.length - 1]);
//                        if (sortOrder == SortOrder.ASCENDING) {
//                            resultQuery.orderBy(resultBuilder.asc(pathValue));
//                        } else if (sortOrder == SortOrder.DESCENDING) {
//                            resultQuery.orderBy(resultBuilder.desc(pathValue));
//                        }
//                    } else {
//                        if (sortOrder == SortOrder.ASCENDING) {
//                            resultQuery.orderBy(resultBuilder.asc(resultRoot.get(sortField)));
//                        } else if (sortOrder == SortOrder.DESCENDING) {
//                            resultQuery.orderBy(resultBuilder.desc(resultRoot.get(sortField)));
//                        }
//                    }
//                }
//                TypedQuery<T> tq = entityManager.createQuery(resultQuery);
//                //tq.setHint("javax.persistence.cache.storeMode", "REFRESH"); // EbM 2013 10 02
////        if (params != null && (!params.isEmpty())) {
////            for (String key : params.keySet()) {
////                tq.setParameter(key, params.get(key));
////            }
////        }
//                return tq.setFirstResult(first).setMaxResults(pageSize).getResultList();
//            }
//
//            public void setResultConditions(Predicate predicate) {
//                this.resultPermanentPredicate = predicate;
//            }
//
//            public void setCountConditions(Predicate predicate) {
//                this.countPermanentPredicate = predicate;
//            }
//
//            /**
//             * @return the countRoot
//             */
//            public Root<T> getCountRoot() {
//                return countRoot;
//            }
//
//            /**
//             * @param countRoot the countRoot to set
//             */
//            public void setCountRoot(Root<T> countRoot) {
//                this.countRoot = countRoot;
//            }
//
//            /**
//             * @return the resultRoot
//             */
//            public Root<T> getResultRoot() {
//                return resultRoot;
//            }
//
//            /**
//             * @param resultRoot the resultRoot to set
//             */
//            public void setResultRoot(Root<T> resultRoot) {
//                this.resultRoot = resultRoot;
//            }
//
//            /**
//             * @return the countQuery
//             */
//            public CriteriaQuery<Long> getCountQuery() {
//                return countQuery;
//            }
//
//            /**
//             * @param countQuery the countQuery to set
//             */
//            public void setCountQuery(CriteriaQuery<Long> countQuery) {
//                this.countQuery = countQuery;
//            }
//
//            /**
//             * @return the resultQuery
//             */
//            public CriteriaQuery<T> getResultQuery() {
//                return resultQuery;
//            }
//
//            /**
//             * @param resultQuery the resultQuery to set
//             */
//            public void setResultQuery(CriteriaQuery<T> resultQuery) {
//                this.resultQuery = resultQuery;
//            }
//
//            /**
//             * @return the countBuilder
//             */
//            public CriteriaBuilder getCountBuilder() {
//                return countBuilder;
//            }
//
//            /**
//             * @param countBuilder the countBuilder to set
//             */
//            public void setCountBuilder(CriteriaBuilder countBuilder) {
//                this.countBuilder = countBuilder;
//            }
//
//            /**
//             * @return the builder
//             */
//            public CriteriaBuilder getResultBuilder() {
//                return resultBuilder;
//            }
//
//            /**
//             * @param builder the builder to set
//             */
//            public void setResultBuilder(CriteriaBuilder resultBuilder) {
//                this.resultBuilder = resultBuilder;
//            }
//
//            /**
//             * @return the resultForeignKeyMap
//             */
//            public Map<String, Path<String>> getResultForeignKeyMap() {
//                return resultForeignKeyMap;
//            }
//
//            /**
//             * @param resultForeignKeyMap the resultForeignKeyMap to set
//             */
//            public void setResultForeignKeyMap(Map<String, Path<String>> resultForeignKeyMap) {
//                this.resultForeignKeyMap = resultForeignKeyMap;
//            }
//
//            /**
//             * @return the countForeignKeyMap
//             */
//            public Map<String, Path<String>> getCountForeignKeyMap() {
//                return countForeignKeyMap;
//            }
//
//            /**
//             * @param countForeignKeyMap the countForeignKeyMap to set
//             */
//            public void setCountForeignKeyMap(Map<String, Path<String>> countForeignKeyMap) {
//                this.countForeignKeyMap = countForeignKeyMap;
//            }
//
//            /**
//             * @return the queryClass
//             */
//            public Class<T> getQueryClass() {
//                return queryClass;
//            }
//
//            /**
//             * @param queryClass the queryClass to set
//             */
//            public void setQueryClass(Class<T> queryClass) {
//                this.queryClass = queryClass;
//            }
//
//            /**
//             * @return the rootClass
//             */
//            public Class<T> getRootClass() {
//                return rootClass;
//            }
//
//            /**
//             * @param rootClass the rootClass to set
//             */
//            public void setRootClass(Class<T> rootClass) {
//                this.rootClass = rootClass;
//            }
//        }
////<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< end LazyLoad
//
////        @Override
////        public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object/*EbM-5*/> filters) {
////            if (singleResult) {
////                return (List<T>) this.getWrappedData();
////            }
////            setRowCount(lazyLoad.count(filters));
////            return lazyLoad.getResultList(first, pageSize, sortField, sortOrder, filters);
////        }
//        @Override
//        public Object getRowKey(T entite) {
//            return entite.getId();
//        }
//
//        @Override
//        public T getRowData(String rowKey) {
//            List<T> entites = (List<T>) this.getWrappedData();
//
//            for (T entite : entites) {
//                if (entite.getId().equals(rowKey)) {
//                    return entite;
//                }
//            }
//
//            return null;
//        }
//
//        /**
//         * @return the lazyLoad
//         */
//        public ControllerParent.OkapiLazyModel.LazyLoad getLazyLoad() {
//            return lazyLoad;
//        }
//
//        /**
//         * @param lazyLoad the lazyLoad to set
//         */
//        public void setLazyLoad(ControllerParent.OkapiLazyModel.LazyLoad lazyLoad) {
//            this.lazyLoad = lazyLoad;
//        }
//
//        public <P extends BaseBeanEntite> void setSingleResultWrappedData(P obj) {
//            List<P> list = new ArrayList<>(1);
//            list.add(obj);
//            setWrappedData(list);
//            setRowCount(1);
//            singleResult = Boolean.TRUE;
//            //setRowIndex(1);
//        }
//    }
//    public boolean isOkapiLazyModelEmpty() {
//        if (okapiLazyModel == null || okapiLazyModel.getRowCount() < 1) {
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
//    }
    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate2() {
        return date2;
    }

    public String getSpecialite() {
        return specialite;
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 11 08 13
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
      /**
     * Generate a single random integer between aLowerLimit and aUpperLimit,
     * inclusive.
     *
     * Important: if this method is called twice in rapid succession, it will
     * return the same value.
     *
     * @param reason any string; this should be be short : 2 ou 3 characters
     *
     * @exception IllegalArgumentException if aLowerLimit is not less than
     * aUpperLimit.
     *
     */
    public static int pickNumberInRange(int aLowerLimit, int aUpperLimit) {
        Random generator = new Random();
        // get the range, casting to long to avoid overflow problems
        long range = (long) aUpperLimit - (long) aLowerLimit + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * generator.nextDouble());
        return (int) (fraction + aLowerLimit);
    }

    
    /*
     * g?n?rer un identifiant ? partir des nom et pr?nom
     */
    public static String genererIdentifiant(String nomDeFamille, String prenom) {
        if (StringUtils.isEmpty(nomDeFamille) || StringUtils.isEmpty(prenom)) {
            return null;
        }
        String sn = _enleverAccents(nomDeFamille).replaceAll("\\s", "");
        String fn = _enleverAccents(prenom).replaceAll("\\s", "");

        // alphabet admis : [a-zA-Z 0-9_.-]+
        sn = sn.replaceAll("[^a-zA-Z 0-9_.-]+", "");
        fn = fn.replaceAll("[^a-zA-Z 0-9_.-]+", "");

        if ((sn.length() + fn.length() + 1) <= 20) {
            // longueur(nom + prenom +1) <= 20; on prend prenom + "." + nom
            return (fn + "." + sn).toLowerCase();
        }

        String trailer = sn;
        if (trailer.length() > 18) {
            trailer = trailer.substring(0, 18);
        }
        String head = fn.substring(0, 20 - (trailer.length() + 1));
        return (head + "." + trailer).toLowerCase();
    }

    /*
     * g?n?rer un identifiant ? partir des nom et pr?nom
     */
    public static String genererIdentifiantSansDoublon(String nomDeFamille, String prenom) {
        if (StringUtils.isEmpty(nomDeFamille) || StringUtils.isEmpty(prenom)) {
            return null;
        }
        String identifiant = genererIdentifiant(nomDeFamille, prenom);
        String randomString = Integer.toString(pickNumberInRange(1, 999));
        return identifiant.substring(0, identifiant.length() - randomString.length()) + randomString;
    }
    
    
    
    /*
     * g?n?rer un identifiant ? partir des nom et pr?nom
     */
    public static String genererIdentifiantSansDoublon(String nomDeFamille, String prenom, String trailer) {
        if (StringUtils.isEmpty(nomDeFamille) || StringUtils.isEmpty(prenom)) {
            return null;
        }
        String identifiant = genererIdentifiant(nomDeFamille, prenom);
        return identifiant.substring(0, identifiant.length() - trailer.length()) + trailer;
    }

    private static String _enleverAccents(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        String value = Normalizer.normalize(s, Normalizer.Form.NFD);
        return value.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private static String _enleverCaracteres(String s, String re) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        return s.replaceAll(re, "");
    }
    
 
    
}
