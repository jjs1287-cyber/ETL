package org.etlforge.integration.data.script.de;

import com.google.common.base.Preconditions;
import java.util.EnumSet;
import org.etlforge.integration.data.script.ExpressionSource;

public final class DataElementReference {
   private final String deName;
   private final ExpressionSource source;
   private final EnumSet<AccessType> access;

   public DataElementReference(String var1, ExpressionSource var2, EnumSet<AccessType> var3) {
      Preconditions.checkNotNull(var1, "deName");
      Preconditions.checkNotNull(var2, "source");
      this.deName = var1;
      this.source = var2;
      this.access = var3.clone();
   }

   public String getDataElement() {
      return this.deName;
   }

   public ExpressionSource getSource() {
      return this.source;
   }

   public EnumSet<AccessType> getAccessType() {
      return this.access.clone();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof DataElementReference)) {
         return false;
      }

      DataElementReference var2 = (DataElementReference)var1;
      return this.deName.equalsIgnoreCase(var2.deName) && this.source.equals(var2.source) && this.access.equals(var2.access);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.deName.toLowerCase().hashCode();
      var1 = 31 * var1 + this.source.hashCode();
      return 31 * var1 + this.access.hashCode();
   }

   @Override
   public String toString() {
      return this.deName;
   }
}
