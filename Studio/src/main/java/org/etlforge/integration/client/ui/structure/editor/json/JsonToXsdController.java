package org.etlforge.integration.client.ui.structure.editor.json;

import org.etlforge.integration.client.ui.structure.editor.xsd.CreateXsdUiJob;
import org.etlforge.integration.client.ui.structure.editor.xsd.FileType;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileFactory;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileToXsdController;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileToXsdUi;

final class JsonToXsdController extends SampleFileToXsdController {
   public static JsonToXsdController install(SampleFileToXsdUi var0) {
      JsonToXsdController var1 = new JsonToXsdController(var0);
      var0.setController(var1);
      return var1;
   }

   private JsonToXsdController(SampleFileToXsdUi var1) {
      super(FileType.JSON, var1, "json2xsd");
   }

   @Override
   protected CreateXsdUiJob createUiJob(SampleFileFactory var1) {
      return new JsonToXsdUiJob(var1);
   }
}
