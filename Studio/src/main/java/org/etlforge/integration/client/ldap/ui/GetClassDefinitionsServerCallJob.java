package org.etlforge.integration.client.ldap.ui;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetObjectClassesCallback;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.client.LdapInfoProviderService;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.ui.wait.WaitLock;

public final class GetClassDefinitionsServerCallJob extends UiJob {
   private final LdapLocation location;
   private final GetObjectClassesCallback internalCallback;

   public GetClassDefinitionsServerCallJob(LdapLocation var1, GetObjectClassesCallback var2, WaitLock var3) {
      super(PackageResources.GetClassDefinitionsJob.NAME);
      this.location = var1;
      this.internalCallback = new GetClassDefinitionsServerCallJob.Callback(var2, var3);
      this.setUncaughtExceptionHandler(new ExceptionHandler<Throwable>() {
         @Override
         public void caught(Throwable var1) {
            GetClassDefinitionsServerCallJob.this.internalCallback.caught(var1);
         }
      });
   }

   @Override
   protected void runImpl() {
      if (!this.isCancelled()) {
         LdapInfoProviderService var1 = new LdapInfoProviderService();
         var1.getObjectClassDefinitions(this.location, this.internalCallback, false);
      }
   }

   @Override
   protected void onJobCancelled() {
      this.internalCallback.cancelled();
   }

   private static final class Callback implements GetObjectClassesCallback {
      private final GetObjectClassesCallback externalCallback;
      private final WaitLock waitLock;

      public Callback(GetObjectClassesCallback var1, WaitLock var2) {
         this.externalCallback = var1;
         this.waitLock = var2;
      }

      @Override
      public void objectClassesRetrieved(ObjectClassDefinition[] var1) {
         this.waitLock.release();
         this.externalCallback.objectClassesRetrieved(var1);
      }

      @Override
      public void cancelled() {
         this.waitLock.release();
         this.externalCallback.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         this.waitLock.release();
         this.externalCallback.caught(var1);
         ErrorLog.attention(GetClassDefinitionsServerCallJob.class, PackageResources.GetClassDefinitionsJob.ERROR, var1);
      }
   }
}
