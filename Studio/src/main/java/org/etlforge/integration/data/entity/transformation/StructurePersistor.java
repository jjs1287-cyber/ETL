package org.etlforge.integration.data.entity.transformation;

import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.WebServiceStructure;
import org.etlforge.integration.data.structure.XsdBasedStructure;
import org.etlforge.util.persist.Persistor;

public final class StructurePersistor {
   private static final String INPUT = "InputStructure";
   private static final String OUTPUT = "OutputStructure";
   private static final String DOC_ID = "docId";
   private static final String WSCALL_ID = "wsCallId";
   private static final String CONNECTOR_CALL_TYPE = "callType";
   private static final String CONNECTOR_CALL_ID = "callId";

   public static void saveInputStructure(Transformation var0, DataStructure var1, Persistor var2) {
      if (isApplicable(var1)) {
         DataStructureType var3 = var1.getStructureType();
         Persistor var4 = var2.createChild("InputStructure");
         if (var3.isTextBased()) {
            var4.putString("docId", var0.getSourceTextDocumentId().toString());
         } else if (var1 instanceof WebServiceStructure) {
            var4.putString("wsCallId", ((WebServiceStructure)var1).getFunctionCallId().toString());
         } else if (var1 instanceof XsdBasedStructure) {
            saveConnectorCallStructure(var4, (XsdBasedStructure)var1);
         } else {
            var1.writeTo(var4);
         }
      }
   }

   private static void saveConnectorCallStructure(Persistor var0, XsdBasedStructure var1) {
      IntegrationEntityId var2 = var1.getFunctionCallId();
      var0.putString("callId", var2.toString());
      var0.putString("callType", var2.getEntityType().name());
   }

   public static void saveOutputStructure(Transformation var0, DataStructure var1, Persistor var2) {
      if (isApplicable(var1)) {
         DataStructureType var3 = var1.getStructureType();
         Persistor var4 = var2.createChild("OutputStructure");
         if (var3.isTextBased()) {
            var4.putString("docId", var0.getTargetTextDocumentId().toString());
         } else if (var1 instanceof WebServiceStructure) {
            var4.putString("wsCallId", ((WebServiceStructure)var1).getFunctionCallId().toString());
         } else if (var1 instanceof XsdBasedStructure) {
            saveConnectorCallStructure(var4, (XsdBasedStructure)var1);
         } else {
            var1.writeTo(var4);
         }
      }
   }

   public static DataStructure restoreInputStructure(Transformation var0, DataStructureType var1, Persistor var2, IntegrationEntityLookup var3) {
      Persistor var4 = var2.getFirstChild("InputStructure");
      if (var4 == null) {
         return null;
      }

      DataStructure var5 = null;
      switch (var1) {
         case ComplexText:
         case Text:
            return lookupTextStructure(var4, var3);
         case WebServiceRequest:
            return lookupWebServiceCall(var4, var3).getInputStructure();
         case WebServiceResponse:
            return lookupWebServiceCall(var4, var3).getOutputStructure();
         case ConnectorCallRequest:
            return lookupConnectorCall(var4, var3).getInputStructure();
         case ConnectorCallResponse:
            return lookupConnectorCall(var4, var3).getOutputStructure();
         case None:
            var5 = new NoStructure();
            break;
         case Database:
            var5 = createDatabaseInputStructure(var0, var3);
            break;
         case LDAP:
            var5 = createLdapInputStructure(var0, var3);
            break;
         case JSON:
            return null;
         default:
            throw new RuntimeException("Unexpected structure type: " + var1);
      }

      var5.restoreFrom(var4);
      return var5;
   }

   public static DataStructure restoreOutputStructure(Transformation var0, DataStructureType var1, Persistor var2, IntegrationEntityLookup var3) {
      Persistor var4 = var2.getFirstChild("OutputStructure");
      if (var4 == null) {
         return null;
      }

      DataStructure var5 = null;
      switch (var1) {
         case ComplexText:
         case Text:
            return lookupTextStructure(var4, var3);
         case WebServiceRequest:
            return lookupWebServiceCall(var4, var3).getInputStructure();
         case WebServiceResponse:
            return lookupWebServiceCall(var4, var3).getOutputStructure();
         case ConnectorCallRequest:
            return lookupConnectorCall(var4, var3).getInputStructure();
         case ConnectorCallResponse:
            return lookupConnectorCall(var4, var3).getOutputStructure();
         case None:
            var5 = new NoStructure();
            break;
         case Database:
            var5 = createDatabaseOutputStructure(var0, var3);
            break;
         case LDAP:
            var5 = createLdapOutputStructure(var0, var3);
            break;
         default:
            throw new RuntimeException("Unexpected structure type: " + var1);
      }

      var5.restoreFrom(var4);
      return var5;
   }

