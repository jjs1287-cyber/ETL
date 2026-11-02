package org.etlforge.application.ui.window.event;

import java.util.EventListener;

public interface ActiveObjectProviderListener extends EventListener {
   void activeObjectsChanged(ActiveObjectProviderEvent var1);
}
