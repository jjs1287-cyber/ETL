package org.etlforge.integration.data.structure;

import com.google.common.base.Predicate;
import java.io.Serializable;

public enum DataStructurePredicates implements Predicate<DataStructure> {
   FILE_BASED {
      public boolean apply(DataStructure var1) {
         return var1.isFileBased();
      }
   },
   TEXT_BASED {
      public boolean apply(DataStructure var1) {
         return var1.isTextBased();
      }
   };

   public static Predicate<DataStructure> ofType(DataStructureType var0) {
      return new DataStructurePredicates.TypeBased(var0);
   }

   DataStructurePredicates() {
   }

   private static class TypeBased implements Predicate<DataStructure>, Serializable {
      private final DataStructureType type;

      public TypeBased(DataStructureType var1) {
         this.type = var1;
      }

      public boolean apply(DataStructure var1) {
         return var1.getStructureType() == this.type;
      }

      @Override
      public String toString() {
         return "DataStructures of type " + this.type;
      }
   }
}
