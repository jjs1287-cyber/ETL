package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.location.DataLocationType;

public final class DataLoaderRequestSourceConfig implements RequestSourceConfig {
   @Override
   public ImmutableSet<DataLocationType> getLocationTypes() {
      return ImmutableSet.of(DataLocationType.LocalFile, DataLocationType.FTP, DataLocationType.FileShare, DataLocationType.Database);
   }

   @Override
   public ImmutableSet<RequestSourceType> getStructureTypes(DataLocationType var1) {
      if (var1.isFileBased()) {
         return ImmutableSet.of(RequestSourceType.TEXT);
      } else if (var1 == DataLocationType.Database) {
         return ImmutableSet.of(RequestSourceType.DATABASE);
      } else {
         throw new RuntimeException("Unsupported location type: " + var1);
      }
   }

   @Override
   public ImmutableSet<String> getFileExtensions() {
      return ImmutableSet.of(".csv", ".txt");
   }
}
