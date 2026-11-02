package org.etlforge.application.ui.window;

public interface AddContentCallback {
   void contentAdded(WindowSection var1, WindowSectionContent var2);

   void cancelled();

   void failed(Exception var1);
}
