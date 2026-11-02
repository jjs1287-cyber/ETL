package org.etlforge.application.ui.job;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import org.etlforge.system.callback.Receiver;

public abstract class ResultProducingUiJob<T> extends UiJob {
   private volatile Receiver<T> resultReceiver;
   private volatile boolean ignoreNullResult = true;

   public ResultProducingUiJob(String var1) {
      super(var1);
   }

   public final void setResultReceiver(Receiver<T> var1) {
      this.resultReceiver = Preconditions.checkNotNull(var1);
   }

   public final void setIgnoreNullResult(boolean var1) {
      this.ignoreNullResult = var1;
   }

   protected final void resultWasComputed(final T var1) {
      if (var1 != null || !this.ignoreNullResult) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ResultProducingUiJob.this.resultReceiver.handle((T)var1);
            }
         });
      }
   }
}
