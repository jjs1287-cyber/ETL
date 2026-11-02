package org.etlforge.integration.client.admin.engine;

import com.google.common.base.Preconditions;
import java.util.EnumSet;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.ServerStatus;
import org.etlforge.integration.client.server.AbstractServerCallback;

public abstract class AbstractEngineJob extends UiJob {
   private final ServerEngine engine;
   private final Admin serverApi;
   private final AbstractEngineJob.CallbackBase callback;

   protected AbstractEngineJob(ServerEngine var1, Admin var2, String var3) {
      super(var3);
      this.engine = Preconditions.checkNotNull(var1, "engine");
      this.serverApi = Preconditions.checkNotNull(var2, "serverApi");
      this.callback = this.createCallback();
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.callback);
   }

   protected final ServerEngine getEngine() {
      return this.engine;
   }

   protected final Admin getServerApi() {
      return this.serverApi;
   }

   protected final AbstractEngineJob.CallbackBase getCallback() {
      return this.callback;
   }

   protected abstract EnumSet<ServerStatus> getErrorStatus();

   protected abstract AbstractEngineJob.CallbackBase createCallback();

   @Override
   protected final void onJobCancelled() {
      this.callback.cancelled();
   }

   protected final void updateEngine(EnumSet<ServerStatus> var1, String var2) {
      this.engine.setStatus(var1);
      this.engine.setDetails(var2);
   }

   protected class CallbackBase extends AbstractServerCallback {
      @Override
      public final void caught(Throwable var1) {
         if (!AbstractEngineJob.this.isCancelled()) {
            AbstractEngineJob.this.setCanBeCancelled(false);
            AbstractEngineJob.this.setMayBeInterrupted(false);
            AbstractEngineJob.this.setMessage(UiJobMessages.ERROR);
            AbstractEngineJob.this.updateEngine(AbstractEngineJob.this.getErrorStatus(), var1.getMessage());
            super.caught(var1);
         }
      }

      @Override
      public final void cancelled() {
         AbstractEngineJob.this.setMessage(UiJobMessages.CANCELLED);
         AbstractEngineJob.this.engine.setDetails("");
         this.updateEngineStatusWhenCancelled();
      }

      protected void updateEngineStatusWhenCancelled() {
      }
   }
}
