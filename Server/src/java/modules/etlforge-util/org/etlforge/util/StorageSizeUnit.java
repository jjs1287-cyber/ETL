package org.etlforge.util;

import com.google.common.base.Preconditions;
import java.text.NumberFormat;

public enum StorageSizeUnit {
   Bytes(1L, "b"),
   Kilobytes(1024L, "Kb"),
   Megabytes(1048576L, "Mb"),
   Gigabytes(1073741824L, "Gb");

   private long inBytes;
   private String abbreviation;

   public long inBytes() {
      return this.inBytes;
   }

   public long toBytes(long var1) {
      return this.inBytes * var1;
   }

   public String abbreviation() {
      return this.abbreviation;
   }

   public static StorageSizeUnit fromAbbreviation(String var0) {
      Preconditions.checkNotNull(var0, "abbrev");

      for (StorageSizeUnit var4 : values()) {
         if (var4.abbreviation.equalsIgnoreCase(var0)) {
            return var4;
         }
      }

      return null;
   }

   public String format(long var1) {
      return NumberFormat.getInstance().format(this.convertToThisUnit(var1)) + " " + this.abbreviation();
   }

   public long convertToThisUnit(long var1) {
      return (long)Math.ceil((double)var1 / this.inBytes);
   }

   StorageSizeUnit(long var3, String var5) {
      this.inBytes = var3;
      this.abbreviation = var5;
   }
}
