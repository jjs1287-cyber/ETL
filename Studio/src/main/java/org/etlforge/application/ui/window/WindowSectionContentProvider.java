package org.etlforge.application.ui.window;

public interface WindowSectionContentProvider {
   String getContentId();

   WindowSectionContent getContent(WindowSection var1);
}
