package org.etlforge.application.ui.view;

public interface ViewManager {
   boolean hasView(String var1);

   void showView(String var1);

   boolean hasVisibleViews();

   void removeView(String var1);

   void addViewManagerListener(ViewManagerListener var1);

   void removeViewManagerListener(ViewManagerListener var1);
}
