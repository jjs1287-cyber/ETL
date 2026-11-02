package com.etlforge.integration.client.ui.sfdc.soql.v4;

import java.awt.Component;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.util.List;
import javax.swing.SwingUtilities;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.ui.dialog.KongaDialog;

public final class FieldListDialog {
   private final FieldList list;
   private KongaDialog dialog;
   private boolean closeOnSelection;

   public FieldListDialog(SObject var1, SObjectSelectionListener var2) {
      this.list = new FieldList(var1, new FieldListDialog.Listener(var2));
   }

   public void setCloseOnSelection(boolean var1) {
      this.closeOnSelection = var1;
   }

   public void setSelectedField(SObject.Field var1) {
      this.list.setSelectedField(var1);
   }

   public void open(Component var1) {
      if (this.dialog != null) {
         this.list.requestFocus();
      } else {
         Window var2 = SwingUtilities.getWindowAncestor(var1);
         this.dialog = new KongaDialog(var2, "Fields", ModalityType.MODELESS) {
            @Override
            protected void onDialogOpened() {
               super.onDialogOpened();
               FieldListDialog.this.list.requestFocus();
            }

            @Override
            protected void onDialogClose() {
               super.onDialogClose();
               FieldListDialog.this.dialog = null;
            }
         };
         this.dialog.standardLayout(this.list, KongaDialog.CLOSE_ONLY);
         this.dialog.setCloseButtonAsDefault();
         this.dialog.manageLocation(FieldListDialog.class);
         this.dialog.setVisible(true);
      }
   }

   public void close() {
      if (this.dialog != null) {
         this.dialog.setVisible(false);
         this.dialog.dispose();
         this.dialog = null;
      }
   }

   private class Listener extends SObjectSelectionListener.Adapter {
      private final SObjectSelectionListener external;

      public Listener(SObjectSelectionListener var2) {
         this.external = var2;
      }

      @Override
      public void fieldsSelected(SObject var1, List<SObject.Field> var2) {
         this.external.fieldsSelected(var1, var2);
         if (FieldListDialog.this.closeOnSelection) {
            FieldListDialog.this.dialog.dispose();
         }
      }
   }
}
