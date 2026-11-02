package org.etlforge.integration.client.plugin.pipeline.cache;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.etlforge.application.cache.AbstractTextFileCache;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.plugin.pipeline.PipelinePluginEntitySupport;
import org.etlforge.integration.client.plugin.pipeline.xml.XmlToManifestTranslator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.plugin.PluginSpecificationException;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.io.Folder;

public final class FileBasedPipelinePluginManifestCache implements PipelinePluginManifestCache {
   private boolean doDiskRead = true;
   private final CacheContentFile contentFile;
   private final Map<String, PipelinePluginManifest> cachedManifests = Maps.newHashMap();
   private final AbstractTextFileCache delegate;
   private final IdGenerator idGenerator = IdGenerator.newGenerator();
   private final List<PipelinePluginManifestCacheListener> cacheListeners = Lists.newCopyOnWriteArrayList();

   public FileBasedPipelinePluginManifestCache(Folder var1) {
      this.delegate = new AbstractTextFileCache("PipelinePluginManifestCache", var1.asJavaFile(), false) {
         @Override
         protected String getFileName(Object var1) {
            return var1 + ".xml";
         }
      };
      this.contentFile = new CacheContentFile(var1.asJavaFile());
   }

   @Override
   public void add(List<PipelinePluginManifest> var1) throws DataCacheException {
      if (!var1.isEmpty()) {
         for (PipelinePluginManifest var3 : var1) {
            this.addManifest(var3);
         }

         this.tellListenersAboutNewManifests(var1);
      }
   }

   private void addManifest(PipelinePluginManifest var1) throws DataCacheException {
      Object var2 = this.getPluginId(var1);
      this.delegate.ensureParentFolderExists();
      this.delegate.store(var2, var1.getDefinition(), Charsets.UTF_8);
      this.cachedManifests.put(var1.getName(), var1);
      this.contentFile.add(var2);
   }

   private void tellListenersAboutNewManifests(List<PipelinePluginManifest> var1) {
      for (PipelinePluginManifestCacheListener var3 : this.cacheListeners) {
         var3.manifestsWereCached(this, var1);
      }
   }

   @Override
   public List<PipelinePosition> getPipelinePositions(EntityType var1) throws DataCacheException {
      ArrayList var2 = new ArrayList();

      for (PipelinePluginManifest var4 : this.getCachedManifests()) {
         for (PipelinePosition var8 : PipelinePluginEntitySupport.getApplicablePositions(var4, var1)) {
            var2.add(var8);
         }
      }

      return var2;
   }

   @Override
   public List<PipelinePluginManifest> getCachedManifests() throws DataCacheException {
      return this.doDiskRead ? this.readCachedManifestsFromDisk() : Lists.newArrayList(this.cachedManifests.values());
   }

   @Override
   public PipelinePluginManifest getCachedManifest(String var1) throws DataCacheException {
      if (this.doDiskRead) {
         this.readCachedManifestsFromDisk();
      }

      return this.cachedManifests.get(var1);
   }

   private List<PipelinePluginManifest> readCachedManifestsFromDisk() throws DataCacheException {
      try {
         this.delegate.ensureParentFolderExists();
         String[] var1 = this.contentFile.getAll();
         List var2 = this.readCachedManifestsFromDisk(var1);

         for (Object __etlItem97 : var2) {
            PipelinePluginManifest var4 = (PipelinePluginManifest) __etlItem97;
            this.cachedManifests.put(var4.getName(), var4);
         }

         this.doDiskRead = false;
         return var2;
      } catch (PluginSpecificationException var5) {
         throw new DataCacheException("Failed to read the cached plugin manifests. ", var5);
      }
   }

   private List<PipelinePluginManifest> readCachedManifestsFromDisk(Object[] var1) throws DataCacheException, PluginSpecificationException {
      HashSet var2 = Sets.newHashSet();
      XmlToManifestTranslator var3 = new XmlToManifestTranslator();

      for (String var5 : this.getXmlStrings(var1)) {
         PipelinePluginManifest[] var6 = var3.translate(var5);

         for (PipelinePluginManifest var10 : var6) {
            var2.add(var10);
         }
      }

      return Lists.newArrayList(var2);
   }

   private Iterable<String> getXmlStrings(Object[] var1) throws DataCacheException {
      HashSet var2 = new HashSet();

      for (Object var6 : var1) {
         String var7 = this.delegate.retrieve(var6, Charsets.UTF_8);
         if (var7 != null) {
            var2.add(var7);
         }
      }

      return var2;
   }

   @Override
   public void removeAll() {
      this.contentFile.clear();
      this.delegate.deleteAll();
      this.cachedManifests.clear();
      this.doDiskRead = false;
      this.fireCachedCleared();
   }

   private void fireCachedCleared() {
      for (PipelinePluginManifestCacheListener var2 : this.cacheListeners) {
         var2.cacheWasCleared(this);
      }
   }

   @Override
   public String getID() {
      return "PipelinePluginManifestCache";
   }

   @Override
   public void addCacheListener(PipelinePluginManifestCacheListener var1) {
      this.cacheListeners.add(var1);
   }

   @Override
   public void removeCacheListener(PipelinePluginManifestCacheListener var1) {
      this.cacheListeners.remove(var1);
   }

   private Object getPluginIdFromName(String var1) {
      return this.idGenerator.getIdFromPluginName(var1);
   }

   private Object getPluginId(PipelinePluginManifest var1) {
      return this.getPluginIdFromName(var1.getName());
   }
}
