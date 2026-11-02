package org.etlforge.integration.uidelegate.entity.tooltip;

import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ToolTipStringFactory {
   public static String getToolTip(IntegrationEntity var0) {
      IntegrationEntityToolTip var1 = var0.getExtensionObject(IntegrationEntityToolTip.class);
      return var1 == null ? null : var1.getToolTip();
   }

   private ToolTipStringFactory() {
   }
}
