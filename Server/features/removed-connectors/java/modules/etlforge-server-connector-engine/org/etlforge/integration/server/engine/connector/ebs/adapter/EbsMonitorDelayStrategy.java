package org.etlforge.integration.server.engine.connector.ebs.adapter;

public class EbsMonitorDelayStrategy {
   private static int maxMilliseconds = 100000;
   private int delayMilliseconds = 500;

   EbsMonitorDelayStrategy() {
   }

   void delay() throws InterruptedException {
      Thread.sleep(this.delayMilliseconds);
      if (this.delayMilliseconds < maxMilliseconds) {
         this.delayMilliseconds <<= 1;
         if (this.delayMilliseconds > maxMilliseconds) {
            this.delayMilliseconds = maxMilliseconds;
         }
      }
   }
}
