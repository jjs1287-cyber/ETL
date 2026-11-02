package org.etlforge.integration.client.ui.interchange;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.MutableAppliedPipelinePluginStore;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.util.ServerComparators;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;

final class MissingPluginManifestFinder {
   private static final Set<IntegrationProjectId> checkedProjects = Collections.synchronizedSet(new HashSet<>());
   private final IntegrationProject project;
   private final ProjectPluginInfo projectPlugins;
   private final List<KnownServer> knownServers;
   private final PipelinePluginManifestServerCache serverCache;
   private Logger logger = Logger.getAnonymousLogger();

   public MissingPluginManifestFinder(IntegrationProject var1, ProjectPluginInfo var2, ServerManager var3, PipelinePluginManifestServerCache var4) {
      this.project = var1;
      this.projectPlugins = var2;
      this.serverCache = var4;
      this.knownServers = var3.getRegisteredServers();
      Collections.sort(this.knownServers, Collections.reverseOrder(ServerComparators.SORT_BY_LATEST_LOGIN));
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   public void run() {
      Application.getWorker().submitForParallel(new Runnable() {
         @Override
         public void run() {
            MissingPluginManifestFinder.this.runImpl();
         }
      });
   }

   private void runImpl() {
      try {
         if (!checkedProjects.add(this.project.getID())) {
            return;
         }

         this.logger.info("Looking for missing plugin manifests in the project " + this.project.getName());
         Set var1 = this.collectPluginNames();
         if (var1.isEmpty()) {
            return;
         }

         this.restoreMissingManifests(var1);
      } catch (Exception var2) {
         ErrorLog.internal(MissingPluginManifestFinder.class, "An error occurred while looking for missing plugin manifests.", var2);
      }
   }

   private Set<String> collectPluginNames() {
      HashSet var1 = Sets.newHashSet();
      this.collectPluginNames(Source.class, var1);
      this.collectPluginNames(Target.class, var1);
      this.collectPluginNames(WebServiceCall.class, var1);
      this.logger.info("The following plugins are used in the project: " + var1);
      return var1;
   }

   private void collectPluginNames(Class<? extends IntegrationEntity> var1, Set<String> var2) {
      MutableAppliedPipelinePluginStore var3 = this.projectPlugins.getAppliedPlugins();

      for (IntegrationEntity var5 : this.project.getAllEntitiesOfClass(var1)) {
         for (AppliedPipelinePlugin var9 : var3.getAllAssociations(var5)) {
            var2.add(var9.getPluginId().getName());
         }
      }
   }

   private void restoreMissingManifests(Set<String> var1) {
      PipelinePluginManifestCache var2 = this.projectPlugins.getManifestCache();

      for (String var4 : var1) {
         if (this.isMissing(var4)) {
            MissingPluginManifest var5 = new MissingPluginManifest(var4, var2);
            var5.setLogger(this.logger);
            var5.restore(this.serverCache, this.knownServers);
         }
      }
   }

   private boolean isMissing(String var1) {
      try {
         PipelinePluginManifestCache var2 = this.projectPlugins.getManifestCache();
         PipelinePluginManifest var3 = var2.getCachedManifest(var1);
         if (var3 != null) {
            return false;
         }
      } catch (DataCacheException var4) {
         this.logger.info("Error from cache: " + var4.getMessage());
         var4.printStackTrace();
      }

      return true;
   }
}
