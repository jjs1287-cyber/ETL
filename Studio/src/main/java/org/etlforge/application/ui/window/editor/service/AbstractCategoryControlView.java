package org.etlforge.application.ui.window.editor.service;

import java.awt.Component;
import java.awt.Point;
import java.util.Collection;
import java.util.List;
import javax.swing.SwingUtilities;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.AbstractEditorSelectorWindow;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorSelectorWindow;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorThumbnailSelectorWindow;
import org.etlforge.application.ui.window.editor.QuickSwitchEditorWindow;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.graphics.RectangleCorner;
import org.etlforge.ui.widget.window.WindowCornerPosition;

abstract class AbstractCategoryControlView implements CategoryControlView {
   private static final int THUMBNAIL_SELECTOR_LIMIT = ApplicationConfiguration.getInt("EditorService.ThumbnailSelector.Limit", 9);
   private final CategoryControlModel model;
   private final boolean southPosition;
   private Component globalEditorSelectorTrigger;

   public AbstractCategoryControlView(CategoryControlModel var1, boolean var2) {
      this.model = var1;
      this.southPosition = var2;
   }

   protected final void setGlobalEditorSelectorTrigger(Component var1) {
      this.globalEditorSelectorTrigger = var1;
   }

   @Override
   public final EditorSelectorWindow openEditorSelector(ApplicationWindow var1, EditorCategory var2, KongaDialog var3) {
      EditorSelectorWindow var4 = this.createEditorSelector(var2, var3);
      WindowCornerPosition var5 = this.getSelectorWindowPosition(var2, var4);
      var4.open(var1.getFrame(), var5);

      for (Component var7 : this.getAllCategoryComponents()) {
         var4.addBlindSpot(var7);
      }

      return var4;
   }

   private EditorSelectorWindow createEditorSelector(EditorCategory var1, KongaDialog var2) {
      List var3 = this.model.getEditors(var1);
      EditorService var4 = this.model.getEditorService();
      AbstractEditorSelectorWindow var5;
      if (var3.size() <= THUMBNAIL_SELECTOR_LIMIT) {
         var5 = new EditorThumbnailSelectorWindow(var4, var3, this.southPosition);
      } else {
         QuickSwitchEditorWindow var6 = new QuickSwitchEditorWindow(var4, var3);
         var6.setIncludeSearchField(false);
         var5 = var6;
      }

      if (var2 != null) {
         var5.setDialog(var2);
      }

      if (var3.size() > 1) {
         var5.setSelectedEditor(var4.getActiveEditor());
         var5.setNotifications(this.model.getNotifications());
      }

      Component var7 = this.getActiveArea();
      var5.setActiveArea(var7);
      return var5;
   }

   private WindowCornerPosition getSelectorWindowPosition(EditorCategory var1, EditorSelectorWindow var2) {
      Component var3 = this.getCategoryComponent(var1);
      int var4 = -((var2.getSize().width - var3.getWidth()) / 2);
      byte var5 = 2;
      RectangleCorner var6;
      Point var7;
      if (this.southPosition) {
         var6 = RectangleCorner.LOWER_LEFT;
         var7 = new Point(var4, -var5);
      } else {
         var6 = RectangleCorner.UPPER_LEFT;
         var7 = new Point(var4, var3.getHeight() + var5);
      }

      SwingUtilities.convertPointToScreen(var7, var3);
      return new WindowCornerPosition(var6, var7);
   }

   protected abstract Component getCategoryComponent(EditorCategory var1);

   protected abstract Collection<Component> getAllCategoryComponents();

   protected abstract Component getActiveArea();

   @Override
   public final void openGlobalEditorSelector() {
      WindowCornerPosition var1 = this.getEditorListPosition();
      QuickSwitchEditorWindow var2 = QuickSwitchEditorWindow.openAt(this.model.getAppWin(), var1);
      if (var2 != null) {
         Component var3 = this.getActiveArea();
         var2.setActiveArea(var3);
      }
   }

   private WindowCornerPosition getEditorListPosition() {
      if (this.globalEditorSelectorTrigger == null) {
         return null;
      }

      RectangleCorner var1;
      Point var2;
      if (this.southPosition) {
         var1 = RectangleCorner.LOWER_LEFT;
         var2 = new Point(-80, -2);
      } else {
         var1 = RectangleCorner.UPPER_LEFT;
         var2 = new Point(-80, this.globalEditorSelectorTrigger.getHeight() - 2);
      }

      SwingUtilities.convertPointToScreen(var2, this.globalEditorSelectorTrigger);
      return new WindowCornerPosition(var1, var2);
   }
}
