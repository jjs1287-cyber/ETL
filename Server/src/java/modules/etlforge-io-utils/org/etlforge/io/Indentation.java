package org.etlforge.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;

public final class Indentation {
   private final String unit;
   private final int level;
   private final String indent;

   public Indentation() {
      this("  ", 0);
   }

   public Indentation(String var1) {
      this(var1, 0);
   }

   private Indentation(String var1, int var2) {
      this.unit = var1;
      this.level = var2;
      StringBuilder var3 = new StringBuilder();

      for (int var4 = 0; var4 < var2; var4++) {
         var3.append(var1);
      }

      this.indent = var3.toString();
   }

   public void writeTo(StringBuilder var1) {
      if (this.level > 0) {
         var1.append(this.indent);
      }
   }

   public void writeTo(Appendable var1) throws IOException {
      if (this.level > 0) {
         var1.append(this.indent);
      }
   }

   public void writeTo(Writer var1) throws IOException {
      if (this.level > 0) {
         var1.write(this.indent);
      }
   }

   public void writeTo(PrintStream var1) {
      if (this.level > 0) {
         var1.print(this.indent);
      }
   }

   public Indentation increase() {
      return new Indentation(this.unit, this.level + 1);
   }

   public Indentation decrease() {
      Preconditions.checkState(this.level > 0, "Cannot decrease this Indentation any further");
      return new Indentation(this.unit, this.level - 1);
   }

   public String get() {
      return this.indent;
   }

   @Override
   public String toString() {
      return "" + this.level;
   }
}
