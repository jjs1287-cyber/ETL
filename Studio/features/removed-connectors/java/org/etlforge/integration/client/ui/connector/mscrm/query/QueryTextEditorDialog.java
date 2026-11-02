package org.etlforge.integration.client.ui.connector.mscrm.query;

import com.google.common.base.Optional;
import java.awt.Window;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.connector.mscrm.query.wizard.QueryEditor;
import org.etlforge.integration.client.ui.connector.mscrm.query.wizard.QuerySelectionModel;
import org.etlforge.integration.client.ui.connector.mscrm.query.wizard.TextQueryEditor;
import org.etlforge.integration.data.connector.FetchXmlValidator;
import org.etlforge.integration.mscrm.query.FetchXml;
import org.etlforge.ui.wait.WaitLock;

final class QueryTextEditorDialog {
   public static Optional<String> editQueryXml(Window var0, String var1, String var2) {
      QuerySelectionModel var3 = new QuerySelectionModel();
      var3.setSelectedQuery(new FetchXml("Query", var2));
      TextQueryEditor var4 = new TextQueryEditor(var3);
      QueryTextEditorDialog.Dialog var5 = new QueryTextEditorDialog.Dialog(var4, var1, var0);
      var5.setVisible(true);
      if (var5.wasOkPressed()) {
         String var6 = var5.getQueryXml();
         if (!var6.equals(var2)) {
            return Optional.of(var6);
         }
      }

      return Optional.absent();
   }

   private static class Dialog extends WizardStyleDialog {
      private final QueryEditor editor;
      private final String expectedObjectName;

      public Dialog(QueryEditor var1, String var2, Window var3) {
         super(var3, "Edit Query XML", var1);
         this.editor = var1;
         this.expectedObjectName = var2;
         this.manageLocation(QueryTextEditorDialog.class);
         this.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
         this.setOkButtonAsDefault();
      }

      @Override
      protected void onOk() {
         WaitLock var1 = this.startWait();
         QueryTextEditorDialog.XmlValidator var2 = new QueryTextEditorDialog.XmlValidator(this, this.expectedObjectName, var1);
         var2.execute();
      }

      public void closeDialogAfterOk() {
         super.onOk();
      }

      public String getQueryXml() {
         return this.editor.getFetchXml();
      }
   }

   private static class XmlValidator extends KongaSwingWorker<Void, Void> {
      private final String xml;
      private final String expectedObjectName;
      private final QueryTextEditorDialog.Dialog dialog;
      private final WaitLock waitLock;

      public XmlValidator(QueryTextEditorDialog.Dialog var1, String var2, WaitLock var3) {
         this.dialog = var1;
         this.expectedObjectName = var2;
         this.xml = var1.getQueryXml();
         this.waitLock = var3;
         this.doNotLogErrors();
      }

      protected Void doInBackground() throws Exception {
         FetchXmlValidator.Result var1 = FetchXmlValidator.validate(this.xml);
         if (!var1.isValid()) {
            throw new Exception(var1.getError());
         } else {
            String var2 = var1.getObjectName();
            if (!var2.equalsIgnoreCase(this.expectedObjectName)) {
               throw new Exception(
                  "It is not allowed to change the object being queried. Original object: " + this.expectedObjectName + ". New object: " + var2
               );
            } else {
               return null;
            }
         }
      }

      @Override
      protected void onFailure(Throwable var1) {
         NonBlockingAlert.error(this.dialog, var1.getMessage(), "Invalid Query");
      }

      @Override
      protected void finished() {
         this.waitLock.release();
      }

      protected void onSuccess(Void var1) {
         this.dialog.closeDialogAfterOk();
      }
   }
}
