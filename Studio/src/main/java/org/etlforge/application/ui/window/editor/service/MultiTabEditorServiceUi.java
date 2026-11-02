package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.SupportedLookAndFeel;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;

final class MultiTabEditorServiceUi extends AbstractEditorServiceUi {
   private final CategoryControlModel categoryModel;
   private final CategoryController controller;
   private final Map<String, Editor> editorIdMap;
   private final KongaTabbedPane tabs;
   private final Map<EditorCategory, Integer> tabIndeces;
   private final List<CardPanel> tabCards;
   private final MultiTabEditorServiceUi.TabListener tabListener;
   private final MultiTabEditorServiceUi.Notifications notifications;

   public MultiTabEditorServiceUi(DefaultEditorService var1) {
      super(var1);
      this.categoryModel = new CategoryControlModel(var1.getAppWin(), var1);
      this.controller = new CategoryController(var1.getAppWin(), this.categoryModel);
      this.tabs = createTabs();
      this.tabListener = new MultiTabEditorServiceUi.TabListener();
      this.tabs.addChangeListener(this.tabListener);
      this.tabIndeces = Maps.newHashMap();
      this.tabCards = Lists.newArrayList();
      this.editorIdMap = Maps.newHashMap();
      this.categoryModel.addListener(new MultiTabEditorServiceUi.ModelListener());
      this.notifications = new MultiTabEditorServiceUi.Notifications(var1);
      this.controller.setView(new MultiTabEditorServiceUi.CategoryViewImpl());
   }

