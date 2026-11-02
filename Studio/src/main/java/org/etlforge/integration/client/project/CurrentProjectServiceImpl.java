package org.etlforge.integration.client.project;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import org.etlforge.integration.client.project.event.ProjectManagerEvent;
import org.etlforge.integration.client.project.event.ProjectManagerListener;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.Disposable;

public final class CurrentProjectServiceImpl extends CurrentProjectService implements Disposable {
   private final ProjectManager projectManager;
   private final CurrentProjectServiceImpl.EventDispatcher internalListener = new CurrentProjectServiceImpl.EventDispatcher();
   private volatile ManagedProject managedProject;
   private volatile IntegrationProject project;

   public static CurrentProjectServiceImpl create(ProjectManager var0) {
      CurrentProjectServiceImpl var1 = new CurrentProjectServiceImpl(var0);
      var1.activate();
      return var1;
   }

   private CurrentProjectServiceImpl(ProjectManager var1) {
      this.projectManager = Preconditions.checkNotNull(var1, "projectManager");
      this.managedProject = var1.getCurrentProject();
      this.project = this.managedProject == null ? null : this.managedProject.getProject();
   }

   private void activate() {
      this.projectManager.addProjectManagerListener(this.internalListener);
   }

   @Override
   public void dispose() {
      this.projectManager.removeProjectManagerListener(this.internalListener);
   }

   @Nullable
   @Override
   public IntegrationProject getProject() {
      return this.project;
   }

   @Nullable
   @Override
   public ManagedProject getManagedProject() {
      return this.managedProject;
   }

   @Override
   public void closeProject() throws CloseProjectVetoException {
      this.projectManager.closeCurrentProject();
   }

   private class EventDispatcher implements ProjectManagerListener, CloseProjectRequestListener {
      private EventDispatcher() {
      }

      @Override
      public void projectOpened(ProjectManagerEvent var1) {
         CurrentProjectServiceImpl.this.project = var1.getProject();
         CurrentProjectServiceImpl.this.managedProject = var1.getManagedProject();
         CurrentProjectServiceImpl.this.fireProjectOpened(CurrentProjectServiceImpl.this.project, CurrentProjectServiceImpl.this.managedProject);
      }

      @Override
      public void projectClosed(ProjectManagerEvent var1) {
         IntegrationProject var2 = CurrentProjectServiceImpl.this.project;
         ManagedProject var3 = CurrentProjectServiceImpl.this.managedProject;
         CurrentProjectServiceImpl.this.project = null;
         CurrentProjectServiceImpl.this.managedProject = null;
         CurrentProjectServiceImpl.this.fireProjectClosed(var2, var3);
      }

      @Override
      public void closeProjectRequested(IntegrationProject var1) throws CloseProjectVetoException {
         CurrentProjectServiceImpl.this.fireCloseProjectRequested(var1);
      }
   }
}
