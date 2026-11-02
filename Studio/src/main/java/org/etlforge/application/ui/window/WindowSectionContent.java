package org.etlforge.application.ui.window;

import javax.swing.Icon;
import javax.swing.JComponent;

public interface WindowSectionContent extends WindowElement {
   WindowSection getWindowSection();

   String getTitle();

   Icon getIcon();

   void decorate(JComponent var1);

   boolean isDetachable();

   @Override
   void addToWindow(ApplicationWindow var1);

   @Override
   boolean removeFromWindow(boolean var1);
}
