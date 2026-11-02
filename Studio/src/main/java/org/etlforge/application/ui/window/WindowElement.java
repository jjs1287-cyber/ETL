package org.etlforge.application.ui.window;

import java.util.EnumSet;
import org.etlforge.application.ui.window.event.WindowElementStateListener;

public interface WindowElement {
   String getId();

   ApplicationWindow getWindow();

   EnumSet<WindowElementState> getState();

   void addToWindow(ApplicationWindow var1);

   boolean removeFromWindow(boolean var1);

   void addResource(WindowElementResource var1);

   void removeResource(WindowElementResource var1);

   void appendState(WindowElementState var1);

   void revokeState(WindowElementState var1);

   void addStateListener(WindowElementStateListener var1);

   void removeStateListener(WindowElementStateListener var1);
}
