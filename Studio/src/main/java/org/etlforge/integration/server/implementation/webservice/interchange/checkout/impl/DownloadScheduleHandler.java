package org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl;

import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.Schedule;

public class DownloadScheduleHandler implements DownloadEntityHandler {
   public Schedule createEntity(StringKeyValuePairs var1) {
      return ScheduleFactory.getSchedule(var1);
   }
}
