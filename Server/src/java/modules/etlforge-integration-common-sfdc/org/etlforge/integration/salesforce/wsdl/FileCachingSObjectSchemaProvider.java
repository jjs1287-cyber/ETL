package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.io.Folder;
import org.etlforge.system.ThreeValuedBool;
import org.etlforge.util.KongaSerializationUtils;
import org.etlforge.util.name.Nameables;

public final class FileCachingSObjectSchemaProvider implements CachingSObjectSchemaProvider {
   private final SObjectSchemaProvider impl;
   private final FileCachingSObjectSchemaProvider.FlatObjectListCache flatListCache;

   public FileCachingSObjectSchemaProvider(SObjectSchemaProvider var1, Folder var2) {
      this(var1, var2, true, FileCacheErrorReporter.STANDARD_ERR);
   }

   public FileCachingSObjectSchemaProvider(SObjectSchemaProvider var1, Folder var2, FileCacheErrorReporter var3) {
      this(var1, var2, true, var3);
   }

   public FileCachingSObjectSchemaProvider(SObjectSchemaProvider var1, Folder var2, boolean var3, FileCacheErrorReporter var4) {
      this.impl = Preconditions.checkNotNull(var1);
      this.flatListCache = new FileCachingSObjectSchemaProvider.FlatObjectListCache(var1, var2.getFile("objects.cache"), var4);
      if (var3) {
         this.flatListCache.delete();
      }
   }

   private static boolean couldBeTrue(ThreeValuedBool var0) {
      return !var0.isFalse();
   }

   @Override
   public synchronized List<SObjectSchema> listAllObjects() throws SalesforceException {
      return this.flatListCache.listObjects();
   }

   @Override
   public synchronized SObjectSchema describeObject(String var1) throws SalesforceException {
      return this.flatListCache.getSingleObject(var1);
   }

   @Override
   public List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException {
      return this.flatListCache.getMultipleObjects(var1);
   }

   @Override
   public synchronized void clearCache() {
      this.flatListCache.clear();
   }

   @Override
   public synchronized SObjectSchema refreshObjectDefinition(String var1) throws SalesforceException {
      return this.flatListCache.refreshObject(var1);
   }

   @Override
   public synchronized void close() {
      this.impl.close();
   }

   private abstract static class CacheBase {
      protected final File file;
      protected ThreeValuedBool fileIsValid = ThreeValuedBool.MAYBE;
      protected final FileCacheErrorReporter errorReporter;

      public CacheBase(File var1, FileCacheErrorReporter var2) {
         this.file = var1;
         this.errorReporter = var2;
      }

      protected final void reportError(Exception var1, String var2) {
         this.errorReporter.report(var1, var2);
      }

      protected final FileCachingSObjectSchemaProvider.ObjectMap deserializeObjectMap(File var1, String var2) {
         try {
            FileCachingSObjectSchemaProvider.ObjectMap var3 = KongaSerializationUtils.deserializeFromFile(
               var1, FileCachingSObjectSchemaProvider.ObjectMap.class
            );
            if (!var3.isEmpty()) {
               var3.setErrorReporter(this.errorReporter);
               this.fileIsValid = ThreeValuedBool.TRUE;
               return var3;
            }
         } catch (Exception var4) {
            this.reportError(var4, "An error occurred reading the cache of " + var2);
         }

         this.fileIsValid = ThreeValuedBool.FALSE;
         return null;
      }
   }

   private static class FlatObjectListCache extends FileCachingSObjectSchemaProvider.CacheBase {
      private final SObjectSchemaProvider impl;
      private FileCachingSObjectSchemaProvider.ObjectMap knownObjects;

      public FlatObjectListCache(SObjectSchemaProvider var1, File var2, FileCacheErrorReporter var3) {
         super(var2, var3);
         this.impl = Preconditions.checkNotNull(var1);
      }

      public List<SObjectSchema> listObjects() throws SalesforceException {
         if (this.knownObjects == null && this.file.canRead() && FileCachingSObjectSchemaProvider.couldBeTrue(this.fileIsValid)) {
            this.knownObjects = this.readFlatObjectListFromFile();
         }

         if (this.knownObjects == null || this.knownObjects.isIncomplete()) {
            this.knownObjects = this.getListOfFlatObjectsFromProviderAndCacheIt();
         }

         return this.knownObjects.getAll();
      }

      private FileCachingSObjectSchemaProvider.ObjectMap readFlatObjectListFromFile() {
         return this.deserializeObjectMap(this.file, "flat SObjects");
      }

      private FileCachingSObjectSchemaProvider.ObjectMap getListOfFlatObjectsFromProviderAndCacheIt() throws SalesforceException {
         FileCachingSObjectSchemaProvider.ObjectMap var1 = new FileCachingSObjectSchemaProvider.ObjectMap(this.errorReporter);

         for (SObjectSchema var3 : this.impl.listAllObjects()) {
            var1.add(var3);
         }

         var1.setComplete(true);
         this.fileIsValid = var1.writeToDisk(this.file);
         return var1;
      }

