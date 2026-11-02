package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.SupportedLookAndFeel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.ComponentPainters;
import org.etlforge.ui.widget.panel.KongaPanel;

final class CategoryControlPanel extends AbstractCategoryControlView implements UiProvider {
   private final CategoryControlModel model;
   private final CategoryController controller;
   private final JComponent activeArea;
   private final LinkedHashMap<EditorCategory, CategoryControlPanel.Button> categoryButtons;
   private final JButton showAllEditorsButton;
   private final JPanel ui;

   public CategoryControlPanel(CategoryControlModel var1, CategoryController var2, JComponent var3, boolean var4) {
      super(var1, var4);
      this.model = var1;
      this.controller = var2;
      this.activeArea = var3;
      this.categoryButtons = Maps.newLinkedHashMap();
      this.showAllEditorsButton = new JButton(new CategoryControlPanel.ShowAllEditorsAction(var2, var4));
      this.showAllEditorsButton.setVisible(false);
      this.showAllEditorsButton.setContentAreaFilled(false);
      this.ui = this.createEmptyUi(var4);
      var2.setView(this);
      var1.addListener(new CategoryControlPanel.ModelListener());
      this.setGlobalEditorSelectorTrigger(this.showAllEditorsButton);
   }

   private JPanel createEmptyUi(boolean var1) {
      KongaPanel var2 = new KongaPanel(new CategoryPanelLayout());
      if (var1) {
         var2.setPainter(ComponentPainters.verticalGradient(Color.LIGHT_GRAY, Colors.ETLFORGE_BLUE));
      }

      if (!SupportedLookAndFeel.CROSS_PLATFORM.isInstalled()) {
         var2.setBackground(Colors.ETLFORGE_BLUE);
      }

      return var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      ArrayList var1 = Lists.newArrayList(this.categoryButtons.values());
      if (!var1.isEmpty()) {
         JButton var2 = ((CategoryControlPanel.Button)var1.get(0)).button;
         var2.requestFocusInWindow();
      }
   }

   @Override
   protected Component getCategoryComponent(EditorCategory var1) {
      CategoryControlPanel.Button var2 = this.categoryButtons.get(var1);
      return var2.button;
   }

   @Override
   protected Collection<Component> getAllCategoryComponents() {
      ArrayList var1 = Lists.newArrayList();

      for (CategoryControlPanel.Button var3 : this.categoryButtons.values()) {
         var1.add(var3.button);
      }

      return var1;
   }

   @Override
   protected Component getActiveArea() {
      return this.activeArea;
   }

   private static class Button {
      private final EditorCategory category;
      public final JButton button;
      private final Action closeOthersAction;
      private final CategoryControlPanel.ButtonListener listener;

      public Button(EditorCategory var1, CategoryController var2, Action var3, Action var4) {
         this.category = var1;
         this.closeOthersAction = var4;
         this.button = this.createButton(var3);
         this.listener = new CategoryControlPanel.ButtonListener(var1, var2, this.button);
         this.installPopupMenu(var2, var3);
      }

      private JButton createButton(Action var1) {
         JButton var2 = new JButton();
         TextStyles.SmallListText.makeOver(var2);
         if (this.category.icon != null) {
            var2.setIcon(this.category.icon);
         } else {
            var2.setText(this.category.name);
         }

         return var2;
      }

      private void installPopupMenu(CategoryController var1, Action var2) {
         CategoryComponentPopupMenuFactory var3 = new CategoryComponentPopupMenuFactory(var1, this.listener.mouseHandler);
         JPopupMenu var4 = var3.createPopupMenu(var2, this.closeOthersAction);
         this.button.setComponentPopupMenu(var4);
      }

      public void dispose() {
         this.listener.dispose();
      }

      public void update(CategoryControlModel var1) {
         List var2 = var1.getCategories();
         this.updateButtonName(var1);
         this.updateCloseOthersAction(var2);
      }

      public void updateButtonName(CategoryControlModel var1) {
         String var2 = var1.getCategoryDisplayName(this.category);
         this.button.setText(var2);
      }

      private void updateCloseOthersAction(List<EditorCategory> var1) {
         this.closeOthersAction.setEnabled(var1.size() > 1);
      }

