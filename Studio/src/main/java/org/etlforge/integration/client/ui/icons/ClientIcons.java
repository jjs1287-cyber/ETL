package org.etlforge.integration.client.ui.icons;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.IconProvider;

public final class ClientIcons {
   public static final IconProvider ICONS = load();
   public static final ImageIcon APPLICATION_16 = (ImageIcon)getIcon(getApplicationIconKey(16));
   public static final ImageIcon APPLICATION_32 = (ImageIcon)getIcon(getApplicationIconKey(32));
   public static final Icon ADD_GROUP_16 = getIcon("Group.Add.16");
   public static final Icon ADD_GROUP_24 = getIcon("Group.Add.24");
   public static final Icon ADD_GROUP_32 = getIcon("Group.Add.32");
   public static final Icon ADD_USER_16 = getIcon("User.Add.16");
   public static final Icon ADD_USER_24 = getIcon("User.Add.24");
   public static final Icon ADD_USER_32 = getIcon("User.Add.32");
   public static final Icon ADMINISTRATION_16 = getIcon("Administration.16");
   public static final Icon ADMINISTRATION_24 = getIcon("Administration.24");
   public static final Icon AUTO_MAPPER_16 = getIcon("AutoMapper.16");
   public static final Icon AUTO_MAPPER_24 = getIcon("AutoMapper.24");
   public static final Icon AUTO_MAPPER_32 = getIcon("AutoMapper.32");
   public static final Icon BACKUP_16 = getIcon("Backup.16");
   public static final Icon BACKUP_24 = getIcon("Backup.24");
   public static final Icon BACKUP_32 = getIcon("Backup.32");
   public static final Icon BUSINESS_ANALYST_16 = getIcon("BusinessAnalyst.16");
   public static final Icon CONNECTOR_16 = getIcon("Connector.16");
   public static final Icon CONNECTOR_24 = getIcon("Connector.24");
   public static final Icon CONNECTOR_32 = getIcon("Connector.32");
   public static final Icon CONNECTOR_64 = getIcon("Connector.64");
   public static final Icon DATA_LOADER_LOGO = getIcon("SFDC.DataLoader.Logo");
   public static final Icon DELETE_GROUP_16 = getIcon("Group.Delete.16");
   public static final Icon DELETE_GROUP_24 = getIcon("Group.Delete.24");
   public static final Icon DELETE_GROUP_32 = getIcon("Group.Delete.32");
   public static final Icon DELETE_USER_16 = getIcon("User.Delete.16");
   public static final Icon DELETE_USER_24 = getIcon("User.Delete.24");
   public static final Icon DELETE_USER_32 = getIcon("User.Delete.32");
   public static final Icon DEPENDENT_ITEMS_16 = getIcon("DependentItems.16");
   public static final Icon DEPENDENT_ITEMS_24 = getIcon("DependentItems.24");
   public static final Icon DEPENDENT_ITEMS_32 = getIcon("DependentItems.32");
   public static final Icon DEPLOY_16 = getIcon("Deploy.16");
   public static final Icon DEPLOY_24 = getIcon("Deploy.24");
   public static final Icon DEPLOY_32 = getIcon("Deploy.32");
   public static final Icon DEVELOPER_16 = getIcon("Developer.16");
   public static final Icon FORMULA_16 = getIcon("Function.16");
   public static final Icon FORMULA_24 = getIcon("Function.24");
   public static final Icon GLOBAL_DATA_ELEMENT_16 = getIcon("DataElement.Global.16");
   public static final Icon GROUP_16 = getIcon("Group.16");
   public static final Icon GROUP_24 = getIcon("Group.24");
   public static final Icon GROUP_32 = getIcon("Group.32");
   public static final Icon INTEGRATION_16 = getIcon("Integration.16");
   public static final Icon INTEGRATION_24 = getIcon("Integration.24");
   public static final Icon ETLFORGE_CONNECT_16 = getIcon("ETLForgeConnect.16");
   public static final Icon ETLFORGE_CONNECT_24 = getIcon("ETLForgeConnect.24");
   public static final Icon ETLFORGE_CONNECT_32 = getIcon("ETLForgeConnect.32");
   public static final Icon JITTERPAK_16 = getIcon("Jitterpak.16");
   public static final Icon JITTERPAK_32 = getIcon("Jitterpak.32");
   public static final Icon LICENSE_16 = getIcon("License.16");
   public static final Icon LOAD_SOURCE_16 = getIcon("LoadSource.16");
   public static final Icon LOAD_SOURCE_24 = getIcon("LoadSource.24");
   public static final Icon MAP_16 = getIcon("Map.16");
   public static final Icon MAP_24 = getIcon("Map.24");
   public static final Icon PLUGIN_16 = getIcon("Plugin.16");
   public static final Icon PROXY_16 = getIcon("Proxy.16");
   public static final Icon PROXY_24 = getIcon("Proxy.24");
   public static final Icon PROXY_32 = getIcon("Proxy.32");
   public static final Icon TEST_CONNECTION_16 = getIcon("TestConnection.16");
   public static final Icon TEST_CONNECTION_24 = getIcon("TestConnection.24");
   public static final Icon TEST_CONNECTION_32 = getIcon("TestConnection.32");
   public static final Icon TEST_OPERATION_16 = getIcon("TestOperation.16");
   public static final Icon TEST_OPERATION_24 = getIcon("TestOperation.24");
   public static final Icon TEST_TRANSFORMATION_16 = getIcon("TestTransformation.16");
   public static final Icon TEST_TRANSFORMATION_24 = getIcon("TestTransformation.24");
   public static final Icon TEST_WEBSERVICE_16 = getIcon("TestWebService.16");
   public static final Icon TEST_WEBSERVICE_24 = getIcon("TestWebService.24");
   public static final Icon PERMISSIONS_16 = getIcon("Permissions.16");
   public static final Icon RUN_OPERATION_16 = getIcon("RunOperation.16");
   public static final Icon RUN_OPERATION_24 = getIcon("RunOperation.24");
   public static final Icon SERVER_16 = getIcon("Server.16");
   public static final Icon SERVER_HTTPS_16 = getIcon("Server.Https.16");
   public static final Icon SOURCE_DATA_ELEMENT_16 = getIcon("DataElement.Source.16");
   public static final Icon USER_16 = getIcon("User.16");
   public static final Icon USER_PROFILE_16 = getIcon("User.Profile.16");
   public static final Icon USER_PROFILE_24 = getIcon("User.Profile.24");
   public static final Icon USER_PROFILE_32 = getIcon("User.Profile.32");
   public static final Icon VALIDATE_16 = getIcon("Validation.16");
   public static final Icon VALIDATE_24 = getIcon("Validation.24");
   public static final Icon WAND_16 = getIcon("Wand.16");
   public static final Icon ZOOM_16 = getIcon("Zoom.16");

   private static IconProvider load() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadIconsFromPropertiesFile("org/etlforge/integration/client/ui/icons/Icons.properties", "org.etlforge.integration.client.ui.icons");
   }

   public static Icon getIcon(String var0) {
      return ICONS.getIcon(var0);
   }

   private static String getApplicationIconKey(int var0) {
      String var1 = ApplicationConfiguration.getString("Application.Icon.Key", "Application");
      return var1 + "." + var0;
   }

   private ClientIcons() {
   }
}