   private static boolean isApplicable(DataStructure var0) {
      return var0 != null;
   }

   private static TextStructure lookupTextStructure(Persistor var0, IntegrationEntityLookup var1) {
      IntegrationEntityId var2 = null;

      try {
         var2 = new TextDocumentId(var0.getString("docId"));
      } catch (RuntimeException var4) {
         throw new RuntimeException("Invalid persistor: missing or invalid DOC_ID field");
      }

      TextDocument var3 = var1.getEntity(var2, TextDocument.class);
      if (var3 == null) {
         throw new RuntimeException("The TextDocument with ID " + var2 + " could not be found");
      } else {
         return var3.getDataStructure();
      }
   }

   private static WebServiceCall lookupWebServiceCall(Persistor var0, IntegrationEntityLookup var1) {
      IntegrationEntityId var2 = null;

      try {
         var2 = new WebServiceCallId(var0.getString("wsCallId"));
      } catch (RuntimeException var4) {
         throw new RuntimeException("Invalid persistor: missing or invalid WSCALL_ID field");
      }

      WebServiceCall var3 = var1.getEntity(var2, WebServiceCall.class);
      if (var3 == null) {
         throw new RuntimeException("The WebServiceCall with ID " + var2 + " could not be found");
      } else {
         return var3;
      }
   }

   private static ConnectorCall lookupConnectorCall(Persistor var0, IntegrationEntityLookup var1) {
      IntegrationEntityId var2 = null;

      try {
         EntityType var3 = EntityType.valueOf(var0.getString("callType"));
         var2 = var3.getIntegrationId(var0.getString("callId"));
      } catch (Exception var4) {
         throw new RuntimeException("Invalid persistor: missing or invalid CONNECTOR_CALL_TYPE or CONNECTOR_CALL_ID field");
      }

      IntegrationEntity var6 = var1.getEntity(var2);
      if (var6 == null) {
         throw new RuntimeException("The ConnectorCall with ID " + var2 + " could not be found");
      } else {
         return (ConnectorCall)var6;
      }
   }

   private static DatabaseStructure createDatabaseInputStructure(Transformation var0, IntegrationEntityLookup var1) {
      return createDatabaseStructure(getSourceLocation(var0, var1));
   }

   private static DatabaseStructure createDatabaseOutputStructure(Transformation var0, IntegrationEntityLookup var1) {
      return createDatabaseStructure(getTargetLocation(var0, var1));
   }

   private static DatabaseStructure createDatabaseStructure(DataLocation var0) {
      if (!(var0 instanceof DatabaseLocation)) {
         throw new RuntimeException("Unexpected source type: Expected Database, found " + var0.getDataLocationType());
      } else {
         return new DatabaseStructure((DatabaseLocation)var0);
      }
   }

   private static LdapStructure createLdapInputStructure(Transformation var0, IntegrationEntityLookup var1) {
      return createLdapStructure(getSourceLocation(var0, var1));
   }

   private static LdapStructure createLdapOutputStructure(Transformation var0, IntegrationEntityLookup var1) {
      return createLdapStructure(getTargetLocation(var0, var1));
   }

   private static LdapStructure createLdapStructure(DataLocation var0) {
      if (!(var0 instanceof LdapLocation)) {
         throw new RuntimeException("Unexpected source type: Expected LDAP, found " + var0.getDataLocationType());
      } else {
         return new LdapStructure((LdapLocation)var0);
      }
   }

   private static DataLocation getSourceLocation(Transformation var0, IntegrationEntityLookup var1) {
      Source var2 = var1.getEntity(var0.getSourceId(), Source.class);
      if (var2 == null) {
         throw new RuntimeException("Source not found: " + var0.getSourceId());
      } else {
         return var2.getLocation();
      }
   }

   private static DataLocation getTargetLocation(Transformation var0, IntegrationEntityLookup var1) {
      Target var2 = var1.getEntity(var0.getTargetId(), Target.class);
      if (var2 == null) {
         throw new RuntimeException("Target not found: " + var0.getTargetId());
      } else {
         return var2.getLocation();
      }
   }

   private StructurePersistor() {
   }
}
