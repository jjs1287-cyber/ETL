package org.etlforge.integration.client.ldap.ui;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetLdapStructuresCallback;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.client.LdapInfoProviderService;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.ui.wait.WaitLock;

public final class GetClassStructuresServerCallJob extends UiJob {
   private final NumericOid[] oids;
   private final LdapLocation location;
   private final GetLdapStructuresCallback internalCallback;

   public GetClassStructuresServerCallJob(NumericOid[] var1, LdapLocation var2, GetLdapStructuresCallback var3, WaitLock var4) {
      super("");
      this.oids = var1;
      this.location = var2;
      this.internalCallback = new GetClassStructuresServerCallJob.Callback(var3, var4);
      this.setUncaughtExceptionHandler(new ExceptionHandler<Throwable>() {
         @Override
         public void caught(Throwable var1) {
            GetClassStructuresServerCallJob.this.internalCallback.caught(var1);
         }
      });
   }

   @Override
   protected void onJobCancelled() {
      this.internalCallback.cancelled();
   }

   @Override
   protected void runImpl() {
      if (!this.isCancelled()) {
         LdapInfoProviderService var1 = new LdapInfoProviderService();
         var1.getStructures(this.location, this.oids, this.internalCallback, false);
      }
   }

   private static final class Callback implements GetLdapStructuresCallback {
      private final GetLdapStructuresCallback externalCallback;
      private final WaitLock waitLock;

      public Callback(GetLdapStructuresCallback var1, WaitLock var2) {
         this.externalCallback = var1;
         this.waitLock = var2;
      }

      @Override
      public void structuresRetrieved(ObjectClassStructure[] var1) {
         this.waitLock.release();
         this.externalCallback.structuresRetrieved(var1);
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
         ErrorLog.attention(GetClassDefinitionsServerCallJob.class, PackageResources.GetStructuresJob.ERROR, var1);
      }
   }
}
