package org.etlforge.integration.client.ui.project.info;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class ProjectConsoleModel extends AbstractPropertyBasedModel {
   public static final String CURRENT_SERVER = "currentServer";
   public static final String TALKING_TO_SERVER = "talkingToServer";
   public static final String DOWNLOADING_INFO = "downloadingInfo";
   public static final String PROJECTS = "projects";
   public static final String CACHED_INFO = "cachedInfo";
   private final Property<ServerInfo> currentServer = this.newProperty("currentServer");
   private final Property<Boolean> talkingToServer = this.newProperty("talkingToServer", Boolean.FALSE);
   private final Property<Boolean> downloadingInfo = this.newProperty("downloadingInfo", Boolean.FALSE);
   private final ListProperty<IntegrationProject> projects = this.newListProperty("projects");
   private final Property<Boolean> cachedInfo;

   public ProjectConsoleModel() {
      this.projects.set(new ArrayList<>());
      this.cachedInfo = this.newProperty("cachedInfo", Boolean.FALSE);
   }

   public ServerInfo getCurrentServer() {
      return this.currentServer.get();
   }

   public void setCurrentServer(ServerInfo var1) {
      if (!Objects.equal(var1, this.getCurrentServer())) {
         this.currentServer.set(var1);
         this.removeProjects();
      }
   }

   public boolean isTalkingToServer() {
      return this.talkingToServer.get();
   }

   public void setTalkingToServer(boolean var1) {
      this.talkingToServer.set(var1);
   }

   public boolean isDownloadingInfo() {
      return this.downloadingInfo.get();
   }

   public void setDownloadingInfo(boolean var1) {
      this.downloadingInfo.set(var1);
      this.talkingToServer.set(var1);
   }

   public List<IntegrationProject> getProjects() {
      return this.projects.get();
   }

   public void setProjects(List<IntegrationProject> var1) {
      this.projects.set(var1);
   }

   public void removeProjects() {
      this.projects.set((List<IntegrationProject>)null);
   }

   public void setSchedulesEnabled(Collection<ScheduleId> var1, boolean var2) {
      for (IntegrationProject var4 : this.projects.get()) {
         for (ScheduleId var6 : var1) {
            Schedule var7 = var4.getItemLookup().getEntity(var6, Schedule.class);
            if (var7 != null) {
               var7.setEnabled(var2);
            }
         }
      }
   }

   public void setOperationSchedulesEnabled(Collection<OperationId> var1, boolean var2) {
      for (IntegrationProject var4 : this.projects.get()) {
         for (OperationId var6 : var1) {
            Operation var7 = var4.getItemLookup().getEntity(var6, Operation.class);
            if (var7 != null) {
               var7.setScheduleEnabled(var2);
            }
         }
      }
   }

   public void setInfoIsCached(boolean var1) {
      this.cachedInfo.set(var1);
   }

   public boolean isInfoCached() {
      return this.cachedInfo.get();
   }
}
