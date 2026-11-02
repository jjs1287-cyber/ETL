package org.etlforge.application.ui.window.editor.service;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.window.editor.EditorCategory;

final class CloseCategoryAction extends AbstractAction {
   private final EditorCategory category;
   private final CategoryController controller;

   public CloseCategoryAction(EditorCategory var1, CategoryController var2) {
      super(createName(var1));
      this.category = var1;
      this.controller = var2;
   }

   private static String createName(EditorCategory var0) {
      String var1 = "Close All";
      if (!var0.name.equals("Other")) {
         var1 = var1 + " " + var0.name;
      }

      return var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CloseCategoryAction.this.controller.closeCategory(CloseCategoryAction.this.category);
         }
      });
   }
}
