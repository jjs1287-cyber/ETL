package org.etlforge.integration.client.ui.interchange.core.action.resources;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.jitterpack.resources.JitterpackResources;
import org.etlforge.integration.uidelegate.project.ProjectIcons;

public final class Icons extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.smallIcon("action.ic.Copy"), ApplicationResources.getIcon("Copy.16")},
      {ActionDictionaryKeys.mediumIcon("action.ic.Copy"), ApplicationResources.getIcon("Copy.24")},
      {ActionDictionaryKeys.largeIcon("action.ic.Copy"), ApplicationResources.getIcon("Copy.32")},
      {ActionDictionaryKeys.smallIcon("action.ic.Delete"), CommonIcons.DELETE_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.Delete"), CommonIcons.DELETE_24},
      {ActionDictionaryKeys.largeIcon("action.ic.Delete"), CommonIcons.DELETE_32},
      {ActionDictionaryKeys.smallIcon("action.ic.DeleteRootFolder"), CommonIcons.DELETE_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.DeleteRootFolder"), CommonIcons.DELETE_24},
      {ActionDictionaryKeys.largeIcon("action.ic.DeleteRootFolder"), CommonIcons.DELETE_32},
      {ActionDictionaryKeys.smallIcon("action.ic.DeploySelectedObjects"), ClientIcons.DEPLOY_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.DeploySelectedObjects"), ClientIcons.DEPLOY_24},
      {ActionDictionaryKeys.largeIcon("action.ic.DeploySelectedObjects"), ClientIcons.DEPLOY_32},
      {ActionDictionaryKeys.smallIcon("action.ic.Find"), CommonIcons.FIND_16},
      {ActionDictionaryKeys.smallIcon("action.ic.NewProject"), ProjectIcons.PROJECT_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.NewProject"), ProjectIcons.PROJECT_24},
      {ActionDictionaryKeys.largeIcon("action.ic.NewProject"), ProjectIcons.PROJECT_32},
      {ActionDictionaryKeys.smallIcon("action.ic.Open"), CommonIcons.OPEN_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.Open"), CommonIcons.OPEN_24},
      {ActionDictionaryKeys.largeIcon("action.ic.Open"), CommonIcons.OPEN_32},
      {ActionDictionaryKeys.smallIcon("action.ic.RunAllOperations"), ClientIcons.RUN_OPERATION_16},
      {ActionDictionaryKeys.smallIcon("action.ic.Permissions"), ClientIcons.PERMISSIONS_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.Permissions"), ClientIcons.getIcon("Permissions.24")},
      {ActionDictionaryKeys.smallIcon("action.ic.Validate"), ClientIcons.VALIDATE_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.Validate"), ClientIcons.getIcon("Validation.24")},
      {ActionDictionaryKeys.smallIcon("action.ic.Deploy"), ClientIcons.DEPLOY_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.Deploy"), ClientIcons.DEPLOY_24},
      {ActionDictionaryKeys.largeIcon("action.ic.Deploy"), ClientIcons.DEPLOY_32},
      {ActionDictionaryKeys.smallIcon("action.ic.TrashCan"), ApplicationResources.getIcon("Trash.16")},
      {ActionDictionaryKeys.mediumIcon("action.ic.TrashCan"), ApplicationResources.getIcon("Trash.24")},
      {ActionDictionaryKeys.largeIcon("action.ic.TrashCan"), ApplicationResources.getIcon("Trash.32")},
      {ActionDictionaryKeys.smallIcon("action.ic.ImportJitterpak"), JitterpackResources.IMPORT_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.ImportJitterpak"), JitterpackResources.IMPORT_24},
      {ActionDictionaryKeys.largeIcon("action.ic.ImportJitterpak"), JitterpackResources.IMPORT_32},
      {ActionDictionaryKeys.smallIcon("action.ic.ExportJitterpak"), JitterpackResources.EXPORT_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.ExportJitterpak"), JitterpackResources.EXPORT_24},
      {ActionDictionaryKeys.largeIcon("action.ic.ExportJitterpak"), JitterpackResources.EXPORT_32},
      {ActionDictionaryKeys.smallIcon("action.ic.AssignPlugin"), ClientIcons.PLUGIN_16},
      {ActionDictionaryKeys.smallIcon("action.ic.OpenProjectFolder"), CommonIcons.OPEN_16},
      {ActionDictionaryKeys.smallIcon("action.ic.TestEmail"), ClientIcons.getIcon("TestEmail.16")},
      {ActionDictionaryKeys.mediumIcon("action.ic.TestEmail"), ClientIcons.getIcon("TestEmail.24")},
      {ActionDictionaryKeys.smallIcon("action.ic.DataElements"), ClientIcons.GLOBAL_DATA_ELEMENT_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.DataElements"), ClientIcons.getIcon("DataElement.Global.24")},
      {ActionDictionaryKeys.largeIcon("action.ic.DataElements"), ClientIcons.getIcon("DataElement.Global.32")},
      {ActionDictionaryKeys.smallIcon("action.ic.ViewDependencies"), ClientIcons.DEPENDENT_ITEMS_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.ViewDependencies"), ClientIcons.DEPENDENT_ITEMS_24},
      {ActionDictionaryKeys.largeIcon("action.ic.ViewDependencies"), ClientIcons.DEPENDENT_ITEMS_32},
      {ActionDictionaryKeys.smallIcon("action.ic.TestAllConnections"), ClientIcons.TEST_CONNECTION_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.TestAllConnections"), ClientIcons.TEST_CONNECTION_24},
      {ActionDictionaryKeys.largeIcon("action.ic.TestAllConnections"), ClientIcons.TEST_CONNECTION_32}
   };

   @Override
   protected Object[][] getContents() {
      for (Object[] var4 : contents) {
         if (var4[1] == null) {
            System.out.println(var4[0]);
         }
      }

      return contents;
   }
}
