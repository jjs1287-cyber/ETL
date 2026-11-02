package org.etlforge.integration.activity.serverapi.history;

import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.client.filter.TransferSizeFilter;
import org.etlforge.integration.activity.serverapi.ActivityRequest;
import org.etlforge.util.SortOrder;

public final class HistoryRequest extends ActivityRequest {
   public volatile SortOrder sortOrder;
   public volatile TimeWindowFilter timeFilter;
   public volatile TransferSizeFilter transferSizeFilter;
}
