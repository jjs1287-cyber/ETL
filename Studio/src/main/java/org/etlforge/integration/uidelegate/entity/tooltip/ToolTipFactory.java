package org.etlforge.integration.uidelegate.entity.tooltip;

import org.etlforge.application.extension.ExtensionObjectFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class ToolTipFactory implements ExtensionObjectFactory {
   @Override
   public final Class[] getExtensionTypes() {
      return new Class[]{IntegrationEntityToolTip.class};
   }

   @Override
   public final Object getExtensionObject(Object var1, Class var2) {
      return var2.equals(IntegrationEntityToolTip.class) ? this.createToolTip((IntegrationEntity)var1) : null;
   }

   protected IntegrationEntityToolTip createToolTip(IntegrationEntity var1) {
      return new IntegrationEntityToolTip(var1);
   }
}
