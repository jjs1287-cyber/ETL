package com.etlforge.integration.client.ui.admin.scheduler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.admin.project.EnableOperationSchedulesCallback;
import com.etlforge.integration.client.admin.project.ProjectAdministration;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.swing.Icon;
import org.etlforge.integration.client.project.info.ProjectInfoCache;
import org.etlforge.integration.client.scheduler.ScheduledOperation;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModel;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModelAction;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.ui.selection.AbstractSelectionAction;

public abstract class SetOperationSchedulesEnabledAction extends AbstractSelectionAction implements ProjectConsoleModelAction {
   private final ProjectAdministration serverApi;
   private final boolean newEnabledState;
   private SetOperationScheduledEnabledActionCallback callback;
   private Set<ScheduledOperation> operationSchedules;
   private ProjectInfoCache cache = ProjectInfoCache.EMPTY;

   public SetOperationSchedulesEnabledAction(ProjectAdministration var1, String var2, Icon var3, boolean var4) {
      super(var2, var3);
      this.serverApi = var1;
      this.newEnabledState = var4;
      this.callback = new SetOperationScheduledEnabledActionCallback() {
         @Override
         public void operationsUpdated(Collection<OperationId> var1, boolean var2x) {
         }

         @Override
         public void setTalkingToServer(boolean var1) {
         }
      };
   }

   public final void setCallback(SetOperationScheduledEnabledActionCallback var1) {
      this.callback = var1;
   }

   @Override
   public final void setProjectCache(ProjectInfoCache var1) {
      this.cache = var1;
   }

   @Override
   public final void setModel(final ProjectConsoleModel var1) {
      this.setCallback(new SetOperationScheduledEnabledActionCallback() {
         @Override
         public void operationsUpdated(Collection<OperationId> var1x, boolean var2) {
            SetOperationSchedulesEnabledAction.this.cache.clear();
            var1.setOperationSchedulesEnabled(var1x, var2);
         }

         @Override
         public void setTalkingToServer(boolean var1x) {
            var1.setTalkingToServer(var1x);
         }
      });
   }

   @Override
   protected final void handleSelectionChange() {
      this.operationSchedules = Sets.newHashSet();

      for (Object var2 : this.getSelection().items()) {
         if (var2 instanceof Schedule) {
            this.disable();
            return;
         }

         if (var2 instanceof ScheduledOperation) {
            this.operationSchedules.add((ScheduledOperation)var2);
         }
      }

      this.setEnabled(!this.operationSchedules.isEmpty());
   }

   private void disable() {
      this.operationSchedules = null;
      this.setEnabled(false);
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      if (this.operationSchedules != null && !this.operationSchedules.isEmpty()) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SetOperationSchedulesEnabledAction.this.submitJob();
            }
         });
      }
   }

   private void submitJob() {
      ArrayList var1 = Lists.newArrayList();

      for (ScheduledOperation var3 : this.operationSchedules) {
         var1.add(var3.getOperation().getID());
      }

      this.callback.setTalkingToServer(true);
      EnableOperationSchedulesJob var4 = new EnableOperationSchedulesJob(this.serverApi, var1, this.newEnabledState, new EnableOperationSchedulesCallback() {
         @Override
         public void done(Collection<OperationId> var1, boolean var2) {
            SetOperationSchedulesEnabledAction.this.callback.operationsUpdated(var1, var2);
            SetOperationSchedulesEnabledAction.this.callback.setTalkingToServer(false);
         }

         @Override
         public void cancelled() {
            SetOperationSchedulesEnabledAction.this.callback.setTalkingToServer(false);
         }

         @Override
         public void caught(Throwable var1) {
            SetOperationSchedulesEnabledAction.this.callback.setTalkingToServer(false);
         }
      });
      var4.submit();
   }
}
