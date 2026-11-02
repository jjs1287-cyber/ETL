package org.etlforge.integration.server.implementation.webservice.interchange.ldap.impl;

import java.rmi.RemoteException;
import java.util.zip.DataFormatException;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.mapping.serverapi.GetMappingStructureCallback;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetLdapStructuresCallback;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetObjectClassesCallback;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapClassSearchParameters;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapInfoProvider;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapStructureSearchParameters;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ETLForgeServerEncoding;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.LdapConnectionParameters;
import org.etlforge.integration.ldap.structure.ClassStructureXmlPersistor;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.LdapInfoProviderServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.WsClassStructureReturn;
import org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.WsKeyValuePairs;
import org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.WsLdapConnectionParams;
import org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.WsObjectClassInfo;
import org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.WsObjectClassInfoReturn;
import org.etlforge.lang.Arrays2;
import org.etlforge.util.net.Port;

public final class LdapInfoProviderWsImpl extends JavaWebServiceServerCall implements LdapInfoProvider {
   private static final String SERVICE_NAME = "konga_ldap_infoprovider";
   private String userName;
   private String password;

   public LdapInfoProviderWsImpl(JavaWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void getLdapMappingStructure(LdapStructure var1, boolean var2, GetMappingStructureCallback var3) {
      if (Thread.currentThread().isInterrupted()) {
         var3.cancelled();
      } else {
         MappingStructureBuilder var4 = var2 ? MappingStructureBuilder.newTargetStructureFactory() : MappingStructureBuilder.newSourceStructureFactory();
         var4.create(var1, var3);
      }
   }

   @Override
   public void getLdapStructures(LdapStructureSearchParameters var1, GetLdapStructuresCallback var2) {
      if (Thread.currentThread().isInterrupted()) {
         var2.cancelled();
      } else {
         try {
            org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.LdapInfoProvider var3 = this.getProvider(var2);
            if (var3 == null) {
               return;
            }

            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
               return;
            }

            WsClassStructureReturn var4 = this.getStructuresImpl(var1, var3);
            this.giveReturnedStructuresToCallback(var4, var2);
         } catch (RemoteException var5) {
            var2.caught(this.convert(var5));
         } catch (IntegrationServerException var6) {
            var2.caught(new IntegrationServerException("Server exception: " + var6.getMessage(), var6));
         }
      }
   }

   @Override
   public void getObjectClasses(LdapClassSearchParameters var1, GetObjectClassesCallback var2) {
      if (Thread.currentThread().isInterrupted()) {
         var2.cancelled();
      } else {
         try {
            org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.LdapInfoProvider var3 = this.getProvider(var2);
            if (var3 == null) {
               return;
            }

            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
               return;
            }

            WsObjectClassInfoReturn var4 = this.getObjectClassesImpl(var1, var3);
            this.giveReturnedClassesToCallback(var4, var2);
         } catch (RemoteException var5) {
            var2.caught(this.convert(var5));
         } catch (IntegrationServerException var6) {
            var2.caught(new IntegrationServerException("Server exception: " + var6.getMessage(), var6));
         }
      }
   }

   private WsObjectClassInfoReturn getObjectClassesImpl(
      LdapClassSearchParameters var1, org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.LdapInfoProvider var2
   ) throws RemoteException {
      String[] var3 = this.getSourceAndTargetId(var1.getSourceOrTargetId());
      WsLdapConnectionParams var4 = this.getConnectionParamsForWs(var1.getConnectionParameters());
      return var2.getObjectClasses(
         this.userName,
         this.password,
         var3[0],
         var3[1],
         var4,
         var1.getSearchString(),
         var1.skipObsoleteClasses(),
         var1.skipAbstractClasses(),
         new WsKeyValuePairs()
      );
   }

   private void giveReturnedClassesToCallback(WsObjectClassInfoReturn var1, GetObjectClassesCallback var2) {
      if (Thread.currentThread().isInterrupted()) {
         var2.cancelled();
      } else {
         WsObjectClassInfo[] var3 = var1.getReturnedClasses();
         ObjectClassDefinition[] var4 = new ObjectClassDefinition[var3.length];
         int var5 = 0;

         for (int var6 = var3.length; var5 < var6; var5++) {
            var4[var5] = new ObjectClassDefinitionImpl(var3[var5]);
         }

         if (Thread.currentThread().isInterrupted()) {
            var2.cancelled();
         } else {
            var2.objectClassesRetrieved(var4);
         }
      }
   }

   private WsClassStructureReturn getStructuresImpl(
      LdapStructureSearchParameters var1, org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.LdapInfoProvider var2
   ) throws RemoteException {
      String[] var3 = this.getSourceAndTargetId(var1.getSourceOrTargetId());
      WsLdapConnectionParams var4 = this.getConnectionParamsForWs(var1.getConnectionParameters());
      String[] var5 = this.getClassOidsAsStrings(var1);
      return var2.getObjectStructures(this.userName, this.password, var3[0], var3[1], var4, var5, var1.skipObsoleteAttributes(), new WsKeyValuePairs());
   }

   private String[] getClassOidsAsStrings(LdapStructureSearchParameters var1) {
      NumericOid[] var2 = var1.getObjectClasses();
      return Arrays2.stringify(var2);
   }

   private void giveReturnedStructuresToCallback(WsClassStructureReturn var1, GetLdapStructuresCallback var2) {
      if (Thread.currentThread().isInterrupted()) {
         var2.cancelled();
      } else {
         ClassStructureXmlPersistor var3 = new ClassStructureXmlPersistor();
         String[] var4 = var1.getCompressedStructureStrings();
         ObjectClassStructure[] var5 = new ObjectClassStructure[var4.length];
         int var6 = 0;

         for (int var7 = var4.length; var6 < var7; var6++) {
            try {
               String var8 = ETLForgeServerEncoding.base64DecodeAndInflateString(var4[var6]);
               ObjectClassStructure var9 = var3.load(var8);
               var5[var6] = var9;
            } catch (DataFormatException var10) {
               var2.caught(new IntegrationServerException("The data returned by the server is corrupt", var10));
            }
         }

         if (Thread.currentThread().isInterrupted()) {
            var2.cancelled();
         } else {
            var2.structuresRetrieved(var5);
         }
      }
   }

   private org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.LdapInfoProvider getProvider(ServerCallback var1) throws IntegrationServerException {
      try {
         LdapInfoProviderServiceLocator var2 = new LdapInfoProviderServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_ldap_infoprovider", var3, var1)) {
            return null;
         }

         this.userName = this.getConfiguration().getUserName();
         this.password = this.getConfiguration().getPasswordHash();
         var2.setkonga_ldap_infoproviderEndpointAddress(var3.toString());
         return var2.getkonga_ldap_infoprovider();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   private String[] getSourceAndTargetId(IntegrationEntityId var1) {
      String[] var2 = new String[2];
      if (var1 instanceof SourceId) {
         var2[0] = var1.toString();
      } else if (var1 instanceof TargetId) {
         var2[1] = var1.toString();
      }

      return var2;
   }

   private WsLdapConnectionParams getConnectionParamsForWs(LdapConnectionParameters var1) {
      WsLdapConnectionParams var2 = new WsLdapConnectionParams();
      var2.setServer(var1.serverName);
      Port var3 = var1.port;
      if (var3 != null) {
         var2.setPort(String.valueOf(var3.getPort()));
      }

      var2.setUser(var1.userName);
      var2.setPassword(var1.password);
      var2.setSecurityLevel(var1.securityLevel.getId());
      return var2;
   }
}
