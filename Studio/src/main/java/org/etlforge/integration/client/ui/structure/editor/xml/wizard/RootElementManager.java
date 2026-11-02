package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.UiJobManager;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.ui.util.UiUtils;

final class RootElementManager {
   private final XmlStructureWizardStartPage startPage;
   private final RootElementManager.RootElementCache rootCache;
   private GetRootsJob getRootsJob;

   public RootElementManager(XmlStructureWizardStartPage var1) {
      this.startPage = var1;
      this.rootCache = new RootElementManager.RootElementCache();
   }

   public Collection<String> getRoots(ServerFile var1) {
      return this.rootCache.getRoots(var1);
   }

   public boolean isCached(ServerFile var1) {
      return this.rootCache.isCached(var1);
   }

   public void updateRootCache(ServerFile var1, Collection<String> var2) {
      this.rootCache.updateRootCache(var1, var2);
   }

   public void retrieveRootNames(ServerFile var1) {
      UiJobManager var2 = ApplicationUi.getJobManager();
      if (this.getRootsJob != null) {
         var2.cancelJob(this.getRootsJob);
      }

      this.getRootsJob = new GetRootsJob(this.startPage, var1);
      this.getRootsJob.submit();
      UiUtils.setEnabledRecursively(this.startPage.getMainPanel(), false);

      try {
         this.getRootsJob.waitUntilDone();
      } catch (InterruptedException var7) {
         Thread.currentThread().interrupt();
         var7.printStackTrace();
      } finally {
         UiUtils.setEnabledRecursively(this.startPage.getMainPanel(), true);
      }
   }

   public void cancelCurrentJob() {
      UiJobManager var1 = ApplicationUi.getJobManager();
      if (this.getRootsJob != null) {
         var1.cancelJob(this.getRootsJob);
         this.getRootsJob = null;
      }
   }

   private static final class RootElementCache {
      private final Map<ServerFile, Set<String>> cache = Maps.newHashMap();

      private RootElementCache() {
      }

      public Collection<String> getRoots(ServerFile var1) {
         Set var2 = this.cache.get(var1);
         if (var2 == null) {
            var2 = Sets.newLinkedHashSet();
            this.cache.put(var1, var2);
         }

         return ImmutableSet.copyOf(var2);
      }

      public boolean isCached(ServerFile var1) {
         return this.cache.containsKey(var1);
      }

      public void updateRootCache(ServerFile var1, Collection<String> var2) {
         Set var3 = this.getUniqueRoots(var2);
         this.cache.put(var1, var3);
      }

      private Set<String> getUniqueRoots(Collection<String> var1) {
         LinkedHashSet var2 = Sets.newLinkedHashSet();
         if (var1 != null) {
            var2.addAll(var1);
         }

         return var2;
      }
   }
}
