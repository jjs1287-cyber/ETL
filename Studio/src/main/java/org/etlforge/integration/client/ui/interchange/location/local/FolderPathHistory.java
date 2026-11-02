package org.etlforge.integration.client.ui.interchange.location.local;

import org.etlforge.integration.client.datalocation.cache.LocalFileLocationCache;
import org.etlforge.ui.text.contentassist.StringValueHistory;

final class FolderPathHistory implements StringValueHistory {
   private final LocalFileLocationCache cache;

   public FolderPathHistory(LocalFileLocationCache var1) {
      this.cache = var1;
   }

   @Override
   public String[] values() {
      return this.cache.getFolderPaths();
   }

   @Override
   public boolean delete(String var1) {
      return this.cache.removeFolderPath(var1);
   }
}
