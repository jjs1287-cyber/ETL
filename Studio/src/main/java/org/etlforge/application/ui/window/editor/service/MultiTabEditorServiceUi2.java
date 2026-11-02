package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.widget.SimpleTabPane;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.util.KeyUtils;

final class MultiTabEditorServiceUi2 extends AbstractEditorServiceUi {
   public static final boolean SUPPORTED = SimpleTabPane.SUPPORTED;
   private final CategoryControlModel categoryModel;
   private final CategoryController controller;
   private final Map<String, Editor> editorIdMap;
   private final SimpleTabPane tabs;
   private final Map<EditorCategory, Integer> tabIndeces;
   private final List<CardPanel> tabCards;
   private final MultiTabEditorServiceUi2.TabListener tabListener;
   private final MultiTabEditorServiceUi2.Notifications notifications;
   private final MultiTabEditorServiceUi2.ModelListener modelListener;

   public MultiTabEditorServiceUi2(DefaultEditorService var1) {
      super(var1);
      this.categoryModel = new CategoryControlModel(var1.getAppWin(), var1);
      this.controller = new CategoryController(var1.getAppWin(), this.categoryModel);
      this.tabs = new MultiTabEditorServiceUi2.TabPaneImpl();
      this.tabListener = new MultiTabEditorServiceUi2.TabListener();
      this.tabs.addChangeListener(this.tabListener);
      this.tabIndeces = Maps.newHashMap();
      this.tabCards = Lists.newArrayList();
      this.editorIdMap = Maps.newHashMap();
      this.modelListener = new MultiTabEditorServiceUi2.ModelListener();
      this.categoryModel.addListener(this.modelListener);
      this.notifications = new MultiTabEditorServiceUi2.Notifications(var1);
      this.controller.setView(new MultiTabEditorServiceUi2.CategoryViewImpl());
   }

   @Override
   public void addEditor(Editor var1) {
      this.editorIdMap.put(var1.getId(), var1);
      this.categoryModel.openEditor(var1);
      this.updateEditorStates(var1);
      this.getEditorService().fireActiveEditorChanged();
   }

   private void updateEditorStates(Editor var1) {
      for (Editor var3 : this.editorIdMap.values()) {
         if (var3 != var1) {
            var3.revokeState(WindowElementState.ACTIVE);
         }
      }

      if (var1 != null) {
         var1.appendState(WindowElementState.ACTIVE);
      }
   }

   @Override
   public Editor getActiveEditor() {
      if (this.tabs.getTabCount() == 0) {
         return null;
      }

      int var1 = this.tabs.getSelectedIndex();
      CardPanel var2 = this.tabCards.get(var1);
      String var3 = var2.getCurrentCard();
      return this.editorIdMap.get(var3);
   }

   @Override
   public boolean isDisplayingEditor(Editor var1) {
      return this.editorIdMap.containsKey(var1.getId());
   }

   @Override
   public void selectEditor(Editor var1) {
      if (var1 != this.getActiveEditor()) {
         this.selectEditorImpl(var1);
      }
   }

   private void selectEditorImpl(Editor var1) {
      this.tabListener.internalChange = true;
      EditorCategory var2 = this.categoryModel.categorize(var1);
      int var3 = this.switchToTab(var2);
      CardPanel var4 = this.tabCards.get(var3);
      var4.show(var1.getId());
      this.getEditorService().fireActiveEditorChanged();
      this.updateEditorStates(var1);
      this.tabListener.internalChange = false;
   }

   private int switchToTab(EditorCategory var1) {
      int var2 = this.tabIndeces.get(var1);
      this.tabs.setSelectedIndex(var2);
      return var2;
   }

   @Override
   public int getNumberOfOpenEditors() {
      return this.editorIdMap.size();
   }

   @Override
   public List<Editor> getOpenEditors() {
      return Lists.newArrayList(this.editorIdMap.values());
   }

   @Override
   public void updateTitleAndIcon(Editor var1) {
   }

   @Override
   public void removeEditor(Editor var1, Editor var2) {
      if (this.editorIdMap.remove(var1.getId()) != null) {
         this.categoryModel.closeEditor(var1);
         if (var2 != null) {
            this.selectEditorImpl(var2);
         }
      }
   }

