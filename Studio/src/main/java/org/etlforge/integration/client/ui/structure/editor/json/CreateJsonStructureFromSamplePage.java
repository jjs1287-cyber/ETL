package org.etlforge.integration.client.ui.structure.editor.json;

import java.io.File;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.structure.editor.xsd.CreateXsdFromSampleFilePage;
import org.etlforge.integration.client.ui.structure.editor.xsd.CreateXsdResult;
import org.etlforge.integration.client.ui.structure.editor.xsd.FileType;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileToXsdUi;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.xml.ExpandedName;

public final class CreateJsonStructureFromSamplePage extends CreateXsdFromSampleFilePage implements JsonStructureWizardPage {
   public CreateJsonStructureFromSamplePage(Wizard var1, XsdFileStore var2) {
      super(var1, FileType.JSON, "Create Sructure From JSON Sample", var2);
   }

   @Override
   protected void installController(SampleFileToXsdUi var1) {
      JsonToXsdController.install(var1);
   }

   @Override
   public void applyTo(JsonStructure var1) {
      CreateXsdResult var2 = this.getResult();
      File var3 = var2.getMainXsd();
      var1.setFileName(var3.getName());
      ExpandedName var4 = var2.getRootName();
      var1.setRootName(var4.toString());
   }

   @Override
   public JsonStructure getDefinedStructure() {
      CreateXsdResult var1 = this.getResult();
      File var2 = var1.getMainXsd();
      ExpandedName var3 = var1.getRootName();
      return new JsonStructure(var2.getName(), var3.toString());
   }
}
