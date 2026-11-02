package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.apache.commons.lang.time.DateUtils;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.util.SingleLockWaitServiceBinding;
import org.etlforge.integration.client.scheduler.ScheduledOperation;
import org.etlforge.integration.client.scheduler.ScheduledRun;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.DateFieldBinding;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.selection.SelectionSourceConverter;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.time.DatePicker;
import org.etlforge.ui.wait.SingleLockWaitService;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.Property;

public final class ScheduledRunsView implements UiProvider {
   private final ScheduledRunsModel model;
   private final KongaTextField selectedDayField;
   private final Action selectDayAction;
   private final Action disableScheduleAction;
   private final Action disableOperationScheduleAction;
   private final ScheduledRunsTable table;
   private final KongaLayeredPane overlayContainer;
   private final SingleLockWaitService waitService;
   private final ScheduledRunsView.RefreshAction refreshAction;
   private Border captionBorder;

   public ScheduledRunsView(ScheduledRunsModel var1, ProjectAdministration var2) {
      this.model = var1;
      this.selectedDayField = new KongaTextField(10);
      this.selectedDayField.setFixedSize(true);
      this.selectedDayField.setEditable(false);
      this.selectDayAction = new ScheduledRunsView.SelectDayAction();
      this.table = new ScheduledRunsTable(var1);
      this.refreshAction = new ScheduledRunsView.RefreshAction();
      this.disableScheduleAction = this.createDisableScheduleAction(var2);
      this.disableOperationScheduleAction = this.createDisableOperationScheduleAction(var2);
      this.captionBorder = new BorderBuilder().etched().empty(3);
      this.overlayContainer = new KongaLayeredPane();
      this.waitService = new SingleLockWaitService(this.overlayContainer);
      this.createModelBindings();
   }

   private void createModelBindings() {
      this.createDayBinding();
      this.createIsDownloadingBinding();
   }

   private void createDayBinding() {
      Property var1 = this.model.getProperty("day");
      DateFieldBinding var2 = new DateFieldBinding(var1, this.selectedDayField, DateFormat.getDateInstance());
      var2.syncUi();
   }

   private void createIsDownloadingBinding() {
      Property var1 = this.model.getProperty("isDownloading");
      new SingleLockWaitServiceBinding(var1, this.overlayContainer);
      new ScheduledRunsView.DaySelectionEnabler(var1);
   }

   public void setCaptionBorder(Border var1) {
      this.captionBorder = var1;
   }

   public void setDownloadHandler(Action var1) {
      this.refreshAction.setDelegate(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      new ScheduledRunsView.Layout(this.overlayContainer);
      var1.display(this.overlayContainer);
      this.installKeyBindings();
   }

   @Override
   public void requestFocus() {
   }

   private void installKeyBindings() {
      this.overlayContainer.getInputMap(2).put(KeyUtils.REFRESH, "refresh");
      this.overlayContainer.getActionMap().put("refresh", this.refreshAction);
   }

   private Action createDisableScheduleAction(ProjectAdministration var1) {
      DisableSchedulesAction var2 = new DisableSchedulesAction(var1);
      var2.setCallback(new ScheduledRunsView.SetScheduledEnabledActionCallbackImpl());
      var2.setSelectionSource(new SelectionSourceConverter(this.table.asSelectionSource(), new SelectionConverter.Adapter() {
         @Override
         public Object fromSelection(Object var1) {
            return ((ScheduledRun)var1).getSchedule();
         }
      }));
      return var2;
   }

   private void setWaiting(boolean var1) {
      this.waitService.setWaiting(var1);
   }

   private Action createDisableOperationScheduleAction(ProjectAdministration var1) {
      DisableOperationSchedulesAction var2 = new DisableOperationSchedulesAction(var1);
      var2.setCallback(new ScheduledRunsView.SetOperationScheduledEnabledActionCallbackImpl());
      var2.setSelectionSource(new SelectionSourceConverter(this.table.asSelectionSource(), new SelectionConverter.Adapter() {
         @Override
         public Object fromSelection(Object var1) {
            ScheduledRun var2x = (ScheduledRun)var1;
            return new ScheduledOperation(var2x.getSchedule(), var2x.getOperation());
         }
      }));
      return var2;
   }

   private class DaySelectionEnabler extends AbstractBinding<Boolean> {
      public DaySelectionEnabler(Property<Boolean> var2) {
         super(var2);
      }

      protected void updateUiWithValue(Boolean var1) {
         boolean var2 = !var1;
         ScheduledRunsView.this.selectDayAction.setEnabled(var2);
      }
   }

   private final class Layout {
      public Layout(KongaLayeredPane var2) {
         JPanel var3 = Vertical.borderLayout()
            .of(this.northControlPanel(), ScheduledRunsView.this.table, this.southControlPanel())
            .withGap(10)
            .withEmptyBorder(10)
            .deeplyNonOpaque()
            .container();
         var2.setBaseComponent(var3);
      }

      private Object northControlPanel() {
         BoxBuilder var1 = Horizontal.box();
         var1.add(EntityIcons.forType(EntityType.Schedule).getDefaultIcon(EntityIcons.Size.LARGE))
            .add(EnterpriseAdminResources.getString("ScheduledRuns.Caption"))
            .rigid(10)
            .addAllWithSpace(
               5,
               ScheduledRunsView.this.selectedDayField,
               ScheduledRunsView.this.new PreviousDayAction().forToolbar(),
               ScheduledRunsView.this.new NextDayAction().forToolbar()
            )
            .rigid(10)
            .add(ScheduledRunsView.this.selectDayAction);
         if (ScheduledRunsView.this.refreshAction != null) {
            var1.rigid(20).add(ScheduledRunsView.this.refreshAction);
         }

         return var1.glue().withBorder(ScheduledRunsView.this.captionBorder);
      }

      private Object southControlPanel() {
         Grid var1 = Grid.oneRow(10);
         var1.addAll(ScheduledRunsView.this.disableScheduleAction, ScheduledRunsView.this.disableOperationScheduleAction);
         var1.withBorder(Empty.border(0, 0, 10, 0));
         return InvisiblePanel.of(var1);
      }
   }

   private final class NextDayAction extends ScheduledRunsView.StepOneDayAction {
      public NextDayAction() {
         super(1, ApplicationResources.ICONS.getIcon("ArrowRight.16"));
         this.putValue("ShortDescription", EnterpriseAdminResources.getString("ScheduledRuns.NextDay.ToolTip"));
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(39));
      }
   }

