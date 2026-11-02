package org.etlforge.integration.client.ui.interchange.entity.schedule.action;

import java.util.Date;
import javax.swing.JComponent;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.scheduler.prefs.NumberOfSimulatedRunsPreference;
import org.etlforge.integration.client.scheduler.server.TestScheduleProvider;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.schedule.page.SchedulePage;
import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.integration.client.ui.scheduler.TestLocalScheduleJob;
import org.etlforge.integration.client.ui.scheduler.TestScheduleResultViewer;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.data.validation.ValidationContext;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class TestScheduleAction extends AbstractSingleActiveObjectAction<Schedule> {
   public static final String ID = "action.ic.TestSchedule";
   private final ApplicationWindow appWin;
   private final TestScheduleProvider provider;

   public TestScheduleAction(ApplicationWindow var1, TestScheduleProvider var2) {
      super(var1.getActiveObjectService(), "action.ic.TestSchedule", ScheduleActionDictionary.DICTIONARY, Schedule.class);
      this.appWin = var1;
      this.provider = var2;
   }

   protected void runOnObject(Schedule var1) {
      UiCompanion var2 = UiCompanion.of(var1);
      boolean var3 = var2.saveIfDirty();
      if (var3) {
         SchedulePage var4 = (SchedulePage)var2.openPage();
         if (var4 != null) {
            this.submitTest(var1, var4);
         }
      }
   }

   private void submitTest(Schedule var1, SchedulePage var2) {
      Application.getWorker().submitForParallel(new TestScheduleAction.Runner(var1, var2));
   }

   private final class ResultDisplayer implements Receiver<Date[]> {
      private final Schedule schedule;
      private final SchedulePage page;
      private final String title = ScheduleResources.getString("TestSchedule.Result.Title");

      public ResultDisplayer(Schedule var2, SchedulePage var3) {
         this.schedule = var2;
         this.page = var3;
      }

      public void handle(Date[] var1) {
         TestScheduleResultViewer var2 = new TestScheduleResultViewer(this.schedule, var1);
         if (this.page != null) {
            this.showInOverlay(var2);
         } else {
            this.showInDialog(var2);
         }
      }

      private void showInOverlay(UiProvider var1) {
         TestScheduleAction.this.appWin.getEditorService().openEditor(this.page);
         OverlayDecorator var2 = this.createOverlay(var1);
         var2.show(this.page.getOverlayContainer(), new OverlayCenterPositioner());
      }

      private OverlayDecorator createOverlay(UiProvider var1) {
         JComponent var2 = new BorderLayoutBuilder(var1).withBorder(Empty.border(10)).container();
         OverlayDecorator var3 = new OverlayDecorator(var2, this.title) {
            @Override
            protected void onHidden() {
               ResultDisplayer.this.page.requestFocus();
            }
         };
         var3.setBlocking(true);
         var3.useCloseOnly();
         var3.setFadeIn(AnimationPreference.get());
         var3.setAlpha(0.8F);
         return var3;
      }

      private void showInDialog(UiProvider var1) {
         UiProviderDialogDisplayer.display(TestScheduleAction.this.appWin.getFrame(), var1, this.title, KongaDialog.CLOSE_ONLY);
      }
   }

   private final class Runner implements ApplicationWorker.SafeRunnable {
      private final Schedule schedule;
      private final SchedulePage page;

      public Runner(Schedule var2, SchedulePage var3) {
         this.schedule = var2;
         this.page = var3;
      }

      @Override
      public void run() {
         if (this.isValid()) {
            TestLocalScheduleJob var1 = new TestLocalScheduleJob(this.schedule, TestScheduleAction.this.provider);
            var1.setNumberOfRuns(NumberOfSimulatedRunsPreference.get());
            var1.setResultReceiver(TestScheduleAction.this.new ResultDisplayer(this.schedule, this.page));
            WaitService var2 = this.page != null ? this.page.getWaitService() : TestScheduleAction.this.appWin;
            var1.submit(var2, 0);
         } else {
            Alert.error("The Schedule is invalid.", "Invalid Schedule");
         }
      }

      private boolean isValid() {
         ValidationResult var1 = this.schedule.validate(new ValidationContext() {
            @Override
            public IntegrationEntityLookup getDataStore() {
               return null;
            }

            @Override
            public ProjectDependencies getDependencies() {
               return null;
            }
         });
         return !var1.isFatal();
      }
   }
}
