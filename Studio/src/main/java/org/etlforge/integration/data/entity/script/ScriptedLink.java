package org.etlforge.integration.data.entity.script;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.link.AbstractEntityLink;

public final class ScriptedLink extends AbstractEntityLink {
   public ScriptedLink(IntegrationEntity var1, IntegrationEntity var2) {
      super(var1, var2);
      if (!(var2 instanceof ScriptedLinkSource)) {
         throw new IllegalArgumentException("dependant must be a ScriptedLinkSource");
      }
   }

   @Override
   public boolean isStrong() {
      return false;
   }

   public ScriptedLinkSource getLinkSource() {
      return (ScriptedLinkSource)this.getDependantObject();
   }
}
