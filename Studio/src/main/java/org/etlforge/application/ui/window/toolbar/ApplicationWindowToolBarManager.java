package org.etlforge.application.ui.window.toolbar;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JToolBar;

public final class ApplicationWindowToolBarManager implements ToolBarManager {
   private final LinkedHashMap<String, ToolBar> toolBarMap = Maps.newLinkedHashMap();
   private final LinkedHashMap<String, JToolBar> jtoolBarMap = Maps.newLinkedHashMap();
   private final Map<ToolBarExtensionPoint, ToolBar> extensions = Maps.newHashMap();

   public void setToolBars(List<? extends ToolBar> var1) {
      this.removeAll();

      for (ToolBar var3 : var1) {
         this.toolBarMap.put(var3.getID(), var3);
      }
   }

   private void removeAll() {
      this.toolBarMap.clear();
      this.jtoolBarMap.clear();
      this.extensions.clear();
   }

   @Override
   public ToolBar getToolBar(String var1) {
      return this.toolBarMap.get(var1);
   }

   @Override
   public JToolBar getJToolBar(String var1) {
      return this.jtoolBarMap.get(var1);
   }

   @Override
   public List<JToolBar> getJToolBars() {
      ArrayList var1 = Lists.newArrayList();

      for (ToolBar var3 : this.toolBarMap.values()) {
         this.addEntries(var3, var1);
      }

      for (ToolBarExtensionPoint var5 : ToolBarExtensionPoint.values()) {
         ToolBar var6 = this.extensions.get(var5);
         if (var6 != null) {
            var1.add(var6.createJToolBar());
         }
      }

      return var1;
   }

   private void addEntries(ToolBar var1, List<JToolBar> var2) {
      JToolBar var3 = var1.createJToolBar();
      this.jtoolBarMap.put(var1.getID(), var3);
      var2.add(var3);
   }
}
