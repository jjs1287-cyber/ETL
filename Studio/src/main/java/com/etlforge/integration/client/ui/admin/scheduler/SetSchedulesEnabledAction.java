package com.etlforge.integration.client.ui.admin.scheduler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.admin.project.EnableSchedulesCallback;
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
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionUtils;

public abstract class SetSchedulesEnabledAction extends AbstractSelectionAction implements ProjectConsoleModelAction {
   private final ProjectAdministration serverApi;
   private final boolean newScheduleState;
   private SetScheduledEnabledActionCallback callback;
   private Set<Schedule> schedules;
   private ProjectInfoCache cache = ProjectInfoCache.EMPTY;

   protected SetSchedulesEnabledAction(ProjectAdministration var1, String var2, Icon var3, boolean var4) {
      super(var2, var3);
      this.serverApi = var1;
      this.newScheduleState = var4;
      this.callback = new SetScheduledEnabledActionCallback() {
         @Override
         public void schedulesUpdated(Collection<ScheduleId> var1, boolean var2x) {
         }

         @Override
         public void setTalkingToServer(boolean var1) {
         }
      };
   }

   public final void setCallback(SetScheduledEnabledActionCallback var1) {
      this.callback = var1;
   }

   @Override
   public final void setProjectCache(ProjectInfoCache var1) {
      this.cache = var1;
   }

   @Override
   public final void setModel(final ProjectConsoleModel var1) {
      this.setCallback(new SetScheduledEnabledActionCallback() {
         @Override
         public void schedulesUpdated(Collection<ScheduleId> var1x, boolean var2) {
            SetSchedulesEnabledAction.this.cache.clear();
            var1.setSchedulesEnabled(var1x, var2);
         }

         @Override
         public void setTalkingToServer(boolean var1x) {
            var1.setTalkingToServer(var1x);
         }
      });
   }

   @Override
   protected final void handleSelectionChange() {
      Selection var1 = this.getSelection();
      if (SelectionUtils.contains(var1, ScheduledOperation.class)) {
         this.setEnabled(false);
         this.schedules = null;
      } else {
         this.schedules = Sets.newHashSet(SelectionUtils.getSelectedItemsOfType(var1, Schedule.class));
         this.setEnabled(!this.schedules.isEmpty());
      }
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      if (this.schedules != null && !this.schedules.isEmpty()) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SetSchedulesEnabledAction.this.submitJob();
            }
         });
      }
   }

   private void submitJob() {
      ArrayList var1 = Lists.newArrayList();

      for (Schedule var3 : this.schedules) {
         var1.add(var3.getID());
      }

      this.callback.setTalkingToServer(true);
      EnableSchedulesJob var4 = new EnableSchedulesJob(this.serverApi, var1, this.newScheduleState, new EnableSchedulesCallback() {
         @Override
         public void done(Collection<ScheduleId> var1, boolean var2) {
            SetSchedulesEnabledAction.this.callback.schedulesUpdated(var1, var2);
            SetSchedulesEnabledAction.this.callback.setTalkingToServer(false);
         }

         @Override
         public void cancelled() {
            SetSchedulesEnabledAction.this.callback.setTalkingToServer(false);
         }

         @Override
         public void caught(Throwable var1) {
            SetSchedulesEnabledAction.this.callback.setTalkingToServer(false);
         }
      });
      var4.submit();
   }
}
