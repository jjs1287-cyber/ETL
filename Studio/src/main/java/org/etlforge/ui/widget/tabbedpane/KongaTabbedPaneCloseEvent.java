package org.etlforge.ui.widget.tabbedpane;

import java.util.EventObject;
import javax.swing.JTabbedPane;

public class KongaTabbedPaneCloseEvent extends EventObject {
   private final int selectedIndex;

   public KongaTabbedPaneCloseEvent(JTabbedPane var1, int var2) {
      super(var1);
      this.selectedIndex = var2;
   }

   public int getSelectedIndex() {
      return this.selectedIndex;
   }

   public JTabbedPane getSource() {
      return (JTabbedPane)super.getSource();
   }
}
