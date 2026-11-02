package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.metadata.DataLocationMetaData;

public final class SourceFolders extends LocatableFolders {
   public SourceFolders(RootFolder var1) {
      super(var1);
   }

   @Override
   protected boolean isSupported(DataLocationType var1) {
      return DataLocationMetaData.getInstance().isSupportedSourceType(var1);
   }
}
