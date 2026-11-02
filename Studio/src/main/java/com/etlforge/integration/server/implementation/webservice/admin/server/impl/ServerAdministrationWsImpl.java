package com.etlforge.integration.server.implementation.webservice.admin.server.impl;

import com.etlforge.integration.client.admin.server.DownloadServerFileCallback;
import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.MigrateUsersAndGroupsCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.admin.server.UploadServerConfigurationFileCallback;
import com.etlforge.integration.client.admin.server.ViewServerConfigurationFileCallback;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public class ServerAdministrationWsImpl implements ServerAdministration {
   private final CppWebServiceConfiguration cppConfig;
   private final JavaWebServiceConfiguration javaConfig;

   public ServerAdministrationWsImpl(CppWebServiceConfiguration var1, JavaWebServiceConfiguration var2) {
      this.cppConfig = var1;
      this.javaConfig = var2;
   }

   @Override
   public void migrateUsersAndGroups(List<User> var1, List<Group> var2, String var3, MigrateUsersAndGroupsCallback var4) {
      ServerAdminCppWsImpl var5 = new ServerAdminCppWsImpl(this.cppConfig);
      var5.migrateUsersAndGroups(var1, var2, var3, var4);
   }

   @Override
   public void viewServerConfigurationFile(ViewServerConfigurationFileCallback var1) {
      ServerFilesWsImpl var2 = fileAdministration();
      var2.viewServerConfigurationFile(var1);
   }

   @Override
   public void uploadServerConfigurationFile(File var1, UploadServerConfigurationFileCallback var2) {
      ServerFilesWsImpl var3 = fileAdministration();
      var3.uploadServerConfigurationFile(var1, var2);
   }

   @Override
   public void downloadServerFile(String var1, DownloadServerFileCallback var2) {
      ServerFilesWsImpl var3 = fileAdministration();
      var3.downloadServerFile(var1, var2);
   }

   @Override
   public void listOperationLogFiles(Date var1, Date var2, GetServerFileListCallback var3) {
      ServerFilesWsImpl var4 = fileAdministration();
      var4.listOperationLogFiles(var1, var2, var3);
   }

   @Override
   public void listDebugLogFiles(Date var1, Date var2, GetServerFileListCallback var3) {
      ServerFilesWsImpl var4 = fileAdministration();
      var4.listDebugLogFiles(var1, var2, var3);
   }

   @Override
   public void listPluginLogFiles(GetServerFileListCallback var1) {
      ServerFilesWsImpl var2 = fileAdministration();
      var2.listPluginLogFiles(var1);
   }

   @Override
   public void listServerLogFiles(GetServerFileListCallback var1) {
      ServerFilesWsImpl var2 = fileAdministration();
      var2.listServerLogFiles(var1);
   }
   private ServerFilesWsImpl fileAdministration() {
      String backend = System.getProperty("etlforge.serverAdmin.backend", "native");
      if ("native".equalsIgnoreCase(backend)) return new ServerFilesWsImpl(this.cppConfig);
      if ("java".equalsIgnoreCase(backend)) return new ServerFilesWsImpl(this.javaConfig);
      throw new IllegalArgumentException("etlforge.serverAdmin.backend must be native or java");
   }}
