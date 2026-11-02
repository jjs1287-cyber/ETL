package org.etlforge.integration.data.location;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.util.net.Port;

public final class PortAccessibleSupport {
   private final DataLocation data;
   private final String propertyName;

   public PortAccessibleSupport(DataLocation var1, String var2) {
      Preconditions.checkNotNull(var1, "data");
      Preconditions.checkNotNull(var2, "propertyName");
      this.data = var1;
      this.propertyName = var2;
   }

   public Port setPort(Port var1) {
      Port var2 = this.getPort();
      if (Objects.equal(var2, var1)) {
         return var1;
      }

      if (var1 != null) {
         this.data.setProperty(this.propertyName, var1.getPort());
      } else {
         this.data.setProperty(this.propertyName, null);
      }

      return var2;
   }

   public Port getPort() {
      String var1 = this.data.getProperty(this.propertyName);
      if (var1 == null) {
         return Port.DEFAULT;
      }

      try {
         int var2 = Integer.valueOf(var1);
         return var2 != 0 && var2 != -1 ? Port.valueOf(var2) : Port.DEFAULT;
      } catch (NumberFormatException var3) {
         return Port.INVALID;
      }
   }
}
