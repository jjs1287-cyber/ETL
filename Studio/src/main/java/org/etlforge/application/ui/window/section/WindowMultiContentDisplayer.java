package org.etlforge.application.ui.window.section;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.SupportedLookAndFeel;
import org.etlforge.application.ui.widget.SimpleTabPane;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.tabbedpane.DefaultTabReorderHandler;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPaneCloseEvent;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPaneCloseHandler;
import org.etlforge.ui.widget.tabbedpane.TabReorderHandler;

final class WindowMultiContentDisplayer {
   private final MultiContentWindowSection windowSection;
   private final WindowMultiContentDisplayer.TabControl tabPane;
   private final WindowMultiContentDisplayer.TabPaneListener tabPaneListener;
   private final List<WindowSectionContent> contents;
   private final Map<String, Integer> idToIndex;

   public WindowMultiContentDisplayer(MultiContentWindowSection var1, boolean var2, Action var3) {
      this.windowSection = var1;
      this.contents = Lists.newArrayList();
      this.idToIndex = Maps.newHashMap();
      this.tabPaneListener = new WindowMultiContentDisplayer.TabPaneListener();
      this.tabPane = this.createTabControl(var2, var3, this.tabPaneListener);
   }

   private WindowMultiContentDisplayer.TabControl createTabControl(boolean var1, Action var2, WindowMultiContentDisplayer.TabPaneListener var3) {
      if (SimpleTabPane.SUPPORTED) {
         WindowMultiContentDisplayer.SimpleTabControl var5 = new WindowMultiContentDisplayer.SimpleTabControl(var1, var2);
         var5.addChangeListene(var3);
         return var5;
      } else {
         WindowMultiContentDisplayer.SwingTabControl var4 = new WindowMultiContentDisplayer.SwingTabControl(
            var1, var2, new WindowMultiContentDisplayer.SwingReorderer()
         );
         var4.addChangeListene(var3);
         var4.setCloseHandler(var3);
         return var4;
      }
   }

   public void addContent(WindowSectionContent var1, JComponent var2) {
      if (!this.idToIndex.containsKey(var1.getId())) {
         this.addContentImpl(var1, var2);
      }
   }

   public void removeContent(WindowSectionContent var1) {
      if (this.idToIndex.containsKey(var1.getId())) {
         this.removeContentImpl(var1);
      }
   }

   private void removeContentImpl(WindowSectionContent var1) {
      int var2 = this.idToIndex.remove(var1.getId());
      this.tabPane.removeTabAt(var2);
      this.contents.remove(var1);
      this.rebuildIndexMap();
   }

   private void rebuildIndexMap() {
      this.idToIndex.clear();
      int var1 = 0;

      for (WindowSectionContent var3 : this.contents) {
         this.idToIndex.put(var3.getId(), var1);
         var1++;
      }
   }

   public WindowSectionContent getActiveContent() {
      int var1 = this.tabPane.getSelectedIndex();
      return var1 >= 0 ? this.contents.get(var1) : null;
   }

