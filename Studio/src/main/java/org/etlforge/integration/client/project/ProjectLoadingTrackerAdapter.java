package org.etlforge.integration.client.project;

import org.etlforge.integration.data.entity.EntityType;

public abstract class ProjectLoadingTrackerAdapter implements ProjectLoadingTracker {
   @Override
   public void beginLoadingProject() {
   }

   @Override
   public void doneLoadingProject() {
   }

   @Override
   public void beginLoadingEntityType(EntityType var1) {
   }

   @Override
   public void beginLoadingPluginInfo() {
   }

   @Override
   public void beginLoadingUiState() {
   }

   @Override
   public void doneLoadingEntityType(EntityType var1) {
   }

   @Override
   public void doneLoadingPluginInfo() {
   }

   @Override
   public void doneLoadingUiState() {
   }
}
