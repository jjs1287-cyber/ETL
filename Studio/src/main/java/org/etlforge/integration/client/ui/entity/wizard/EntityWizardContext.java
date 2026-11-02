package org.etlforge.integration.client.ui.entity.wizard;

import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;

public interface EntityWizardContext {
   ApplicationWindow getWindow();

   IntegrationProject getProject();

   ProjectPersistor getProjectPersistor();

   EntityExplorerSupport getProjectExplorer();

   FileStore getFileStore();
}
