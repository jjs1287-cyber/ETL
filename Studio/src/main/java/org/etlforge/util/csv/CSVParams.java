package org.etlforge.util.csv;

import com.google.common.base.Objects;

public final class CSVParams {
   public static final CSVParams DEFAULT = new CSVParams(',', '"');
   public final char delimiter;
   public final char qualifier;

   public CSVParams(char var1, char var2) {
      this.delimiter = var1;
      this.qualifier = var2;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("delimiter", this.delimiter).add("qualifier", this.qualifier).toString();
   }
}
