package org.etlforge.integration.server.implementation.webservice.pluginmanagement.impl;

import com.google.common.collect.Lists;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginInfoProvider;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginManifestDownloadCallback;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.server.implementation.webservice.pluginmanagement.client.FilterAsXml;
import org.etlforge.integration.server.implementation.webservice.pluginmanagement.client.PluginManager;
import org.etlforge.integration.server.implementation.webservice.pluginmanagement.client.PluginManagerServiceLocator;

public final class PluginManagementWsImpl extends CppWebServiceServerCall implements PipelinePluginInfoProvider {
   private static final String SERVICE_NAME = "konga_plugin_management";

   public PluginManagementWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void downloadPluginManifests(PipelinePluginManifestDownloadCallback var1) {
      PluginManagementWsImpl.Impl var2 = new PluginManagementWsImpl.Impl(this.getConfiguration());
      var2.downloadPluginManifests(var1);
   }

   private static class Impl {
      private final WebServiceConfiguration cfg;
      private String user;
      private String password;

      public Impl(WebServiceConfiguration var1) {
         this.cfg = var1;
      }

      public void downloadPluginManifests(PipelinePluginManifestDownloadCallback var1) {
         try {
            PluginManager var2 = this.getPluginManager(var1);
            if (var2 == null) {
               return;
            }

            ServerInfo var3 = this.cfg.getServerInfo();
            FilterAsXml[] var4 = var2.getFilters(this.user, this.password);
            int var5 = var4 == null ? 0 : var4.length;
            ArrayList var6 = Lists.newArrayList();
            if (var5 > 0 && var4 != null) {
               for (int var7 = 0; var7 < var5; var7++) {
                  if (var4[var7] != null) {
                     var6.add(new PluginAsXmlImpl(var4[var7]));
                  }
               }
            }

            var1.pluginInfoDownloaded(var3, (PluginAsXmlImpl[]) var6.toArray(new PluginAsXmlImpl[var6.size()]));
         } catch (RemoteException var8) {
            var1.caught(this.cfg.convertToIntegrationServerException(var8));
         } catch (IntegrationServerException var9) {
            var1.caught(new IntegrationServerException("Server exception: " + var9.getMessage(), var9));
         }
      }

      private PluginManager getPluginManager(ServerCallback var1) throws IntegrationServerException {
         PluginManagerServiceLocator var2 = new PluginManagerServiceLocator();

         try {
            StringBuilder var3 = new StringBuilder();
            if (!this.cfg.configureWebservice(var2, "konga_plugin_management", var3, var1)) {
               return null;
            }

            this.user = this.cfg.getUserName();
            this.password = this.cfg.getPasswordHash();
            var2.setkonga_plugin_managementEndpointAddress(var3.toString());
            return var2.getkonga_plugin_management();
         } catch (ServiceException var4) {
            throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
         }
      }
   }
}
