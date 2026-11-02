package org.etlforge.integration.etlforgeconnector;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import javax.annotation.Nullable;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.string.StringLimiter;

public final class SimpleConnectorObject implements ConnectorObject {
   private final String name;
   private final String description;

   public SimpleConnectorObject(String var1) {
      this(var1, null);
   }

   public SimpleConnectorObject(String var1, @Nullable String var2) {
      this.name = ArgCheck.checkNotEmpty(var1);
      this.description = Strings.nullToEmpty(var2);
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.name, this.description);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SimpleConnectorObject)) {
         return false;
      }

      SimpleConnectorObject var2 = (SimpleConnectorObject)var1;
      return this.name.equals(var2.name) && this.description.equals(var2.description);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("name", this.name).add("description", StringLimiter.limit(this.description, 100)).toString();
   }
}
