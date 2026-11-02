package org.etlforge.integration.client.project.export;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.jitterpack.ExportItemPreparer;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class WebServiceCallPreparer extends ExportItemPreparer {
   private final WebServiceCall wsCall;

   @ExtensionObjectConstructor
   public WebServiceCallPreparer(WebServiceCall var1) {
      this.wsCall = var1;
   }

   @Override
   public void prepareItem(PostExportManager var1) {
      this.blankOutPassword(this.wsCall, "httppassword", var1);
   }
}
