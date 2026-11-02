package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.SupportedLookAndFeel;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CardPanel;

public final class CardEditorServiceUi extends AbstractEditorServiceUi {
   private static final boolean CATEGORIES_POSITION_SOUTH = ApplicationConfiguration.getString("EditorService.CategoryPosition", "north")
      .equalsIgnoreCase("south");
   private final CardPanel cards = new CardPanel();
   private final Map<String, Editor> editorIdMap = Maps.newHashMap();
   private final CategoryControlModel categoryModel;
   private final CategoryControlPanel categoryView;
   private final CategoryController categoryController;
   private final BorderLayoutBuilder layout;

   public CardEditorServiceUi(DefaultEditorService var1) {
      super(var1);
      this.categoryModel = new CategoryControlModel(var1.getAppWin(), var1);
      this.categoryModel.setLongNameMode(false);
      this.categoryController = new CategoryController(var1.getAppWin(), this.categoryModel);
      this.categoryView = new CategoryControlPanel(this.categoryModel, this.categoryController, this.cards.container(), CATEGORIES_POSITION_SOUTH);
      this.layout = new BorderLayoutBuilder();
      if (!SupportedLookAndFeel.CROSS_PLATFORM.isInstalled()) {
         this.cards.container().setBackground(Colors.ETLFORGE_BLUE);
      }
   }

   @Override
   public void addEditor(Editor var1) {
      this.editorIdMap.put(var1.getId(), var1);
      if (this.editorIdMap.size() == 1) {
         this.addControlPanel();
      }

      this.categoryModel.openEditor(var1);
      this.createAndAddCard(var1);
      this.updateEditorStates(var1);
      this.getEditorService().fireActiveEditorChanged();
      var1.requestFocus();
   }

   private void createAndAddCard(Editor var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      var2.setBorder(new LineBorder(Color.GRAY));
      this.cards.add(var2, var1.getId());
      this.cards.show(var1.getId());
   }

   private void updateEditorStates(Editor var1) {
      for (Editor var3 : this.editorIdMap.values()) {
         if (var3 != var1) {
            var3.revokeState(WindowElementState.ACTIVE);
         }
      }

      var1.appendState(WindowElementState.ACTIVE);
   }

   @Override
   public Editor getActiveEditor() {
      String var1 = this.cards.getCurrentCard();
      return this.editorIdMap.get(var1);
   }

   @Override
   public boolean isDisplayingEditor(Editor var1) {
      return this.cards.contains(var1.getId());
   }

   @Override
   public void selectEditor(Editor var1) {
      this.selectEditor(var1, false);
   }

   private void selectEditor(Editor var1, boolean var2) {
      boolean var3 = var2 || !var1.getId().equals(this.cards.getCurrentCard());
      this.cards.show(var1.getId());
      if (var3) {
         this.getEditorService().fireActiveEditorChanged();
         this.updateEditorStates(var1);
      }
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
         this.cards.remove(var1.getId());
         if (this.editorIdMap.isEmpty()) {
            this.removeControlPanel();
         } else if (var2 != null) {
            this.selectEditor(var2, true);
         }
      }
   }

   private void removeControlPanel() {
      if (CATEGORIES_POSITION_SOUTH) {
         this.layout.removePosition("South");
      } else {
         this.layout.removePosition("North");
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
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layout.center(this.cards);
      this.addControlPanel();
      this.layout.decorate(var1);
   }

   private void addControlPanel() {
      if (CATEGORIES_POSITION_SOUTH) {
         this.layout.south(this.categoryView);
      } else {
         this.layout.north(this.categoryView);
      }

      this.layout.refreshLayout();
   }

   @Override
   public void requestFocus() {
      this.cards.requestFocus();
   }

   @Override
   public void openQuickSelector() {
      this.categoryController.openGlobalEditorSelector();
   }
}
