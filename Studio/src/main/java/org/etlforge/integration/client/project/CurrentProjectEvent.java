package org.etlforge.integration.client.project;

import java.util.EventObject;
import org.etlforge.integration.data.project.IntegrationProject;

public final class CurrentProjectEvent extends EventObject {
   private final IntegrationProject project;
   private final ManagedProject managedProject;

   public CurrentProjectEvent(CurrentProjectService var1, IntegrationProject var2, ManagedProject var3) {
      super(var1);
      this.project = var2;
      this.managedProject = var3;
   }

   public CurrentProjectService getSource() {
      return (CurrentProjectService)super.getSource();
   }

   public IntegrationProject getProject() {
      return this.project;
   }

   public ManagedProject getManagedProject() {
      return this.managedProject;
   }
}
