package org.etlforge.integration.client.ui.project.recent;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import org.etlforge.integration.client.project.ProjectLocation;

class ProjectRenderer extends DefaultListCellRenderer {
   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      JLabel var6 = (JLabel)super.getListCellRendererComponent(var1, var2, var3, var4, var5);
      if (var2 instanceof ProjectLocation) {
         var6.setToolTipText(((ProjectLocation)var2).getLocationPath());
      } else {
         var6.setToolTipText(null);
      }

      return var6;
   }
}
