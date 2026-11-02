package org.etlforge.integration.activity.ui.log;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JPanel;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.AbstractOperationLogModel;
import org.etlforge.integration.activity.ui.component.filter.FilterDescriptionPanel;
import org.etlforge.integration.activity.ui.util.DeletableTableAction;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;

public final class OperationLogDisplayer implements UiProvider {
   private final AbstractOperationLogModel model;
   private final FilterDescriptionPanel filterDescriptionPanel;
   private final OperationLogTableColumn[] columnsToInclude;
   private LogEntryWidget entryDisplayer;
   private final LogDisplayModeSwitch displayModeSwitch;
   private final JPanel entryDisplayerContainer;
   private SelectionListener selectionListener;
   private boolean typeSwitchEnabled = !ClientInfo.isDataLoader();
   private DeletableTableAction selectAllAction;
   private DeletableTableAction unselectAllAction;

   public OperationLogDisplayer(AbstractOperationLogModel var1, FilterDescriptionPanel var2, OperationLogTableColumn[] var3) {
      this.model = var1;
      this.filterDescriptionPanel = var2;
      this.columnsToInclude = (OperationLogTableColumn[])var3.clone();
      this.displayModeSwitch = new LogDisplayModeSwitch(var1);
      this.entryDisplayerContainer = new JPanel(new BorderLayout());
      this.entryDisplayer = this.createEntryDisplayer();
      this.bindProperties();
   }

   private LogEntryWidget createEntryDisplayer() {
      LogDisplayMode var1 = this.model.getDisplayMode();
      LogEntryWidget var2 = var1.newDisplayer(this.model, this.columnsToInclude);
      if (this.selectionListener != null) {
         var2.addSelectionListener(this.selectionListener);
      }

      if (this.selectAllAction != null && this.unselectAllAction != null) {
         var2.setBulkSelectionActions(this.selectAllAction, this.unselectAllAction);
      }

      return var2;
   }

   private void bindProperties() {
      OperationLogDisplayer.DisplayModeBinding var1 = new OperationLogDisplayer.DisplayModeBinding();
      var1.syncUi();
   }

   public OperationLogEntry getSelectedLogEntry() {
      return this.entryDisplayer.getSelectedLogEntry();
   }

   public void setSelectionListener(SelectionListener var1) {
      this.entryDisplayer.removeSelectionListener(this.selectionListener);
      this.selectionListener = var1;
      if (var1 != null) {
         this.entryDisplayer.addSelectionListener(var1);
      }
   }

   public void setTypeSwitchEnabled(boolean var1) {
      this.typeSwitchEnabled = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      UiProvider var3;
      if (this.typeSwitchEnabled) {
         BorderLayoutBuilder var4 = new BorderLayoutBuilder(8, 0);
         var4.center(this.filterDescriptionPanel).east(this.displayModeSwitch);
         var3 = var4;
      } else {
         var3 = this.filterDescriptionPanel;
      }

      ContainerCanvas.decorate(this.entryDisplayer, this.entryDisplayerContainer, "Center");
      var2.center(this.entryDisplayerContainer).south(var3);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.entryDisplayer.requestFocus();
   }

   public void setBulkSelectionActions(DeletableTableAction var1, DeletableTableAction var2) {
      this.selectAllAction = var1;
      this.unselectAllAction = var2;
      this.entryDisplayer.setBulkSelectionActions(var1, var2);
   }

   private class DisplayModeBinding extends AbstractBinding<LogDisplayMode> {
      public DisplayModeBinding() {
         super(OperationLogDisplayer.this.model.getProperty("displayMode"));
      }

      protected void updateUiWithValue(LogDisplayMode var1) {
         if (var1 != OperationLogDisplayer.this.entryDisplayer.getMode()) {
            this.switchDisplayMode();
            this.rememberPreferredMode(var1);
         }
      }

      private void switchDisplayMode() {
         this.createDisplayer();
         this.rebuildLayout();
      }

      private void createDisplayer() {
         OperationLogDisplayer.this.entryDisplayer.dispose();
         OperationLogDisplayer.this.entryDisplayer = OperationLogDisplayer.this.createEntryDisplayer();
         OperationLogDisplayer.this.entryDisplayerContainer.removeAll();
      }

      private void rebuildLayout() {
         ContainerCanvas.decorate(OperationLogDisplayer.this.entryDisplayer, OperationLogDisplayer.this.entryDisplayerContainer, "Center");
         Container var1 = OperationLogDisplayer.this.entryDisplayerContainer.getParent();
         if (var1 != null) {
            UiUtils.revalidate(var1);
         }
      }

      private void rememberPreferredMode(LogDisplayMode var1) {
         LogDisplayMode.storePreferredMode(var1);
      }
   }
}
