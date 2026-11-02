package org.etlforge.ui.event;

import java.util.EventListener;

public interface ApplicationMessageListener extends EventListener {
   void incomingMessage(ApplicationMessageEvent var1);
}