   private static KongaTabbedPane createTabs() {
      KongaTabbedPane var0 = new KongaTabbedPane();
      if (!SupportedLookAndFeel.CROSS_PLATFORM.isInstalled()) {
         var0.setOpaque(true);
         var0.setBackground(Colors.ETLFORGE_BLUE);
      }

      var0.disableCtrlUp();
      var0.setKeyboardNavigationEnabled(false);
      var0.setInterpretTitlesAsMnemonicStrings(false);
      return var0;
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

   private class CategoryViewImpl extends AbstractCategoryControlView {
      public CategoryViewImpl() {
         super(MultiTabEditorServiceUi.this.categoryModel, false);
      }

      @Override
      protected Component getCategoryComponent(EditorCategory var1) {
         int var2 = MultiTabEditorServiceUi.this.tabIndeces.get(var1);
         return MultiTabEditorServiceUi.this.tabs.getTabComponentAt(var2);
      }

      @Override
      protected Collection<Component> getAllCategoryComponents() {
         ArrayList var1 = Lists.newArrayList();

         for (int var2 = 0; var2 < MultiTabEditorServiceUi.this.tabs.getTabCount(); var2++) {
            var1.add(MultiTabEditorServiceUi.this.tabs.getTabComponentAt(var2));
         }

         return var1;
      }

      @Override
      protected Component getActiveArea() {
         int var1 = MultiTabEditorServiceUi.this.tabs.getSelectedIndex();
         CardPanel var2 = MultiTabEditorServiceUi.this.tabCards.get(var1);
         return var2.container();
      }
   }

   private class ModelListener implements CategoryControlModelListener {
      private final List<MultiTabEditorServiceUi.TabComponentController> tabControllers = Lists.newArrayList();

      private ModelListener() {
      }

      @Override
      public void categoryOpened(EditorCategory var1) {
         MultiTabEditorServiceUi.this.tabListener.internalChange = true;
         int var2 = MultiTabEditorServiceUi.this.tabs.getTabCount();
         Component var3 = this.addTab(var1);
         this.installTabController(var3, var1);
         this.updateTabControllers();
         MultiTabEditorServiceUi.this.tabIndeces.put(var1, var2);
         MultiTabEditorServiceUi.this.tabListener.internalChange = false;
      }

      private Component addTab(EditorCategory var1) {
         CardPanel var2 = new CardPanel();
         String var3 = MultiTabEditorServiceUi.this.categoryModel.getCategoryDisplayName(var1);
         MultiTabEditorServiceUi.this.tabs.addTab(var3, var1.icon, var2);
         MultiTabEditorServiceUi.this.tabCards.add(var2);
         Component var4 = MultiTabEditorServiceUi.this.tabs.getTabComponentAt(MultiTabEditorServiceUi.this.tabs.getTabCount() - 1);
         Fonts.convertToBold(var4);
         return var4;
      }

      private void installTabController(Component var1, EditorCategory var2) {
         MultiTabEditorServiceUi.TabComponentController var3 = MultiTabEditorServiceUi.this.new TabComponentController(var2, var1);
         this.tabControllers.add(var3);
      }

      private void updateTabControllers() {
         for (MultiTabEditorServiceUi.TabComponentController var2 : this.tabControllers) {
            var2.updateCloseOthersActions();
         }
      }

      @Override
      public void editorOpened(EditorCategory var1, Editor var2) {
         int var3 = this.switchToTabSilently(var1);
         CardPanel var4 = MultiTabEditorServiceUi.this.tabCards.get(var3);
         String var5 = var2.getId();
         var4.add(var2, var5);
         var4.show(var5);
         this.updateTabTitle(var1, var3);
         var2.requestFocus();
         this.updateTabControllers();
      }

      private int switchToTabSilently(EditorCategory var1) {
         MultiTabEditorServiceUi.this.tabListener.internalChange = true;
         int var2 = MultiTabEditorServiceUi.this.switchToTab(var1);
         MultiTabEditorServiceUi.this.tabListener.internalChange = false;
         return var2;
      }

      private void updateTabTitle(EditorCategory var1, int var2) {
         String var3 = MultiTabEditorServiceUi.this.categoryModel.getCategoryDisplayName(var1);
         MultiTabEditorServiceUi.this.tabs.setTitleAt(var2, var3);
      }

      @Override
      public void editorClosed(EditorCategory var1, Editor var2) {
         int var3 = MultiTabEditorServiceUi.this.tabIndeces.get(var1);
         CardPanel var4 = MultiTabEditorServiceUi.this.tabCards.get(var3);
         var4.remove(var2.getId());
         this.updateTabTitle(var1, var3);
         this.updateTabControllers();
      }

      @Override
      public void categoryClosed(EditorCategory var1) {
         MultiTabEditorServiceUi.this.tabListener.internalChange = true;
         int var2 = MultiTabEditorServiceUi.this.tabIndeces.remove(var1);
         MultiTabEditorServiceUi.TabComponentController var3 = this.tabControllers.remove(var2);
         var3.dispose();
         MultiTabEditorServiceUi.this.tabCards.remove(var2);
         MultiTabEditorServiceUi.this.tabs.removeTabAt(var2);
         this.rebuildTabToIndexMap();
         this.updateTabControllers();
         MultiTabEditorServiceUi.this.tabListener.internalChange = false;
      }

      private void rebuildTabToIndexMap() {
         MultiTabEditorServiceUi.this.tabIndeces.clear();
         List var1 = MultiTabEditorServiceUi.this.categoryModel.getCategories();

         for (int var2 = 0; var2 < var1.size(); var2++) {
            EditorCategory var3 = (EditorCategory)var1.get(var2);
            MultiTabEditorServiceUi.this.tabIndeces.put(var3, var2);
         }
      }

      @Override
      public void editorPropertyChanged(EditorCategory var1, Editor var2) {
         int var3 = MultiTabEditorServiceUi.this.tabIndeces.get(var1);
         this.updateTabTitle(var1, var3);
      }
   }

   private class Notifications extends TabNotificationService {
      public Notifications(EditorService var2) {
         super(var2, MultiTabEditorServiceUi.this);
      }

      @Override
      protected boolean noNeedToNotify(Editor var1) {
         return false;
      }

      @Override
      protected Component getTabComponent(Editor var1) {
         int var2 = this.getTabIndex(var1);
         return var2 >= 0 && var2 < MultiTabEditorServiceUi.this.tabs.getTabCount() ? MultiTabEditorServiceUi.this.tabs.getTabComponentAt(var2) : null;
      }

      private int getTabIndex(Editor var1) {
         EditorCategory var2 = MultiTabEditorServiceUi.this.categoryModel.categorize(var1);
         Integer var3 = MultiTabEditorServiceUi.this.tabIndeces.get(var2);
         return var3 == null ? -1 : var3;
      }

      @Override
      protected void notificationAdded(Editor var1) {
         MultiTabEditorServiceUi.this.categoryModel.addNotification(var1);
      }

      @Override
      protected void notificationStopped(Editor var1) {
         MultiTabEditorServiceUi.this.categoryModel.removeNotification(var1);
      }
   }

   private class TabComponentController extends MouseAdapter implements Disposable {
      private final EditorCategory category;
      private final Component component;
      private final CategoryComponentMouseListener hoverer;
      private final Action closeOthersAction;
      private final Action closeOtherEditorsInCategoryAction;

      public TabComponentController(EditorCategory var2, Component var3) {
         this.category = var2;
         this.component = var3;
         this.hoverer = new CategoryComponentMouseListener(var2, MultiTabEditorServiceUi.this.controller);
         var3.addMouseListener(this);
         var3.addMouseListener(this.hoverer);
         if (var3 instanceof JComponent) {
            this.closeOthersAction = new CloseOtherCategoriesAction(var2, MultiTabEditorServiceUi.this.controller);
            this.closeOtherEditorsInCategoryAction = new CloseOtherEditorsInCategory(var2, MultiTabEditorServiceUi.this.controller);
            JPopupMenu var4 = this.createPopupMenu(var2);
            ((JComponent)var3).setComponentPopupMenu(var4);
         } else {
            this.closeOthersAction = null;
            this.closeOtherEditorsInCategoryAction = null;
         }
      }

      private JPopupMenu createPopupMenu(EditorCategory var1) {
         CategoryComponentPopupMenuFactory var2 = new CategoryComponentPopupMenuFactory(MultiTabEditorServiceUi.this.controller, this.hoverer);
         CloseCategoryAction var3 = new CloseCategoryAction(var1, MultiTabEditorServiceUi.this.controller);
         return var2.createPopupMenu(var3, this.closeOtherEditorsInCategoryAction, this.closeOthersAction);
      }

      public void updateCloseOthersActions() {
         this.closeOthersAction.setEnabled(MultiTabEditorServiceUi.this.categoryModel.getCategories().size() > 1);
         this.closeOtherEditorsInCategoryAction.setEnabled(MultiTabEditorServiceUi.this.categoryModel.getNumberOfEditorsInCategory(this.category) > 1);
      }

      @Override
      public void dispose() {
         this.component.removeMouseListener(this);
         this.component.removeMouseListener(this.hoverer);
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         this.hoverer.cancel();
         if (!var1.isPopupTrigger() && !var1.isMetaDown()) {
            if (var1.isControlDown()) {
               MultiTabEditorServiceUi.this.switchToTab(this.category);
            } else {
               MultiTabEditorServiceUi.this.controller.activateCategory(this.category);
            }
         }
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         if (var1.isPopupTrigger()) {
            this.hoverer.cancel();
         }
      }
   }

   private class TabListener implements ChangeListener {
      public boolean internalChange;

      private TabListener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         int var2 = MultiTabEditorServiceUi.this.tabs.getSelectedIndex();
         MultiTabEditorServiceUi.this.categoryModel.setActiveIndex(var2);
         if (!this.internalChange) {
            this.updateEditorStates();
            MultiTabEditorServiceUi.this.getEditorService().fireActiveEditorChanged();
         }
      }

      private void updateEditorStates() {
         Editor var1 = MultiTabEditorServiceUi.this.getActiveEditor();
         MultiTabEditorServiceUi.this.updateEditorStates(var1);
      }
   }
}
