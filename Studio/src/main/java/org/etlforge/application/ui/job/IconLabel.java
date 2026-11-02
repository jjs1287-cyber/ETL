package org.etlforge.application.ui.job;

import java.awt.Container;
import javax.swing.JLabel;

final class IconLabel {
   private final JLabel label = new JLabel(ProcessConsoleIcons.PROCESS_OFF_24);

   void addToParent(Container var1, Object var2) {
      var1.add(this.label, var2);
   }

   void setOn() {
      this.label.setIcon(ProcessConsoleIcons.PROCESS_ON_24);
   }

   void setOff() {
      this.label.setIcon(ProcessConsoleIcons.PROCESS_OFF_24);
   }
}
