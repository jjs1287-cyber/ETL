package org.etlforge.integration.data.structure;

import org.etlforge.integration.data.location.DataLocationType;

public final class StructureLocationAssociations {
   public static boolean hasAssociatedStructure(DataLocationType var0) {
      return getAssociatedStructureType(var0) != null;
   }

   public static DataStructureType getAssociatedStructureType(DataLocationType var0) {
      switch (var0) {
         case Database:
            return DataStructureType.Database;
         case LDAP:
            return DataStructureType.LDAP;
         default:
            return null;
      }
   }

   private StructureLocationAssociations() {
   }
}
