package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.location.DataLocationType;

public final class DataLoaderQueryTargetConfig implements QueryTargetConfig {
   @Override
   public ImmutableSet<DataLocationType> getLocationTypes() {
      return ImmutableSet.of(DataLocationType.LocalFile, DataLocationType.FTP, DataLocationType.FileShare, DataLocationType.Database);
   }

   @Override
   public ImmutableSet<QueryTargetType> getStructureTypes(DataLocationType var1) {
      switch (var1) {
         case LocalFile:
         case FTP:
         case FileShare:
            return ImmutableSet.of(QueryTargetType.TEXT);
         case Database:
            return ImmutableSet.of(QueryTargetType.DATABASE);
         default:
            throw new RuntimeException("Unsupported location type: " + var1);
      }
   }

   @Override
   public ImmutableSet<String> getFileExtensions() {
      return ImmutableSet.of(".csv", ".txt");
   }
}
