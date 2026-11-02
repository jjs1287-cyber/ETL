package org.etlforge.integration.data.structure;

public final class NoStructure extends DataStructure {
   public static String getJtr(boolean var0) {
      String var1 = var0 ? "No target is specified" : "No source is specified";
      return "<?xml version=\"1.0\" ?><JTR Type=\"Unknown\"><CROM Name=\"" + var1 + "\"/></JTR>";
   }

   public NoStructure() {
      super(DataStructureType.None, "None");
   }

   @Override
   public StructureNature getStructureNature() {
      return null;
   }

   @Override
   public boolean isCompatibleWith(DataStructure var1) {
      return var1.getStructureType() == DataStructureType.None;
   }

   @Override
   public boolean isChunkable() {
      return false;
   }
}
