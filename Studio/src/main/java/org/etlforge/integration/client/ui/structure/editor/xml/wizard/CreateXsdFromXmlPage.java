package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.io.File;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.structure.editor.xml.xml2xsd.XmlToXsdController;
import org.etlforge.integration.client.ui.structure.editor.xsd.CreateXsdFromSampleFilePage;
import org.etlforge.integration.client.ui.structure.editor.xsd.CreateXsdResult;
import org.etlforge.integration.client.ui.structure.editor.xsd.FileType;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileToXsdUi;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.xml.ExpandedName;

public final class CreateXsdFromXmlPage extends CreateXsdFromSampleFilePage implements XmlStructureWizardPage {
   public CreateXsdFromXmlPage(Wizard var1, XsdFileStore var2) {
      super(var1, FileType.XML, "Create XSD From XML Sample", var2);
   }

   @Override
   protected void installController(SampleFileToXsdUi var1) {
      XmlToXsdController.install(var1);
   }

   @Override
   public void applyTo(XmlStructure var1) {
      CreateXsdResult var2 = this.getResult();
      File var3 = var2.getMainXsd();
      var1.setFilePath(var3.getName());
      ExpandedName var4 = var2.getRootName();
      var1.setRootNodeName(var4.toString());
   }
}
