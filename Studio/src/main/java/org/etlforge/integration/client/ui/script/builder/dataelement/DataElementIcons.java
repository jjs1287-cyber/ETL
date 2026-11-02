package org.etlforge.integration.client.ui.script.builder.dataelement;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.debug.client.de.DataElement;

public final class DataElementIcons {
   public static final Icon GLOBAL = ClientIcons.GLOBAL_DATA_ELEMENT_16;
   public static final Icon SOURCE = ClientIcons.SOURCE_DATA_ELEMENT_16;
   public static final Icon TITLE = ClientIcons.GLOBAL_DATA_ELEMENT_16;

   public static Icon getIcon(DataElement var0) {
      switch (var0.getScope()) {
         case GLOBAL:
            return GLOBAL;
         case LOCAL:
            return null;
         case SOURCE:
            return SOURCE;
         default:
            throw new RuntimeException("Unexpected scope: " + var0.getScope());
      }
   }

   private DataElementIcons() {
   }
}