   public void setActiveContent(WindowSectionContent var1) {
      final Integer var2 = this.idToIndex.get(var1.getId());
      if (var2 != null) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               WindowMultiContentDisplayer.this.tabPane.setSelectedIndex(var2);
            }
         });
      }
   }

   public void decorate(JComponent var1) {
      BorderLayoutBuilder.from(var1).center(this.tabPane.getUi());
   }

   private void addContentImpl(WindowSectionContent var1, JComponent var2) {
      this.contents.add(var1);
      this.tabPane.addTab(var1, var2);
      int var3 = this.tabPane.getTabCount() - 1;
      this.idToIndex.put(var1.getId(), var3);
      this.tabPane.setSelectedIndex(var3);
   }

   private void handleIndexSwitch(int var1, int var2) {
      WindowSectionContent var3 = this.contents.remove(var1);
      this.contents.add(var2, var3);
      this.rebuildIndexMap();
   }

   private class SimpleTabControl implements WindowMultiContentDisplayer.TabControl, SimpleTabPane.DndCallback {
      private final SimpleTabPane tabs = new SimpleTabPane();
      private final boolean closeable;
      private final Action detachAction;

      public SimpleTabControl(boolean var2, Action var3) {
         this.tabs.enableTabReordering(this);
         this.tabs.installKeyBinding(var3);
         this.closeable = var2;
         this.detachAction = var3;
      }

      @Override
      public void tabWasMoved(int var1, int var2) {
         WindowMultiContentDisplayer.this.handleIndexSwitch(var1, var2);
      }

      @Override
      public int getTabCount() {
         return this.tabs.getTabCount();
      }

      @Override
      public void addTab(WindowSectionContent var1, JComponent var2) {
         this.tabs.addTab(var1.getTitle(), var1.getIcon(), var2);
         JPopupMenu var3 = this.createTabContextMenu(var1);
         if (var3 != null) {
            JLabel var4 = this.tabs.getTabComponentAt(this.tabs.getTabCount() - 1);
            var4.setComponentPopupMenu(var3);
         }
      }

      private JPopupMenu createTabContextMenu(WindowSectionContent var1) {
         KongaPopupMenu var2 = null;
         if (this.closeable || this.detachAction != null) {
            var2 = new KongaPopupMenu();
            if (this.closeable) {
               var2.add(new CloseWindowSectionContentAction(WindowMultiContentDisplayer.this.windowSection, var1));
            }

            if (this.detachAction != null) {
               var2.add(this.detachAction);
            }
         }

         return var2;
      }

      @Override
      public void removeTabAt(int var1) {
         this.tabs.removeTabAt(var1);
      }

      @Override
      public int getSelectedIndex() {
         return this.tabs.getSelectedIndex();
      }

      @Override
      public void setSelectedIndex(int var1) {
         this.tabs.setSelectedIndex(var1);
      }

      @Override
      public void addChangeListene(ChangeListener var1) {
         this.tabs.addChangeListener(var1);
      }

      @Override
      public UiProvider getUi() {
         return this.tabs;
      }
   }

   private class SwingReorderer implements TabReorderHandler {
      private final TabReorderHandler delegate = new DefaultTabReorderHandler();

      private SwingReorderer() {
      }

      @Override
      public void handle(KongaTabbedPane var1, int var2, int var3) {
         WindowMultiContentDisplayer.this.handleIndexSwitch(var2, var3);
         this.delegate.handle(var1, var2, var3);
      }
   }

   private static final class SwingTabControl implements WindowMultiContentDisplayer.TabControl {
      private final KongaTabbedPane tabs = new KongaTabbedPane();

      public SwingTabControl(boolean var1, Action var2, TabReorderHandler var3) {
         if (!SupportedLookAndFeel.CROSS_PLATFORM.isInstalled()) {
            this.tabs.setOpaque(true);
            this.tabs.setBackground(Colors.ETLFORGE_BLUE);
         }

         this.tabs.setUnselectedTabBackground(Colors.UNSELECTED_TAB_BACKGROUND);
         this.tabs.setCloseable(var1);
         this.tabs.setCanCloseOtherTabs(false);
         this.tabs.enableTabReordering(var3);
         this.tabs.installKeyBinding(var2);
         this.tabs.setAdditionalTabActions(var2);
      }

      @Override
      public UiProvider getUi() {
         return this.tabs;
      }

      @Override
      public int getTabCount() {
         return this.tabs.getTabCount();
      }

      @Override
      public void addTab(WindowSectionContent var1, JComponent var2) {
         this.tabs.addTab(var1.getTitle(), var1.getIcon(), var2);
      }

      @Override
      public void removeTabAt(int var1) {
         this.tabs.removeTabAt(var1);
      }

      @Override
      public int getSelectedIndex() {
         return this.tabs.getSelectedIndex();
      }

      @Override
      public void setSelectedIndex(int var1) {
         this.tabs.setSelectedIndex(var1);
      }

      @Override
      public void addChangeListene(ChangeListener var1) {
         this.tabs.addChangeListener(var1);
      }

      public void setCloseHandler(KongaTabbedPaneCloseHandler var1) {
         this.tabs.setCloseHandler(var1);
      }
   }

   private interface TabControl {
      int getTabCount();

      void addTab(WindowSectionContent var1, JComponent var2);

      void removeTabAt(int var1);

      int getSelectedIndex();

      void setSelectedIndex(int var1);

      void addChangeListene(ChangeListener var1);

      UiProvider getUi();
   }

   private class TabPaneListener implements KongaTabbedPaneCloseHandler, ChangeListener {
      private int selectedIndex = -1;

      private TabPaneListener() {
      }

      @Override
      public void closeCurrentTabRequested(KongaTabbedPaneCloseEvent var1) {
         int var2 = var1.getSelectedIndex();
         WindowSectionContent var3 = var2 >= 0 ? WindowMultiContentDisplayer.this.contents.get(var2) : null;
         if (var3 != null) {
            WindowMultiContentDisplayer.this.windowSection.removeContent(var3);
         }
      }

      @Override
      public void closeAllTabsRequested(KongaTabbedPaneCloseEvent var1) {
      }

      @Override
      public void closeOtherTabsRequested(KongaTabbedPaneCloseEvent var1) {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         int var2 = WindowMultiContentDisplayer.this.tabPane.getSelectedIndex();
         if (var2 != this.selectedIndex) {
            int var3 = 0;

            for (int var4 = WindowMultiContentDisplayer.this.contents.size(); var3 < var4; var3++) {
               if (var3 != var2) {
                  WindowMultiContentDisplayer.this.contents.get(var3).revokeState(WindowElementState.ACTIVE);
               }
            }

            this.selectedIndex = var2;
            WindowSectionContent var5 = null;
            if (var2 != -1) {
               var5 = WindowMultiContentDisplayer.this.contents.get(var2);
               var5.appendState(WindowElementState.ACTIVE);
               WindowMultiContentDisplayer.this.windowSection.ensureVisible();
            }

            WindowMultiContentDisplayer.this.windowSection.fireActiveContentChanged(var5);
         }
      }
   }
}
