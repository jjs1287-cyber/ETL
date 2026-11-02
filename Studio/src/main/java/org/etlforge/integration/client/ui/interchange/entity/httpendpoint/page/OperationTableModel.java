package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import org.etlforge.integration.data.entity.httpendpoint.OperationRunMode;
import org.etlforge.integration.data.entity.httpendpoint.ResponseType;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.table.AbstractKongaTableModel;
import org.etlforge.ui.widget.table.TableColumnMetaData;

final class OperationTableModel extends AbstractKongaTableModel {
   public static final TableColumnMetaData OPERATION = new TableColumnMetaData("Operation", OperationId.class, false, 100);
   public static final TableColumnMetaData MODE = new TableColumnMetaData("Mode", OperationRunMode.class, true, 40);
   public static final TableColumnMetaData RESPONSE_GENERATOR = new TableColumnMetaData("Final Target as Response", Boolean.class, true, 50);
   private final ResponseModel responseModel;
   private boolean externalModification;

   public OperationTableModel(ResponseModel var1) {
      super(OPERATION, MODE, RESPONSE_GENERATOR);
      this.responseModel = var1;
      this.rebuild();
   }

   public void rebuild() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            OperationTableModel.this.externalModification = true;

            try {
               OperationTableModel.this.deleteAll();

               for (TriggeredOperation var2 : OperationTableModel.this.responseModel.asTriggeredOperations().getOperationsForVerb(null)) {
                  OperationTableModel.this.addRow(var2);
               }
            } finally {
               OperationTableModel.this.externalModification = false;
            }
         }
      });
   }

   @Override
   protected Object[] getColumnDataFromObject(Object var1) {
      if (var1 instanceof TriggeredOperation) {
         TriggeredOperation var2 = (TriggeredOperation)var1;
         return new Object[]{var2.getOperationId(), var2.getRunMode(), var2.isResponseGenerator()};
      } else {
         this.throwIllegalRowObject(var1, TriggeredOperation.class);
         return null;
      }
   }

   @Override
   public boolean isCellEditable(int var1, int var2) {
      TableColumnMetaData var3 = this.getColumnDescriptor(var2);
      if (var3 == MODE) {
         return true;
      } else {
         return var3 == RESPONSE_GENERATOR ? this.isResponseGeneratorEnabled(var1) : false;
      }
   }

   @Override
   public void setValueAt(Object var1, int var2, int var3) {
      super.setValueAt(var1, var2, var3);
      if (!this.externalModification) {
         TableColumnMetaData var4 = this.getColumnDescriptor(var3);
         OperationId var5 = this.getOperationId(var2);
         if (var4 == MODE) {
            this.responseModel.setRunMode(var5, (OperationRunMode)var1);
         } else {
            if (var4 != RESPONSE_GENERATOR) {
               throw new RuntimeException("Column " + var4 + " is not supposed to be editable");
            }

            if (var1 == Boolean.TRUE) {
               this.responseModel.setResponseGenerator(var5);
            } else {
               this.responseModel.setResponseGenerator(null);
            }
         }
      }
   }

   public void setAsynchronous() {
      this.externalModification = true;

      try {
         for (int var1 = 0; var1 < this.getRowCount(); var1++) {
            this.setValueAt(OperationRunMode.ASYNCHRONOUS, var1, MODE);
         }
      } finally {
         this.externalModification = false;
      }
   }

   public void setResponseGenerator(OperationId var1) {
      this.externalModification = true;

      try {
         for (int var2 = 0; var2 < this.getRowCount(); var2++) {
            OperationId var3 = this.getOperationId(var2);
            this.setValueAt(var3 == var1, var2, RESPONSE_GENERATOR);
         }
      } finally {
         this.externalModification = false;
      }
   }

   public boolean isResponseGeneratorEnabled(int var1) {
      return this.responseModel.getResponseType() == ResponseType.OPERATION && this.getValueAt(var1, MODE) == OperationRunMode.SYNCHRONOUS;
   }

   public void updateRunModes() {
      this.externalModification = true;

      try {
         for (int var1 = 0; var1 < this.getRowCount(); var1++) {
            OperationRunMode var2 = this.responseModel.getRunMode(this.getOperationId(var1));
            this.setValueAt(var2, var1, MODE);
         }
      } finally {
         this.externalModification = false;
      }
   }

   private OperationId getOperationId(int var1) {
      return (OperationId)this.getValueAt(var1, OPERATION);
   }
}
