package org.etlforge.integration.client.project;

import java.util.logging.Logger;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.integration.client.project.event.ProjectManagerEvent;
import org.etlforge.integration.client.project.event.ProjectManagerListener;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.io.KongaIoUtils;

final class ProjectEventLoggerImpl implements ProjectEventLogger, ProjectManagerListener, ProjectListener {
   private static final String LOGGER_NAME = "ProjectEventLog";
   private final ProjectManager manager;
   private boolean isOn;
   private Logger logger;

   public ProjectEventLoggerImpl(ProjectManager var1) {
      this.manager = var1;
      this.checkSystemProperty();
   }

   private void checkSystemProperty() {
      if (Boolean.getBoolean("LogProjectEvents")) {
         this.switchOn();
      }
   }

   @Override
   public void switchOn() {
      if (!this.isOn) {
         if (this.logger == null) {
            this.createLogger();
         }

         this.isOn = true;
         this.manager.addProjectManagerListener(this);
         ManagedProject var1 = this.manager.getCurrentProject();
         if (var1 != null) {
            var1.getProject().addProjectListener(this);
         }
      }
   }

   private void createLogger() {
      this.logger = ApplicationLog.getLogger("ProjectEventLog", true);
   }

   @Override
   public void switchOff() {
      if (this.isOn) {
         this.isOn = false;
         this.manager.removeProjectManagerListener(this);
         ManagedProject var1 = this.manager.getCurrentProject();
         if (var1 != null) {
            var1.getProject().removeProjectListener(this);
         }
      }
   }

   @Override
   public void entitiesAdded(IntegrationProjectEvent var1) {
      this.log(new ProjectEventLoggerImpl.EventDescription("CREATED", var1));
   }

   @Override
   public void entitiesRevived(IntegrationProjectEvent var1) {
      this.log(new ProjectEventLoggerImpl.EventDescription("REVIVED", var1));
   }

   @Override
   public void entitiesDeleted(IntegrationProjectEvent var1) {
      this.log(new ProjectEventLoggerImpl.EventDescription("DELETED", var1));
   }

   @Override
   public void entitiesMoved(IntegrationProjectEvent var1) {
      this.log(new ProjectEventLoggerImpl.EventDescription("MOVED", var1));
   }

   @Override
   public void projectClosed(ProjectManagerEvent var1) {
      var1.getProject().removeProjectListener(this);
   }

   @Override
   public void projectOpened(ProjectManagerEvent var1) {
      var1.getProject().addProjectListener(this);
   }

   private void log(ProjectEventLoggerImpl.EventDescription var1) {
      if (this.logger != null) {
         this.logger.info(var1.toString() + KongaIoUtils.LINE_BREAK);
      }
   }

   private static class EventDescription {
      private final String caption;
      private final IntegrationProjectEvent event;

      public EventDescription(String var1, IntegrationProjectEvent var2) {
         this.caption = var1;
         this.event = var2;
      }

      @Override
      public String toString() {
         StringBuilder var1 = new StringBuilder(this.caption);
         var1.append(KongaIoUtils.LINE_BREAK);

         for (IntegrationEntity var3 : this.event.getEntities()) {
            var1.append(toString(var3, "    ", KongaIoUtils.LINE_BREAK));
         }

         return var1.toString();
      }

      private static String toString(IntegrationEntity var0, String var1, String var2) {
         StringBuilder var3 = new StringBuilder();
         if (var1 != null) {
            var3.append(var1);
         }

         if (var0 == null) {
            var3.append("[null]");
         } else {
            var3.append("Name=").append(var0.getName());
            var3.append(" ID=").append(var0.getID());
            IntegrationEntity var4 = var0.getParent();
            if (var4 != null && !(var4 instanceof RootFolder)) {
               var3.append(" Parent={").append(toString(var0.getParent(), null, null)).append("}");
            }
         }

         if (var2 != null) {
            var3.append(var2);
         }

         return var3.toString();
      }
   }
}
