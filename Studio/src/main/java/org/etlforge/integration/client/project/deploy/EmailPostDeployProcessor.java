package org.etlforge.integration.client.project.deploy;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class EmailPostDeployProcessor extends DefaultPostDeployEntityProcessor {
   @ExtensionObjectConstructor
   public EmailPostDeployProcessor(EmailMessage var1) {
      super(var1);
   }

   @Override
   protected void blankOutPasswords(IntegrationEntity var1) {
      var1.setProperty("account_password", null);
   }
}
