package org.etlforge.integration.data.entity;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.HashSet;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.id.AutoDeskCreateId;
import org.etlforge.integration.data.entity.id.AutoDeskDeleteId;
import org.etlforge.integration.data.entity.id.AutoDeskEndpointId;
import org.etlforge.integration.data.entity.id.AutoDeskGetId;
import org.etlforge.integration.data.entity.id.AutoDeskUpdateId;
import org.etlforge.integration.data.entity.id.AutoDeskUpsertId;
import org.etlforge.integration.data.entity.id.EBSEndpointId;
import org.etlforge.integration.data.entity.id.EBSFunctionId;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.FolderId;
import org.etlforge.integration.data.entity.id.HttpEndpointId;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.entity.id.JmsMessageId;
import org.etlforge.integration.data.entity.id.MSAXEndpointId;
import org.etlforge.integration.data.entity.id.MSAXFunctionId;
import org.etlforge.integration.data.entity.id.MSCrmCreateId;
import org.etlforge.integration.data.entity.id.MSCrmDeleteId;
import org.etlforge.integration.data.entity.id.MSCrmEndpointId;
import org.etlforge.integration.data.entity.id.MSCrmQueryId;
import org.etlforge.integration.data.entity.id.MSCrmUpdateId;
import org.etlforge.integration.data.entity.id.MSCrmUpsertId;
import org.etlforge.integration.data.entity.id.MSGPCreateId;
import org.etlforge.integration.data.entity.id.MSGPDeleteId;
import org.etlforge.integration.data.entity.id.MSGPEndpointId;
import org.etlforge.integration.data.entity.id.MSGPUpdateId;
import org.etlforge.integration.data.entity.id.NetSuiteCreateId;
import org.etlforge.integration.data.entity.id.NetSuiteDeleteId;
import org.etlforge.integration.data.entity.id.NetSuiteEndpointId;
import org.etlforge.integration.data.entity.id.NetSuiteQueryId;
import org.etlforge.integration.data.entity.id.NetSuiteUpdateId;
import org.etlforge.integration.data.entity.id.NetSuiteUpsertId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.RootFolderId;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.entity.id.SalesforceCreateId;
import org.etlforge.integration.data.entity.id.SalesforceDeleteId;
import org.etlforge.integration.data.entity.id.SalesforceQueryId;
import org.etlforge.integration.data.entity.id.SalesforceUpdateId;
import org.etlforge.integration.data.entity.id.SalesforceUpsertId;
import org.etlforge.integration.data.entity.id.SapEndpointId;
import org.etlforge.integration.data.entity.id.SapFunctionId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.id.WebServiceCallId;

