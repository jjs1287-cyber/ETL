package org.etlforge.integration.client.project;

import org.etlforge.integration.data.entity.EntityType;

public interface ProjectLoadingTracker {
   ProjectLoadingTracker SILENT = new ProjectLoadingTrackerAdapter() {};

   void beginLoadingProject();

   void doneLoadingProject();

   void beginLoadingEntityType(EntityType var1);

   void doneLoadingEntityType(EntityType var1);

   void beginLoadingPluginInfo();

   void doneLoadingPluginInfo();

   void beginLoadingUiState();

   void doneLoadingUiState();
}
