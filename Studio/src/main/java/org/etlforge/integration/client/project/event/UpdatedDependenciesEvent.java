package org.etlforge.integration.client.project.event;

import java.util.EventObject;
import org.etlforge.integration.client.project.ManagedProject;

public final class UpdatedDependenciesEvent extends EventObject {
   public UpdatedDependenciesEvent(ManagedProject var1) {
      super(var1);
   }

   public ManagedProject getSource() {
      return (ManagedProject)super.getSource();
   }
}