   private final class PreviousDayAction extends ScheduledRunsView.StepOneDayAction {
      public PreviousDayAction() {
         super(-1, ApplicationResources.ICONS.getIcon("ArrowLeft.16"));
         this.putValue("ShortDescription", EnterpriseAdminResources.getString("ScheduledRuns.PreviousDay.ToolTip"));
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(37));
      }
   }

   private static final class RefreshAction extends AbstractAction implements PropertyChangeListener {
      private Action delegate;

      public RefreshAction() {
         ActionDecorators.REFRESH.decorate(this);
         this.setEnabled(false);
      }

      public void setDelegate(Action var1) {
         if (this.delegate != null) {
            this.delegate.removePropertyChangeListener(this);
         }

         this.delegate = var1;
         if (var1 != null) {
            var1.addPropertyChangeListener(this);
         }

         this.updateEnabledState();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.delegate != null) {
            this.delegate.actionPerformed(var1);
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if ("enabled".equals(var1.getPropertyName())) {
            this.updateEnabledState();
         }
      }

      private void updateEnabledState() {
         this.setEnabled(this.delegate != null && this.delegate.isEnabled());
      }
   }

   private final class SelectDayAction extends AbstractAction {
      public SelectDayAction() {
         super(EnterpriseAdminResources.getString("ScheduledRuns.SelectDay.Name"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SelectDayAction.this.doSelect();
            }
         });
      }

      private void doSelect() {
         Receiver var1 = new Receiver<Date>() {
            public void handle(Date var1) {
               if (var1 != null) {
                  ScheduledRunsView.this.model.setDay(var1);
               }
            }
         };
         DatePicker.selectFromOverlay(var1, ScheduledRunsView.this.overlayContainer, ScheduledRunsView.this.model.getDay(), null, null);
      }
   }

   private final class SetOperationScheduledEnabledActionCallbackImpl implements SetOperationScheduledEnabledActionCallback {
      private SetOperationScheduledEnabledActionCallbackImpl() {
      }

      @Override
      public void operationsUpdated(Collection<OperationId> var1, boolean var2) {
         ActionUtils.run(ScheduledRunsView.this.refreshAction, ScheduledRunsView.this, null);
      }

      @Override
      public synchronized void setTalkingToServer(boolean var1) {
         ScheduledRunsView.this.setWaiting(var1);
      }
   }

   private class SetScheduledEnabledActionCallbackImpl implements SetScheduledEnabledActionCallback {
      private SetScheduledEnabledActionCallbackImpl() {
      }

      @Override
      public void schedulesUpdated(Collection<ScheduleId> var1, boolean var2) {
         ActionUtils.run(ScheduledRunsView.this.refreshAction, ScheduledRunsView.this, null);
      }

      @Override
      public synchronized void setTalkingToServer(boolean var1) {
         ScheduledRunsView.this.setWaiting(var1);
      }
   }

   private abstract class StepOneDayAction extends AbstractAction {
      private final int direction;

      protected StepOneDayAction(int var2, Icon var3) {
         super(null, var3);
         this.direction = var2;
      }

      @Override
      public final void actionPerformed(ActionEvent var1) {
         Date var2 = DateUtils.addDays(ScheduledRunsView.this.model.getDay(), this.direction);
         ScheduledRunsView.this.model.setDay(var2);
      }

      public final KongaButton forToolbar() {
         return new KongaButton(this).makeToolbarButton();
      }
   }
}
