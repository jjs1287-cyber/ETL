package org.etlforge.application.ui.window.event;

import java.util.List;

public interface ActiveObjectProvider {
   String getProviderId();

   boolean isActive();

   List<?> getActiveObjects();

   void addActiveObjectListener(ActiveObjectProviderListener var1);

   void removeActiveObjectListener(ActiveObjectProviderListener var1);
}
