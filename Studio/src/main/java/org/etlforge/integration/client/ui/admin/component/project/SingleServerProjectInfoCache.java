package org.etlforge.integration.client.ui.admin.component.project;

import java.util.List;
import org.etlforge.integration.client.project.info.ProjectInfoCache;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.client.server.login.event.LoginListener;
import org.etlforge.integration.client.server.login.event.LoginRequestEvent;
import org.etlforge.integration.client.server.login.event.LogoutRequestEvent;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.Disposable;

public final class SingleServerProjectInfoCache implements ProjectInfoCache, Disposable {
   private final ProjectInfoCache cacheImpl;
   private final LoginService loginService;
   private SingleServerProjectInfoCache.ConnectionListener connectionListener;

   public SingleServerProjectInfoCache(ProjectInfoCache var1, LoginService var2) {
      this.cacheImpl = var1;
      this.loginService = var2;
      this.connectionListener = new SingleServerProjectInfoCache.ConnectionListener();
      var2.addLoginListener(this.connectionListener);
      this.clear();
   }

   @Override
   public List<IntegrationProject> getProjects() {
      return this.cacheImpl.getProjects();
   }

   @Override
   public void storeProjects(List<IntegrationProject> var1) {
      this.cacheImpl.storeProjects(var1);
   }

   @Override
   public void clear() {
      this.cacheImpl.clear();
   }

   @Override
   public void dispose() {
      this.loginService.removeLoginListener(this.connectionListener);
   }

   private class ConnectionListener extends LoginListener.Adapter {
      private ConnectionListener() {
      }

      @Override
      public void loginRequested(LoginRequestEvent var1) {
         SingleServerProjectInfoCache.this.cacheImpl.clear();
      }

      @Override
      public void logoutRequested(LogoutRequestEvent var1) {
         SingleServerProjectInfoCache.this.cacheImpl.clear();
      }
   }
}
