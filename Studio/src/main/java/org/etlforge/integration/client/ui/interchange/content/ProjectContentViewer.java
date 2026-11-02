package org.etlforge.integration.client.ui.interchange.content;

import java.util.List;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.client.entity.search.ProjectItemOrder;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.util.KeyUtils;

public interface ProjectContentViewer extends WindowSectionContent {
   KeyStroke ACTIVATOR_KEY = KeyUtils.getCtrlShiftDown(82);

   void newProjectOpened(IntegrationProject var1);

   void projectClosed();

   WindowSectionContent getWindowContent();

   void selectRoot();

   void selectEntity(IntegrationEntity var1);

   List<IntegrationEntity> getSelectedEntities();

   void renameEntity(IntegrationEntity var1);

   List<IntegrationEntity> getExpandedEntities();

   void setExpandedEntities(List<IntegrationEntity> var1);

   void refresh(Folder var1);

   void install();

   void uninstall();

   void expand(RootFolder var1);

   void expandAll(RootFolder var1);

   void collapseAll(RootFolder var1);

   <T extends IntegrationEntity> ProjectItemOrder<T> getItemOrder(EntityType var1, Class<T> var2);

   void giveFocusToFilterField();

   void setProcessFilter(ProcessFilter var1);
}
