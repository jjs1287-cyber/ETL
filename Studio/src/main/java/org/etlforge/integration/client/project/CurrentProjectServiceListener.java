package org.etlforge.integration.client.project;

import org.etlforge.integration.data.project.IntegrationProject;

public interface CurrentProjectServiceListener extends CloseProjectRequestListener {
   void projectOpened(CurrentProjectEvent var1);

   void projectClosed(CurrentProjectEvent var1);

   @Override
   void closeProjectRequested(IntegrationProject var1) throws CloseProjectVetoException;

   abstract class Adapter implements CurrentProjectServiceListener {
      @Override
      public void projectOpened(CurrentProjectEvent var1) {
      }

      @Override
      public void projectClosed(CurrentProjectEvent var1) {
      }

      @Override
      public void closeProjectRequested(IntegrationProject var1) throws CloseProjectVetoException {
      }
   }
}
