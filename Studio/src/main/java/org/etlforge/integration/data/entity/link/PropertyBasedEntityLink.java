package org.etlforge.integration.data.entity.link;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.ArgCheck;

public final class PropertyBasedEntityLink extends AbstractEntityLink {
   private final String propertyName;

   public PropertyBasedEntityLink(IntegrationEntity var1, IntegrationEntity var2, String var3) {
      super(var1, var2);
      this.propertyName = ArgCheck.checkNotEmpty(var3);
   }

   @Override
   public boolean isStrong() {
      return false;
   }

   @Override
   protected void mainObjectDeleted() {
      super.mainObjectDeleted();
      this.getDependantObject().setStringProperty(this.propertyName, null);
   }
}
