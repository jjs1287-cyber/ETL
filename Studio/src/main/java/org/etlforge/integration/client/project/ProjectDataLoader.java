package org.etlforge.integration.client.project;

import java.util.List;
import java.util.Set;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.system.callback.Receiver;

public interface ProjectDataLoader {
   Set<ProjectLoadingIssue> loadProject(ProjectLoadingTracker var1) throws InterchangeLoadingException;

   void loadDeletedItems(Receiver<EntityDescriptor> var1) throws InterchangeLoadingException;

   void loadUiData();

   ProjectPipelinePluginStore loadPluginInfo() throws InterchangeLoadingException;

   List<EntityDescriptor> getEntitiesToDisplayOnStartUp();

   EntityDescriptor getActiveEntityOnStartUp();

   List<EntityDescriptor> getExpandedOnStartUp();
}
