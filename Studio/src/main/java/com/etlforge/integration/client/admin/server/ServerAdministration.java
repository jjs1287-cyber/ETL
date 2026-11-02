package com.etlforge.integration.client.admin.server;

import java.io.File;
import java.util.Date;
import java.util.List;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public interface ServerAdministration extends ServerCall {
   void migrateUsersAndGroups(List<User> var1, List<Group> var2, String var3, MigrateUsersAndGroupsCallback var4);

   void viewServerConfigurationFile(ViewServerConfigurationFileCallback var1);

   void uploadServerConfigurationFile(File var1, UploadServerConfigurationFileCallback var2);

   void listServerLogFiles(GetServerFileListCallback var1);

   void listPluginLogFiles(GetServerFileListCallback var1);

   void listDebugLogFiles(Date var1, Date var2, GetServerFileListCallback var3);

   void listOperationLogFiles(Date var1, Date var2, GetServerFileListCallback var3);

   void downloadServerFile(String var1, DownloadServerFileCallback var2);
}
