package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.location.DataLocationType;

public final class StandardRequestSourceConfig implements RequestSourceConfig {
   @Override
   public ImmutableSet<DataLocationType> getLocationTypes() {
      return ImmutableSet.of(
         DataLocationType.LocalFile,
         DataLocationType.FTP,
         DataLocationType.FileShare,
         DataLocationType.TemporaryStorage,
         DataLocationType.Database,
         DataLocationType.HTTP,
         DataLocationType.LDAP,
         DataLocationType.None
      );
   }

   @Override
   public ImmutableSet<RequestSourceType> getStructureTypes(DataLocationType var1) {
      if (var1.isFileBased()) {
         return ImmutableSet.of(RequestSourceType.TEXT, RequestSourceType.XML, RequestSourceType.JSON);
      } else if (var1 == DataLocationType.Database) {
         return ImmutableSet.of(RequestSourceType.DATABASE);
      } else if (var1 == DataLocationType.LDAP) {
         return ImmutableSet.of(RequestSourceType.LDAP);
      } else if (var1 == DataLocationType.None) {
         return ImmutableSet.of(RequestSourceType.NONE);
      } else {
         throw new RuntimeException("Unsupported location type: " + var1);
      }
   }

   @Override
   public ImmutableSet<String> getFileExtensions() {
      return ImmutableSet.of(".csv", ".txt", ".xml", ".json");
   }
}
