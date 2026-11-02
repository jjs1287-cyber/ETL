package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.entity.EntityDropHandler;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.httpendpoint.OperationRunMode;
import org.etlforge.integration.data.entity.httpendpoint.TriggerableOperationFilter;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.event.DoubleClick;
import org.etlforge.ui.event.Mouse;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.PopupMenuRequestListener;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableEnumEditor;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;

final class OperationTable implements UiProvider {
   private final ResponseModel responseModel;
   private final OperationTableModel tableModel;
   private final KongaRowTable table;
   private IntegrationEntityLookup operationLookup;
   private final Action addAction;
   private final SelectionAction editAction;
   private final SelectionAction removeAction;

   public OperationTable(ResponseModel var1, Action var2, SelectionAction var3, SelectionAction var4) {
      this.responseModel = var1;
      this.addAction = var2;
      this.editAction = var3;
      this.removeAction = var4;
      this.tableModel = new OperationTableModel(var1);
      this.table = this.createTable();
      var3.setSelectionSource(this.table);
      var4.setSelectionSource(this.table);
      var1.addPropertyChangeListener(new OperationTable.ResponseModelListener());
   }

   private KongaRowTable createTable() {
      KongaTable var1 = ComponentFactories.tableFactory().newTable(this.tableModel);
      this.installRenderersAndEditors(var1);
      this.installTableStyle(var1);
      this.installTableController(var1);
      var1.setVisibleRowCount(2);
      var1.setTransferHandler(new OperationTable.OperationDropHandler());
      PopupMenuRequestListener.install(var1, new OperationTable.ContextMenuHandler());
      return new KongaRowTable(var1);
   }

   private void installRenderersAndEditors(KongaTable var1) {
      var1.setCellRendererFor(OperationTableModel.OPERATION, new OperationTable.NameRenderer());
      var1.setCellEditorFor(OperationTableModel.MODE, new TableEnumEditor(OperationRunMode.class));
   }

   private void installTableStyle(KongaTable var1) {
      TableStyle var2 = TableStyles.normal();
      var2.addRendererPreparer(new OperationTable.RendererPreparerImpl());
      var2.makeOver(var1);
   }

   private void installTableController(KongaTable var1) {
      ActionKeyBinding.install(this.removeAction, var1, KeyUtils.DELETE);
      DoubleClick.on(var1).inColumn(OperationTableModel.OPERATION).toInvoke(this.editAction);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public void setOperationLookup(IntegrationEntityLookup var1) {
      this.operationLookup = var1;
      this.repaint();
   }

   public void repaint() {
      this.table.getAdaptee().repaint();
   }

   private final class ContextMenuHandler implements PopupMenuRequestHandler {
      private ContextMenuHandler() {
      }

      @Override
      public boolean popupMenuRequested(Component var1, MouseEvent var2) {
         KongaPopupMenu var3 = new KongaPopupMenu();
         KongaTable var4 = (KongaTable)var1;
         if (Mouse.onTableRow(var4).apply(var2)) {
            var3.add(OperationTable.this.editAction);
            JMenuItem var5 = new JMenuItem(OperationTable.this.removeAction);
            if (var4.getSelectedRowCount() == 1) {
               var5.setText("Remove Operation");
            }

            var3.add(var5);
         } else {
            var3.add(OperationTable.this.addAction);
         }

         var3.show(var1, var2.getX(), var2.getY());
         return true;
      }
   }

   private final class NameRenderer extends DefaultTableCellRenderer {
      private NameRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         if (var2 instanceof OperationId) {
            this.decorateOperation(var2);
         } else {
            this.setIcon(EntityIcons.forType(EntityType.Operation).getDefaultIcon(EntityIcons.Size.SMALL));
         }

         return this;
      }

      private void decorateOperation(Object var1) {
         OperationId var2 = (OperationId)var1;
         if (OperationTable.this.operationLookup != null) {
            Operation var3 = OperationTable.this.operationLookup.getEntity(var2, Operation.class);
            if (var3 != null) {
               this.setIcon(IconStore.validationDecoratedIcon(var3));
               this.setText(var3.getName());
            }
         }
      }
   }

   private final class OperationDropHandler extends EntityDropHandler {
      private OperationDropHandler() {
      }

      @Override
      protected boolean accepts(IntegrationEntity var1, TransferSupport var2) {
         return TriggerableOperationFilter.FILTER.apply(var1) && !OperationTable.this.responseModel.contains(((Operation)var1).getID());
      }

      @Override
      protected boolean handleDrop(IntegrationEntity var1, TransferSupport var2) {
         if (var1 instanceof Operation) {
            OperationTable.this.responseModel.addOperation((Operation)var1);
            return true;
         } else {
            return false;
         }
      }
   }

   private final class RendererPreparerImpl implements TableRendererPreparer {
      private RendererPreparerImpl() {
      }

      @Override
      public boolean doDefaultPreparation() {
         return true;
      }

      @Override
      public Component prepare(JTable var1, TableCellRenderer var2, Component var3, int var4, int var5) {
         TableColumnMetaData var6 = ((KongaTable)var1).getColumnDescriptor(var5);
         if (var6 == OperationTableModel.RESPONSE_GENERATOR) {
            var3.setEnabled(OperationTable.this.tableModel.isResponseGeneratorEnabled(var4));
         }

         return var3;
      }
   }

   private final class ResponseModelListener implements PropertyChangeListener {
      private ResponseModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (var2 == null) {
            OperationTable.this.tableModel.rebuild();
         } else if ("asynchronous".equals(var2)) {
            if (OperationTable.this.responseModel.isAsynchronous()) {
               OperationTable.this.tableModel.setAsynchronous();
            }
         } else if ("operations".equals(var2)) {
            OperationTable.this.tableModel.rebuild();
            OperationTable.this.table.requestFocus();
         } else if ("response_generator".equals(var2)) {
            OperationTable.this.tableModel.setResponseGenerator(OperationTable.this.responseModel.getResponseGenerator());
         } else if ("run_mode".equals(var2)) {
            OperationTable.this.tableModel.updateRunModes();
         }

         OperationTable.this.table.getAdaptee().repaint();
      }
   }
}
