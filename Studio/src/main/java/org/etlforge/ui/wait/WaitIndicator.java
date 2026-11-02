package org.etlforge.ui.wait;

import javax.swing.JComponent;

public interface WaitIndicator {
   void start();

   void stop();

   JComponent getUi();
}
