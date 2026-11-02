package org.etlforge.application.ui.view;

import org.etlforge.application.ui.window.ApplicationWindow;

public interface View {
   String getID();

   ApplicationWindow getWindow();

   boolean prepareForDisplay();

   boolean allowsRemoval();

   void prepareForRemoval();

   void setActive(boolean var1);
}
