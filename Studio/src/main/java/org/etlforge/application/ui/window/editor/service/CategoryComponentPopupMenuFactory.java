package org.etlforge.application.ui.window.editor.service;

import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class CategoryComponentPopupMenuFactory {
   private final CategoryController controller;
   private final CategoryComponentMouseListener hoverer;

   public CategoryComponentPopupMenuFactory(CategoryController var1) {
      this(var1, null);
   }

   public CategoryComponentPopupMenuFactory(CategoryController var1, CategoryComponentMouseListener var2) {
      this.controller = var1;
      this.hoverer = var2;
   }

   public JPopupMenu createPopupMenu(Action... var1) {
      KongaPopupMenu var2 = new KongaPopupMenu();

      for (Action var6 : var1) {
         var2.add(var6);
      }

      var2.addPopupMenuListener(new PopupMenuListener() {
         @Override
         public void popupMenuWillBecomeVisible(PopupMenuEvent var1) {
            this.setHovererBlocked(true);
            CategoryComponentPopupMenuFactory.this.controller.closeEditorSelector();
         }

         @Override
         public void popupMenuWillBecomeInvisible(PopupMenuEvent var1) {
            this.setHovererBlocked(false);
         }

         @Override
         public void popupMenuCanceled(PopupMenuEvent var1) {
            this.setHovererBlocked(false);
         }

         private void setHovererBlocked(boolean var1) {
            CategoryComponentPopupMenuFactory.this.controller.setHovererBlocked(var1);
            if (CategoryComponentPopupMenuFactory.this.hoverer != null) {
               CategoryComponentPopupMenuFactory.this.hoverer.setBlocked(var1);
            }
         }
      });
      return var2;
   }
}
