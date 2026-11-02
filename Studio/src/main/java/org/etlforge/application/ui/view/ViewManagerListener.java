package org.etlforge.application.ui.view;

import java.util.EventListener;

public interface ViewManagerListener extends EventListener {
   void viewIsLoaded(ViewManagerEvent var1);

   void viewIsDisplayed(ViewManagerEvent var1);

   void viewWasRemoved(ViewManagerEvent var1);
}
