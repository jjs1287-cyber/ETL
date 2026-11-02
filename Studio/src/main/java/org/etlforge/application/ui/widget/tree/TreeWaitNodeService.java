package org.etlforge.application.ui.widget.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class TreeWaitNodeService {
   private final KongaTree tree;
   private final String waitNodeText;
   private TreeWaitNodeService.WaitNode waitNode;
   private Timer waitNodeUpdater;

   public TreeWaitNodeService(KongaTree var1, String var2) {
      this.tree = var1;
      this.waitNodeText = var2;
   }

   public void setWaiting(final boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (var1) {
               TreeWaitNodeService.this.startWait();
            } else {
               TreeWaitNodeService.this.stopWait();
            }
         }
      });
   }

   public void startWait() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (TreeWaitNodeService.this.waitNode == null) {
               TreeWaitNodeService.this.createWaitNode();
               TreeWaitNodeService.this.startWaitNode();
            }
         }
      });
   }

   private void createWaitNode() {
      this.waitNode = new TreeWaitNodeService.WaitNode();
      DefaultKongaTreeModel var1 = (DefaultKongaTreeModel)this.tree.getModel();
      var1.insertNode(this.waitNode, var1.getRoot());
      this.tree.expandPath(this.tree.getPath(this.tree.getModel().getRoot()));
   }

   private void startWaitNode() {
      KongaTreeNode var1 = this.tree.getModel().getRoot();
      int var2 = var1.getChildCount();
      if (var2 <= 1) {
         if (var2 != 1 || var1.getChildAt(0) == this.waitNode) {
            this.waitNodeUpdater = new Timer(250, new TreeWaitNodeService.WaitNodeUpdater());
            this.waitNodeUpdater.setRepeats(true);
            this.waitNodeUpdater.start();
         }
      }
   }

   public void stopWait() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (TreeWaitNodeService.this.waitNode != null) {
               TreeWaitNodeService.this.stopNodeUpdater();
               TreeWaitNodeService.this.removeWaitNode();
            }
         }
      });
   }

   private void stopNodeUpdater() {
      if (this.waitNodeUpdater != null) {
         this.waitNodeUpdater.stop();
         this.waitNodeUpdater = null;
      }
   }

   private void removeWaitNode() {
      this.waitNode.setUserObject(this.waitNodeText);
      if (this.waitNode.getParent() == this.tree.getModel().getRoot()) {
         ((DefaultKongaTreeModel)this.tree.getModel()).removeNode(this.waitNode);
      }

      this.waitNode = null;
   }

   private class WaitNode extends DefaultKongaTreeNode implements TreeNodeDecorator {
      public WaitNode() {
         super(null, TreeWaitNodeService.this.waitNodeText, false);
      }

      @Override
      public void decorate(TreeNodeDecor var1) {
         var1.setIcon(null);
         var1.setToolTip(null);
         var1.setTextStyle(TextStyles.DefaultText.asTextDecorator());
      }
   }

   private class WaitNodeUpdater implements ActionListener {
      private int count;

      private WaitNodeUpdater() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.count++;
         StringBuilder var2 = new StringBuilder(TreeWaitNodeService.this.waitNodeText);

         for (int var3 = 0; var3 < this.count; var3++) {
            var2.append(".");
         }

         TreeWaitNodeService.this.waitNode.setUserObject(var2);
         TreeWaitNodeService.this.tree.getModel().nodeChanged(TreeWaitNodeService.this.waitNode);
         if (this.count == 6) {
            this.count = 1;
         }
      }
   }
}
