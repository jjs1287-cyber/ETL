package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.location.DataLocationType;

public final class StandardQueryTargetConfig implements QueryTargetConfig {
   @Override
   public ImmutableSet<DataLocationType> getLocationTypes() {
      return ImmutableSet.of(
         DataLocationType.LocalFile,
         DataLocationType.FTP,
         DataLocationType.FileShare,
         DataLocationType.TemporaryStorage,
         DataLocationType.Database,
         DataLocationType.HTTP,
         DataLocationType.LDAP
      );
   }

   @Override
   public ImmutableSet<QueryTargetType> getStructureTypes(DataLocationType var1) {
      if (var1.isFileBased()) {
         return ImmutableSet.of(QueryTargetType.TEXT, QueryTargetType.XML, QueryTargetType.JSON);
      } else if (var1 == DataLocationType.Database) {
         return ImmutableSet.of(QueryTargetType.DATABASE);
      } else if (var1 == DataLocationType.LDAP) {
         return ImmutableSet.of(QueryTargetType.LDAP);
      } else {
         throw new RuntimeException("Unsupported location type: " + var1);
      }
   }

   @Override
   public ImmutableSet<String> getFileExtensions() {
      return ImmutableSet.of(".csv", ".txt", ".xml", ".json");
   }
}
