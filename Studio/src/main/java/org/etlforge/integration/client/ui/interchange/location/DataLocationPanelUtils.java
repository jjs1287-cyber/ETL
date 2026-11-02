package org.etlforge.integration.client.ui.interchange.location;

import javax.swing.JComponent;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.ui.Empty;
import org.etlforge.ui.widget.DropDownPanel;

public final class DataLocationPanelUtils {
   public static DropDownPanel createOptionsDropDown(JComponent var0, boolean var1) {
      var0.setBorder(Empty.border(5, 25, 0, 0));
      return new DropDownPanel(var0, LocationResources.getString("Optional"), var1);
   }

   private DataLocationPanelUtils() {
   }
}
