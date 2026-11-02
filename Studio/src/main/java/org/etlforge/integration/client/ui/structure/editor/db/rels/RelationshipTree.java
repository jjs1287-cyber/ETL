package org.etlforge.integration.client.ui.structure.editor.db.rels;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.client.database.ui.TableIcon;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.ui.widget.tree.UserObjectNodeObjectConverter;

final class RelationshipTree implements UiProvider, SelectionSource {
   private final KongaTree tree;
   private final TreeSelectionSource selectionSource;
   private final RelationshipTree.ModelBuilder modelBuilder;
   private Function<DatabaseObject, String> tableNameDecorator;

   public RelationshipTree(DatabaseObject[] var1, boolean var2) {
      this.modelBuilder = new RelationshipTree.ModelBuilder(var1);
      this.tree = this.createTree(var1.length, var2);
      this.selectionSource = this.createSelectionSource();
      this.tableNameDecorator = new Function<DatabaseObject, String>() {
         public String apply(DatabaseObject var1) {
            return "";
         }
      };
   }

   public void setTableNameDecorator(Function<DatabaseObject, String> var1) {
      this.tableNameDecorator = var1;
   }

   public void setDefaultAction(SelectionAction var1) {
      this.selectionSource.setDefaultAction(var1);
   }

   public void installKeybindings(Action... var1) {
      for (Action var5 : var1) {
         ActionKeyBinding.install(var5, this.tree);
      }
   }

   public void setTransferHandler(TransferHandler var1) {
      this.tree.setTransferHandler(var1);
      this.tree.setDragEnabled(var1 != null);
   }

   private KongaTree createTree(int var1, boolean var2) {
      KongaTree var3 = new KongaTree(this.modelBuilder.noRelationshipsModel());
      var3.setRootVisible(false);
      var3.setCellRenderer(new RelationshipTree.Renderer(var2));
      var3.setRowHeight(20);
      var3.setVisibleRowCount(Math.min(var1 + 1, 20));
      var3.setSelectionMode(KongaTree.SelectionMode.DISCONTIGUOUS_TREE_SELECTION);
      var3.setToggleClickCount(-1);
      return var3;
   }

   private TreeSelectionSource createSelectionSource() {
      TreeSelectionSource var1 = new TreeSelectionSource(this.tree);
      var1.setNodeObjectConverter(new UserObjectNodeObjectConverter());
      return var1;
   }

   public void update(List<TableRelationship> var1) {
      DefaultKongaTreeModel var2 = this.modelBuilder.build(var1);
      this.tree.setModel(var2);
      this.tree.expandAll();
   }

   public void refresh() {
      Object var1 = this.tree.getExpansionMemento(KongaTree.NodeKeyExtractor.TRIVIAL);
      int[] var2 = this.tree.getSelectionRows();
      DefaultKongaTreeModel var3 = (DefaultKongaTreeModel)this.tree.getModel();
      var3.fireTreeStructureChanged(this.tree, null);
      this.tree.restoreExpansion(var1);
      this.tree.setSelectionRows(var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      String var2 = StructureEditorResources.getString("Db.RelationshipTree.Caption");
      new BorderLayoutBuilder(0, 3).north(var2).center(this.tree).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public OrderedSelection getSelection() {
      return this.selectionSource.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionSource.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionSource.removeSelectionListener(var1);
   }

   private static class ModelBuilder {
      private final DatabaseObject[] tables;
      private Map<DatabaseObject, KongaTreeNode> nodes;

      public ModelBuilder(DatabaseObject[] var1) {
         this.tables = (DatabaseObject[])var1.clone();
      }

      public KongaTreeModel noRelationshipsModel() {
         return this.build(new ArrayList<>());
      }

      public DefaultKongaTreeModel build(List<TableRelationship> var1) {
         this.createNodes(var1);
         KongaTreeNode var2 = this.createRoot();
         return new DefaultKongaTreeModel(var2);
      }

      private void createNodes(List<TableRelationship> var1) {
         this.nodes = Maps.newLinkedHashMap();

         for (TableRelationship var3 : var1) {
            KongaTreeNode var4 = this.getNode(var3.getParentObject());
            KongaTreeNode var5 = this.getNode(var3.getChildObject());
            var4.addChild(var5);
         }

         this.addBackTopLevelNodes();
      }

      private void addBackTopLevelNodes() {
         for (DatabaseObject var4 : this.tables) {
            if (!this.nodes.containsKey(var4)) {
               this.getNode(var4);
            }
         }
      }

      private KongaTreeNode getNode(DatabaseObject var1) {
         KongaTreeNode var2 = this.nodes.get(var1);
         if (var2 == null) {
            var2 = new DefaultKongaTreeNode(var1, true);
            this.nodes.put(var1, var2);
         }

         return var2;
      }

      private KongaTreeNode createRoot() {
         KongaTreeNode var1 = createEmptyRootNode();

         for (KongaTreeNode var3 : this.nodes.values()) {
            if (var3.getParent() == null) {
               var1.addChild(var3);
            }
         }

         return var1;
      }

      private static KongaTreeNode createEmptyRootNode() {
         return new DefaultKongaTreeNode("root", true);
      }
   }

   private class Renderer extends DefaultTreeCellRenderer {
      private final boolean allowMultipleRoots;
      private final TableIcon icon;

      public Renderer(boolean var2) {
         this.allowMultipleRoots = var2;
         this.icon = new TableIcon();
         this.setOpenIcon(this.icon);
         this.setClosedIcon(this.icon);
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         KongaTreeNode var8 = (KongaTreeNode)var2;
         Object var9 = var8.getUserObject();
         if (var9 instanceof DatabaseObject) {
            this.setText(this.getText((DatabaseObject)var9));
         }

         this.checkValidState(var1, var8, var3);
         return this;
      }

      private String getText(DatabaseObject var1) {
         return var1.getName() + RelationshipTree.this.tableNameDecorator.apply(var1);
      }

      private void checkValidState(JTree var1, KongaTreeNode var2, boolean var3) {
         if (!this.allowMultipleRoots && this.isMultipleRoot(var1, var2)) {
            this.renderMultipleRoot(var3);
         } else {
            this.renderValidNode(var3);
         }
      }

      private boolean isMultipleRoot(JTree var1, KongaTreeNode var2) {
         return this.isRootTableNode(var1, var2) && this.multipleTableRootsDefined(var1);
      }

      private boolean isRootTableNode(JTree var1, KongaTreeNode var2) {
         return var2.getParent() == var1.getModel().getRoot();
      }

      private boolean multipleTableRootsDefined(JTree var1) {
         return var1.getModel().getChildCount(var1.getModel().getRoot()) > 1;
      }

      private void renderMultipleRoot(boolean var1) {
         this.icon.setColor(Colors.ERROR_TEXT_COLOR);
         this.setForeground(var1 ? this.getTextSelectionColor() : Colors.ERROR_TEXT_COLOR);
         this.setToolTipText("Multiple root tables are not allowed");
      }

      private void renderValidNode(boolean var1) {
         this.icon.restoreDefaultColor();
         this.setForeground(var1 ? this.getTextSelectionColor() : this.getTextNonSelectionColor());
         this.setToolTipText(this.getText());
      }
   }
}
