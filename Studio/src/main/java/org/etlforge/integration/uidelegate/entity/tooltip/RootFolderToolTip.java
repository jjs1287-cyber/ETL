package org.etlforge.integration.uidelegate.entity.tooltip;

import org.etlforge.integration.data.entity.RootFolder;

public final class RootFolderToolTip extends IntegrationEntityToolTip {
   public RootFolderToolTip(RootFolder var1) {
      super(var1, false);
      this.addContent(var1);
   }

   private void addContent(RootFolder var1) {
      this.addEntry("Number of " + var1.getItemType().getPlural() + ":", String.valueOf(var1.getNumberOfItemsRecursively()));
   }
}
