package org.etlforge.integration.data.project.event;

import java.util.EventListener;

public interface ProjectListener extends EventListener {
   void entitiesAdded(IntegrationProjectEvent var1);

   void entitiesRevived(IntegrationProjectEvent var1);

   void entitiesDeleted(IntegrationProjectEvent var1);

   void entitiesMoved(IntegrationProjectEvent var1);

   abstract class Adapter implements ProjectListener {
      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
      }

      @Override
      public void entitiesMoved(IntegrationProjectEvent var1) {
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
      }
   }
}
