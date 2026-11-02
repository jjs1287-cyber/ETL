package org.etlforge.integration.activity.serverapi;

import org.etlforge.integration.activity.client.filter.MaxEntriesToGetFilter;
import org.etlforge.integration.activity.client.filter.OperationNameFilter;

public abstract class ActivityRequest {
   public volatile Long opInstanceId;
   public volatile ActivitySubjectId subjectId;
   public volatile OperationNameFilter nameFilter;
   public volatile MaxEntriesToGetFilter maxNumberOfEntriesFilter;
}
