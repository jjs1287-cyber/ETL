package org.etlforge.integration.client.ui.connector.mscrm.query;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.FetchXmlParser;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.QueryBuilderController;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.QueryBuilderRootModel;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.QueryBuilderUi;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.UndoRedo;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.wait.WaitLock;

final class EditInQueryBuilderAction extends AbstractEditQueryAction {
   private final Supplier<MSCrmObjectLookup> objectLookupSupplier;

   public EditInQueryBuilderAction(Supplier<MSCrmObjectLookup> var1, Receiver<String> var2) {
      super("In the ETLForge Query Builder", var2);
      this.objectLookupSupplier = var1;
      this.putValue("ShortDescription", "Edit the fetch XML in ETLForge's query builder");
   }

   @Override
   protected void edit() {
      MSCrmObjectLookup var1 = this.objectLookupSupplier.get();
      WaitLock var2 = this.getPage().getWaitService().startWait();
      EditInQueryBuilderAction.QueryBuilderEditor var3 = new EditInQueryBuilderAction.QueryBuilderEditor(var1, var2);
      var3.execute();
   }

   private class QueryBuilderEditor extends KongaSwingWorker<QueryBuilderRootModel, Void> {
      private final WaitLock waitLock;
      private final MSCrmObjectLookup objectLookup;

      public QueryBuilderEditor(MSCrmObjectLookup var2, WaitLock var3) {
         this.objectLookup = var2;
         this.waitLock = var3;
      }

      protected QueryBuilderRootModel doInBackground() throws Exception {
         FetchXmlParser var1 = new FetchXmlParser(this.objectLookup);
         return var1.parse(EditInQueryBuilderAction.this.getCurrentXml());
      }

      @Override
      protected void finished() {
         this.waitLock.release();
      }

      protected void onSuccess(QueryBuilderRootModel var1) {
         QueryBuilderUi var2 = new QueryBuilderUi(var1, new QueryBuilderController(this.objectLookup));
         WizardStyleDialog var3 = this.createDialog(var2);
         UndoRedoController var4 = UndoRedo.install(var2, var3);
         var3.setVisible(true);
         var4.dispose();
         Optional var5 = this.getEditedXml(var1, var3);
         if (var5.isPresent()) {
            EditInQueryBuilderAction.this.applyChange((String)var5.get());
         }
      }

      private WizardStyleDialog createDialog(QueryBuilderUi var1) {
         String var2 = "Edit Query XML: " + var1.getModel().getObject().getName();
         WizardStyleDialog var3 = new WizardStyleDialog(EditInQueryBuilderAction.this.getPage().getWindow().getFrame(), var2, var1);
         var3.setOkButtonAsDefault();
         var3.manageLocation(QueryTextEditorDialog.class);
         return var3;
      }

      private Optional<String> getEditedXml(QueryBuilderRootModel var1, WizardStyleDialog var2) {
         Optional var3 = Optional.absent();
         if (var2.wasOkPressed()) {
            String var4 = var1.buildXml();
            if (!var4.equals(EditInQueryBuilderAction.this.getCurrentXml())) {
               var3 = Optional.of(var4);
            }
         }

         return var3;
      }
   }
}