   @Override
   public void addDetachedEditor(Editor var1) {
      this.addEditor(var1);
   }

   @Override
   public void detachEditor(Editor var1) {
      this.getEditorService().openEditor(var1);
   }

   @Override
   public void addNotification(Editor var1) {
      this.notifications.addNotification(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tabs.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tabs.requestFocus();
   }

   @Override
   public void prepareForUiSwitch() {
      for (CardPanel var2 : this.tabCards) {
         var2.removeAll();
      }
   }

   private class ActivatorKeyAction extends AbstractAction {
      private final int index;

      public ActivatorKeyAction(int var2) {
         assert var2 >= 1 && var2 <= 10 : "index is 1-based";
         this.index = var2;
         int var3 = var2 == 10 ? 48 : 48 + var2;
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(var3));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int var2 = this.index - 1;
         if (var2 < MultiTabEditorServiceUi2.this.categoryModel.getNumberOfCategories()) {
            EditorCategory var3 = MultiTabEditorServiceUi2.this.categoryModel.getCategoryAt(var2);
            MultiTabEditorServiceUi2.this.controller.activateCategory(var3);
         }
      }
   }

   private class CategoryViewImpl extends AbstractCategoryControlView {
      public CategoryViewImpl() {
         super(MultiTabEditorServiceUi2.this.categoryModel, false);
      }

      @Override
      protected Component getCategoryComponent(EditorCategory var1) {
         int var2 = MultiTabEditorServiceUi2.this.tabIndeces.get(var1);
         return MultiTabEditorServiceUi2.this.tabs.getTabComponentAt(var2);
      }

      @Override
      protected Collection<Component> getAllCategoryComponents() {
         ArrayList var1 = Lists.newArrayList();

         for (int var2 = 0; var2 < MultiTabEditorServiceUi2.this.tabs.getTabCount(); var2++) {
            var1.add(MultiTabEditorServiceUi2.this.tabs.getTabComponentAt(var2));
         }

         return var1;
      }

      @Override
      protected Component getActiveArea() {
         int var1 = MultiTabEditorServiceUi2.this.tabs.getSelectedIndex();
         CardPanel var2 = MultiTabEditorServiceUi2.this.tabCards.get(var1);
         return var2.container();
      }
   }

   private class ModelListener implements CategoryControlModelListener {
      private final List<MultiTabEditorServiceUi2.TabComponentController> tabControllers = Lists.newArrayList();

      private ModelListener() {
      }

      @Override
      public void categoryOpened(EditorCategory var1) {
         MultiTabEditorServiceUi2.this.tabListener.internalChange = true;
         int var2 = MultiTabEditorServiceUi2.this.tabs.getTabCount();
         JComponent var3 = this.addTab(var1);
         this.installTabController(var3, var1);
         this.updateTabControllers();
         MultiTabEditorServiceUi2.this.tabIndeces.put(var1, var2);
         MultiTabEditorServiceUi2.this.tabListener.internalChange = false;
      }

      private JComponent addTab(EditorCategory var1) {
         int var2 = MultiTabEditorServiceUi2.this.tabs.getTabCount();
         CardPanel var3 = new CardPanel();
         MultiTabEditorServiceUi2.this.tabCards.add(var3);
         String var4 = MultiTabEditorServiceUi2.this.categoryModel.getCategoryDisplayName(var1);
         MultiTabEditorServiceUi2.this.tabs.addTab(var4, var1.icon, var3.container());
         return MultiTabEditorServiceUi2.this.tabs.getTabComponentAt(var2);
      }

      private void installTabController(JComponent var1, EditorCategory var2) {
         MultiTabEditorServiceUi2.TabComponentController var3 = MultiTabEditorServiceUi2.this.new TabComponentController(var2, var1);
         this.tabControllers.add(var3);
      }

      public void updateTabControllers() {
         for (MultiTabEditorServiceUi2.TabComponentController var2 : this.tabControllers) {
            var2.updateActions();
         }
      }