public enum EntityType {
   None(0, null, "Invalid", "-"),
   Project(20, "org.etlforge.integration.data.project.IntegrationProject", "Project", "Projects"),
   Operation(6, "org.etlforge.integration.data.entity.Operation", "Operation", "Operations", "&Operation"),
   Transformation(4, "org.etlforge.integration.data.entity.Transformation", "Transformation", "Transformations", "&Transformation"),
   Source(2, "org.etlforge.integration.data.entity.Source", "Source", "Sources", "&Source"),
   Target(3, "org.etlforge.integration.data.entity.Target", "Target", "Targets", "T&arget"),
   WebServiceCall(10, "org.etlforge.integration.data.entity.WebServiceCall", "Web Service Method", "Web Service Methods", "&Web Service Method"),
   Document(17, "org.etlforge.integration.data.entity.TextDocument", "File Format", "File Formats", "&File Format"),
   Schedule(5, "org.etlforge.integration.data.entity.Schedule", "Schedule", "Schedules", "Schedu&le"),
   EmailMessage(16, "org.etlforge.integration.data.entity.EmailMessage", "Email Message", "Email Messages", "&Email Message"),
   JmsMessage(22, "org.etlforge.integration.data.entity.JmsMessage", "JMS Message", "JMS Messages", "&JMS Message"),
   ProjectVariable(35, "org.etlforge.integration.data.entity.ProjectVariable", "Project Variable", "Project Variables", "Project &Variable"),
   XsltTransform(34, "org.etlforge.integration.data.entity.XsltTransform", "XSLT Transform", "XSLT Transforms", "&XSLT Transform"),
   Script(23, "org.etlforge.integration.data.entity.Script", "Script", "Scripts", "Sc&ript"),
   HttpEndpoint(24, "org.etlforge.integration.data.entity.HttpEndpoint", "Hosted HTTP Endpoint", "Hosted HTTP Endpoints", "Hosted &HTTP Endpoint"),
   SalesforceQuery(25, "org.etlforge.integration.data.entity.SalesforceQuery", "Salesforce Query", "Salesforce Queries", "Salesforce &Query"),
   SalesforceUpsert(26, "org.etlforge.integration.data.entity.SalesforceUpsert", "Salesforce Upsert", "Salesforce Upserts", "Salesforce &Upsert"),
   SalesforceCreate(27, "org.etlforge.integration.data.entity.SalesforceCreate", "Salesforce Insert", "Salesforce Inserts", "Salesforce &Insert"),
   SalesforceUpdate(28, "org.etlforge.integration.data.entity.SalesforceUpdate", "Salesforce Update", "Salesforce Updates", "Salesforce U&pdate"),
   SalesforceBulkLoad(
      29, "org.etlforge.integration.data.entity.SalesforceBulkLoad", "Salesforce Bulk Process", "Salesforce Bulk Processes", "Salesforce &Bulk Process"
   ),
   SalesforceDelete(30, "com.etlforge.integration.data.entity.SalesforceDelete", "Salesforce Delete", "Salesforce Deletes", "Salesforce &Delete"),
   SalesforceConnector(31, "org.etlforge.integration.data.entity.SalesforceConnector", "Salesforce Org", "Salesforce Orgs", "Salesforce Or&g"),
   DatabaseToDatabaseConnect(101, null, "Database To Database", "Database To Database"),
   DatabaseToTextConnect(102, null, "Database To Text", "Database To Text"),
   DatabaseToXmlConnect(103, null, "Database To XML", "Database To XML"),
   TextToDatabaseConnect(104, null, "Text To Database", "Text To Database"),
   XmlToDatabaseConnect(105, null, "XML To Database", "XML To Database"),
   SapEndpoint(201, "org.etlforge.integration.data.connector.SapEndpoint", "SAP Endpoint", "SAP Endpoints"),
   SapFunction(202, "org.etlforge.integration.data.connector.SapFunction", "SAP Function", "SAP Functions"),
   MSCrmEndpoint(203, "org.etlforge.integration.data.connector.MSCrmEndpoint", "MS CRM Endpoint", "MS CRM Endpoints"),
   MSCrmCreate(204, "org.etlforge.integration.data.connector.MSCrmCreate", "MS CRM Create", "MS CRM Creates") {
      @Override
      public int getServerId() {
         return MSCrmFunction.id;
      }
   },
   MSCrmUpdate(205, "org.etlforge.integration.data.connector.MSCrmUpdate", "MS CRM Update", "MS CRM Updates") {
      @Override
      public int getServerId() {
         return MSCrmFunction.id;
      }
   },
   MSCrmDelete(206, "org.etlforge.integration.data.connector.MSCrmDelete", "MS CRM Delete", "MS CRM Deletes") {
      @Override
      public int getServerId() {
         return MSCrmFunction.id;
      }
   },
   MSCrmQuery(207, "org.etlforge.integration.data.connector.MSCrmQuery", "MS CRM Query", "MS CRM Queries") {
      @Override
      public int getServerId() {
         return MSCrmFunction.id;
      }
   },
   MSCrmUpsert(209, "org.etlforge.integration.data.connector.MSCrmUpsert", "MS CRM Upsert", "MS CRM Upserts") {
      @Override
      public int getServerId() {
         return MSCrmFunction.id;
      }
   },
   MSCrmFunction(208, null, "MS CRM Function", "MS CRM Functions") {
      @Override
      public boolean isUmbrellaType() {
         return true;
      }

      @Override
      public ImmutableSet<EntityType> getEncompassedTypes() {
         return ImmutableSet.of(EntityType.MSCrmQuery, EntityType.MSCrmUpsert, EntityType.MSCrmCreate, EntityType.MSCrmUpdate, EntityType.MSCrmDelete);
      }
   },
   MSGPEndpoint(210, "org.etlforge.integration.data.connector.MSGPEndpoint", "MS GP Endpoint", "MS GP Endpoints"),
   MSGPFunction(211, null, "MS GP Function", "MS GP Functions") {
      @Override
      public boolean isUmbrellaType() {
         return true;
      }

      @Override
      public ImmutableSet<EntityType> getEncompassedTypes() {
         return ImmutableSet.of(EntityType.MSGPCreate, EntityType.MSGPUpdate, EntityType.MSGPDelete);
      }
   },
   MSGPCreate(212, "org.etlforge.integration.data.connector.MSGPCreate", "MS GP Create", "MS GP Creates") {
      @Override
      public int getServerId() {
         return MSGPFunction.id;
      }
   },
   MSGPUpdate(213, "org.etlforge.integration.data.connector.MSGPUpdate", "MS GP Update", "MS GP Updates") {
      @Override
      public int getServerId() {
         return MSGPFunction.id;
      }
   },
   MSGPDelete(214, "org.etlforge.integration.data.connector.MSGPDelete", "MS GP Delete", "MS GP Deletes") {
      @Override
      public int getServerId() {
         return MSGPFunction.id;
      }
   },
   AutoDeskEndpoint(215, "org.etlforge.integration.data.connector.AutoDeskEndpoint", "PLM 360 Endpoint", "PLM 360 Endpoints"),
   AutoDeskFunction(216, null, "AutoDesk Function", "AutoDesk Functions") {
      @Override
      public boolean isUmbrellaType() {
         return true;
      }

      @Override
      public ImmutableSet<EntityType> getEncompassedTypes() {
         return ImmutableSet.of(
            EntityType.AutoDeskGet, EntityType.AutoDeskCreate, EntityType.AutoDeskUpsert, EntityType.AutoDeskUpdate, EntityType.AutoDeskDelete
         );
      }
   },
   AutoDeskGet(217, "org.etlforge.integration.data.connector.AutoDeskGet", "PLM 360 Get", "PLM 360 Gets") {
      @Override
      public int getServerId() {
         return AutoDeskFunction.id;
      }
   },
   AutoDeskCreate(218, "org.etlforge.integration.data.connector.AutoDeskCreate", "PLM 360 Create", "PLM 360 Creates") {
      @Override
      public int getServerId() {
         return AutoDeskFunction.id;
      }
   },
   AutoDeskUpdate(219, "org.etlforge.integration.data.connector.AutoDeskUpdate", "PLM 360 Update", "PLM 360 Updates") {
      @Override
      public int getServerId() {
         return AutoDeskFunction.id;
      }
   },
   AutoDeskDelete(220, "org.etlforge.integration.data.connector.AutoDeskDelete", "PLM 360 Delete", "PLM 360 Deletes") {
      @Override
      public int getServerId() {
         return AutoDeskFunction.id;
      }
   },
   AutoDeskUpsert(227, "org.etlforge.integration.data.connector.AutoDeskUpsert", "PLM 360 Upsert", "PLM 360 Upserts") {
      @Override
      public int getServerId() {
         return AutoDeskFunction.id;
      }
   },
   MSAXEndpoint(221, "org.etlforge.integration.data.connector.MSAXEndpoint", "MS AX Endpoint", "MS AX Endpoints"),
   MSAXFunction(222, "org.etlforge.integration.data.connector.MSAXFunction", "MS AX Function", "MS AX Functions"),
   EBSEndpoint(225, "org.etlforge.integration.data.connector.EBSEndpoint", "EBS Endpoint", "EBS Endpoints"),
   EBSFunction(226, "org.etlforge.integration.data.connector.EBSFunction", "EBS Concurrency Program", "EBS Concurrency Programs"),
   NetSuiteEndpoint(228, "org.etlforge.integration.data.connector.NetSuiteEndpoint", "NetSuite Endpoint", "NetSuite Endpoints"),
   NetSuiteCreate(229, "org.etlforge.integration.data.connector.NetSuiteCreate", "NetSuite Create", "NetSuite Creates") {
      @Override
      public int getServerId() {
         return NetSuiteFunction.id;
      }
   },
   NetSuiteUpdate(230, "org.etlforge.integration.data.connector.NetSuiteUpdate", "NetSuite Update", "NetSuite Updates") {
      @Override
      public int getServerId() {
         return NetSuiteFunction.id;
      }
   },
   NetSuiteDelete(231, "org.etlforge.integration.data.connector.NetSuiteDelete", "NetSuite Delete", "NetSuite Deletes") {
      @Override
      public int getServerId() {
         return NetSuiteFunction.id;
      }
   },
   NetSuiteUpsert(232, "org.etlforge.integration.data.connector.NetSuiteUpsert", "NetSuite Upsert", "NetSuite Upserts") {
      @Override
      public int getServerId() {
         return NetSuiteFunction.id;
      }
   },
   NetSuiteQuery(233, "org.etlforge.integration.data.connector.NetSuiteQuery", "NetSuite Search", "NetSuite Searches") {
      @Override
      public int getServerId() {
         return NetSuiteFunction.id;
      }
   },
   NetSuiteFunction(234, null, "NetSuite Function", "NetSuite Functions") {
      @Override
      public boolean isUmbrellaType() {
         return true;
      }

      @Override
      public ImmutableSet<EntityType> getEncompassedTypes() {
         return ImmutableSet.of(
            EntityType.NetSuiteQuery, EntityType.NetSuiteCreate, EntityType.NetSuiteUpdate, EntityType.NetSuiteUpsert, EntityType.NetSuiteDelete
         );
      }
   },
   SystemSource(-1, null, "System Source", "System Sources"),
   SystemTarget(-2, null, "System Target", "System Targets"),
   Folder(1, "org.etlforge.integration.data.entity.Folder", "Folder", "Folders", "Folder"),
   RootFolder(-3, "org.etlforge.integration.data.entity.RootFolder", "Root Folder", "Root Folders");

