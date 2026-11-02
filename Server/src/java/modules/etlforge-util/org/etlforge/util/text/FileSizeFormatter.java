package org.etlforge.util.text;

import org.etlforge.lang.Arrays2;
import org.etlforge.util.StorageSizeUnit;

public class FileSizeFormatter {
   private StorageSizeUnit smallestUnit = StorageSizeUnit.Kilobytes;
   private StorageSizeUnit fixedUnit;

   public FileSizeFormatter() {
      this(null);
   }

   public FileSizeFormatter(StorageSizeUnit var1) {
      this.fixedUnit = var1;
   }

   public void setSmallestUnit(StorageSizeUnit var1) {
      this.smallestUnit = var1;
   }

   public void setFixedUnit(StorageSizeUnit var1) {
      this.fixedUnit = var1;
   }

   public String format(Object var1) {
      if (var1 instanceof Number) {
         return this.getTextFor(((Number)var1).longValue());
      } else {
         return var1 != null ? var1.toString() : null;
      }
   }

   private String getTextFor(long var1) {
      StorageSizeUnit var3 = this.getUnit(var1);
      return var3.format(var1);
   }

   private StorageSizeUnit getUnit(long var1) {
      if (this.fixedUnit != null) {
         return this.fixedUnit;
      }

      StorageSizeUnit[] var3 = Arrays2.reverse(StorageSizeUnit.values());

      for (StorageSizeUnit var7 : var3) {
         if (var7 == this.smallestUnit) {
            break;
         }

         if (var1 > var7.inBytes()) {
            return var7;
         }
      }

      return this.smallestUnit;
   }
}
