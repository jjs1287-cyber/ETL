package org.etlforge.integration.activity.ui.util;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public final class DeletableTableActionsFactory {
   public static DeletableTableAction selectAll() {
      return new DeletableTableActionsFactory.SelectAllAction();
   }

   public static DeletableTableAction unselectAll() {
      return new DeletableTableActionsFactory.UnselectAllAction();
   }

   private DeletableTableActionsFactory() {
   }

   private static final class SelectAllAction extends AbstractAction implements DeletableTableAction {
      private DeletableTableModel tableModel;

      public SelectAllAction() {
         super(PackageResources.Actions.SELECT_ALL);
      }

      @Override
      public void setTableModel(DeletableTableModel var1) {
         this.tableModel = var1;
         this.setEnabled(var1.hasDeletableEntries());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.tableModel.selectAll();
      }
   }

   private static final class UnselectAllAction extends AbstractAction implements DeletableTableAction {
      private DeletableTableModel tableModel;

      public UnselectAllAction() {
         super(PackageResources.Actions.UNSELET_ALL);
      }

      @Override
      public void setTableModel(DeletableTableModel var1) {
         this.tableModel = var1;
         this.setEnabled(var1.hasDeletableEntries());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.tableModel.unselectAll();
      }
   }
}
