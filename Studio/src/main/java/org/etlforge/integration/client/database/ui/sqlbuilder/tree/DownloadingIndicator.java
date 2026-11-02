package org.etlforge.integration.client.database.ui.sqlbuilder.tree;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.Timer;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;

class DownloadingIndicator implements ActionListener {
   private final JXTreeTable treeTable;
   private final MutableTreeTableNode parent;
   private final DownloadingIndicatorNode node;
   private Timer timer;

   private DownloadingIndicator(JXTreeTable var1, MutableTreeTableNode var2, String var3) {
      this.treeTable = var1;
      this.parent = var2;
      this.node = new DownloadingIndicatorNode(var3);
      this.timer = new Timer(125, this);
   }

   public DownloadingIndicator(JXTreeTable var1, RootNode var2) {
      this(var1, var2, "Downloading list of tables");
   }

   public DownloadingIndicator(JXTreeTable var1, DatabaseTableNode var2) {
      this(var1, var2, "Downloading columns");
   }

   public void start() {
      this.timer.start();
      this.addIndicatorNodeToParent();
   }

   private void addIndicatorNodeToParent() {
      ArrayList var1 = Lists.newArrayList();
      Enumeration var2 = this.parent.children();

      while (var2.hasMoreElements()) {
         MutableTreeTableNode var3 = (MutableTreeTableNode)var2.nextElement();
         var1.add(var3);
      }

      DefaultTreeTableModel var5 = (DefaultTreeTableModel)this.treeTable.getTreeTableModel();

      for (Object __etlItem50 : var1) {
         MutableTreeTableNode var4 = (MutableTreeTableNode) __etlItem50;
         var5.removeNodeFromParent(var4);
      }

      var5.insertNodeInto(this.node, this.parent, 0);
   }

   public void stop() {
      this.timer.stop();
      DefaultTreeTableModel var1 = (DefaultTreeTableModel)this.treeTable.getTreeTableModel();
      if (this.node.getParent() != null) {
         var1.removeNodeFromParent(this.node);
      }

      this.timer = null;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.node.update();
      DbObjectsTreeModel var2 = (DbObjectsTreeModel)this.treeTable.getTreeTableModel();
      if (var2.isPartOfModel(this.node)) {
         var2.setValueAt(this.node.toString(), this.node, 0);
      }
   }
}
