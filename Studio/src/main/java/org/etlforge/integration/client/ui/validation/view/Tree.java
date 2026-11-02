package org.etlforge.integration.client.ui.validation.view;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.Icon;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.entity.tree.DynamicProjectTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeRenderer;
import org.etlforge.integration.client.ui.entity.tree.NodeToEntity;
import org.etlforge.integration.client.ui.validation.resources.ValidationUiResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.project.validation.ValidationModelEvent;
import org.etlforge.integration.data.project.validation.ValidationModelListener;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.OverlayMessagePainter;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

final class Tree implements Disposable, UiProvider {
   private ValidationModel model;
   private final ValidationModelListener modelListener = new Tree.ValidationChangeHandler();
   private final DynamicProjectTree tree = new Tree.TreeImpl();
   private final TreeSelectionSource treeSelectionSource;

   public Tree(ValidationModel var1) {
      this.installTreeFilter();
      this.treeSelectionSource = new TreeSelectionSource(this.tree.getAdaptee());
      this.treeSelectionSource.setNodeObjectConverter(new NodeToEntity());
      this.setModel(var1);
   }

   private void installTreeFilter() {
      this.tree.setFilter(new InvalidItemsTreeFilter(this.model));
   }

   public void setModel(final ValidationModel var1) {
      if (var1 != this.model) {
         this.removeValidationModelListener();
         if (var1 != null) {
            var1.addListener(this.modelListener);
         }

         this.model = var1;
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               Tree.this.tree.setProject(var1 != null ? var1.getProject() : null);
               Tree.this.installTreeFilter();
               Tree.this.tree.expandAll();
               Tree.this.setTreePainter();
            }
         });
      }
   }

   private void removeValidationModelListener() {
      if (this.model != null) {
         this.model.removeListener(this.modelListener);
      }
   }

   private void setTreePainter() {
      ComponentPainter var1 = this.shouldTreePainterBeUsed() ? this.createValidProjectPainter() : null;
      this.tree.getAdaptee().setPainter(var1);
   }

   private boolean shouldTreePainterBeUsed() {
      return this.model == null ? false : this.tree.getAdaptee().getRowCount() <= 1;
   }

   private ComponentPainter createValidProjectPainter() {
      OverlayMessagePainter var1 = new OverlayMessagePainter(ValidationUiResources.getString("Displayer.AllValid"));
      Rectangle var2 = this.tree.getAdaptee().getRowBounds(0);
      var1.setLocation(new Point(var2.x + 20, var2.y + var2.height + 16));
      var1.setTextStyle(TextStyles.AdminHintBold);
      return var1;
   }

   @Override
   public void dispose() {
      this.tree.dispose();
      this.removeValidationModelListener();
   }

   public void expandAll() {
      this.tree.expandAll();
   }

   public void collapseAll() {
      this.tree.collapseAll();
   }

   public void setDefaultAction(SelectionAction var1) {
      this.treeSelectionSource.setDefaultAction(var1);
      PopupMenuRequestHandlers.install(this.tree.getAdaptee(), var1);
   }

   public void addSelectionListener(SelectionListener var1) {
      this.treeSelectionSource.addSelectionListener(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      this.treeSelectionSource.removeSelectionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tree.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   private class TreeImpl extends DynamicProjectTree {
      private TreeImpl() {
      }

      @Override
      protected IntegrationEntityTree createTree(IntegrationEntityTreeModel var1) {
         IntegrationEntityTree var2 = new IntegrationEntityTree(var1);
         var2.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
         var2.setCellRenderer(Tree.this.new TreeRenderer());
         return var2;
      }
   }

   private class TreeRenderer extends IntegrationEntityTreeRenderer {
      private TreeRenderer() {
      }

      @Override
      protected Icon getIcon(IntegrationEntityTreeNode var1) {
         IntegrationEntity var2 = var1.getUserObject();
         ValidationStatus var3 = Tree.this.model.getStatus(var2, true);
         EntityIcons.Category var4 = EntityIcons.Category.fromValidationStatus(var3);
         return EntityIcons.forType(var2.getEntityType()).getIcon(var4, EntityIcons.Size.SMALL);
      }
   }

   private class ValidationChangeHandler implements ValidationModelListener {
      private ValidationChangeHandler() {
      }

      @Override
      public void validationWasPerformed(ValidationModelEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Tree.this.installTreeFilter();
               Tree.this.setTreePainter();
            }
         });
      }
   }
}
