package org.etlforge.integration.client.structure.cache;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.io.Folder;

public final class FileBasedInputAndOutputMappingTreeStructureCache implements InputAndOutputMappingTreeStructureCache {
   private final MappingTreeStructureCache inputCache;
   private final MappingTreeStructureCache outputCache;

   public FileBasedInputAndOutputMappingTreeStructureCache(String var1, Folder var2) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "cacheId may not be null or blank");
      Preconditions.checkNotNull(var2, "location");
      Folder var3 = var2.subFolder(var1);
      this.inputCache = this.createRequestCache(var3);
      this.outputCache = this.createResponseCache(var3);
   }

   @Override
   public void delete(IntegrationEntityId var1) {
      this.inputCache.delete(var1);
      this.outputCache.delete(var1);
   }

   @Override
   public Optional<MappingTreeStructure> getInputStructure(IntegrationEntityId var1) throws DataCacheException {
      return this.inputCache.retrieve(var1);
   }

   @Override
   public Optional<MappingTreeStructure> getOutputStructure(IntegrationEntityId var1) throws DataCacheException {
      return this.outputCache.retrieve(var1);
   }

   @Override
   public Writer getInputStructureWriter(IntegrationEntityId var1) throws IOException {
      return this.inputCache.getWriter(var1);
   }

   @Override
   public Writer getOutputStructureWriter(IntegrationEntityId var1) throws IOException {
      return this.outputCache.getWriter(var1);
   }

   @Override
   public void copy(IntegrationEntityId var1, IntegrationEntityId var2) throws DataCacheException {
      this.inputCache.copy(var1, var2);
      this.outputCache.copy(var1, var2);
   }

   private MappingTreeStructureCache createRequestCache(Folder var1) {
      return new FileBasedMappingTreeStructureCache(var1, "_input");
   }

   private MappingTreeStructureCache createResponseCache(Folder var1) {
      return new FileBasedMappingTreeStructureCache(var1, "_output");
   }
}
