package org.etlforge.integration.data.structure;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import org.etlforge.system.ArgCheck;

public final class CallStructureSchema {
   private final String schemaName;
   private final String rootElement;

   public CallStructureSchema(String var1, String var2) {
      this.schemaName = ArgCheck.checkNotEmpty(var1, "schemaName");
      this.rootElement = Strings.nullToEmpty(var2);
   }

   public String getSchemaName() {
      return this.schemaName;
   }

   public String getRootElement() {
      return this.rootElement;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.schemaName, this.rootElement);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof CallStructureSchema)) {
         return false;
      }

      CallStructureSchema var2 = (CallStructureSchema)var1;
      return this.schemaName.equals(var2.schemaName) && this.rootElement.equals(var2.rootElement);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("schema", this.schemaName).add("root-element", this.rootElement).toString();
   }
}