   private static final ImmutableSet<Integer> RESERVED_IDS = ImmutableSet.of(7, 9, 12, 13, 14, 15, 18, 19, 21);
   private final int id;
   private final Optional<String> implementingClass;
   private final String displayName;
   private final String plural;
   private final String mnemonic;

   EntityType(int var3, String var4, String var5, String var6) {
      this(var3, var4, var5, var6, var5);
   }

   EntityType(int var3, String var4, String var5, String var6, String var7) {
      this.id = var3;
      this.implementingClass = Optional.fromNullable(var4);
      this.displayName = var5;
      this.plural = var6;
      this.mnemonic = var7;
   }

   public static EntityType fromId(int var0) {
      for (EntityType var4 : values()) {
         if (var4.id == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid ID (" + var0 + ").");
   }

   public int getId() {
      return this.id;
   }

   public int getServerId() {
      return this.id;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getPlural() {
      return this.plural;
   }

   public String getMnemonicString() {
      return this.mnemonic;
   }

   public boolean isWizardType() {
      switch (this) {
         case SalesforceQuery:
         case SalesforceUpsert:
         case SalesforceCreate:
         case SalesforceUpdate:
         case SalesforceBulkLoad:
         case SalesforceDelete:
         case DatabaseToDatabaseConnect:
         case DatabaseToTextConnect:
         case DatabaseToXmlConnect:
         case TextToDatabaseConnect:
         case XmlToDatabaseConnect:
            return true;
         default:
            return false;
      }
   }

   public IntegrationEntityId createIntegrationEntityId() {
      IntegrationEntityId var1 = this.createIntegrationEntityIdImpl(null);
      assert var1.getEntityType() == this;
      return var1;
   }

   public IntegrationEntityId getIntegrationId(String var1) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "stringRep cannot be null or empty.");
      IntegrationEntityId var2 = this.createIntegrationEntityIdImpl(var1);
      assert var2.getEntityType() == this;
      return var2;
   }

   private IntegrationEntityId createIntegrationEntityIdImpl(String var1) {
      switch (this) {
         case SalesforceQuery:
            return var1 == null ? new SalesforceQueryId() : new SalesforceQueryId(var1);
         case SalesforceUpsert:
            return var1 == null ? new SalesforceUpsertId() : new SalesforceUpsertId(var1);
         case SalesforceCreate:
            return var1 == null ? new SalesforceCreateId() : new SalesforceCreateId(var1);
         case SalesforceUpdate:
            return var1 == null ? new SalesforceUpdateId() : new SalesforceUpdateId(var1);
         case SalesforceBulkLoad:
            return var1 == null ? new SalesforceBulkLoadId() : new SalesforceBulkLoadId(var1);
         case SalesforceDelete:
            return var1 == null ? new SalesforceDeleteId() : new SalesforceDeleteId(var1);
         case DatabaseToDatabaseConnect:
         case DatabaseToTextConnect:
         case DatabaseToXmlConnect:
         case TextToDatabaseConnect:
         case XmlToDatabaseConnect:
            throw new UnsupportedOperationException(this.name() + " is used only as a marker. Instances are not meant to be created.");
         case None:
            return null;
         case NetSuiteEndpoint:
            return var1 == null ? new NetSuiteEndpointId() : new NetSuiteEndpointId(var1);
         case NetSuiteFunction:
            throw new UnsupportedOperationException("EntityType.NetSuiteFunction is used only as a marker. It cannot be instantiated.");
         case NetSuiteCreate:
            return var1 == null ? new NetSuiteCreateId() : new NetSuiteCreateId(var1);
         case NetSuiteUpdate:
            return var1 == null ? new NetSuiteUpdateId() : new NetSuiteUpdateId(var1);
         case NetSuiteUpsert:
            return var1 == null ? new NetSuiteUpsertId() : new NetSuiteUpsertId(var1);
         case NetSuiteDelete:
            return var1 == null ? new NetSuiteDeleteId() : new NetSuiteDeleteId(var1);
         case NetSuiteQuery:
            return var1 == null ? new NetSuiteQueryId() : new NetSuiteQueryId(var1);
         case EBSEndpoint:
            return var1 == null ? new EBSEndpointId() : new EBSEndpointId(var1);
         case EBSFunction:
            return var1 == null ? new EBSFunctionId() : new EBSFunctionId(var1);
         case AutoDeskEndpoint:
            return var1 == null ? new AutoDeskEndpointId() : new AutoDeskEndpointId(var1);
         case AutoDeskFunction:
            throw new UnsupportedOperationException("EntityType.AutoDeskFunction is used only as a marker. It cannot be instantiated.");
         case AutoDeskGet:
            return var1 == null ? new AutoDeskGetId() : new AutoDeskGetId(var1);
         case AutoDeskCreate:
            return var1 == null ? new AutoDeskCreateId() : new AutoDeskCreateId(var1);
         case AutoDeskUpsert:
            return var1 == null ? new AutoDeskUpsertId() : new AutoDeskUpsertId(var1);
         case AutoDeskUpdate:
            return var1 == null ? new AutoDeskUpdateId() : new AutoDeskUpdateId(var1);
         case AutoDeskDelete:
            return var1 == null ? new AutoDeskDeleteId() : new AutoDeskDeleteId(var1);
         case MSAXEndpoint:
            return var1 == null ? new MSAXEndpointId() : new MSAXEndpointId(var1);
         case MSAXFunction:
            return var1 == null ? new MSAXFunctionId() : new MSAXFunctionId(var1);
         case MSGPEndpoint:
            return var1 == null ? new MSGPEndpointId() : new MSGPEndpointId(var1);
         case MSGPFunction:
            throw new UnsupportedOperationException("EntityType.MSGPFunction is used only as a marker. It cannot be instantiated.");
         case MSGPCreate:
            return var1 == null ? new MSGPCreateId() : new MSGPCreateId(var1);
         case MSGPUpdate:
            return var1 == null ? new MSGPUpdateId() : new MSGPUpdateId(var1);
         case MSGPDelete:
            return var1 == null ? new MSGPDeleteId() : new MSGPDeleteId(var1);
         case MSCrmFunction:
            throw new UnsupportedOperationException("EntityType.MSCrmFunction is used only as a marker. It cannot be instantiated.");
         case MSCrmCreate:
            return var1 == null ? new MSCrmCreateId() : new MSCrmCreateId(var1);
         case MSCrmUpsert:
            return var1 == null ? new MSCrmUpsertId() : new MSCrmUpsertId(var1);
         case MSCrmUpdate:
            return var1 == null ? new MSCrmUpdateId() : new MSCrmUpdateId(var1);
         case MSCrmDelete:
            return var1 == null ? new MSCrmDeleteId() : new MSCrmDeleteId(var1);
         case MSCrmQuery:
            return var1 == null ? new MSCrmQueryId() : new MSCrmQueryId(var1);
         case MSCrmEndpoint:
            return var1 == null ? new MSCrmEndpointId() : new MSCrmEndpointId(var1);
         case Folder:
            return var1 == null ? new FolderId() : new FolderId(var1);
         case RootFolder:
            return var1 == null ? new RootFolderId() : new RootFolderId(var1);
         case Source:
         case SystemSource:
            return var1 == null ? new SourceId() : new SourceId(var1);
         case Target:
         case SystemTarget:
            return var1 == null ? new TargetId() : new TargetId(var1);
         case Transformation:
            return var1 == null ? new TransformationId() : new TransformationId(var1);
         case Schedule:
            return var1 == null ? new ScheduleId() : new ScheduleId(var1);
         case Operation:
            return var1 == null ? new OperationId() : new OperationId(var1);
         case WebServiceCall:
            return var1 == null ? new WebServiceCallId() : new WebServiceCallId(var1);
         case EmailMessage:
            return var1 == null ? new EmailMessageId() : new EmailMessageId(var1);
         case Document:
            return var1 == null ? new TextDocumentId() : new TextDocumentId(var1);
         case Project:
            return var1 == null ? new IntegrationProjectId() : new IntegrationProjectId(var1);
         case JmsMessage:
            return var1 == null ? new JmsMessageId() : new JmsMessageId(var1);
         case ProjectVariable: return var1 == null ? new org.etlforge.integration.data.entity.id.ProjectVariableId() : new org.etlforge.integration.data.entity.id.ProjectVariableId(var1);
         case XsltTransform: return var1 == null ? new org.etlforge.integration.data.entity.id.XsltTransformId() : new org.etlforge.integration.data.entity.id.XsltTransformId(var1);
         case Script: return var1 == null ? new ScriptId() : new ScriptId(var1);
         case HttpEndpoint:
            return var1 == null ? new HttpEndpointId() : new HttpEndpointId(var1);
         case SalesforceConnector:
            return var1 == null ? new SalesforceConnectorId() : new SalesforceConnectorId(var1);
         case SapEndpoint:
            return var1 == null ? new SapEndpointId() : new SapEndpointId(var1);
         case SapFunction:
            return var1 == null ? new SapFunctionId() : new SapFunctionId(var1);
         default:
            return null;
      }
   }

   public RootFolderId getRootFolderId() {
      switch (this) {
         case SalesforceQuery:
            return new RootFolderId("7c4267ae-4e2d-4ad7-bb09-79f18dcfd456");
         case SalesforceUpsert:
            return new RootFolderId("8f5db685-b459-4cd3-91c8-0843f48da60d");
         case SalesforceCreate:
            return new RootFolderId("78080c25-8fe5-4d79-9ff8-f974f923c4ef");
         case SalesforceUpdate:
            return new RootFolderId("fbc9c784-d8e0-4fef-80d0-7ed41a7c4b27");
         case SalesforceBulkLoad:
            return new RootFolderId("34ef5e67-57e2-4366-9636-fe47c69e1b03");
         case SalesforceDelete:
            return new RootFolderId("d152d0e9-84e9-4f42-b6d6-66a1caeb3647");
         case DatabaseToDatabaseConnect:
            return new RootFolderId("0b51bfc3-d71c-4743-b9b3-b80ddf2418c8");
         case DatabaseToTextConnect:
            return new RootFolderId("fc8ba1d7-697a-434b-93bb-3d9b3e0ddea0");
         case DatabaseToXmlConnect:
            return new RootFolderId("7de0ab15-4464-4f4e-8731-aa58db0b2218");
         case TextToDatabaseConnect:
            return new RootFolderId("7db4404d-5336-4b48-8654-bfa769943eee");
         case XmlToDatabaseConnect:
            return new RootFolderId("14185b92-7d8a-4b60-a0f3-6e2b8efcadc3");
         case None:
            return null;
         case NetSuiteEndpoint:
            return new RootFolderId("57cd1bea-82e5-477f-a7eb-5aa096485b83");
         case NetSuiteFunction:
            return new RootFolderId("e280b3a8-a96a-437c-82fc-08fecb102f26");
         case NetSuiteCreate:
            return new RootFolderId("b9a2a600-6693-499b-b7ac-ae32d407b8ca");
         case NetSuiteUpdate:
            return new RootFolderId("05b19e66-6b48-4a0a-85cd-791498c59709");
         case NetSuiteUpsert:
            return new RootFolderId("4b1b9b79-3ff1-4af0-a770-054e7bf44382");
         case NetSuiteDelete:
            return new RootFolderId("2e03ae53-7fe3-4826-82e1-605149266fe1");
         case NetSuiteQuery:
            return new RootFolderId("ff667c6c-09a7-4208-8652-a7037892b012");
         case EBSEndpoint:
            return new RootFolderId("9697450b-668d-4528-b5c4-de1b91340808");
         case EBSFunction:
            return new RootFolderId("4cf3abdf-cd1f-44a8-845d-d9cb2ce50b2a");
         case AutoDeskEndpoint:
            return new RootFolderId("7286e30f-fb35-414b-b463-dccfa40e62e9");
         case AutoDeskFunction:
            return new RootFolderId("3983fffd-0229-4374-b5a6-7f7153563817");
         case AutoDeskGet:
            return new RootFolderId("d620fbcf-1d6f-4b50-8a18-a41e33c706a0");
         case AutoDeskCreate:
            return new RootFolderId("2208b94a-8e6d-48da-ab7a-bc81c9c1298b");
         case AutoDeskUpsert:
            return new RootFolderId("c525cf06-c92e-44f1-95d5-7ee2eee86d3f");
         case AutoDeskUpdate:
            return new RootFolderId("b6c90f82-f526-41e6-a6e6-7e9c24d867f5");
         case AutoDeskDelete:
            return new RootFolderId("28ac908a-3e69-497b-a1ef-581efa65ad7b");
         case MSAXEndpoint:
            return new RootFolderId("a5a779db-5d0d-464f-b2ce-b95eac913400");
         case MSAXFunction:
            return new RootFolderId("d343ad8b-951f-49fc-a501-805c4e8771b4");
         case MSGPEndpoint:
            return new RootFolderId("a11c712c-c80a-4e64-9181-6550fcbdd773");
         case MSGPFunction:
            return new RootFolderId("f29e2caa-fc00-4f2e-a2f6-f4e95de6a55c");
         case MSGPCreate:
            return new RootFolderId("4c7c841a-7eb9-4082-861f-456ebe9c7a5a");
         case MSGPUpdate:
            return new RootFolderId("4abbf5af-145d-431b-a984-94f6a01bd5dc");
         case MSGPDelete:
            return new RootFolderId("a28f5022-e3b0-43f6-986d-d71ac80b47a2");
         case MSCrmFunction:
            return new RootFolderId("db181ed1-3be2-46c2-bb87-b06b68fe95ea");
         case MSCrmCreate:
            return new RootFolderId("c029916a-e2c5-4456-88a1-0311fc8e8b05");
         case MSCrmUpsert:
            return new RootFolderId("a0b85712-3c7b-4816-a608-fdc780cd14bd");
         case MSCrmUpdate:
            return new RootFolderId("a7f1b702-3ee9-407a-81cc-4f495bd5bcfd");
         case MSCrmDelete:
            return new RootFolderId("9c8075c2-4cab-4742-bd4b-cce3d6fb65e7");
         case MSCrmQuery:
            return new RootFolderId("f2e15a8e-5faa-4ac1-b514-d7327208debb");
         case MSCrmEndpoint:
            return new RootFolderId("405290ab-2296-4855-bf69-5e3482bf48e7");
         case Folder:
            return new RootFolderId("28bdab5d-e30d-4139-8db8-012b48af6f87");
         case RootFolder:
            return new RootFolderId("66cfdd47-f3dc-4d23-a730-09ed10929b75");
         case Source:
         case SystemSource:
            return new RootFolderId("444fe1fb-b55e-4d78-98a5-a8c8630b9e18");
         case Target:
         case SystemTarget:
            return new RootFolderId("da28e350-23b1-4d9b-9c47-3b1235d24d9d");
         case Transformation:
            return new RootFolderId("a4e5b9a6-b5c9-45d8-a6f0-28c7052c2948");
         case Schedule:
            return new RootFolderId("c09602fa-c4d3-4ff7-af66-b4711cb8a950");
         case Operation:
            return new RootFolderId("c61a6640-30cd-4a33-88cc-7c7445af79a7");
         case WebServiceCall:
            return new RootFolderId("287dbe69-a787-4cbe-8f0c-23654242ac33");
         case EmailMessage:
            return new RootFolderId("67490cfc-bf4b-4afc-a039-44dc8a82b7a3");
         case Document:
            return new RootFolderId("569879b1-abcf-4a3f-b677-58d09a800346");
         case Project:
         default:
            return null;
         case JmsMessage:
            return new RootFolderId("b93d8493-0a81-4277-b35c-b3e221c700ad");
         case ProjectVariable: return new RootFolderId("d0273500-e7f0-4000-8000-000000000035");
         case XsltTransform: return new RootFolderId("ba340129-305a-4dad-a948-e71c17e1b042");
         case Script: return new RootFolderId("05d186bd-faa9-4f44-85a2-6a2464476e1c");
         case HttpEndpoint:
            return new RootFolderId("08928c8d-7db2-48f8-8383-76229a6e6620");
         case SalesforceConnector:
            return new RootFolderId("ab7539c3-47ec-4569-981d-a45a64124434");
         case SapEndpoint:
            return new RootFolderId("6eabf97f-2133-4f39-a81c-ab24129523c8");
         case SapFunction:
            return new RootFolderId("baf8bc23-a39b-4d71-9522-ad0aa11a66c6");
      }
   }

   private static void sanityCheck() {
      ensureUniqueIDs();
      ensureUniqueRootFolderIDs();
   }

   private static void ensureUniqueIDs() throws AssertionError {
      HashSet var0 = Sets.newHashSet(RESERVED_IDS);

      for (EntityType var4 : values()) {
         if (!var0.add(var4.id)) {
            throw new RuntimeException("Duplicate EntityType ID: " + var4.id);
         }
      }
   }

   private static void ensureUniqueRootFolderIDs() {
      HashSet var0 = Sets.newHashSet();

      for (EntityType var4 : values()) {
         if (var4 != SystemSource && var4 != SystemTarget && var4 != MSCrmFunction) {
            RootFolderId var5 = var4.getRootFolderId();
            if (var5 != null && !var0.add(var5.toString())) {
               throw new RuntimeException("Duplicate EntityType RootFolder ID: " + var4);
            }
         }
      }
   }

   public Optional<String> getImplementingClassName() {
      return this.implementingClass;
   }

   public boolean canBeInstantiated() {
      return this.implementingClass.isPresent();
   }

   public String getIndefiniteArticle() {
      switch (this) {
         case Operation:
         case EmailMessage:
         case HttpEndpoint:
         case SapEndpoint:
         case SapFunction:
            return "an";
         case WebServiceCall:
         case Document:
         case Project:
         case JmsMessage:
         case Script:
         case SalesforceConnector:
         default:
            return "a";
      }
   }

   public boolean hasRootFolder() {
      return this != Project && this != RootFolder;
   }

   public boolean isFolder() {
      return this == Folder || this == Project;
   }

   public static ImmutableSet<EntityType> allTypes() {
      return ImmutableSet.copyOf(values());
   }

   public boolean isUmbrellaType() {
      return false;
   }

   public ImmutableSet<EntityType> getEncompassedTypes() {
      return ImmutableSet.of();
   }

   static {
      sanityCheck();
   }
}
