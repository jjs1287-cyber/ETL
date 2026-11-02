package org.etlforge.application.ui.window;

import java.awt.event.KeyEvent;

public interface WindowKeyDispatcher {
   boolean dispatch(KeyEvent var1, ApplicationWindow var2);

   String getId();
}
