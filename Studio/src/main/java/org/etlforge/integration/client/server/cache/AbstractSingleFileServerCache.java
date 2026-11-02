package org.etlforge.integration.client.server.cache;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.application.cache.DataCache;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.common.server.ServerGuid;

public abstract class AbstractSingleFileServerCache implements DataCache {
   private final String cacheId;
   private final ServerGuid serverId;
   private final File parentDirectory;

   public AbstractSingleFileServerCache(String var1, ServerGuid var2, File var3) {
      this.cacheId = Preconditions.checkNotNull(var1, "cacheId");
      this.serverId = Preconditions.checkNotNull(var2, "serverId");
      this.parentDirectory = Preconditions.checkNotNull(var3, "parentDir");
   }

   @Override
   public final String getID() {
      return this.cacheId;
   }

   public final ServerGuid getServerID() {
      return this.serverId;
   }

   protected abstract String getFileName();

   protected final File createFile() throws DataCacheException {
      if (this.parentDirectory.exists() && !this.parentDirectory.isDirectory()) {
         throw new DataCacheException("The specified parent directory is not a directory: " + this.parentDirectory.getAbsolutePath());
      } else if (!this.parentDirectory.exists() && !this.parentDirectory.mkdirs()) {
         throw new DataCacheException("Failed to create the specified parent directory: " + this.parentDirectory.getAbsolutePath());
      } else {
         return new File(this.parentDirectory, this.getFileName());
      }
   }
}
