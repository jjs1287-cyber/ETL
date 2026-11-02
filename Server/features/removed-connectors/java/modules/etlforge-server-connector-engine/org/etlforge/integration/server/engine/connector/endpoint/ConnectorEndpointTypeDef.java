package org.etlforge.integration.server.engine.connector.endpoint;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerNotSupportedRuntimeException;

public final class ConnectorEndpointTypeDef {
   static final String SapSystem = "SAP";
   static final String MSCrmSystem = "MSCRM";
   static final String MSGPSystem = "MSGP";
   static final String AutoDeskSystem = "AUTODESK";
   static final String MSAXSystem = "MSAX";
   static final String EBSSystem = "EBS";
   static final String NetSuiteSystem = "NETSUITE";
   private static final ConnectorEndpointTypeDef def = new ConnectorEndpointTypeDef();
   private final Map<String, String> passwordSqlMap = Maps.newConcurrentMap();

   static boolean isSapSystem(String var0) {
      return "SAP".equals(var0);
   }

   static boolean isMSCrmSystem(String var0) {
      return "MSCRM".equals(var0);
   }

   static boolean isMSGPSystem(String var0) {
      return "MSGP".equals(var0);
   }

   static boolean isAutoDeskSystem(String var0) {
      return "AUTODESK".equals(var0);
   }

   static boolean isMSAXSystem(String var0) {
      return "MSAX".equals(var0);
   }

   static boolean isEBSSystem(String var0) {
      return "EBS".equals(var0);
   }

   static boolean isNetSuiteSystem(String var0) {
      return "NETSUITE".equals(var0);
   }

   private ConnectorEndpointTypeDef() {
      this.setPasswordSql();
   }

   private synchronized void setPasswordSql() {
      this.passwordSqlMap.clear();
      this.passwordSqlMap.put("SAP".toUpperCase(), "Select SapPassword from GuidsForSapEndpointsView");
      this.passwordSqlMap.put("MSCRM".toUpperCase(), "Select MSCrmPassword from GuidsForMSCrmEndpointsView");
      this.passwordSqlMap.put("MSAX".toUpperCase(), "Select MSAXPassword from GuidsForMSAXEndpointsView");
      this.passwordSqlMap.put("AUTODESK".toUpperCase(), "Select AutoDeskPassword from GuidsForAutoDeskEndpointsView");
      this.passwordSqlMap.put("EBS".toUpperCase(), "Select EbsPassword from GuidsForEbsEndpointsView");
      this.passwordSqlMap.put("NETSUITE".toUpperCase(), "Select NetSuitePassword from GuidsForNetSuiteEndpointsView");
   }

   private String getEndpointPasswordSqlMap(String var1) {
      return this.passwordSqlMap.get(var1);
   }

   public static String getEndpointPasswordSql(String var0) {
      String var1 = def.getEndpointPasswordSqlMap(var0);
      if (var1 == null) {
         if (isMSGPSystem(var0)) {
            return null;
         } else {
            throw new ConnectorServerNotSupportedRuntimeException("sql not defined to retrieve password for " + var0);
         }
      } else {
         return var1;
      }
   }

   public static String getMscrmSystem() {
      return "MSCRM";
   }
}
