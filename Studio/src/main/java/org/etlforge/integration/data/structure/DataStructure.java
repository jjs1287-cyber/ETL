package org.etlforge.integration.data.structure;

import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.location.DataLocation;

public abstract class DataStructure extends IntegrationData {
   private final DataStructureType type;

   public DataStructure(DataStructureType var1) {
      this.type = var1;
   }

   public DataStructure(DataStructureType var1, String var2) {
      super(var2);
      this.type = var1;
   }

   public final DataStructureType getStructureType() {
      return this.type;
   }

   public abstract StructureNature getStructureNature();

   public boolean isChunkable() {
      return true;
   }

   public boolean isCompatibleWith(DataStructure var1) {
      return this.equals(var1);
   }

   public boolean isCompatibleWith(DataLocation var1) {
      return false;
   }

   public final boolean isFileBased() {
      return this.type.isFileBased();
   }

   public final boolean isTextBased() {
      return this.type.isTextBased();
   }

   public final boolean isFlat() {
      return this.getStructureNature() == StructureNature.Flat;
   }
}
