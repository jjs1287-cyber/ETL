package org.etlforge.integration.client.project.export;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.jitterpack.ExportItemPreparer;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.data.entity.EmailMessage;

public final class EmailMessagePreparer extends ExportItemPreparer {
   private final EmailMessage email;

   @ExtensionObjectConstructor
   public EmailMessagePreparer(EmailMessage var1) {
      this.email = var1;
   }

   @Override
   public void prepareItem(PostExportManager var1) {
      this.blankOutPassword(this.email, "account_password", var1);
   }
}
