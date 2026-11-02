package org.etlforge.application.ui.window.editor.service;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.ui.DelayedJob;

final class CategoryComponentMouseListener extends MouseAdapter {
   private final EditorCategory category;
   private final CategoryController controller;
   private DelayedJob timer;
   private boolean blocked;

   public CategoryComponentMouseListener(EditorCategory var1, CategoryController var2) {
      this.category = var1;
      this.controller = var2;
   }

   public void setBlocked(boolean var1) {
      this.blocked = var1;
      if (var1) {
         this.cancel();
      }
   }

   public void cancel() {
      if (this.timer != null) {
         this.timer.cancel();
         this.timer = null;
      }
   }

   @Override
   public void mousePressed(MouseEvent var1) {
      this.cancel();
   }

   @Override
   public void mouseEntered(MouseEvent var1) {
      this.cancel();
      if (!this.blocked) {
         int var2 = this.controller.getHoverDelay();
         this.timer = new DelayedJob(var2, new Runnable() {
            @Override
            public void run() {
               CategoryComponentMouseListener.this.controller.handleHover(CategoryComponentMouseListener.this.category);
               CategoryComponentMouseListener.this.timer = null;
            }
         });
         this.timer.schedule();
      }
   }

   @Override
   public void mouseExited(MouseEvent var1) {
      this.cancel();
   }
}
