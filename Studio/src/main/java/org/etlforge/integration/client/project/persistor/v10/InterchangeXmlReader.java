package org.etlforge.integration.client.project.persistor.v10;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.InterchangeLoadingException;
import org.etlforge.integration.client.project.ProjectDataLoader;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectLoadingIssue;
import org.etlforge.integration.client.project.ProjectLoadingTracker;
import org.etlforge.integration.client.project.ProjectPipelinePluginStore;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.event.ApplicationMessageEvent;

public class InterchangeXmlReader implements ProjectDataLoader {
   private final ProjectFile projectFile;
   private final File rootDirectory;
   private final List<EntityDescriptor> displayOnStartUp = Lists.newArrayList();
   private final List<EntityDescriptor> expandedOnStartup = Lists.newArrayList();
   private EntityDescriptor activeOnStartup;
   private final Logger logger;

   public InterchangeXmlReader(ProjectFile var1, Logger var2) {
      this.logger = Preconditions.checkNotNull(var2, "logger");
      this.projectFile = var1;
      this.rootDirectory = var1.getParentFolder();
   }

   @Override
   public Set<ProjectLoadingIssue> loadProject(ProjectLoadingTracker var1) throws InterchangeLoadingException {
      throw new UnsupportedOperationException("Pre-2.0 projects are no longer supported.");
   }

   @Override
   public void loadDeletedItems(Receiver<EntityDescriptor> var1) {
      Preconditions.checkNotNull(var1, "receiver");
      String var2 = this.projectFile.getProperty("deleted-file");
      File var3 = new File(this.rootDirectory, var2);
      this.logger.info("Now loading deleted items from the file " + var3.getAbsolutePath());
      if (!var3.exists()) {
         this.logger.info("No deleted items information availeble, file does not exist: " + var3.getAbsolutePath());
      } else {
         DeletedItemsSaxParser var4 = new DeletedItemsSaxParser(var1);
         var4.setLogger(this.logger);

         try {
            var4.loadDeletedItems(var3);
         } catch (Exception var7) {
            this.logger.throwing("InterchangeXmlReader", "loadDeletedItems", var7);
            String var6 = "Failed to load the deleted items from XML: " + var7.getMessage();
            ApplicationMessageEvent.postErrorMessage(this, var6);
            ErrorLog.log(this.getClass(), var6, var7);
         }
      }
   }

   @Override
   public void loadUiData() {
      String var1 = this.projectFile.getProperty("uistate-file");
      File var2 = new File(this.rootDirectory, var1);
      this.logger.info("Now loading UI state from the file " + var2.getAbsolutePath());
      if (!var2.exists()) {
         this.logger.info("No UI state information availeble, file does not exist: " + var2.getAbsolutePath());
      } else {
         ProjectUiSaxParser var3 = new ProjectUiSaxParser();

         try {
            var3.loadUiData(var2);
         } catch (Exception var6) {
            this.logger.throwing("InterchangeXmlReader", "loadUiData", var6);
            String var5 = "Failed to load the UI state from XML: " + var6.getMessage();
            ApplicationMessageEvent.postErrorMessage(this, var5);
            ErrorLog.log(this.getClass(), var5, var6);
         }

         this.displayOnStartUp.addAll(var3.getOpenPages());
         this.expandedOnStartup.addAll(var3.getExpandedItems());
         this.activeOnStartup = var3.getActiveOnStartup();
      }
   }

   IntegrationEntity loadEntity(File var1) throws XmlLoadingException, FileNotFoundException {
      throw new UnsupportedOperationException("Loading of pre-2.0 projects is no longer supported.");
   }

   @Override
   public List<EntityDescriptor> getEntitiesToDisplayOnStartUp() {
      return this.displayOnStartUp;
   }

   @Override
   public EntityDescriptor getActiveEntityOnStartUp() {
      return this.activeOnStartup;
   }

   @Override
   public List<EntityDescriptor> getExpandedOnStartUp() {
      return this.expandedOnStartup;
   }

   @Override
   public ProjectPipelinePluginStore loadPluginInfo() throws InterchangeLoadingException {
      try {
         return PipelinePluginInfoReader.read(this.projectFile);
      } catch (IOException var2) {
         throw new InterchangeLoadingException(var2);
      }
   }

   void log(Level var1, String var2) {
      this.logger.log(var1, var2);
   }
}
