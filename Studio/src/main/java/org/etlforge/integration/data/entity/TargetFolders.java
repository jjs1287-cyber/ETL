package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.metadata.DataLocationMetaData;

public final class TargetFolders extends LocatableFolders {
   public TargetFolders(RootFolder var1) {
      super(var1);
   }

   @Override
   protected boolean isSupported(DataLocationType var1) {
      return DataLocationMetaData.getInstance().isSupportedTargetType(var1);
   }
}
