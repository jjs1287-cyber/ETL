package org.etlforge.application.ui.window.toolbar;

import java.util.List;
import javax.swing.JToolBar;

public interface ToolBarManager {
   ToolBar getToolBar(String var1);

   JToolBar getJToolBar(String var1);

   List<JToolBar> getJToolBars();
}
