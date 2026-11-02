package org.etlforge.integration.data.entity.script;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface GuidPathCacheListener {
   void pathsHaveChanged(List<GuidPathCacheListener.PathChange> var1);

   final class PathChange {
      public final IntegrationEntity item;
      public final String oldPath;
      public final String newPath;

      public PathChange(IntegrationEntity var1, String var2, String var3) {
         this.item = var1;
         this.oldPath = var2;
         this.newPath = var3;
      }
   }
}
