package org.etlforge.integration.client.ui.structure.editor.json;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public final class JsonStructureWizard extends Wizard<JsonStructure> {
   private volatile JsonStructure createdStructure;

   public JsonStructureWizard(JsonStructure var1, XsdFileStore var2) {
      JsonPageChain var3 = new JsonPageChain(this, var2);
      var3.setStructureToEdit(var1);
      this.setPageChain(var3);
      this.setTitle("JSON");
   }

   protected JsonStructure finishWizard() throws WizardPageException {
      JsonStructure var1 = null;

      for (WizardPage var3 : this.getAllApplicablePages()) {
         if (var3 instanceof JsonStructureWizardPage) {
            var1 = ((JsonStructureWizardPage)var3).getDefinedStructure();
            if (var1 != null) {
               break;
            }
         }
      }

      this.createdStructure = var1;
      return var1;
   }

   @Override
   protected boolean onWizardCancelled() {
      return true;
   }

   public JsonStructure getCreatedStructure() {
      return this.createdStructure != null ? new JsonStructure(this.createdStructure) : null;
   }
}
