package org.etlforge.integration.client.ui.project.permissions;

import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface PermissionsView extends WindowSectionContent {
   String CONTENT_ID = "permissions_view";

   void setPermissions(PermissionsManager var1);

   boolean setAccessedEntity(IntegrationEntity var1);
}
