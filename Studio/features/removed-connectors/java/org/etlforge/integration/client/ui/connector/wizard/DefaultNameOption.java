package org.etlforge.integration.client.ui.connector.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public final class DefaultNameOption implements NameOption {
   private final String label;

   public DefaultNameOption(String var1) {
      this.label = Preconditions.checkNotNull(var1, "label");
   }

   @Override
   public String getLabel() {
      return this.label;
   }

   public String apply(ConnectorObject var1) {
      return var1.getName();
   }
}
