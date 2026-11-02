package org.etlforge.ui.widget.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.swing.tree.TreePath;
import org.etlforge.ui.DelayedJob;

final class MouseRenameListener extends MouseAdapter {
   private final KongaTree tree;
   private Timer timer1;
   private Timer timer2;
   private KongaTreeNode currentNode;
   private boolean isActive;

   public MouseRenameListener(KongaTree var1) {
      this.tree = var1;
   }

   @Override
   public void mousePressed(MouseEvent var1) {
      if (var1.isPopupTrigger()) {
         this.cancel();
      }
   }

   public void cancel() {
      if (this.timer1 != null) {
         this.timer1.stop();
         this.timer1 = null;
      }

      if (this.timer2 != null) {
         this.timer2.stop();
         this.timer2 = null;
      }

      this.isActive = false;
      this.currentNode = null;
   }

   @Override
   public void mouseReleased(MouseEvent var1) {
      if (var1.isPopupTrigger()) {
         this.cancel();
      }
   }

   @Override
   public void mouseClicked(MouseEvent var1) {
      if (!var1.isShiftDown() && !var1.isControlDown()) {
         TreePath var2 = this.tree.getPathForLocation(var1.getX(), var1.getY());
         if (var2 != null) {
            Object var3 = var2.getLastPathComponent();
            if (var3 instanceof KongaTreeNode) {
               KongaTreeNode var4 = (KongaTreeNode)var3;
               if (var4 != this.currentNode || !this.isActive) {
                  this.setNode(var4);
               } else if (this.isActive) {
                  this.doRename(this.currentNode);
                  this.cancel();
               }
            }
         }
      } else {
         this.cancel();
      }
   }

   private void setNode(KongaTreeNode var1) {
      this.cancel();
      this.currentNode = var1;
      ActionListener var2 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            MouseRenameListener.this.isActive = true;
            ActionListener var2x = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1) {
                  MouseRenameListener.this.currentNode = null;
                  MouseRenameListener.this.isActive = false;
               }
            };
            MouseRenameListener.this.timer2 = new Timer(2000, var2x);
            MouseRenameListener.this.timer2.setRepeats(false);
            MouseRenameListener.this.timer2.start();
         }
      };
      this.timer1 = new Timer(300, var2);
      this.timer1.setRepeats(false);
      this.timer1.start();
   }

   private void doRename(final KongaTreeNode var1) {
      DelayedJob.createAndSchedule(500, new Runnable() {
         @Override
         public void run() {
            MouseRenameListener.this.tree.onRenameNode(var1);
         }
      });
   }
}