      @Override
      public void editorOpened(EditorCategory var1, Editor var2) {
         int var3 = this.switchToTabSilently(var1);
         CardPanel var4 = MultiTabEditorServiceUi2.this.tabCards.get(var3);
         String var5 = var2.getId();
         var4.add(var2, var5);
         var4.show(var5);
         this.updateTabTitle(var1, var3);
         var2.requestFocus();
         this.updateTabControllers();
      }

      private int switchToTabSilently(EditorCategory var1) {
         MultiTabEditorServiceUi2.this.tabListener.internalChange = true;
         int var2 = MultiTabEditorServiceUi2.this.switchToTab(var1);
         MultiTabEditorServiceUi2.this.tabListener.internalChange = false;
         return var2;
      }

      private void updateTabTitle(EditorCategory var1, int var2) {
         String var3 = MultiTabEditorServiceUi2.this.categoryModel.getCategoryDisplayName(var1);
         MultiTabEditorServiceUi2.this.tabs.setTitleAt(var2, var3);
      }

      @Override
      public void editorClosed(EditorCategory var1, Editor var2) {
         int var3 = MultiTabEditorServiceUi2.this.tabIndeces.get(var1);
         CardPanel var4 = MultiTabEditorServiceUi2.this.tabCards.get(var3);
         var4.remove(var2.getId());
         this.updateTabTitle(var1, var3);
         this.updateTabControllers();
      }

      @Override
      public void categoryClosed(EditorCategory var1) {
         MultiTabEditorServiceUi2.this.tabListener.internalChange = true;
         int var2 = MultiTabEditorServiceUi2.this.tabIndeces.remove(var1);
         this.tabControllers.remove(var2);
         MultiTabEditorServiceUi2.this.tabCards.remove(var2);
         MultiTabEditorServiceUi2.this.tabs.removeTabAt(var2);
         this.rebuildTabToIndexMap();
         this.updateTabControllers();
         MultiTabEditorServiceUi2.this.tabListener.internalChange = false;
      }

      private void rebuildTabToIndexMap() {
         MultiTabEditorServiceUi2.this.tabIndeces.clear();
         List var1 = MultiTabEditorServiceUi2.this.categoryModel.getCategories();

         for (int var2 = 0; var2 < var1.size(); var2++) {
            EditorCategory var3 = (EditorCategory)var1.get(var2);
            MultiTabEditorServiceUi2.this.tabIndeces.put(var3, var2);
         }
      }

      @Override
      public void editorPropertyChanged(EditorCategory var1, Editor var2) {
         int var3 = MultiTabEditorServiceUi2.this.tabIndeces.get(var1);
         this.updateTabTitle(var1, var3);
      }
   }

   private class Notifications extends TabNotificationService {
      public Notifications(EditorService var2) {
         super(var2, MultiTabEditorServiceUi2.this);
      }

      @Override
      protected boolean noNeedToNotify(Editor var1) {
         return false;
      }

      @Override
      protected Component getTabComponent(Editor var1) {
         int var2 = this.getTabIndex(var1);
         return var2 >= 0 && var2 < MultiTabEditorServiceUi2.this.tabs.getTabCount() ? MultiTabEditorServiceUi2.this.tabs.getTabComponentAt(var2) : null;
      }

      private int getTabIndex(Editor var1) {
         EditorCategory var2 = MultiTabEditorServiceUi2.this.categoryModel.categorize(var1);
         Integer var3 = MultiTabEditorServiceUi2.this.tabIndeces.get(var2);
         return var3 == null ? -1 : var3;
      }

      @Override
      protected void notificationAdded(Editor var1) {
         MultiTabEditorServiceUi2.this.categoryModel.addNotification(var1);
      }

      @Override
      protected void notificationStopped(Editor var1) {
         MultiTabEditorServiceUi2.this.categoryModel.removeNotification(var1);
      }
   }

   private class ShowTabContextMenuAction extends AbstractAction {
      private final int index;

