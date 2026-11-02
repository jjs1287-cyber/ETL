package org.etlforge.application.ui.window.editor.service;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.window.editor.EditorCategory;

final class CloseOtherEditorsInCategory extends AbstractAction {
   private final EditorCategory category;
   private final CategoryController controller;

   public CloseOtherEditorsInCategory(EditorCategory var1, CategoryController var2) {
      super(getName(var1));
      this.category = var1;
      this.controller = var2;
   }

   private static String getName(EditorCategory var0) {
      return var0 == CategoryControlModel.OTHER ? "Close Other Pages Under This Tab" : "Close Other " + var0.name;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CloseOtherEditorsInCategory.this.controller.closeOtherEditorsInCategory(CloseOtherEditorsInCategory.this.category);
         }
      });
   }
}
