package org.etlforge.integration.client.ui.interchange.component.toolbar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.swing.JToolBar;
import org.etlforge.application.ui.view.ViewToolBarManager;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.toolbar.AbstractToolBarExtension;
import org.etlforge.application.ui.window.toolbar.ToolBar;
import org.etlforge.application.ui.window.toolbar.ToolBarExtensionPoint;
import org.etlforge.application.ui.window.toolbar.ToolBarManager;

public final class InterchangeToolBarManager implements ViewToolBarManager {
   private final ToolBarManager appWinToolBarManager;
   private final Map<String, ToolBar> toolBarMap;
   private final Map<String, JToolBar> jtoolBarMap;
   private final List<? extends AbstractToolBarExtension> extensions;

   public InterchangeToolBarManager(ApplicationWindow var1, List<? extends AbstractToolBarExtension> var2) {
      this.appWinToolBarManager = var1.getToolBarManager();
      this.toolBarMap = Maps.newHashMap();
      this.jtoolBarMap = Maps.newHashMap();
      this.extensions = var2;
   }

   @Override
   public void addExtensions() {
      for (AbstractToolBarExtension var2 : this.extensions) {
         ToolBar var3 = this.appWinToolBarManager.getToolBar(var2.getId());

         for (ToolBarExtensionPoint var7 : ToolBarExtensionPoint.values()) {
            ImmutableList var8 = var2.getEntries(var7);
            if (var8 != null) {
               var3.addExtension(var7, var8);
            }
         }
      }
   }

   @Override
   public void removeExtensions() {
      for (AbstractToolBarExtension var2 : this.extensions) {
         var2.removeAll(this.appWinToolBarManager);
      }
   }

   @Override
   public ToolBar getToolBar(String var1) {
      ToolBar var2 = this.toolBarMap.get(var1);
      if (var2 == null) {
         var2 = this.appWinToolBarManager.getToolBar(var1);
      }

      return var2;
   }

   @Override
   public JToolBar getJToolBar(String var1) {
      JToolBar var2 = this.jtoolBarMap.get(var1);
      if (var2 == null) {
         var2 = this.appWinToolBarManager.getJToolBar(var1);
      }

      return var2;
   }

   @Override
   public List<JToolBar> getJToolBars() {
      return null;
   }
}
