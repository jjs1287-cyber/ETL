package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.nio.charset.Charset;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.StructureEncodings;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.debug.client.file.DataFileType;

public final class ViewTargetFileAction extends ViewTestDataFileAction {
   public static final String ID = "action.ic.tx.ViewTargetFile";

   public ViewTargetFileAction(TransformationPage var1) {
      super("action.ic.tx.ViewTargetFile", var1, DataFileType.TARGET);
   }

   @Override
   protected Charset getCharset(Transformation var1) {
      DataStructure var2 = var1.getOutputStructure();
      return StructureEncodings.getCharset(var2);
   }
}