      private void loadCachedObjects() {
         if (this.knownObjects == null && this.file.canRead() && FileCachingSObjectSchemaProvider.couldBeTrue(this.fileIsValid)) {
            this.knownObjects = this.readFlatObjectListFromFile();
         }
      }

      public SObjectSchema getSingleObject(String var1) throws SalesforceException {
         SObjectSchema var2 = this.lookForCachedObjectDefinition(var1);
         if (var2 == null) {
            var2 = this.askForSingleObjectAndCacheIt(var1);
         }

         return var2;
      }

      private SObjectSchema lookForCachedObjectDefinition(String var1) {
         this.loadCachedObjects();
         return this.knownObjects == null ? null : this.knownObjects.get(var1);
      }

      private SObjectSchema askForSingleObjectAndCacheIt(String var1) throws SalesforceException {
         SObjectSchema var2 = this.impl.describeObject(var1);
         if (this.knownObjects == null) {
            this.knownObjects = new FileCachingSObjectSchemaProvider.ObjectMap(this.errorReporter);
         }

         this.knownObjects.add(var2);
         this.fileIsValid = this.knownObjects.writeToDisk(this.file);
         return var2;
      }

      public List<SObjectSchema> getMultipleObjects(List<String> var1) throws SalesforceException {
         SObjectSchema[] var2 = this.lookForCachedObjectDefinitions(var1);
         ArrayList var3 = Lists.newArrayList();
         int var4 = 0;

         for (int var5 = var1.size(); var4 < var5; var4++) {
            if (var2[var4] == null) {
               var3.add(var1.get(var4));
            }
         }

         if (!var3.isEmpty()) {
            Map var8 = this.askForMultipleObjectsAndCacheThem(var3);
            int var9 = 0;

            for (int var6 = var1.size(); var9 < var6; var9++) {
               if (var2[var9] == null) {
                  String var7 = (String)var1.get(var9);
                  var2[var9] = (SObjectSchema)var8.get(var7);
               }
            }
         }

         return Lists.newArrayList(var2);
      }

      private SObjectSchema[] lookForCachedObjectDefinitions(List<String> var1) {
         SObjectSchema[] var2 = new SObjectSchema[var1.size()];
         this.loadCachedObjects();
         if (this.knownObjects != null) {
            this.knownObjects.fill(var2, var1);
         }

         return var2;
      }

      private Map<String, SObjectSchema> askForMultipleObjectsAndCacheThem(List<String> var1) throws SalesforceException {
         List var2 = this.impl.describeObjects(var1);
         HashMap var3 = Nameables.toHashMap(var2);
         if (this.knownObjects == null) {
            this.knownObjects = new FileCachingSObjectSchemaProvider.ObjectMap(this.errorReporter);
         }

         this.knownObjects.addAll(var3);
         this.fileIsValid = this.knownObjects.writeToDisk(this.file);
         return var3;
      }

      public SObjectSchema refreshObject(String var1) throws SalesforceException {
         this.loadCachedObjects();
         if (this.knownObjects != null) {
            this.knownObjects.remove(var1);
         }

         return this.askForSingleObjectAndCacheIt(var1);
      }

      public void delete() {
         this.file.delete();
      }

      public void clear() {
         this.knownObjects = null;
         this.delete();
         this.fileIsValid = ThreeValuedBool.FALSE;
      }
   }

   private static class ObjectMap implements Serializable {
      private static final long serialVersionUID = -2069681787414487947L;
      private final TreeMap<String, SObjectSchema> map = new TreeMap<>();
      private boolean complete;
      private transient FileCacheErrorReporter errorReporter;

      public ObjectMap(FileCacheErrorReporter var1) {
         this.errorReporter = var1;
      }

      public void setErrorReporter(FileCacheErrorReporter var1) {
         this.errorReporter = Preconditions.checkNotNull(var1);
      }

      public void add(SObjectSchema var1) {
         this.map.put(var1.getName(), var1);
      }

      public void addAll(Map<String, SObjectSchema> var1) {
         this.map.putAll(var1);
      }

      public SObjectSchema get(String var1) {
         return this.map.get(var1);
      }

      public List<SObjectSchema> getAll() {
         return Lists.newArrayList(this.map.values());
      }

      public void fill(SObjectSchema[] var1, List<String> var2) {
         int var3 = 0;

         for (String var5 : var2) {
            var1[var3] = this.get(var5);
            var3++;
         }
      }

      public boolean isIncomplete() {
         return !this.complete;
      }

      public void setComplete(boolean var1) {
         this.complete = var1;
      }

      public ThreeValuedBool writeToDisk(File var1) {
         try {
            Folder.parentOf(var1).createOnDisk();
            KongaSerializationUtils.serializeToFile(this, var1);
            return ThreeValuedBool.TRUE;
         } catch (IOException var3) {
            this.errorReporter.report(var3, "Could not write the desribed SObjects to disk.");
            return ThreeValuedBool.FALSE;
         }
      }

      public boolean isEmpty() {
         return this.map.isEmpty();
      }

      public void remove(String var1) {
         this.map.remove(var1);
      }
   }
}
