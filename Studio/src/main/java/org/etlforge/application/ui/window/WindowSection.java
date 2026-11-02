package org.etlforge.application.ui.window;

import javax.swing.JComponent;
import org.etlforge.application.ui.window.event.WindowSectionListener;

public interface WindowSection {
   ApplicationWindow getWindow();

   void addContent(WindowSectionContent var1, AddContentCallback var2);

   boolean removeContent(WindowSectionContent var1);

   WindowSectionContent getContent(String var1);

   boolean containsContent(String var1);

   void setActiveContent(WindowSectionContent var1);

   WindowSectionContent getActiveContent();

   void ensureVisible();

   void decorate(JComponent var1);

   void addWindowSectionListener(WindowSectionListener var1);

   void removeWindowSectionListener(WindowSectionListener var1);
}
