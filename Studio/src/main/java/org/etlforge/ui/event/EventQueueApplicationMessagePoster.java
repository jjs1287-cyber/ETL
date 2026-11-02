package org.etlforge.ui.event;

import java.awt.Toolkit;

public class EventQueueApplicationMessagePoster implements ApplicationMessagePoster {
   @Override
   public void postMessage(ApplicationMessageEvent var1) {
      Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(var1);
   }
}
