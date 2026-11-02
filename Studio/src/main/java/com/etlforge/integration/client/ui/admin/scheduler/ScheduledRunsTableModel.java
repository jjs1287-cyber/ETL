package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.util.Collection;
import java.util.Date;
import org.etlforge.integration.client.scheduler.ScheduledRun;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public class ScheduledRunsTableModel extends TypedAbstractKongaTableModel<ScheduledRun> {
   private static final TableColumnMetaData WHEN = new TableColumnMetaData(
      EnterpriseAdminResources.getString("ScheduledRuns.Table.Time"), Date.class, false, 75
   );
   private static final TableColumnMetaData OPERATION = new TableColumnMetaData(
      EnterpriseAdminResources.getString("ScheduledRuns.Table.Operation"), Operation.class, false, 200
   );
   private static final TableColumnMetaData SCHEDULE = new TableColumnMetaData(
      EnterpriseAdminResources.getString("ScheduledRuns.Table.Schedule"), Schedule.class, false, 200
   );

   public ScheduledRunsTableModel() {
      super(ScheduledRun.class, WHEN, OPERATION, SCHEDULE);
   }

   public ScheduledRunsTableModel(Collection<ScheduledRun> var1) {
      super(ScheduledRun.class, new TableColumnMetaData[]{WHEN, OPERATION, SCHEDULE}, var1);
   }

   protected Object[] getColumnDataForTypedObject(ScheduledRun var1) {
      return new Object[]{var1.getTimeOfRun(), var1.getOperation(), var1.getSchedule()};
   }
}
