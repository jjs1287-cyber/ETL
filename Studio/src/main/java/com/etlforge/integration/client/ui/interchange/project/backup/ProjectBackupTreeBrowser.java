package com.etlforge.integration.client.ui.interchange.project.backup;

import com.google.common.collect.Lists;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public class ProjectBackupTreeBrowser implements ProjectBackupBrowser {
   private final KongaTree tree;
   private final TreeSelectionSource selSource;

   public ProjectBackupTreeBrowser(AvailableBackup[] var1) {
      this.tree = this.createTree(var1);
      this.selSource = new TreeSelectionSource(this.tree);
   }

   @Override
   public void decorate(UiCanvas var1) {
      TreePanel var2 = new TreePanel("Available Backups", this.tree);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   @Override
   public AvailableBackup[] getSelectedBackups() {
      ArrayList var1 = Lists.newArrayList();

      for (Object var3 : this.getSelection().items()) {
         if (var3 instanceof AvailableBackup) {
            var1.add((AvailableBackup)var3);
         }
      }

      return (AvailableBackup[]) var1.toArray(new AvailableBackup[var1.size()]);
   }

   @Override
   public void setBackups(AvailableBackup[] var1) {
      final DefaultKongaTreeModel var2 = this.createTreeModel(var1);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ProjectBackupTreeBrowser.this.tree.setModel(var2);
            ProjectBackupTreeBrowser.this.selectFirst();
         }
      });
   }

   @Override
   public void selectFirst() {
      int var1 = this.tree.getModel().getRoot().getChildCount();
      if (var1 > 0) {
         this.tree.makeNodeSelected(this.tree.getModel().getRoot().getChildAt(0), false);
      }
   }

   public OrderedSelection getSelection() {
      return this.selSource.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selSource.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selSource.removeSelectionListener(var1);
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.selSource.setDefaultAction(var1);
   }

   @Override
   public void setInspectBackupAction(SelectionAction var1) {
      throw new NotImplementedYetException();
   }

   private final KongaTree createTree(AvailableBackup[] var1) {
      KongaTree var2 = new KongaTree(this.createTreeModel(var1));
      var2.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      var2.setCellRenderer(new ProjectBackupTreeBrowser.Renderer());
      var2.setDragEnabled(true);
      var2.setTransferHandler(new AvailableBackupTransferHandler(this));
      return var2;
   }

   private DefaultKongaTreeModel createTreeModel(AvailableBackup[] var1) {
      return new AvailableBackupTreeModelBuilder().buildModel(var1);
   }

   private static class Renderer extends DefaultTreeCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         this.setToolTipText(null);
         this.setFont(var1.getFont());
         if (var2 instanceof AvailableBackup) {
            this.decorateBackupNode((AvailableBackup)var2);
         } else if (var2 instanceof DayOfBackup) {
            this.decorateDayOfBackup((DayOfBackup)var2);
         } else {
            this.setFont(Fonts.italic(var1));
            this.setIcon(null);
         }

         return this;
      }

      private void decorateDayOfBackup(DayOfBackup var1) {
         this.setText(RendererUtils.display(var1));
         this.setToolTipText(RendererUtils.tooltip(var1));
      }

      private void decorateBackupNode(AvailableBackup var1) {
         this.setText(RendererUtils.display(var1));
         this.setToolTipText(RendererUtils.tooltip(var1));
         this.setIcon(EntityIcons.forType(EntityType.Project).getDefaultIcon(EntityIcons.Size.SMALL));
      }
   }
}