      public ShowTabContextMenuAction(int var2) {
         assert var2 >= 1 && var2 <= 10 : "index is 1-based";
         this.index = var2;
         int var3 = var2 == 10 ? 48 : 48 + var2;
         this.putValue("AcceleratorKey", KeyUtils.getCtrlShiftDown(var3));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int var2 = this.index - 1;
         if (var2 < MultiTabEditorServiceUi2.this.tabs.getTabCount()) {
            JLabel var3 = MultiTabEditorServiceUi2.this.tabs.getTabComponentAt(var2);
            JPopupMenu var4 = var3.getComponentPopupMenu();
            if (var4 != null) {
               var4.show(var3, var3.getWidth() / 4, var3.getHeight() / 2);
            }
         }
      }
   }

   private class TabComponentController {
      private final EditorCategory category;
      private final Action closeAction;
      private final Action closeOthersAction;
      private final Action closeOtherEditorsInCategoryAction;

      public TabComponentController(EditorCategory var2, JComponent var3) {
         this.category = var2;
         this.closeAction = new CloseCategoryAction(var2, MultiTabEditorServiceUi2.this.controller);
         this.closeOthersAction = new CloseOtherCategoriesAction(var2, MultiTabEditorServiceUi2.this.controller);
         this.closeOtherEditorsInCategoryAction = new CloseOtherEditorsInCategory(var2, MultiTabEditorServiceUi2.this.controller);
         JPopupMenu var4 = this.createPopupMenu(var2);
         var3.setComponentPopupMenu(var4);
      }

      private JPopupMenu createPopupMenu(EditorCategory var1) {
         CategoryComponentPopupMenuFactory var2 = new CategoryComponentPopupMenuFactory(MultiTabEditorServiceUi2.this.controller);
         return var2.createPopupMenu(this.closeAction, this.closeOtherEditorsInCategoryAction, this.closeOthersAction);
      }

      public void updateActions() {
         this.closeOthersAction.setEnabled(MultiTabEditorServiceUi2.this.categoryModel.getNumberOfCategories() > 1);
         EditorCategory var1 = MultiTabEditorServiceUi2.this.categoryModel.getActiveCategory();
         this.closeOtherEditorsInCategoryAction
            .setEnabled(this.category == var1 && MultiTabEditorServiceUi2.this.categoryModel.getNumberOfEditorsInCategory(var1) > 1);
      }
   }

   private class TabListener implements ChangeListener {
      public boolean internalChange;

      private TabListener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         int var2 = MultiTabEditorServiceUi2.this.tabs.getSelectedIndex();
         MultiTabEditorServiceUi2.this.categoryModel.setActiveIndex(var2);
         MultiTabEditorServiceUi2.this.modelListener.updateTabControllers();
         if (!this.internalChange) {
            this.updateEditorStates();
            MultiTabEditorServiceUi2.this.getEditorService().fireActiveEditorChanged();
         }
      }

      private void updateEditorStates() {
         Editor var1 = MultiTabEditorServiceUi2.this.getActiveEditor();
         MultiTabEditorServiceUi2.this.updateEditorStates(var1);
      }
   }

   private class TabPaneImpl extends SimpleTabPane {
      public TabPaneImpl() {
         for (int var2 = 1; var2 <= 10; var2++) {
            this.installKeyBinding(MultiTabEditorServiceUi2.this.new ActivatorKeyAction(var2), 2);
            this.installKeyBinding(MultiTabEditorServiceUi2.this.new ShowTabContextMenuAction(var2), 2);
         }
      }

      @Override
      protected void handleLeftClick(MouseEvent var1, int var2) {
         EditorCategory var3 = MultiTabEditorServiceUi2.this.categoryModel.getCategoryAt(var2);
         if (var1.isControlDown()) {
            MultiTabEditorServiceUi2.this.switchToTab(var3);
         } else {
            MultiTabEditorServiceUi2.this.controller.activateCategory(var3);
         }
      }

      @Override
      protected void handleHover(final int var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditorCategory var1x = MultiTabEditorServiceUi2.this.categoryModel.getCategoryAt(var1);
               MultiTabEditorServiceUi2.this.controller.handleHover(var1x);
            }
         });
      }

      @Override
      public int getHoverDelay() {
         return MultiTabEditorServiceUi2.this.controller.getHoverDelay();
      }
   }
}
