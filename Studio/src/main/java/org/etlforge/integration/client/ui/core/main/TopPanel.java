package org.etlforge.integration.client.ui.core.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.etlforge.application.ui.widget.ApplicationToolBar;
import org.etlforge.integration.client.ui.ConnectionStatusIndicator;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class TopPanel {
   private final JComponent ui;
   private final ConnectionStatusIndicator connectionIndicator;

   public TopPanel(ConnectionStatusIndicator var1) {
      this.connectionIndicator = var1;
      this.ui = new BorderLayoutBuilder().center(" ").east(var1).container();
   }

   public void setLoginAction(Action var1) {
      this.connectionIndicator.setLoginAction(var1);
   }

   public void addToParent(Container var1, Object var2) {
      var1.add(this.ui, var2);
   }

   public void setToolBar(ApplicationToolBar var1) {
      Component var2 = ((BorderLayout)this.ui.getLayout()).getLayoutComponent("West");
      if (var2 != null) {
         this.ui.remove(var2);
      }

      this.ui.add(var1.getPanel(), "West");
      this.ui.validate();
      this.ui.repaint();
   }

   public void setBorder(Border var1) {
      this.ui.setBorder(var1);
   }
}
