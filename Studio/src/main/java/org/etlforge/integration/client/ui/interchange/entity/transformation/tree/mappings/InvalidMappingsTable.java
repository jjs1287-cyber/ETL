package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionContainer;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.action.ActionProvider;
import org.etlforge.ui.dialog.ComponentDialogTrigger;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaRowTableModel;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableGridMode;

public final class InvalidMappingsTable implements UiProvider, ActionProvider {
   private final TreeMapper treeMapper;
   private final KongaRowTable table;
   private final InvalidMappingsTableModel tableModel;
   private final InvalidMappingsTable.DeleteAction deleteMappingsAction = new InvalidMappingsTable.DeleteAction();
   private final InvalidMappingsTable.ViewDetailsAction viewDetailsAction = new InvalidMappingsTable.ViewDetailsAction();
   private InvalidMappingsTable.TreeMapperListener treeMapperListener;
   private boolean dynamic;
   private final List<InvalidMapping> deletedMappings = Lists.newArrayList();

   public InvalidMappingsTable(TreeMapper var1) {
      this.treeMapper = Preconditions.checkNotNull(var1, "tm");
      this.tableModel = new InvalidMappingsTableModel(var1);
      this.table = this.createTable(this.tableModel);
      this.deleteMappingsAction.setSelectionSource(this.table);
      this.table.setDefaultAction(this.viewDetailsAction);
   }

   private KongaRowTable createTable(KongaRowTableModel var1) {
      KongaTable var2 = ComponentFactories.tableFactory().newSortedTable(var1, 8);
      this.installController(var2);
      this.setTableStyle(var2);
      return new KongaRowTable(var2);
   }

   private void installController(KongaTable var1) {
      PopupMenuRequestHandlers.install(var1, this.viewDetailsAction, this.deleteMappingsAction);
      ActionKeyBinding.install(this.deleteMappingsAction, var1);
   }

   private void setTableStyle(KongaTable var1) {
      TableStyle var2 = TableStyles.normal();
      var2.setGridMode(TableGridMode.BOTH);
      var2.makeOver(var1);
   }

   public void setDynamic(boolean var1) {
      if (var1 != this.dynamic) {
         if (var1) {
            this.installTreeMapperListener();
         } else {
            this.removeTreeMapperListener();
         }

         this.dynamic = var1;
      }
   }

   private void installTreeMapperListener() {
      assert this.treeMapperListener == null : "The listener is already installed";
      this.treeMapperListener = new InvalidMappingsTable.TreeMapperListener();
      this.treeMapper.getMappingManager().addInvalidMappingsListener(this.treeMapperListener);
      this.updateModelFrom(this.treeMapper.getMappingManager().getInvalidMappings());
   }

   private void updateModelFrom(InvalidMapping[] var1) {
      this.tableModel.deleteAll();
      this.tableModel.addRows(var1);
   }

   private void removeTreeMapperListener() {
      assert this.treeMapperListener != null : "The listener is not installed";
      this.treeMapper.getMappingManager().removeInvalidMappingsListener(this.treeMapperListener);
      this.treeMapperListener = null;
   }

   public List<InvalidMapping> getDisplayedMappings() {
      return this.tableModel.getMappings();
   }

   public List<InvalidMapping> getDeletedMappings() {
      return Lists.newArrayList(this.deletedMappings);
   }

   @Override
   public void populate(ActionContainer var1) {
      var1.add(this.viewDetailsAction);
      var1.add(this.deleteMappingsAction);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public void setInitialSelection() {
      this.table.selectFirstRow();
   }

   private class DeleteAction extends AbstractSelectionAction {
      public DeleteAction() {
         ActionDecorators.DELETE.decorate(this);
         this.putValue("ShortDescription", TreeStrings.get("InvalidMappings.Delete.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         List var2 = this.removeSelectedRows();
         InvalidMappingsTable.this.deletedMappings.addAll(var2);
         if (InvalidMappingsTable.this.dynamic) {
            this.updateTreeMapper(var2);
         }
      }

      private List<InvalidMapping> removeSelectedRows() {
         ArrayList var1 = Lists.newArrayList();
         IndexedSelection var2 = (IndexedSelection)this.getSelection();
         int[] var3 = var2.indeces();

         for (int var4 = var3.length - 1; var4 >= 0; var4--) {
            int var5 = var3[var4];
            var1.add(InvalidMappingsTable.this.tableModel.getRowObjectAt(var5));
            InvalidMappingsTable.this.tableModel.deleteRow(var5);
         }

         return var1;
      }

      private void updateTreeMapper(List<InvalidMapping> var1) {
         InvalidMappingsTable.this.treeMapperListener.setBlocked(true);

         try {
            InvalidMappingsTable.this.treeMapper.deleteMappings(var1);
         } finally {
            InvalidMappingsTable.this.treeMapperListener.setBlocked(false);
         }
      }
   }

   private class TreeMapperListener implements InvalidMappingsListener {
      private boolean blocked;

      private TreeMapperListener() {
      }

      @Override
      public void invalidMappingsChanged(TreeMapper var1, InvalidMapping[] var2) {
         if (!this.blocked) {
            InvalidMappingsTable.this.updateModelFrom(var2);
         }
      }

      public void setBlocked(boolean var1) {
         this.blocked = var1;
      }
   }

   private class ViewDetailsAction extends AbstractSingleObjectSelectionAction<InvalidMapping> {
      public ViewDetailsAction() {
         super(InvalidMapping.class);
         ActionDecorators.VIEW_DETAILS.decorate(this);
         this.putValue("ShortDescription", TreeStrings.get("InvalidMappings.ViewDetails.ToolTip"));
      }

      protected void run(final InvalidMapping var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ViewDetailsAction.this.displayDetails(var1);
            }
         });
      }

      private void displayDetails(InvalidMapping var1) {
         InvalidMappingDetailsViewer var2 = new InvalidMappingDetailsViewer(var1);
         UiProviderDialogDisplayer.display(
            new ComponentDialogTrigger(InvalidMappingsTable.this.table.getAdaptee()),
            var2,
            TreeStrings.get("InvalidMappings.ViewDetails.Title"),
            KongaDialog.CLOSE_ONLY,
            InvalidMappingDetailsViewer.class
         );
      }
   }
}
