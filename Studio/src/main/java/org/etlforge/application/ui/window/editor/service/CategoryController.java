package org.etlforge.application.ui.window.editor.service;

import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Dialog.ModalityType;
import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorSelectorWindow;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.ui.dialog.KongaDialog;

final class CategoryController {
   private static final int HOVER_DELAY = 1000;
   private final ApplicationWindow appWin;
   private final CategoryControlModel model;
   private CategoryControlView view;
   private EditorSelectorWindow editorSelectorWindow;
   private KongaDialog selectorDialog;
   private EditorCategory currentInvoker;
   private boolean hovererBlocked;

   public CategoryController(ApplicationWindow var1, CategoryControlModel var2) {
      this.appWin = var1;
      this.model = var2;
   }

   public void setView(CategoryControlView var1) {
      this.view = var1;
   }

   public void activateCategory(EditorCategory var1) {
      List var2 = this.model.getEditors(var1);
      assert !var2.isEmpty();
      if (var2.size() == 1) {
         this.switchToSingleEditorCategory((Editor)var2.get(0));
      } else {
         this.openEditorSelector(var1);
      }
   }

   private void switchToSingleEditorCategory(final Editor var1) {
      this.closeEditorSelector();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CategoryController.this.model.getEditorService().openEditor(var1);
         }
      });
   }

   public void closeEditorSelector() {
      if (this.editorSelectorWindow != null) {
         this.editorSelectorWindow.close();
      }
   }

   public void handleHover(EditorCategory var1) {
      EditorCategory var2 = this.model.getActiveCategory();
      if (var1 == var2 && this.model.getNumberOfEditorsInCategory(var1) == 1) {
         this.closeEditorSelector();
      } else if (!this.isMouseInsideSelectorWindow()) {
         List var3 = this.model.getEditors(var1);
         if (var3.size() > 0) {
            this.openEditorSelector(var1);
         }
      }
   }

   private boolean isMouseInsideSelectorWindow() {
      if (this.editorSelectorWindow != null) {
         PointerInfo var1 = MouseInfo.getPointerInfo();
         Point var2 = var1.getLocation();
         return this.editorSelectorWindow.containsPoint(var2);
      } else {
         return false;
      }
   }

   public void openGlobalEditorSelector() {
      this.closeEditorSelector();
      this.view.openGlobalEditorSelector();
   }

   public void openEditorSelector(EditorCategory var1) {
      if (var1 != this.currentInvoker || this.editorSelectorWindow == null) {
         if (this.editorSelectorWindow != null) {
            this.editorSelectorWindow.restoreOriginalEditor();
            this.editorSelectorWindow.dispose();
         }

         if (this.selectorDialog == null) {
            this.selectorDialog = this.createSelectorDialog();
         }

         this.editorSelectorWindow = this.view.openEditorSelector(this.appWin, var1, this.selectorDialog);
         this.currentInvoker = var1;
      }
   }

   private KongaDialog createSelectorDialog() {
      KongaDialog var1 = new KongaDialog(this.appWin.getFrame(), "", ModalityType.MODELESS) {
         @Override
         public void setVisible(boolean var1) {
            if (!var1) {
               this.cleanup();
            }

            super.setVisible(var1);
         }

         @Override
         protected void onDialogClose() {
            this.cleanup();
            super.onDialogClose();
         }

         @Override
         protected void doAdditionalWorkOnDispose() {
            this.cleanup();
         }

         private void cleanup() {
            CategoryController.this.selectorDialog = null;
            CategoryController.this.editorSelectorWindow = null;
            CategoryController.this.currentInvoker = null;
         }
      };
      var1.setUndecorated(true);
      var1.setFocusable(false);
      return var1;
   }

   public boolean isHovererBlocked() {
      return this.hovererBlocked;
   }

   public void setHovererBlocked(boolean var1) {
      this.hovererBlocked = var1;
   }

   public int getHoverDelay() {
      if (this.hovererBlocked) {
         return -1;
      }

      short var1 = 1000;
      if (this.isEditorSelectorOpen()) {
         var1 /= 3;
      }

      return var1;
   }

   private boolean isEditorSelectorOpen() {
      return this.editorSelectorWindow != null;
   }

   public void closeCategory(EditorCategory var1) {
      this.model.closeCategory(var1);
   }

   public void closeOtherCategories(EditorCategory var1) {
      for (EditorCategory var3 : this.model.getCategories()) {
         if (var3 != var1) {
            this.model.closeCategory(var3);
         }
      }
   }

   public void closeOtherEditorsInCategory(EditorCategory var1) {
      EditorService var2 = this.appWin.getEditorService();
      Editor var3 = var2.getActiveEditor();
      if (var3 != null) {
         EditorCategory var4 = this.model.categorize(var3);
         if (var1.equals(var4)) {
            List var5 = this.model.getEditors(var1);
            var5.remove(var3);
            if (!var5.isEmpty()) {
               var2.closeEditors(var5);
            }
         }
      }
   }
}
