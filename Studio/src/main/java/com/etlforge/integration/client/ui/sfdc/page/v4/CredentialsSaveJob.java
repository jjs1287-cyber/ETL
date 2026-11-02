package com.etlforge.integration.client.ui.sfdc.page.v4;

import com.etlforge.integration.client.sfdc.data.LoginScriptExpression;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.system.callback.Receiver;

class CredentialsSaveJob implements EntityPageSaveJob {
   private final Script script;
   private final EntityPersistor persistor;
   private final WsdlsStoreCredentialsUpdater wsdlCredentialsUpdater;
   private final LoginCredentials oldCredentials;
   private final LoginCredentials newCredentials;
   private final Receiver<LoginCredentials> undoRedoStateReceiver;

   public CredentialsSaveJob(
      Script var1, EntityPersistor var2, WsdlsStoreCredentialsUpdater var3, LoginCredentials var4, LoginCredentials var5, Receiver<LoginCredentials> var6
   ) {
      this.script = var1;
      this.persistor = var2;
      this.wsdlCredentialsUpdater = var3;
      this.oldCredentials = var4;
      this.newCredentials = var5;
      this.undoRedoStateReceiver = var6;
   }

   @Override
   public String getId() {
      return "sfdc_credentials_change";
   }

   @Override
   public String getDescription() {
      return "Credentials Change";
   }

   @Override
   public void save() {
      this.apply(this.newCredentials);
   }

   private void apply(LoginCredentials var1) {
      String var2 = this.script.getScript();
      String var3 = LoginScriptExpression.replaceCredentials(var2, var1);
      this.script.setScript(var3);

      try {
         this.persistor.save(this.script);
      } catch (InterchangeSavingException var5) {
         this.script.setScript(var2);
         ErrorLog.attention(CredentialsSaveJob.class, "Failed to update the login script with new credentials.", var5);
         var5.printStackTrace();
      }

      this.wsdlCredentialsUpdater.updateCredentials(var1);
   }

   @Override
   public void postSaveRevert() {
      this.apply(this.oldCredentials);
   }

   @Override
   public void undoWithoutSaving() {
      this.undoRedoStateReceiver.handle(this.oldCredentials);
   }

   @Override
   public void redoWithoutSaving() {
      this.undoRedoStateReceiver.handle(this.newCredentials);
   }
}
