package org.etlforge.integration.client.scheduler.server;

import java.util.Date;
import org.etlforge.integration.client.server.ServerCallback;

public interface TestScheduleCallback extends ServerCallback {
   void testCompleted(Date[] var1);
}
