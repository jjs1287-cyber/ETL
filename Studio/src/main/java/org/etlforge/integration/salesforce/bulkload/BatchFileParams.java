package org.etlforge.integration.salesforce.bulkload;

import org.etlforge.lang.Objects;

public final class BatchFileParams {
   public static final int DEFAULT_RECORDS_PER_FILE = 10000;
   public static final int DEFAULT_CHARACTERS_PER_FILE = 10000000;
   public static final BatchFileParams UNLIMITED = new BatchFileParams(0, 0);
   public static final BatchFileParams DEFAULT = new BatchFileParams(10000, 10000000);
   public final int recordsPerFile;
   public final int charactersPerFile;

   public BatchFileParams(int var1, int var2) {
      this.recordsPerFile = var1 < 0 ? 0 : var1;
      this.charactersPerFile = var2 < 0 ? 0 : var2;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.recordsPerFile, this.charactersPerFile);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof BatchFileParams)) {
         return false;
      }

      BatchFileParams var2 = (BatchFileParams)var1;
      return this.recordsPerFile == var2.recordsPerFile && this.charactersPerFile == var2.charactersPerFile;
   }

   @Override
   public String toString() {
      return String.format("Recs/file: %d, Chars/file: %d", this.recordsPerFile, this.charactersPerFile);
   }
}
