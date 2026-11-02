package org.etlforge.integration.client.ui.operation.options;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.id.TextDocumentId;

final class EditValidationRulesAction extends AbstractAction {
   private final TextDocumentId docId;
   private final EntityExplorerSupport projectExplorer;

   public EditValidationRulesAction(TextDocumentId var1, EntityExplorerSupport var2) {
      super("Edit Rules...");
      this.docId = var1;
      this.projectExplorer = var2;
      this.putValue("ShortDescription", "View/Edit the validation rules");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EditValidationRulesAction.this.open();
         }
      });
   }

   private void open() {
      TextDocument var1 = this.lookupTextDocument();
      if (var1 == null) {
         this.showMissingDocumentError();
      } else {
         this.projectExplorer.editInModalDialog(var1);
      }
   }

   private TextDocument lookupTextDocument() {
      IntegrationEntityLookup var1 = this.projectExplorer.getProject().getItemLookup();
      return var1.getEntity(this.docId, TextDocument.class);
   }

   private void showMissingDocumentError() {
      String var1 = String.format("Cannot find the %s definition with GUID = %s. This is a bug.", EntityType.Document.getDisplayName(), this.docId);
      Alert.error(this.projectExplorer.getWindow().getFrame(), var1, "Error");
   }
}
