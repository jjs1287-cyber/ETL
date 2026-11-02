package org.etlforge.integration.server.implementation.webservice.interchange.ldap;

import java.io.IOException;
import java.rmi.RemoteException;
import org.apache.axis.AxisFault;
import org.etlforge.integration.common.server.ServerErrors;
import org.etlforge.integration.ldap.structure.ClassStructureXmlPersistor;
import org.etlforge.integration.ldap.structure.LdapFunctors;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.ServerUiBackendLog;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.LocationPasswordLookup;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;
import org.etlforge.integration.server.implementation.webservice.interchange.db.Konga_jdbc_infoproviderSoapBindingImpl;
import org.etlforge.integration.server.ldap.LdapConnectionInfo;
import org.etlforge.integration.server.ldap.LdapException;
import org.etlforge.integration.server.ldap.infoprovider.ClassStructureRetriever;
import org.etlforge.integration.server.ldap.infoprovider.ObjectClassDefinitionRetriever;
import org.etlforge.integration.server.ws.WsUtils;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.lang.Arrays2;
import org.etlforge.util.zip.ZipUtils;

public class Konga_ldap_infoproviderSoapBindingImpl implements LdapInfoProvider {
   @Override
   public WsObjectClassInfoReturn getObjectClasses(
      String var1, String var2, String var3, String var4, WsLdapConnectionParams var5, String var6, boolean var7, boolean var8, WsKeyValuePairs var9
   ) throws RemoteException {
      ExternalApiSupport var10 = null;
      LdapConnectionInfo var11 = null;

      try {
         ServerUiBackendLog.LOG.info("Entering LdapInfoProvider.getObjectClasses()");
         logConnectionParameters(var5);
         var10 = ExternalApiSupport.forCall(var1, var2);
         var11 = createConnectionInfo(var10, var5, var3, var4);
         ServerUiBackendLog.LOG.info("Connection info: " + var11);

         try {
            ObjectClassDefinitionRetriever var12 = new ObjectClassDefinitionRetriever();
            ObjectClassDefinition[] var13 = var12.lookupObjectClassDefinitions(var11, var8, var7);
            return this.convert(var13);
         } catch (LdapException var19) {
            convert(var19, var11, "getObjectClasses");
            return null;
         }
      } catch (Exception var20) {
         convert(var20, var11, "getObjectClasses");
         return null;
      } finally {
         ExternalApiSupport.safeDispose(var10);
      }
   }

   @Override
   public WsClassStructureReturn getObjectStructures(
      String var1, String var2, String var3, String var4, WsLdapConnectionParams var5, String[] var6, boolean var7, WsKeyValuePairs var8
   ) throws RemoteException {
      ExternalApiSupport var9 = null;
      LdapConnectionInfo var10 = null;

      try {
         ServerUiBackendLog.LOG.info("Entering LdapInfoProvider.getObjectStructures()");
         logConnectionParameters(var5);
         var9 = ExternalApiSupport.forCall(var1, var2);
         var10 = createConnectionInfo(var9, var5, var3, var4);
         ServerUiBackendLog.LOG.info("Connection info: " + var10);

         try {
            ClassStructureRetriever var11 = new ClassStructureRetriever();
            NumericOid[] var12 = this.translateOids(var6);
            ObjectClassStructure[] var13 = var11.lookupStructures(var10, var12, var7);
            return this.convert(var13);
         } catch (LdapException var19) {
            convert(var19, var10, "getObjectStructures");
            return null;
         }
      } catch (Exception var20) {
         convert(var20, var10, "getObjectClasses");
         return null;
      } finally {
         ExternalApiSupport.safeDispose(var9);
      }
   }

   private static LdapConnectionInfo createConnectionInfo(ExternalApiSupport var0, WsLdapConnectionParams var1, String var2, String var3) throws ServerAccessException, RemoteException {
      LdapConnectionInfoFactory var4 = new LdapConnectionInfoFactory();
      LocationPasswordLookup var5 = new LocationPasswordLookup(var0.getTranDbConnection());
      var5.enableAccessControl(var0.getUser(), var0.getAccessController());
      var4.setLocationPasswordLookup(var5);
      return var4.createConnectionInfo(var1, var2, var3);
   }

