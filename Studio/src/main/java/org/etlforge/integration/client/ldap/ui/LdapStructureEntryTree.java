package org.etlforge.integration.client.ldap.ui;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import org.etlforge.integration.ldap.structure.AttributeComparators;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.util.decorator.TreeNodeDecorRenderer;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public class LdapStructureEntryTree implements AttributeViewer {
   private final LdapStructureEntryNode entryNode;
   private final DefaultKongaTreeModel treeModel;
   private final KongaTree tree;
   private final TreeSelectionSource selectionSource;

   public LdapStructureEntryTree(LdapStructureEntryNode var1, boolean var2) {
      this.entryNode = var1;
      this.treeModel = this.createModel(var1, var2);
      this.tree = new KongaTree(this.treeModel);
      this.tree.setCellRenderer(new TreeNodeDecorRenderer());
      this.selectionSource = new TreeSelectionSource(this.tree);
   }

   public TreeSelectionSource asSelectionSource() {
      return this.selectionSource;
   }

   public void setSelectionMode(KongaTree.SelectionMode var1) {
      this.tree.setSelectionMode(var1);
   }

   public void setRootVisible(boolean var1) {
      this.tree.setRootVisible(var1);
   }

   public LdapStructureEntryNode getEntryNode() {
      return this.entryNode;
   }

   public LdapStructureAttribute[] getAllAttributes() {
      ArrayList var1 = new ArrayList();
      KongaTreeNode var2 = this.treeModel.getRoot();
      int var3 = 0;

      for (int var4 = var2.getChildCount(); var3 < var4; var3++) {
         var1.add(((AttributeNode)var2.getChildAt(var3)).getUserObject());
      }

      return (LdapStructureAttribute[]) var1.toArray(new LdapStructureAttribute[var1.size()]);
   }

   @Override
   public LdapStructureAttribute[] getSelectedAttributes() {
      ArrayList var1 = new ArrayList();

      for (KongaTreeNode var3 : this.tree.getSelectedNodes()) {
         if (var3 instanceof AttributeNode) {
            var1.add(((AttributeNode)var3).getUserObject());
         }
      }

      return (LdapStructureAttribute[]) var1.toArray(new LdapStructureAttribute[var1.size()]);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.tree));
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   @Override
   public void setDragEnabled() {
      if (!this.tree.getDragEnabled()) {
         this.installDrag();
      }
   }

   private DefaultKongaTreeModel createModel(LdapStructureEntryNode var1, boolean var2) {
      DefaultKongaTreeNode var3 = new DefaultKongaTreeNode(var1.getName(), true);

      for (LdapStructureAttribute var5 : this.getAttributesToInclude(var1, var2)) {
         AttributeNode.create(var3, var5);
      }

      return new DefaultKongaTreeModel(var3);
   }

   private Collection<LdapStructureAttribute> getAttributesToInclude(LdapStructureEntryNode var1, boolean var2) {
      List var3 = var2 ? var1.getSelectedAttributes() : var1.getAttributes();
      Collections.sort(var3, AttributeComparators.nameOrder(true));
      return var3;
   }

   private void installDrag() {
      this.tree.setTransferHandler(new LdapStructureEntryTree.DragHandler());
      this.tree.setDragEnabled(true);
      DragSource var1 = new DragSource();
      var1.createDefaultDragGestureRecognizer(this.tree, 1, new DragGestureListener() {
         @Override
         public void dragGestureRecognized(DragGestureEvent var1) {
            LdapStructureEntryTree.this.tree.getTransferHandler().exportAsDrag(LdapStructureEntryTree.this.tree, var1.getTriggerEvent(), 1);
         }
      });
   }

   private class DragHandler extends TransferHandler {
      private DragHandler() {
      }

      @Override
      protected Transferable createTransferable(JComponent var1) {
         LdapStructureAttribute[] var2 = LdapStructureEntryTree.this.getSelectedAttributes();
         return var2.length == 1 ? new TransferableAttribute(var2[0]) : null;
      }

      @Override
      public int getSourceActions(JComponent var1) {
         return 1;
      }
   }
}
