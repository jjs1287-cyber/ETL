package org.etlforge.integration.database;

import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.lang.KongaStringUtils;

public final class BeginEndQuote {
   public static final BeginEndQuote BLANK = new BeginEndQuote("");
   public static final BeginEndQuote POSTGRES = new BeginEndQuote("\"");
   public static final BeginEndQuote ORACLE = new BeginEndQuote("\"");
   public static final BeginEndQuote SQL_SERVER = new BeginEndQuote("[", "]");
   public final String begin;
   public final String end;

   public static BeginEndQuote fromStrings(String var0, String var1) {
      return var0 != null && var1 != null ? new BeginEndQuote(var0, var1) : null;
   }

   public BeginEndQuote(String var1) {
      this(var1, var1);
   }

   public BeginEndQuote(String var1, String var2) {
      this.begin = var1 != null ? var1 : "";
      this.end = var2 != null ? var2 : "";
   }

   public String unescapeName(String var1) {
      return this.begin.length() > 0 && var1.startsWith(this.begin) && this.end.length() > 0 && var1.endsWith(this.end)
         ? KongaStringUtils.removeStartAndEnd(var1, this.begin, this.end)
         : var1;
   }

   public String escapeNameIfNeeded(String var1) {
      return this.escapeName(var1, true);
   }

   public String escapeName(String var1) {
      return this.escapeName(var1, false);
   }

   public String escapeName(String var1, boolean var2) {
      if (!this.begin.isEmpty() && !var1.startsWith(this.begin) || !this.end.isEmpty() && !var1.endsWith(this.end)) {
         return var2 && var1.indexOf(46) < 0 ? var1 : this.begin + var1 + this.end;
      } else {
         return var1;
      }
   }

   public String getQualifiedName(DatabaseObject var1) {
      return this.getQualifiedName(var1.getSchema(), var1.getName());
   }

   public String getQualifiedName(String var1, String var2) {
      return this.escapeName(var1) + "." + this.escapeName(var2);
   }

   public String getSimpleName(DatabaseObject var1) {
      String var2 = var1.getName();
      if (var1.getSchema() != null && var1.getSchema().length() > 0) {
         String var4 = this.escapeName(var1.getSchema(), false) + ".";
         var2 = this.unescapeName(var2, var4);
      } else if (!this.isEmpty()) {
         String var3 = this.begin + this.end + ".";
         var2 = this.unescapeName(var2, var3);
      }

      return var2;
   }

   private String unescapeName(String var1, String var2) {
      return var1.length() > var2.length() && var1.startsWith(var2) ? this.unescapeName(var1.substring(var2.length())) : var1;
   }

   public boolean isEnclosingName(String var1) {
      return this.begin.length() > 0 && var1.startsWith(this.begin) && this.end.length() > 0 && var1.endsWith(this.end);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof BeginEndQuote)) {
         return false;
      }

      BeginEndQuote var2 = (BeginEndQuote)var1;
      return this.begin.equals(var2.begin) && this.end.equals(var2.end);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.begin.hashCode();
      return 31 * var1 + this.end.hashCode();
   }

   @Override
   public String toString() {
      String var1 = this.begin + this.end;
      return var1.isEmpty() ? "[none]" : var1;
   }

   private boolean isEmpty() {
      return this.begin.isEmpty() && this.end.isEmpty();
   }
}
