package org.etlforge.ui.event;

import java.awt.AWTEvent;

public abstract class KongaAWTEvent extends AWTEvent {
   public static final int APPLICATION_MESSAGE_EVENT_ID = 2000;

   public KongaAWTEvent(Object var1, int var2) {
      super(var1, var2);
   }
}
