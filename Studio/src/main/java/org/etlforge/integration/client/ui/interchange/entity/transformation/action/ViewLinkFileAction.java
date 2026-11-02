package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.application.DebugMode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.debug.client.file.DataFileType;

public final class ViewLinkFileAction extends ViewTestDataFileAction {
   public static final String ID = "action.ic.tx.ViewLinkFile";

   public ViewLinkFileAction(TransformationPage var1) {
      super("action.ic.tx.ViewLinkFile", var1, DataFileType.LINK);
   }

   @Override
   public boolean isSupported() {
      return DebugMode.IS_ENABLED;
   }
}
