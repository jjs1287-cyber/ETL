package org.etlforge.integration.activity.serverapi.queue;

import org.etlforge.integration.activity.client.queue.QueueStatusFilter;
import org.etlforge.integration.activity.serverapi.ActivityRequest;

public final class OperationQueueRequest extends ActivityRequest {
   public volatile QueueStatusFilter statusFilter;
}
