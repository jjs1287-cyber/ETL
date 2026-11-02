package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.CurrentProjectServiceListener;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.event.UpdatedDependenciesEvent;
import org.etlforge.integration.data.entity.DeleteHandler;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.entity.ProcessFilterProvider;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.UiUtils;

public final class ProcessFilterInstaller implements ProcessFilterProvider {
   private ManagedProject project;
   private ProcessFilter currentFilter;
   private DeleteHandler deleteHandler;
   private final List<Receiver<ProcessFilter>> monitors = new CopyOnWriteArrayList<>();
   private final ProcessFilterInstaller.FilterUpdater projectListener = new ProcessFilterInstaller.FilterUpdater();

   public void setCurrentProjectService(CurrentProjectService var1) {
      var1.addListener(new CurrentProjectServiceListener.Adapter() {
         @Override
         public void projectOpened(CurrentProjectEvent var1) {
            ManagedProject var2 = var1.getManagedProject();
            ProcessFilterInstaller.this.setProject(var2);
         }

         @Override
         public void projectClosed(CurrentProjectEvent var1) {
            ProcessFilterInstaller.this.setProject(null);
         }
      });
      this.setProject(var1.getManagedProject());
   }

   private void setProject(final ManagedProject var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ProcessFilterInstaller.this.removeFilter();
            if (ProcessFilterInstaller.this.project != null) {
               ProcessFilterInstaller.this.project.getEventBus().unregister(ProcessFilterInstaller.this.projectListener);
            }

            ProcessFilterInstaller.this.project = var1;
            if (var1 != null) {
               var1.getEventBus().register(ProcessFilterInstaller.this.projectListener);
            }
         }
      });
   }

   public void installFilter(Operation var1) {
      Preconditions.checkState(this.project != null, "A filter cannot be applied when there is no project open.");
      this.currentFilter = new ProcessFilter(var1);
      this.applyFilterToDataElementStore();
      this.installDeleteHandler(var1);
      this.notifyMonitors();
   }

   private void installDeleteHandler(Operation var1) {
      this.deleteHandler = new DeleteHandler(var1) {
         @Override
         protected void onDelete(IntegrationEntity var1) {
            UiUtils.runOnEventThread(new Runnable() {
               @Override
               public void run() {
                  ProcessFilterInstaller.this.removeFilter();
               }
            });
         }
      };
   }

   private void applyFilterToDataElementStore() {
      GlobalDataElements var1 = this.project.getProject().getGlobalDataElements();
      var1.setProcessFilter(this.currentFilter);
   }

   private void notifyMonitors() {
      for (Receiver var2 : this.monitors) {
         var2.handle(this.currentFilter);
      }
   }

   public void removeFilter() {
      if (this.currentFilter != null) {
         this.currentFilter = null;
         this.disposeDeleteHandler();
         this.applyFilterToDataElementStore();
         this.notifyMonitors();
      }
   }

   private void disposeDeleteHandler() {
      this.deleteHandler.dispose();
      this.deleteHandler = null;
   }

   @Override
   public void addMonitor(Receiver<ProcessFilter> var1) {
      this.monitors.add(var1);
      if (this.currentFilter != null) {
         var1.handle(this.currentFilter);
      }
   }

   @Override
   public void removeMonitor(Receiver<ProcessFilter> var1) {
      this.monitors.remove(var1);
   }

   private final class FilterUpdater {
      private FilterUpdater() {
      }

      @Subscribe
      public void handle(UpdatedDependenciesEvent var1) {
         if (ProcessFilterInstaller.this.currentFilter != null) {
            ProcessFilterInstaller.this.currentFilter.update();
         }
      }
   }
}
