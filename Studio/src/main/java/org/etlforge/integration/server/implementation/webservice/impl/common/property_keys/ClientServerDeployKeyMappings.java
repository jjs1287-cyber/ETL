package org.etlforge.integration.server.implementation.webservice.impl.common.property_keys;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.xml.Elements;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class ClientServerDeployKeyMappings {
   private Map<EntityType, Map<String, ServerPropertyKey>> clientServerKeyMappings;
   private Map<EntityType, Map<String, ServerPropertyKey>> serverClientKeyMappings;
   private static ClientServerDeployKeyMappings cachedClientServerKeyMappings = new ClientServerDeployKeyMappings();
   private EntityType currentEntityType;
   private Map<String, ServerPropertyKey> currentClientServerKeyMappings;
   private Map<String, ServerPropertyKey> currentServerClientKeyMappings;

   private ClientServerDeployKeyMappings() {
   }

   public static DeployKeyMappings getClientServerKeyMappings(EntityType var0) {
      return new DeployKeyMappings(cachedClientServerKeyMappings.getClientServerKeyMappingsImpl(var0));
   }

   public static DeployKeyMappings getServerClientKeyMappings(EntityType var0) {
      return new DeployKeyMappings(cachedClientServerKeyMappings.getServerClientKeyMappingsImpl(var0));
   }

   private Map<String, ServerPropertyKey> getClientServerKeyMappingsImpl(EntityType var1) {
      this.load();
      return this.clientServerKeyMappings == null ? null : this.clientServerKeyMappings.get(var1);
   }

   private Map<String, ServerPropertyKey> getServerClientKeyMappingsImpl(EntityType var1) {
      this.load();
      return this.serverClientKeyMappings == null ? null : this.serverClientKeyMappings.get(var1);
   }

   private synchronized void load() {
      if (this.clientServerKeyMappings == null || this.serverClientKeyMappings == null) {
         this.clientServerKeyMappings = null;
         this.serverClientKeyMappings = null;

         try {
            this.clientServerKeyMappings = Maps.newHashMap();
            this.serverClientKeyMappings = Maps.newHashMap();
            Document var1 = DomParser.parseResource("Deploy.PropertyMappings.xml");

            for (Element var3 : Elements.under(var1.getDocumentElement())) {
               String var4 = var3.getAttribute("Name");
               if (Strings.isNullOrEmpty(var4)) {
                  throw new Exception("Missing Name attribute in IntegrationEntity node.");
               }

               this.currentEntityType = EntityType.valueOf(var4);
               this.currentClientServerKeyMappings = Maps.newHashMap();
               this.currentServerClientKeyMappings = Maps.newHashMap();

               for (Element var6 : Elements.under(var3)) {
                  this.processKey(var6);
               }

               this.clientServerKeyMappings.put(this.currentEntityType, this.currentClientServerKeyMappings);
               this.serverClientKeyMappings.put(this.currentEntityType, this.currentServerClientKeyMappings);
               this.currentEntityType = null;
               this.currentClientServerKeyMappings = null;
               this.currentServerClientKeyMappings = null;
            }
         } catch (Exception var7) {
            var7.printStackTrace();
            this.clientServerKeyMappings = null;
            this.serverClientKeyMappings = null;
            this.currentEntityType = null;
            this.currentClientServerKeyMappings = null;
            this.currentServerClientKeyMappings = null;
            ErrorLog.attention(
               this.getClass(),
               "Cannot deploy: the client-server deploy key mappings file is invalid. This is a bug. Please report it to ETLForge Support.",
               var7
            );
         }
      }
   }

   private void processKey(Element var1) {
      String var2 = var1.getAttribute("Client");
      if (var2 != null) {
         String var3 = var1.getAttribute("Server");
         String var4 = var1.getAttribute("Type");
         int var5 = 0;
         if (var4 != null && var4.length() > 0) {
            var5 = Integer.parseInt(var4);
         }

         ServerPropertyKeyType var6 = ServerPropertyKeyType.fromInt(var5);
         if (var6 == ServerPropertyKeyType.EXTENDED_WILDCARD || var3 != null) {
            ServerPropertyKey var7 = new ServerPropertyKey(var6, var3, var2);
            this.currentClientServerKeyMappings.put(var2, var7);
            this.currentServerClientKeyMappings.put(var3, var7);
         }
      }
   }

   public static void main(String[] var0) {
      DeployKeyMappings var1 = getClientServerKeyMappings(EntityType.SalesforceDelete);
      ServerPropertyKey var2 = var1.get("sfdc.soapheader.header1");
      System.out.println(var2);
   }
}
