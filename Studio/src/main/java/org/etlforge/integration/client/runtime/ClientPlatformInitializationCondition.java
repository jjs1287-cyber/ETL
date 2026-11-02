package org.etlforge.integration.client.runtime;

import java.util.concurrent.TimeUnit;
import org.etlforge.system.thread.Condition;

public final class ClientPlatformInitializationCondition implements Condition {
   @Override
   public void waitFor() throws InterruptedException {
      ClientPlatform.waitForInitialization();
   }

   @Override
   public boolean waitFor(long var1, TimeUnit var3) throws InterruptedException {
      return ClientPlatform.waitForInitialization(var1, var3);
   }
}
