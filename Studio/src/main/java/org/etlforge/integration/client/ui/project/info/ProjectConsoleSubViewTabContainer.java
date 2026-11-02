package org.etlforge.integration.client.ui.project.info;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.tabbedpane.DefaultTabReorderHandler;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;

final class ProjectConsoleSubViewTabContainer implements ProjectConsoleSubViewContainer {
   private final KongaTabbedPane tabPane = new KongaTabbedPane();
   private final BiMap<String, Integer> idToIndex;
   private final Map<String, ProjectConsoleSubView> views;

   public ProjectConsoleSubViewTabContainer() {
      this.tabPane.enableTabReordering(new ProjectConsoleSubViewTabContainer.TabReorderHandlerImpl());
      this.idToIndex = HashBiMap.create();
      this.views = Maps.newHashMap();
   }

   @Override
   public int getViewCount() {
      return this.views.size();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tabPane.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tabPane.requestFocus();
   }

   @Override
   public ProjectConsoleSubView getSubView(String var1) {
      return this.views.get(var1);
   }

   @Override
   public void showSubView(ProjectConsoleSubView var1) {
      Integer var2 = this.idToIndex.get(var1.getId());
      if (var2 != null) {
         this.tabPane.setSelectedIndex(var2);
      } else {
         this.tabPane.addTab(var1.getTitle(), var1.getIcon(), BorderLayoutBuilder.fillWith(var1));
         this.views.put(var1.getId(), var1);
         int var3 = this.tabPane.getTabCount() - 1;
         this.idToIndex.put(var1.getId(), var3);
         this.tabPane.setSelectedIndex(var3);
      }
   }

   @Override
   public void removeSubView(String var1) {
      Integer var2 = this.idToIndex.remove(var1);
      if (var2 != null) {
         this.views.remove(var1);
         this.tabPane.removeTabAt(var2);
      }
   }

   private class TabReorderHandlerImpl extends DefaultTabReorderHandler {
      private TabReorderHandlerImpl() {
      }

      @Override
      public void handle(KongaTabbedPane var1, int var2, int var3) {
         String var4 = ProjectConsoleSubViewTabContainer.this.idToIndex.inverse().remove(var2);
         String var5 = ProjectConsoleSubViewTabContainer.this.idToIndex.inverse().remove(var3);
         super.handle(var1, var2, var3);
         ProjectConsoleSubViewTabContainer.this.idToIndex.put(var4, var3);
         ProjectConsoleSubViewTabContainer.this.idToIndex.put(var5, var2);
      }
   }
}
