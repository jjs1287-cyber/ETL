package org.etlforge.integration.data.entity.link;

import org.etlforge.integration.data.entity.IntegrationEntity;

public class StrongEntityLink extends AbstractEntityLink {
   public StrongEntityLink(IntegrationEntity var1, IntegrationEntity var2) {
      super(var1, var2);
   }

   @Override
   public boolean isStrong() {
      return true;
   }
}
