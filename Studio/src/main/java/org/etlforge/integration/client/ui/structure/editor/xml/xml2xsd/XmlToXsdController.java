package org.etlforge.integration.client.ui.structure.editor.xml.xml2xsd;

import org.etlforge.integration.client.ui.structure.editor.xsd.CreateXsdUiJob;
import org.etlforge.integration.client.ui.structure.editor.xsd.FileType;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileFactory;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileToXsdController;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileToXsdUi;

public final class XmlToXsdController extends SampleFileToXsdController {
   public static XmlToXsdController install(SampleFileToXsdUi var0) {
      XmlToXsdController var1 = new XmlToXsdController(var0);
      var0.setController(var1);
      return var1;
   }

   private XmlToXsdController(SampleFileToXsdUi var1) {
      super(FileType.XML, var1, "xml2xsd");
   }

   @Override
   protected CreateXsdUiJob createUiJob(SampleFileFactory var1) {
      return new XmlToXsdUiJob(var1);
   }
}
