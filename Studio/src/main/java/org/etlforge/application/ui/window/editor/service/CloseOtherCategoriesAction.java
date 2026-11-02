package org.etlforge.application.ui.window.editor.service;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.window.editor.EditorCategory;

final class CloseOtherCategoriesAction extends AbstractAction {
   private final EditorCategory category;
   private final CategoryController controller;

   public CloseOtherCategoriesAction(EditorCategory var1, CategoryController var2) {
      super("Close Other Tabs");
      this.category = var1;
      this.controller = var2;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CloseOtherCategoriesAction.this.controller.closeOtherCategories(CloseOtherCategoriesAction.this.category);
         }
      });
   }
}