      public void updateCloseOthersAction(CategoryControlModel var1) {
         this.updateCloseOthersAction(var1.getCategories());
      }
   }

   private static class ButtonListener implements ActionListener {
      private final EditorCategory category;
      private final JButton button;
      private final CategoryController controller;
      public final CategoryComponentMouseListener mouseHandler;

      public ButtonListener(EditorCategory var1, CategoryController var2, JButton var3) {
         this.category = var1;
         this.controller = var2;
         this.button = var3;
         this.mouseHandler = new CategoryComponentMouseListener(var1, var2);
         var3.addMouseListener(this.mouseHandler);
         var3.addActionListener(this);
      }

      public void dispose() {
         this.button.removeActionListener(this);
         this.button.removeMouseListener(this.mouseHandler);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.controller.activateCategory(this.category);
      }
   }

   private class ModelListener implements CategoryControlModelListener {
      private ModelListener() {
      }

      @Override
      public void categoryOpened(EditorCategory var1) {
         CategoryControlPanel.Button var2 = this.createButton(var1);
         CategoryControlPanel.this.categoryButtons.put(var1, var2);
         this.updateShowAllEditorsAction();
         this.rebuild();
      }

      private CategoryControlPanel.Button createButton(EditorCategory var1) {
         CloseCategoryAction var2 = new CloseCategoryAction(var1, CategoryControlPanel.this.controller);
         CloseOtherCategoriesAction var3 = new CloseOtherCategoriesAction(var1, CategoryControlPanel.this.controller);
         var3.setEnabled(CategoryControlPanel.this.model.getCategories().size() > 1);
         return new CategoryControlPanel.Button(var1, CategoryControlPanel.this.controller, var2, var3);
      }

      private void updateShowAllEditorsAction() {
         boolean var1 = CategoryControlPanel.this.model.getEditorService().getNumberOfEditors() > 0;
         CategoryControlPanel.this.showAllEditorsButton.setVisible(var1);
      }

      private void rebuild() {
         CategoryControlPanel.this.ui.removeAll();

         for (CategoryControlPanel.Button var2 : CategoryControlPanel.this.categoryButtons.values()) {
            var2.updateCloseOthersAction(CategoryControlPanel.this.model);
            CategoryControlPanel.this.ui.add(var2.button);
         }

         CategoryControlPanel.this.ui.add(CategoryControlPanel.this.showAllEditorsButton);
         this.revalidate();
      }

      private void revalidate() {
         JComponent var1 = (JComponent)CategoryControlPanel.this.ui.getParent();
         var1.revalidate();
         var1.repaint();
      }

      @Override
      public void editorOpened(EditorCategory var1, Editor var2) {
         this.updateCategory(var1);
         this.updateShowAllEditorsAction();
      }

      private void updateCategory(EditorCategory var1) {
         CategoryControlPanel.Button var2 = CategoryControlPanel.this.categoryButtons.get(var1);
         var2.update(CategoryControlPanel.this.model);
      }

      @Override
      public void editorClosed(EditorCategory var1, Editor var2) {
         this.updateCategory(var1);
         this.updateShowAllEditorsAction();
      }

      @Override
      public void categoryClosed(EditorCategory var1) {
         CategoryControlPanel.Button var2 = CategoryControlPanel.this.categoryButtons.remove(var1);
         var2.dispose();
         this.updateShowAllEditorsAction();
         this.rebuild();
      }

      @Override
      public void editorPropertyChanged(EditorCategory var1, Editor var2) {
         CategoryControlPanel.Button var3 = CategoryControlPanel.this.categoryButtons.get(var1);
         var3.updateButtonName(CategoryControlPanel.this.model);
      }
   }

   private static class ShowAllEditorsAction extends AbstractAction {
      private final CategoryController controller;

      public ShowAllEditorsAction(CategoryController var1, boolean var2) {
         this.controller = var1;
         String var3 = var2 ? "ArrowUp.16" : "ArrowDown.16";
         this.putValue("SmallIcon", ApplicationResources.ICONS.getIcon(var3));
         this.putValue("ShortDescription", "Show all open pages");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ShowAllEditorsAction.this.controller.openGlobalEditorSelector();
            }
         });
      }
   }
}
