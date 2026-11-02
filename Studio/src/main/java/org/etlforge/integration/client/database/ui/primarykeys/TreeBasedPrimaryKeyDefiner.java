package org.etlforge.integration.client.database.ui.primarykeys;

import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.dialog.ComponentDialogTrigger;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeCellRenderer;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public class TreeBasedPrimaryKeyDefiner implements PrimaryKeyDefiner {
   private static boolean REMOVE_ALL_KEYS_SUPPORTED = false;
   private final KongaTree tree;
   private final Enablable selectionDependant;
   private PrimaryKeyTreeDefinerModel treeModel;
   private ComponentDialogTrigger dialogTrigger;
   private final TreeBasedPrimaryKeyDefiner.EditAction editAction;
   private JButton editButton;

   public TreeBasedPrimaryKeyDefiner(DatabaseObject[] var1, Enablable var2) {
      this.selectionDependant = var2;
      this.editAction = new TreeBasedPrimaryKeyDefiner.EditAction();
      this.treeModel = PrimaryKeyTreeDefinerModel.newModel(var1);
      this.tree = this.createTree(this.treeModel);
      this.createEditTrigger();
   }

   private KongaTree createTree(KongaTreeModel var1) {
      KongaTree var2 = new KongaTree(var1);
      var2.setVisibleRowCount(8);
      this.installController(var2);
      return this.customize(var2);
   }

   private KongaTree customize(KongaTree var1) {
      var1.setCellRenderer(new TreeBasedPrimaryKeyDefiner.Renderer());
      var1.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      var1.setRootVisible(false);
      var1.setShowsRootHandles(true);
      var1.setToggleClickCount(3);
      var1.expandAll();
      return var1;
   }

   private void installController(KongaTree var1) {
      ActionKeyBinding.install(new TreeBasedPrimaryKeyDefiner.RemoveSelectedKeyAction(), var1);
      TreeSelectionSource var2 = new TreeSelectionSource(var1);
      var2.setDefaultAction(this.editAction);
   }

   private void createEditTrigger() {
      this.editButton = new KongaButton(this.editAction);
      this.dialogTrigger = new ComponentDialogTrigger(this.editButton);
   }

   @Override
   public PrimaryKeys[] getPrimaryKeys() {
      ArrayList var1 = Lists.newArrayList();
      KongaTreeNode var2 = this.treeModel.getRoot();
      int var3 = 0;

      for (int var4 = var2.getChildCount(); var3 < var4; var3++) {
         var1.add(new TreeBasedPrimaryKeyDefiner.PrimaryKeysImpl((TableNode)var2.getChildAt(var3)));
      }

      return (PrimaryKeys[]) var1.toArray(new PrimaryKeys[var1.size()]);
   }

   @Override
   public void setPrimaryKeys(final PrimaryKeys[] var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            TreeBasedPrimaryKeyDefiner.this.treeModel.update(var1);
            TreeBasedPrimaryKeyDefiner.this.tree.setModel(TreeBasedPrimaryKeyDefiner.this.treeModel);
            TreeBasedPrimaryKeyDefiner.this.tree.expandAll();
            TreeBasedPrimaryKeyDefiner.this.keySelectionChanged();
         }
      });
   }

   private void keySelectionChanged() {
      boolean var1 = false;
      DefaultMutableTreeNode var2 = (DefaultMutableTreeNode)this.treeModel.getRoot();
      int var3 = 0;

      for (int var4 = var2.getChildCount(); var3 < var4; var3++) {
         if (((TableNode)var2.getChildAt(var3)).hasKeys()) {
            var1 = true;
            break;
         }
      }

      this.selectionDependant.setEnabled(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 2);
      var2.north(DatabaseUiResources.getString("UpdateKeys.Caption")).center(new JScrollPane(this.tree)).south(this.layoutButtons());
      var2.deeplyNonOpaque();
      var2.decorate(var1);
      this.selectNode(this.treeModel.getRoot().getChildAt(0));
   }

   private UiProvider layoutButtons() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.add(this.editButton);
      if (REMOVE_ALL_KEYS_SUPPORTED) {
         var1.rigid(10).add(new KongaButton(new TreeBasedPrimaryKeyDefiner.RemoveAllKeysAction()));
      }

      var1.glue();
      return var1;
   }

   private void selectNode(final KongaTreeNode var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TreeBasedPrimaryKeyDefiner.this.tree.makeNodeSelected(var1, false);
            TreeBasedPrimaryKeyDefiner.this.tree.requestFocusInWindow();
         }
      });
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   private void rebuildModel() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            TreeBasedPrimaryKeyDefiner.this.treeModel = PrimaryKeyTreeDefinerModel.copyModel(TreeBasedPrimaryKeyDefiner.this.treeModel);
            TreeBasedPrimaryKeyDefiner.this.tree.setModel(TreeBasedPrimaryKeyDefiner.this.treeModel);
            TreeBasedPrimaryKeyDefiner.this.tree.expandAll();
            TreeBasedPrimaryKeyDefiner.this.keySelectionChanged();
         }
      });
   }

   private class EditAction extends AbstractSelectionAction {
      public EditAction() {
         super(DatabaseUiResources.getString("UpdateKeys.Edit.Name"));
         this.putValue("ShortDescription", DatabaseUiResources.getString("UpdateKeys.Edit.ToolTip"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TableNode var2 = this.getSelectedTableNode();
         if (var2 != null) {
            this.selectKeyColumns(var2);
         }
      }

      private TableNode getSelectedTableNode() {
         List var1 = TreeBasedPrimaryKeyDefiner.this.tree.getSelectedNodes();
         if (var1.isEmpty()) {
            return null;
         }

         KongaTreeNode var2 = (KongaTreeNode)var1.get(0);

         while (!(var2 instanceof TableNode)) {
            var2 = var2.getParent();
         }

         return (TableNode)var2;
      }

      private void selectKeyColumns(TableNode var1) {
         ColumnEntryDialog var2 = ColumnEntryDialog.display(var1.getUserObject(), var1.getSelectedKeys(), TreeBasedPrimaryKeyDefiner.this.dialogTrigger);
         if (var2.wasOkPressed()) {
            var1.setSelectedKeys(var2.getSelectedKeys());
            TreeBasedPrimaryKeyDefiner.this.rebuildModel();
         }

         TreeBasedPrimaryKeyDefiner.this.selectNode(var1);
      }
   }

   private static class PrimaryKeysImpl implements PrimaryKeys {
      private final DatabaseObject table;
      private final DatabaseColumn[] keys;

      public PrimaryKeysImpl(TableNode var1) {
         this.table = var1.getUserObject();
         this.keys = var1.getSelectedKeysAsArray();
      }

      @Override
      public DatabaseObject getDatabaseObject() {
         return this.table;
      }

      @Override
      public DatabaseColumn[] getPrimaryKeys() {
         return this.keys;
      }
   }

   private class RemoveAllKeysAction extends AbstractAction {
      public RemoveAllKeysAction() {
         super(DatabaseUiResources.getString("UpdateKeys.Remove.Name"));
         this.putValue("ShortDescription", DatabaseUiResources.getString("UpdateKeys.Remove.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.removeAllKeys();
      }

      private void removeAllKeys() {
         KongaTreeNode var1 = TreeBasedPrimaryKeyDefiner.this.treeModel.getRoot();
         int var2 = 0;

         for (int var3 = var1.getChildCount(); var2 < var3; var2++) {
            TableNode var4 = (TableNode)var1.getChildAt(var2);
            var4.setSelectedKeys(null);
         }

         TreeBasedPrimaryKeyDefiner.this.rebuildModel();
      }
   }

   private class RemoveSelectedKeyAction extends AbstractAction {
      public RemoveSelectedKeyAction() {
         this.putValue("AcceleratorKey", KeyUtils.DELETE);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         KeyNode var2 = this.getSelectedKeyNode();
         if (var2 != null) {
            this.removeKey(var2);
         }
      }

      private KeyNode getSelectedKeyNode() {
         List var1 = TreeBasedPrimaryKeyDefiner.this.tree.getSelectedNodes();
         if (var1.size() != 1) {
            return null;
         }

         KongaTreeNode var2 = (KongaTreeNode)var1.get(0);
         return var2 instanceof KeyNode ? (KeyNode)var2 : null;
      }

      private void removeKey(KeyNode var1) {
         TableNode var2 = (TableNode)var1.getParent();
         boolean var3 = var1 == var2.getFirstChild();
         DefaultKongaTreeNode var4 = var3 ? (KeyNode)var1.getNextSibling() : (KeyNode)var1.getPreviousSibling();
         if (var4 == null) {
            var4 = var2;
         }

         var2.removeKey(var1);
         if (var2.getChildCount() == 0) {
            new NoKeySelectedNode(var2);
         }

         TreeBasedPrimaryKeyDefiner.this.rebuildModel();
         TreeBasedPrimaryKeyDefiner.this.selectNode(var4);
      }
   }

   private static class Renderer extends KongaTreeCellRenderer {
      private static final TextStyle TABLE_STYLE = TextStyles.SmallCaption;
      private static final TextStyle KEY_STYLE = TextStyles.TreeLeafText;
      private static final TextStyle NO_KEYS_SELECTED_STYLE = TextStyles.DefaultItalic;

      private Renderer() {
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         Component var8 = super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         if (var5) {
            if (var2 instanceof KeyNode) {
               decorate(var8, KEY_STYLE, !var3);
            } else {
               decorate(var8, NO_KEYS_SELECTED_STYLE, !var3);
            }
         } else {
            decorate(var8, TABLE_STYLE, !var3);
         }

         return var8;
      }

      private static void decorate(Component var0, TextStyle var1, boolean var2) {
         var0.setFont(var1.getFont());
         if (var2) {
            var0.setForeground(var1.getColor());
         }
      }
   }
}
