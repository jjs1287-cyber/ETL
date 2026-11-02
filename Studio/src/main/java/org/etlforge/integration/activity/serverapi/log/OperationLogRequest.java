package org.etlforge.integration.activity.serverapi.log;

import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.client.log.LogStatusFilter;
import org.etlforge.integration.activity.client.log.MessageFilter;
import org.etlforge.integration.activity.serverapi.ActivityRequest;
import org.etlforge.util.SortOrder;

public final class OperationLogRequest extends ActivityRequest {
   public volatile SortOrder sortOrder;
   public volatile LogStatusFilter statusFilter;
   public volatile TimeWindowFilter timeFilter;
   public volatile MessageFilter messageFilter;
   public volatile long lastLogId = -1L;
}
