package org.etlforge.ui.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class MouseActionListener implements MouseListener {
   @Override
   public final void mouseClicked(MouseEvent var1) {
      if (var1.getButton() == 1) {
         int var2 = var1.getClickCount();
         switch (var2) {
            case 1:
               this.onSingleClick(var1);
               break;
            case 2:
               this.onDoubleClick(var1);
         }
      }
   }

   protected void onSingleClick(MouseEvent var1) {
   }

   protected void onDoubleClick(MouseEvent var1) {
   }

   @Override
   public final void mousePressed(MouseEvent var1) {
      if (var1.isPopupTrigger()) {
         this.onPopupTrigger(var1);
      }
   }

   protected void onPopupTrigger(MouseEvent var1) {
   }

   @Override
   public final void mouseReleased(MouseEvent var1) {
      if (var1.isPopupTrigger()) {
         this.onPopupTrigger(var1);
      }
   }

   @Override
   public void mouseEntered(MouseEvent var1) {
   }

   @Override
   public void mouseExited(MouseEvent var1) {
   }
}
