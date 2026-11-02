package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.event.ActionEvent;
import java.util.Date;
import org.etlforge.integration.client.scheduler.prefs.NumberOfSimulatedRunsPreference;
import org.etlforge.integration.client.scheduler.server.TestScheduleProvider;
import org.etlforge.integration.client.ui.scheduler.TestDeployedScheduleJob;
import org.etlforge.integration.client.ui.scheduler.TestScheduleResultViewer;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.UiUtils;

public final class TestScheduleAction extends AbstractSingleObjectSelectionAction<Schedule> {
   private final TestScheduleProvider provider;

   public TestScheduleAction(TestScheduleProvider var1) {
      super(Schedule.class, EnterpriseAdminResources.getString("TestSchedule.Name"));
      this.provider = var1;
      this.putValue("ShortDescription", EnterpriseAdminResources.getString("TestSchedule.ToolTip"));
   }

   protected void run(Schedule var1, ActionEvent var2) {
      TestDeployedScheduleJob var3 = new TestDeployedScheduleJob(var1, this.provider);
      var3.setResultReceiver(new TestScheduleAction.ResultDisplayer(var1));
      var3.setNumberOfRuns(NumberOfSimulatedRunsPreference.get());
      var3.submit();
   }

   private static final class ResultDisplayer implements Receiver<Date[]> {
      private final Schedule schedule;

      public ResultDisplayer(Schedule var1) {
         this.schedule = var1;
      }

      public void handle(Date[] var1) {
         TestScheduleResultViewer var2 = new TestScheduleResultViewer(this.schedule, var1);
         this.showInDialog(var2);
      }

      private void showInDialog(UiProvider var1) {
         UiProviderDialogDisplayer.display(UiUtils.getActiveFrame(), var1, "Test Schedule Result", KongaDialog.CLOSE_ONLY);
      }
   }
}
