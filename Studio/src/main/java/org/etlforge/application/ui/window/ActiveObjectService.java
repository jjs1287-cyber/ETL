package org.etlforge.application.ui.window;

import java.util.List;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;

public interface ActiveObjectService {
   List<?> getActiveObjects();

   void setActiveObjects(ActiveObjectProvider var1, List<?> var2);

   void addActiveObjectProvider(ActiveObjectProvider var1);

   void removeActiveObjectProvider(ActiveObjectProvider var1);

   void addActiveObjectListener(ActiveObjectProviderListener var1);

   void removeActiveObjectListener(ActiveObjectProviderListener var1);

   void addActiveObjectListener(String var1, ActiveObjectProviderListener var2);

   void removeActiveObjectListener(String var1, ActiveObjectProviderListener var2);
}
