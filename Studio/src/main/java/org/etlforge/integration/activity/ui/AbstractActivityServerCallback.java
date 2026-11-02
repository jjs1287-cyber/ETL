package org.etlforge.integration.activity.ui;

import org.etlforge.integration.client.server.AbstractServerCallback;

public abstract class AbstractActivityServerCallback extends AbstractServerCallback {
   private final AbstractActivityDisplayModel<?> model;

   public AbstractActivityServerCallback(AbstractActivityDisplayModel<?> var1) {
      this.model = var1;
   }

   @Override
   public void caught(Throwable var1) {
      this.model.setTalkingToServer(false);
      super.caught(var1);
   }

   @Override
   public final void cancelled() {
      this.model.setTalkingToServer(false);
   }
}