   private NumericOid[] translateOids(String[] var1) {
      return Arrays2.transform(var1, NumericOid.class, LdapFunctors.STRING_TO_OID);
   }

   private WsObjectClassInfoReturn convert(ObjectClassDefinition[] var1) {
      WsObjectClassInfo[] var2 = new WsObjectClassInfo[var1.length];
      int var3 = 0;

      for (int var4 = var1.length; var3 < var4; var3++) {
         ObjectClassDefinition var5 = var1[var3];
         var2[var3] = new WsObjectClassInfo(var5.getOid().toString(), var5.getName(), var5.getType().name(), var5.getDescription(), var5.isObsolete());
      }

      return new WsObjectClassInfoReturn(var2);
   }

   private WsClassStructureReturn convert(ObjectClassStructure[] var1) throws RemoteException {
      try {
         String[] var2 = new String[var1.length];
         ClassStructureXmlPersistor var3 = new ClassStructureXmlPersistor();
         int var4 = 0;

         for (int var5 = var1.length; var4 < var5; var4++) {
            var2[var4] = this.compress(var1[var4], var3);
         }

         return new WsClassStructureReturn(var2);
      } catch (IOException var6) {
         throw new RemoteException("An I/O error occurred", var6);
      }
   }

   private String compress(ObjectClassStructure var1, ClassStructureXmlPersistor var2) throws IOException {
      String var3 = var2.store(var1);
      return ZipUtils.deflateAndBase64EncodeString(var3);
   }

   private static void convert(LdapException var0, LdapConnectionInfo var1, String var2) throws RemoteException {
      try {
         StringBuilder var3 = new StringBuilder("An error occurred in " + var2 + ".");
         var3.append(" Reported error was: ").append(WsUtils.escapeExceptionMessageAndGetCause(var0));
         var3.append(". Connection parameters: ").append(var1.toStringWithoutPassword());
         WsUtils.appendStackTrace(var0, var3);
         RemoteException var4 = new RemoteException(var3.toString());
         ServerUiBackendLog.LOG.throwing(Konga_ldap_infoproviderSoapBindingImpl.class.getName(), var2, var4);
         throw var4;
      } catch (RuntimeException var5) {
         handleUnexpectedErrorFromErrorHandling(var0, var2);
         throw var5;
      }
   }

   private static void convert(Exception var0, LdapConnectionInfo var1, String var2) throws RemoteException {
      try {
         var0.printStackTrace();
         AxisFault var3 = ServerErrors.makeAxisFault(var0, var2);
         ServerUiBackendLog.LOG.throwing(Konga_jdbc_infoproviderSoapBindingImpl.class.getName(), var2, var3);
         throw var3;
      } catch (RuntimeException var4) {
         handleUnexpectedErrorFromErrorHandling(var4, var2);
         throw var4;
      }
   }

   private static void handleUnexpectedErrorFromErrorHandling(Exception var0, String var1) throws RemoteException {
      WsUtils.handleUnexpectedErrorFromErrorHandling(var0, Konga_ldap_infoproviderSoapBindingImpl.class, var1);
   }

   private static void logConnectionParameters(WsLdapConnectionParams var0) {
      StringBuilder var1 = new StringBuilder("Connection parameters");
      var1.append(KongaIoUtils.LINE_BREAK);
      var1.append("Server: ").append(var0.getServer()).append(KongaIoUtils.LINE_BREAK);
      var1.append("Port: ").append(var0.getPort()).append(KongaIoUtils.LINE_BREAK);
      var1.append("User: ").append(var0.getUser()).append(KongaIoUtils.LINE_BREAK);
      var1.append("Security level: ").append(var0.getSecurityLevel()).append(KongaIoUtils.LINE_BREAK);
      String var2 = var0.getPassword() != null ? "[*removed*]" : "[the password was not submitted - it will be looked up from deployed data]";
      var1.append("Password: ").append(var2).append(KongaIoUtils.LINE_BREAK);
      ServerUiBackendLog.LOG.info(var1.toString());
   }
}
