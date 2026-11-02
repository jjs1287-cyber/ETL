package org.etlforge.integration.data.entity;

import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.id.JmsMessageId;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.script.ScriptedLink;
import org.etlforge.integration.data.entity.script.ScriptedLinkSource;
import org.etlforge.integration.data.entity.script.ScriptedLinkSourceEventSupport;
import org.etlforge.integration.data.entity.script.ScriptedLinkSourceListener;
import org.etlforge.integration.data.entity.transformation.MappingTreeNodePathFilter;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.entity.transformation.SourceBatchParams;
import org.etlforge.integration.data.entity.transformation.StructurePersistor;
import org.etlforge.integration.data.entity.transformation.TransformationLinkCollector;
import org.etlforge.integration.data.entity.transformation.TransformationReferenceReplacer;
import org.etlforge.integration.data.entity.transformation.TransformationScriptLinkCollector;
import org.etlforge.integration.data.entity.transformation.TransformationSourceValidator;
import org.etlforge.integration.data.entity.transformation.TransformationTargetValidator;
import org.etlforge.integration.data.entity.transformation.TransformationValidator;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.data.structure.StructureNature;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.data.structure.XsdBasedStructure;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.integration.structure.mapping.InvalidMappingPersistor;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.lang.Arrays2;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.id.KongaID;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public class Transformation extends IntegrationEntity implements Function, ScriptedLinkSource {
   @IdProperty
   public static final String TRANSFORMATION_ID = "transformation_id";
   @NameProperty
   public static final String TRANSFORMATION_NAME = "transformation_name";
   public static final String SOURCE_TYPE_ID = "source_type_id";
   public static final String TARGET_TYPE_ID = "target_type_id";
   public static final String SOURCE_TEXT_DOC = "source_text_doc";
   public static final String TARGET_TEXT_DOC = "target_text_doc";
   public static final String SOURCE_EDI = "source_edi";
   public static final String TARGET_EDI = "target_edi";
   public static final String SOURCE_EDI_DEFAULT = "source_edi_default";
   public static final String TARGET_EDI_DEFAULT = "target_edi_default";
   public static final String SOURCE_SOURCE_ID = "source_sourceid";
   public static final String TARGET_TARGET_ID = "target_targetid";
   public static final String NATURE_OF_SOURCE = "natureofsource";
   public static final String NATURE_OF_TARGET = "natureoftarget";
   public static final String SOURCE_XML = "source_xml";
   public static final String TARGET_XML = "target_xml";
   public static final String IS_VALID = "isvalid";
   public static final String SOURCE_DTD_ROOT = "sourcedtd_root";
   public static final String TARGET_DTD_ROOT = "targetdtd_root";
   public static final String TARGET_DTD_URL = "target_dtd_url";
   public static final String INSERT_MODE = "insert_mode";
   public static final String TARGET_TABLES = "target_tables";
   public static final String SALESFORCE_OBJECT = "salesforce_object";
   public static final String SALESFORCE_WIZARD_ROLE = "saleforce_wizard_role";
   private static final String SALESFORCE_PARTNER_WIZARD = "salesforce_partner_wizard";
   private static final String TARGET_HEADER = "target_header";
   private static final String AUTO_GENERATED_TARGET_STRUCT = "auto_generated_target_struct";
   public static final String TARGET_FIELD_ID = "target_field_id";
   public static final String TRANSFORMATION = "transformation";
   public static final String FUNCTION_ID = "function_id";
   public static final String INPUT_STRUCTURE = "Transformation#InputStructure";
   public static final String OUTPUT_STRUCTURE = "Transformation#OutputStructure";
   public static final String MAPPINGS = "Transformation#Mappings";
   public static final String SOURCE_JMS_MESSAGE_ID = "SourceJmsMessageId";
   public static final String TARGET_JMS_MESSAGE_ID = "TargetJmsMessageId";
   public static final String INVALID_MAPPINGS = "InvalidMappings";
   public static final String RENAMED_NODES = "RenamedNodes";
   public static final String USE_JB_XML_PARSER = "use_jbxmlparser";
   public static final String DO_SOURCE_XML_VALIDATION = "DoSourceXmlValidation";
   public static final String DO_TARGET_XML_VALIDATION = "DoTargetXmlValidation";
   public static final String TARGET_XML_SCHEMA_LOCATION = "TargetXmlSchemaLocation";
   public static final String TRUNCATE_CHARS_IN_DB_TARGET = "TruncateCharsInDbTarget";
   public static final String INCLUDE_ROWID_IN_INFORMIX_TABLES_INPUT = "include_informix_row_id_input";
   public static final String INCLUDE_ROWID_IN_INFORMIX_TABLES_OUTPUT = "include_informix_row_id_output";
   public static final String SOURCE_BATCH_PARAMS = "SourceBatchParams";
   private static final String SOURCE_BATCH_SIZE = "SourceBatchSize";
   private static final String SOURCE_BATCH_NODE_PATH = "SourceBatchNodePath";
   private static final String NO_SOURCE_DATA_USED_IN_MAPPING = "NoSourceDataUsedInMapping";
   private static final String XML_SOURCE_IS_EDI = "XmlSourceIsEdi";
   private static final String XML_TARGET_IS_EDI = "XmlTargetIsEdi";
   public static final String INPUT_NODE_FILTER = "InputNodeFilter";
   public static final String OUTPUT_NODE_FILTER = "OutputNodeFilter";
   public static final String MAPPING_STRUCTURES = "MappingStructures";
   private static final Version MINIMUM_VERSION_FOR_MANUAL_SQL = VersionFactory.newVersion(3, 0, 1, 40);
   private static final Version MINIMUM_VERSION_FOR_JSON = VersionFactory.newVersion(4, 1, 0, 14);
   private Set<String> mappings = new LinkedHashSet<>();
   private Set<InvalidMapping> invalidMappings = new LinkedHashSet<>();
   private DataStructure inputStructure;
   private DataStructure outputStructure;
   private final transient PropertyChangeListener inputStructureListener;
   private final transient PropertyChangeListener outputStructureListener;
   private Map<String, String> renamedNodes;
   private transient List<ScriptedLink> scriptedLinks;
   private MappingTreeNodePathFilter inputMappingTreeFilter;
   private MappingTreeNodePathFilter outputMappingTreeFilter;
   private ScriptedLinkSourceEventSupport scriptedLinksEventSupport = new ScriptedLinkSourceEventSupport(this);

   @Override
   public String[] getPropertyKeys() {
      return new String[]{
         "transformation_id",
         "transformation_name",
         "source_type_id",
         "target_type_id",
         "source_text_doc",
         "source_edi",
         "source_sourceid",
         "target_text_doc",
         "target_edi",
         "target_targetid",
         "source_xml",
         "target_xml",
         "natureofsource",
         "natureoftarget",
         "insert_mode",
         "target_dtd_url",
         "source_edi_default",
         "target_edi_default",
         "isvalid",
         "sourcedtd_root",
         "targetdtd_root",
         "SourceJmsMessageId",
         "TargetJmsMessageId",
         "use_jbxmlparser",
         "DoSourceXmlValidation",
         "DoTargetXmlValidation",
         "TargetXmlSchemaLocation",
         "TruncateCharsInDbTarget",
         "include_informix_row_id_input",
         "include_informix_row_id_output",
         "SourceBatchSize",
         "SourceBatchNodePath",
         "NoSourceDataUsedInMapping",
         "XmlSourceIsEdi",
         "XmlTargetIsEdi",
         "salesforce_wizard_object",
         "salesforce_object",
         "saleforce_wizard_role",
         "salesforce_partner_wizard",
         "auto_generated_target_struct"
      };
   }

   public static boolean isPassThrough(Transformation var0) {
      return var0 != null && var0.getID().equals(TransformationId.PASS_THROUGH_ID);
   }

   public Transformation(String var1) {
      this(null, var1);
   }

   public Transformation(TransformationId var1, String var2) {
      super(EntityType.Transformation, var1, var2);
      this.inputStructureListener = new Transformation.StructureListener("Transformation#InputStructure");
      this.outputStructureListener = new Transformation.StructureListener("Transformation#OutputStructure");
      this.renamedNodes = Maps.newHashMap();
      this.setNoSourceDataUsedInMapping(true);
   }

   public final TransformationId getID() {
      return (TransformationId)super.getID();
   }

   public void setInputStructure(DataStructure var1) {
      DataStructure var2;
      synchronized (this.getDataLock()) {
         var2 = this.getInputStructure();
         if (var2 != null) {
            var2.removePropertyChangeListener(this.inputStructureListener);
         }

         DataStructureType var4 = var1.getStructureType();
         if (var4 == DataStructureType.XML) {
            this.inputStructure = null;
            XmlStructure var5 = (XmlStructure)var1;
            this.setProperty("source_xml", var5.getFilePath());
            this.setProperty("sourcedtd_root", var5.getRootNodeName());
            this.setProperty("DoSourceXmlValidation", var5.isValidationEnabled());
            this.setProperty("use_jbxmlparser", var5.isUsingJbXmlParser());
         } else if (var4 == DataStructureType.JSON) {
            this.inputStructure = null;
            JsonStructure var8 = (JsonStructure)var1;
            this.setProperty("source_xml", var8.getFileName());
            this.setProperty("sourcedtd_root", var8.getRootName());
            this.setProperty("DoSourceXmlValidation", false);
         } else if (var1 instanceof XsdBasedStructure) {
            this.inputStructure = var1;
            XsdBasedStructure var9 = (XsdBasedStructure)var1;
            this.setProperty("source_xml", var9.getXsdPath());
            this.setProperty("sourcedtd_root", var9.getRootName());
            this.setProperty("DoSourceXmlValidation", true);
         } else {
            this.inputStructure = var1;
            this.setProperty("source_xml", "");
            this.setProperty("sourcedtd_root", "");
            this.getData().putBoolean("XmlSourceIsEdi", false);
            if (var4 == DataStructureType.Database) {
               DatabaseStructure var10 = (DatabaseStructure)var1;
               this.getData().putBoolean("include_informix_row_id_input", var10.getIncludeRowIdInInformixTables());
            }

            var1.addPropertyChangeListener(this.inputStructureListener);
         }

         this.setSourceType(var4);
         this.setProperty("natureofsource", var1.getStructureNature());
      }

      this.firePropertyChange("Transformation#InputStructure", var2, var1);
   }

   @Override
   public DataStructure getInputStructure() {
      synchronized (this.getDataLock()) {
         DataStructureType var2 = this.getSourceType();
         if (var2 == null) {
            return null;
         }

         switch (var2) {
            case XML:
               XmlStructure var3 = new XmlStructure();
               var3.setFilePath(this.getProperty("source_xml"));
               var3.setRootNodeName(this.getProperty("sourcedtd_root"));
               var3.setValidationEnabled(this.getBooleanProperty("DoSourceXmlValidation", true));
               var3.setUseJbXmlParser(this.getBooleanProperty("use_jbxmlparser", false));
               return var3;
            case JSON:
               return new JsonStructure(this.getProperty("source_xml"), this.getProperty("sourcedtd_root"));
            default:
               return this.inputStructure;
         }
      }
   }

   public void setOutputStructure(DataStructure var1) {
      DataStructure var2;
      synchronized (this.getDataLock()) {
         var2 = this.getOutputStructure();
         if (var2 != null) {
            var2.removePropertyChangeListener(this.outputStructureListener);
         }

         DataStructureType var4 = var1.getStructureType();
         switch (var4) {
            case XML:
               XmlStructure var5 = (XmlStructure)var1;
               this.setProperty("target_xml", var5.getFilePath());
               this.setProperty("targetdtd_root", var5.getRootNodeName());
               this.setProperty("target_dtd_url", var5.getTargetDtdUrl());
               this.setProperty("DoTargetXmlValidation", var5.isValidationEnabled());
               this.setProperty("TargetXmlSchemaLocation", var5.getSchemaLocation());
               this.outputStructure = null;
               break;
            case JSON:
               JsonStructure var6 = (JsonStructure)var1;
               this.setProperty("target_xml", var6.getFileName());
               this.setProperty("targetdtd_root", var6.getRootName());
               this.setProperty("DoTargetXmlValidation", false);
               this.outputStructure = null;
               break;
            case Database:
               DatabaseStructure var7 = (DatabaseStructure)var1;
               this.getData().putBoolean("include_informix_row_id_output", var7.getIncludeRowIdInInformixTables());
               this.getData().putBoolean("TruncateCharsInDbTarget", var7.isTruncatingTargetFields());
            case Text:
            case ComplexText:
            case WebServiceRequest:
            case WebServiceResponse:
            case LDAP:
            case None:
               this.outputStructure = var1;
               this.setProperty("target_xml", "");
               this.setProperty("targetdtd_root", "");
               this.setProperty("target_dtd_url", "");
               this.getData().putBoolean("XmlTargetIsEdi", false);
               var1.addPropertyChangeListener(this.outputStructureListener);
               break;
            case ConnectorCallRequest:
            case ConnectorCallResponse:
               this.outputStructure = var1;
               XsdBasedStructure var8 = (XsdBasedStructure)var1;
               this.setProperty("target_xml", var8.getXsdPath());
               this.setProperty("targetdtd_root", var8.getRootName());
               this.setProperty("DoTargetXmlValidation", true);
               break;
            default:
               throw new IllegalArgumentException("Illegal target structure.");
         }

         this.setTargetType(var4);
         this.setProperty("natureoftarget", var1.getStructureNature());
      }

      this.firePropertyChange("Transformation#OutputStructure", var2, var1);
   }

   @Override
   public DataStructure getOutputStructure() {
      synchronized (this.getDataLock()) {
         DataStructureType var2 = this.getTargetType();
         if (var2 == null) {
            return null;
         }

         switch (var2) {
            case XML:
               XmlStructure var3 = new XmlStructure();
               var3.setFilePath(this.getProperty("target_xml"));
               var3.setRootNodeName(this.getProperty("targetdtd_root"));
               var3.setTargetDtdUrl(this.getProperty("target_dtd_url"));
               var3.setValidationEnabled(this.getBooleanProperty("DoTargetXmlValidation", true));
               var3.setSchemaLocation(this.getProperty("TargetXmlSchemaLocation", null));
               return var3;
            case JSON:
               return new JsonStructure(this.getProperty("target_xml"), this.getProperty("targetdtd_root"));
            default:
               return this.outputStructure;
         }
      }
   }

   private void setSourceType(DataStructureType var1) {
      this.setProperty("source_type_id", var1.getId());
   }

   public DataStructureType getSourceType() {
      String var1 = this.getProperty("source_type_id", "");

      try {
         return DataStructureType.fromId(Integer.parseInt(var1));
      } catch (NumberFormatException var3) {
         return null;
      }
   }

   private void setTargetType(DataStructureType var1) {
      this.setProperty("target_type_id", var1.getId());
   }

   public DataStructureType getTargetType() {
      String var1 = this.getProperty("target_type_id", "");

      try {
         return DataStructureType.fromId(Integer.parseInt(var1));
      } catch (NumberFormatException var3) {
         return null;
      }
   }

   public void setSourceTextDocumentId(TextDocumentId var1) {
      TextDocumentId var2 = this.getSourceTextDocumentId();
      if (!KongaID.areEqual(var1, var2)) {
         if (var1 == null) {
            this.setProperty("source_text_doc", "");
         } else {
            this.setProperty("source_text_doc", var1.toString());
         }

         this.firePropertyChange("source_text_doc", var2, var1);
      }
   }

   public TextDocumentId getSourceTextDocumentId() {
      String var1 = this.getProperty("source_text_doc", "");

      try {
         return new TextDocumentId(var1);
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }

   public void setTargetTextDocumentId(TextDocumentId var1) {
      TextDocumentId var2 = this.getTargetTextDocumentId();
      if (!KongaID.areEqual(var1, var2)) {
         if (var1 == null) {
            this.setProperty("target_text_doc", "");
         } else {
            this.setProperty("target_text_doc", var1.toString());
         }

         this.firePropertyChange("target_text_doc", var2, var1);
      }
   }

   public TextDocumentId getTargetTextDocumentId() {
      String var1 = this.getProperty("target_text_doc", "");

      try {
         return new TextDocumentId(var1);
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }

   public SourceId getSourceId() {
      String var1 = this.getProperty("source_sourceid", "");

      try {
         return new SourceId(var1);
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }

   public void setSourceId(SourceId var1) {
      SourceId var2 = this.getSourceId();
      if (!KongaID.areEqual(var1, var2)) {
         if (var1 == null) {
            this.setProperty("source_sourceid", "");
         } else {
            this.setProperty("source_sourceid", var1.toString());
         }

         this.firePropertyChange("source_sourceid", var2, var1);
      }
   }

   public TargetId getTargetId() {
      String var1 = this.getProperty("target_targetid", "");

      try {
         return new TargetId(var1);
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }

   public void setTargetId(TargetId var1) {
      TargetId var2 = this.getTargetId();
      if (!KongaID.areEqual(var1, var2)) {
         if (var1 == null) {
            this.setProperty("target_targetid", "");
         } else {
            this.setProperty("target_targetid", var1.toString());
         }

         this.firePropertyChange("target_targetid", var2, var1);
      }
   }

   public StructureNature getNatureOfSource() {
      DataStructure var1 = this.getInputStructure();
      return var1 == null ? null : var1.getStructureNature();
   }

   public StructureNature getNatureOfTarget() {
      DataStructure var1 = this.getOutputStructure();
      return var1 == null ? null : var1.getStructureNature();
   }

   public DbInsertMode getDbInsertMode() {
      int var1 = this.getIntProperty("insert_mode", -1);
      return EnumWithIdLookup.lookupOr(var1, DbInsertMode.INSERT_ONLY);
   }

   public void setDbInsertMode(DbInsertMode var1) {
      DbInsertMode var2 = this.getDbInsertMode();
      this.setProperty("insert_mode", var1.getId());
      if (var1 != var2) {
         this.firePropertyChange("insert_mode", var2, var1);
      }
   }

   public TransformationId getFunctionID() {
      return this.getID();
   }

   public final SourceBatchParams getSourceBatchParams() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("SourceBatchNodePath");
         int var3 = this.getIntProperty("SourceBatchSize", 0);
         return !StringUtils.isBlank(var2) && var3 > 0 ? new SourceBatchParams(var2, var3) : SourceBatchParams.NONE;
      }
   }

   public void setSourceBatchParams(SourceBatchParams var1) {
      if (var1 == null) {
         var1 = SourceBatchParams.NONE;
      }

      SourceBatchParams var2;
      synchronized (this.getDataLock()) {
         var2 = this.getSourceBatchParams();
         this.setProperty("SourceBatchNodePath", var1.getNodePath());
         this.setProperty("SourceBatchSize", var1.getBatchSize());
      }

      if (!var1.equals(var2)) {
         this.firePropertyChange("SourceBatchParams", var2, var1);
      }
   }

   public void setNoSourceDataUsedInMapping(boolean var1) {
      this.setBooleanProperty("NoSourceDataUsedInMapping", var1);
   }

   public boolean getNoSourceDataUsedInMapping() {
      return this.getBooleanProperty("NoSourceDataUsedInMapping", false);
   }

   public boolean isSourceDataUsedInMapping() {
      return !this.getNoSourceDataUsedInMapping();
   }

   public List<String> getMapList() {
      synchronized (this.getDataLock()) {
         return Lists.newArrayList(this.mappings);
      }
   }

   public List<Mapping> getMappings() {
      List var1 = this.getMapList();
      return Lists.newArrayList(Collections2.transform(var1, MapStringFormat.STRING_TO_MAPPING));
   }

   public void setMappings(List<? extends Mapping> var1) {
      Collection var2 = Collections2.transform(var1, MapStringFormat.MAPPING_TO_STRING);
      this.setMapList(Lists.newArrayList(var2));
   }

   public static String[] getMapListColumnNames() {
      return new String[]{"transformation_id", "target_field_id", "transformation"};
   }

   public List<String[]> getMapListValues() {
      List var1 = this.getMapList();
      ArrayList var2 = Lists.newArrayList();
      String var3 = this.getID().toString();

      for (Object __etlItem571 : var1) {
         String var5 = (String) __etlItem571;
         String[] var6 = KongaStringUtils.splitWithFirstTab(var5);
         if (var6.length != 2) {
            throw new IllegalStateException("The mapping \"" + var5 + "\" is illegal.");
         }

         String[] var7 = new String[]{var3, var6[0], var6[1]};
         var2.add(var7);
      }

      return var2;
   }

   public void setMapList(List<String> var1) {
      if (var1 == null) {
         var1 = Lists.newArrayList();
      }

      Set var2;
      synchronized (this.getDataLock()) {
         var2 = this.mappings;
         this.mappings = new LinkedHashSet<>(var1);
      }

      IntegrationProject var3 = ProjectUtils.getProject(this);
      if (var3 != null) {
         this.updateScriptedLinks(var3.getItemLookup());
      }

      this.firePropertyChange("Transformation#Mappings", var2, var1);
      this.scriptedLinksEventSupport.notifyListeners();
   }

   public void addMapping(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.mappings.add(var1);
         this.firePropertyChange("Transformation#Mappings", null, var1);
      }
   }

   @Override
   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      TransformationLinkCollector var2 = new TransformationLinkCollector(this, var1);
      if (this.inputStructure == null) {
         this.inputStructure = var2.getInputStructure();
      }

      if (this.outputStructure == null) {
         this.outputStructure = var2.getOutputStructure();
      }

      List var3 = var2.getLinks();
      if (this.scriptedLinks == null) {
         this.updateScriptedLinks(var1);
      }

      var3.addAll(this.scriptedLinks);
      return var3;
   }

   @Override
   public List<ScriptedLink> getScriptedLinks(IntegrationEntityLookup var1) {
      synchronized (this.getDataLock()) {
         if (this.scriptedLinks == null) {
            this.updateScriptedLinks(var1);
         }

         return Lists.newArrayList(this.scriptedLinks);
      }
   }

   @Override
   public void doPostImportProcessing(IntegrationEntityLookup var1) {
      this.rebuildScriptedLinks(var1);
   }

   private void rebuildScriptedLinks(IntegrationEntityLookup var1) {
      synchronized (this.getDataLock()) {
         this.scriptedLinks = null;
         this.getScriptedLinks(var1);
      }
   }

   @Override
   public boolean acceptsOutputFrom(Function var1) {
      if (var1 == null) {
         return false;
      }

      DataStructure var2 = var1.getOutputStructure();
      if (var2 == null) {
         return false;
      }

      DataStructure var3 = this.getInputStructure();
      return var3 == null ? false : var3.isCompatibleWith(var2);
   }

   @Override
   public boolean hasRequestTransformation() {
      return false;
   }

   public boolean canGetInputFrom(Source var1) {
      return new TransformationSourceValidator(this, var1).isValid();
   }

   public boolean canSendOutputTo(Target var1) {
      return new TransformationTargetValidator(this, var1).isValid();
   }

   @Override
   public StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = initializeStructureKeyValues(false);
      StringKeyValuePairs var2 = initializeStructureKeyValues(true);
      StringKeyValuePairs var3 = new StringKeyValuePairs();
      var3.putAll(var1);
      var3.putAll(var2);
      var3.putAll(super.getCopyOfKeyValues());
      var3.put("transformation_id", this.getID().toString());
      var3.put("transformation_name", this.getName());
      var3.put("source_type_id", Integer.toString(this.getSourceType().getId()));
      var3.put("target_type_id", Integer.toString(this.getTargetType().getId()));
      var3.put("isvalid", this.isValid() ? "1" : "0");
      return var3;
   }

   private static StringKeyValuePairs initializeStructureKeyValues(boolean var0) {
      StringKeyValuePairs var1 = new StringKeyValuePairs();
      if (var0) {
         var1.put("target_text_doc", "");
         var1.put("target_edi", "");
         var1.put("target_xml", "");
         var1.put("target_targetid", "");
         var1.put("target_text_doc", "");
         var1.put("natureoftarget", "");
         var1.put("target_edi_default", "-1");
         var1.put("targetdtd_root", "");
         var1.put("insert_mode", "-1");
         var1.put("target_dtd_url", "");
      } else {
         var1.put("source_text_doc", "");
         var1.put("source_edi", "");
         var1.put("source_xml", "");
         var1.put("source_sourceid", "");
         var1.put("source_text_doc", "");
         var1.put("natureofsource", "");
         var1.put("source_edi_default", "-1");
         var1.put("sourcedtd_root", "");
      }

      return var1;
   }

   private boolean isValid() {
      synchronized (this.getDataLock()) {
         List var2 = this.getInvalidMappings();
         return var2.isEmpty();
      }
   }

   public Transformation clone() {
      Transformation var1 = (Transformation)super.clone();
      if (this.inputStructure != null) {
         var1.inputStructure = (DataStructure)this.inputStructure.clone();
      }

      if (this.outputStructure != null) {
         var1.outputStructure = (DataStructure)this.outputStructure.clone();
      }

      var1.mappings = new LinkedHashSet<>(this.mappings);
      var1.invalidMappings = new LinkedHashSet<>(this.invalidMappings);
      var1.renamedNodes = Maps.newHashMap(this.renamedNodes);
      var1.scriptedLinks = null;
      var1.scriptedLinksEventSupport = new ScriptedLinkSourceEventSupport(var1);
      return var1;
   }

   public final boolean hasInvalidMappings() {
      synchronized (this.getDataLock()) {
         return !this.invalidMappings.isEmpty();
      }
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      new TransformationValidator(this, var1).validate();
   }

   public JmsMessageId getSourceJmsMessageId() {
      String var1 = this.getProperty("SourceJmsMessageId");
      if (var1 != null && var1.length() > 0) {
         try {
            return new JmsMessageId(var1);
         } catch (IllegalArgumentException var3) {
            var3.printStackTrace();
         }
      }

      return null;
   }

   public boolean isXmlTransformation() {
      DataStructureType var1 = this.getSourceType();
      if (var1 != null && var1.isXmlBased()) {
         return true;
      }

      DataStructureType var2 = this.getTargetType();
      return var2 != null && var2.isXmlBased();
   }

   public List<InvalidMapping> getInvalidMappings() {
      return Lists.newArrayList(this.invalidMappings);
   }

   public void setInvalidMappings(Collection<InvalidMapping> var1) {
      Set var2 = this.invalidMappings;
      this.invalidMappings = var1 != null ? new LinkedHashSet<>(var1) : new LinkedHashSet<>();
      this.firePropertyChange("InvalidMappings", var2, this.invalidMappings);
   }

   public void setRenamedNodes(Map<String, String> var1) {
      this.renamedNodes = Maps.newHashMap(var1);
   }

   public Map<String, String> getRenamedNodes() {
      return Maps.newHashMap(this.renamedNodes);
   }

   public ExpandedName getSalesforceObject() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("salesforce_object", null);
         return StringUtils.isNotEmpty(var2) ? XmlNameFactory.conforming().newExpandedName(var2) : null;
      }
   }

   public void setSalesforceObject(ExpandedName var1) {
      this.setStringProperty("salesforce_object", var1 != null ? var1.toString() : null);
   }

   public boolean isPartOfSalesforceWizard() {
      return SalesforceUtils.isSalesforceWizardObject(this);
   }

   public void setPartOfSalesforceWizard(boolean var1) {
      SalesforceUtils.setSalesforceWizardObject(this, var1);
      this.setProperty("salesforce_partner_wizard", var1 ? "1" : "0");
   }

   public void setSalesforceWizardRole(SalesforceWizardRole var1) {
      String var2 = var1 != null ? var1.toString() : null;
      this.setStringProperty("saleforce_wizard_role", var2);
   }

   public SalesforceWizardRole getSalesforceWizardRole() {
      Exception var1 = null;
      synchronized (this.getDataLock()) {
         String var3 = this.getProperty("saleforce_wizard_role");
         label28:
         if (StringUtils.isNotEmpty(var3)) {
            SalesforceWizardRole var10000;
            try {
               var10000 = SalesforceWizardRole.fromString(var3);
            } catch (Exception var6) {
               var1 = var6;
               break label28;
            }

            return var10000;
         }
      }

      if (var1 != null) {
         ErrorLog.log(
            this.getClass(),
            "Invalid value for the saleforce_wizard_role property in the Transformation " + this.getName() + " [GUID=" + this.getID() + "]",
            var1
         );
      }

      return null;
   }

   @Override
   protected void writeAdditionalData(Persistor var1) {
      this.writeStructures(var1);
      this.writeMappings(var1);
      this.writeInvalidMappings(var1);
      this.writeRenamedNodes(var1);
   }

   @Override
   protected void restoreAdditionalData(Persistor var1) {
      this.restoreMappings(var1);
      this.restoreInvalidMappings(var1);
      this.restoreRenamedNodes(var1);
   }

   @Override
   public void restoreDependentData(Persistor var1, IntegrationEntityLookup var2) {
      DataStructureType var3 = this.getSourceType();
      this.inputStructure = StructurePersistor.restoreInputStructure(this, var3, var1, var2);
      if (this.inputStructure != null) {
         this.inputStructure.addPropertyChangeListener(this.inputStructureListener);
      }

      DataStructureType var4 = this.getTargetType();
      this.outputStructure = StructurePersistor.restoreOutputStructure(this, var4, var1, var2);
      if (this.outputStructure != null) {
         this.outputStructure.addPropertyChangeListener(this.outputStructureListener);
      }
   }

   private void writeStructures(Persistor var1) {
      StructurePersistor.saveInputStructure(this, this.inputStructure, var1);
      StructurePersistor.saveOutputStructure(this, this.outputStructure, var1);
   }

   private void writeMappings(Persistor var1) {
      Persistor var2 = var1.createChild("Mappings");

      for (String var4 : this.mappings) {
         var2.createChild("Mapping").putText("expr", var4);
      }
   }

   private void writeInvalidMappings(Persistor var1) {
      Persistor var2 = var1.createChild("InvalidMappings");

      for (InvalidMapping var4 : this.invalidMappings) {
         InvalidMappingPersistor.save(var4, var2.createChild("Mapping"));
      }
   }

   private void writeRenamedNodes(Persistor var1) {
      Persistor var2 = var1.createChild("RenamedNodes");

      for (Entry var4 : this.renamedNodes.entrySet()) {
         Persistor var5 = var2.createChild("Node");
         var5.putString("key", (String)var4.getKey());
         var5.putString("value", (String)var4.getValue());
      }
   }

   private void restoreMappings(Persistor var1) {
      this.mappings = new LinkedHashSet<>();

      for (Persistor var3 : var1.getFirstChild("Mappings").getChildren("Mapping")) {
         this.mappings.add(var3.getText("expr"));
      }
   }

   private void restoreInvalidMappings(Persistor var1) {
      this.invalidMappings = new LinkedHashSet<>();

      for (Persistor var3 : var1.getFirstChild("InvalidMappings").getChildren("Mapping")) {
         this.invalidMappings.add(InvalidMappingPersistor.restoreFrom(var3));
      }
   }

   private void restoreRenamedNodes(Persistor var1) {
      this.renamedNodes = Maps.newHashMap();

      for (Persistor var3 : var1.getFirstChild("RenamedNodes").getChildren("Node")) {
         this.renamedNodes.put(var3.getString("key"), var3.getString("value"));
      }
   }

   private void updateScriptedLinks(IntegrationEntityLookup var1) {
      TransformationScriptLinkCollector var2 = new TransformationScriptLinkCollector(this, var1);
      this.scriptedLinks = var2.getLinks();
   }

   @Override
   public void importAdditionalDataFrom(IntegrationEntity var1) {
      Transformation var2 = (Transformation)var1;
      this.mappings = new LinkedHashSet<>(var2.mappings);
      this.invalidMappings = new LinkedHashSet<>(var2.invalidMappings);
      this.renamedNodes = new HashMap<>(var2.renamedNodes);
      this.scriptedLinks = null;
      if (this.inputStructure != null) {
         this.inputStructure.removePropertyChangeListener(this.inputStructureListener);
      }

      this.inputStructure = var2.inputStructure != null ? (DataStructure)var2.inputStructure.clone() : null;
      if (this.inputStructure != null) {
         this.inputStructure.addPropertyChangeListener(this.inputStructureListener);
      }

      if (this.outputStructure != null) {
         this.outputStructure.removePropertyChangeListener(this.outputStructureListener);
      }

      this.outputStructure = var2.outputStructure != null ? (DataStructure)var2.outputStructure.clone() : null;
      if (this.outputStructure != null) {
         this.outputStructure.addPropertyChangeListener(this.outputStructureListener);
      }
   }

   @Override
   public Version getRequiredVersion() {
      if (this.isUsingJson()) {
         return MINIMUM_VERSION_FOR_JSON;
      } else {
         return this.isUsingManualSqlStatement() ? MINIMUM_VERSION_FOR_MANUAL_SQL : super.getRequiredVersion();
      }
   }

   private boolean isUsingJson() {
      return this.getSourceType() == DataStructureType.JSON || this.getTargetType() == DataStructureType.JSON;
   }

   private boolean isUsingManualSqlStatement() {
      DataStructure var1 = this.getInputStructure();
      return var1 instanceof DatabaseStructure && ((DatabaseStructure)var1).hasManualSqlStatement();
   }

   @Override
   public void replaceReferences(ReferenceReplacementService var1) {
      synchronized (this.getDataLock()) {
         TransformationReferenceReplacer var3 = new TransformationReferenceReplacer(this, var1);
         var3.replaceReferences();
      }
   }

   public void setXmlSourceIsEdi(boolean var1) {
      synchronized (this.getDataLock()) {
         assert this.getInputStructure() instanceof XmlStructure;
         this.setBooleanProperty("XmlSourceIsEdi", var1);
      }
   }

   public boolean isXmlSourceEdi() {
      return this.getBooleanProperty("XmlSourceIsEdi", false);
   }

   public void setXmlTargetIsEdi(boolean var1) {
      synchronized (this.getDataLock()) {
         assert this.getOutputStructure() instanceof XmlStructure;
         this.setBooleanProperty("XmlTargetIsEdi", var1);
      }
   }

   public boolean isXmlTargetEdi() {
      return this.getBooleanProperty("XmlTargetIsEdi", false);
   }

   public MappingTreeNodePathFilter getInputMappingTreeFilter() {
      synchronized (this.getDataLock()) {
         return this.inputMappingTreeFilter == null ? MappingTreeNodePathFilter.TRANSPARENT : this.inputMappingTreeFilter;
      }
   }

   public void setInputMappingTreeFilter(MappingTreeNodePathFilter var1) {
      MappingTreeNodePathFilter var2;
      synchronized (this.getDataLock()) {
         var2 = this.inputMappingTreeFilter;
         this.inputMappingTreeFilter = var1;
      }

      this.firePropertyChange("InputNodeFilter", var2, this.inputMappingTreeFilter);
   }

   public MappingTreeNodePathFilter getOutputMappingTreeFilter() {
      synchronized (this.getDataLock()) {
         return this.outputMappingTreeFilter == null ? MappingTreeNodePathFilter.TRANSPARENT : this.outputMappingTreeFilter;
      }
   }

   public void setOutputMappingTreeFilter(MappingTreeNodePathFilter var1) {
      MappingTreeNodePathFilter var2;
      synchronized (this.getDataLock()) {
         var2 = this.outputMappingTreeFilter;
         this.outputMappingTreeFilter = var1;
      }

      this.firePropertyChange("OutputNodeFilter", var2, this.inputMappingTreeFilter);
   }

   public void setInputMappingStructure(MappingTreeStructure var1) {
      this.firePropertyChange("MappingStructures", null, Arrays2.of(var1, null));
   }

   public void setOutputMappingStructure(MappingTreeStructure var1) {
      this.firePropertyChange("MappingStructures", null, Arrays2.of(null, var1));
   }

   public void setMappingStructures(MappingTreeStructure var1, MappingTreeStructure var2) {
      this.firePropertyChange("MappingStructures", null, new MappingTreeStructure[]{var1, var2});
   }

   @Override
   public boolean isDeepCopyable() {
      return !this.isPartOfSalesforcePartnerWizard();
   }

   @Override
   public boolean isCopyable() {
      return !this.isPartOfSalesforcePartnerWizard();
   }

   public boolean isPartOfSalesforcePartnerWizard() {
      synchronized (this.getDataLock()) {
         return this.getBooleanProperty("salesforce_partner_wizard", false);
      }
   }

   public boolean hasTargetHeader() {
      return this.getTargetHeader().isPresent();
   }

   public Optional<String> getTargetHeader() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("target_header", null);
         return StringUtils.isEmpty(var2) ? Optional.absent() : Optional.of(var2);
      }
   }

   public void setTargetHeader(String var1) {
      this.setStringProperty("target_header", var1);
   }

   public boolean isTargetStructureAutoGenerated() {
      return this.getBooleanProperty("auto_generated_target_struct", false);
   }

   public void setTargetStructureAutoGenerated(boolean var1) {
      this.setBooleanProperty("auto_generated_target_struct", var1, false);
   }

   public boolean isSourceFlat() {
      DataStructure var1 = this.getInputStructure();
      return var1.isFlat();
   }

   public boolean isTargetFlat() {
      DataStructure var1 = this.getOutputStructure();
      return var1.isFlat();
   }

   @Override
   public void addScriptedLinkSourceListener(ScriptedLinkSourceListener var1) {
      this.scriptedLinksEventSupport.addListener(var1);
   }

   @Override
   public void removeScriptedLinkSourceListener(ScriptedLinkSourceListener var1) {
      this.scriptedLinksEventSupport.removeListener(var1);
   }

   private final class StructureListener implements PropertyChangeListener {
      private final String name;

      public StructureListener(String var2) {
         this.name = var2;
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         Transformation.this.firePropertyChange(this.name, "0", "1");
      }
   }
}
