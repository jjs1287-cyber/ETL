package org.etlforge.integration.activity.client.files;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.integration.common.server.file.OperationFile;

public final class StatusFileDownloaderFactory {
   private final RemoteFileSupport remoteFileSupport;
   private volatile Optional<RemoteFileCache> cache = Optional.absent();

   public StatusFileDownloaderFactory(RemoteFileSupport var1) {
      this.remoteFileSupport = Preconditions.checkNotNull(var1);
   }

   public void setCache(RemoteFileCache var1) {
      this.cache = Optional.of(var1);
   }

   public StatusFileDownloader newDownloader(OperationFile var1) {
      StatusFileDownloader var2 = new StatusFileDownloader(var1, this.remoteFileSupport);
      if (this.cache.isPresent()) {
         var2.setCache(this.cache.get());
      }

      return var2;
   }
}
