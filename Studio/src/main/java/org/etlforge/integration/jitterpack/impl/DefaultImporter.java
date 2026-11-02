package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import org.etlforge.integration.client.project.InterchangeLoadingException;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectLoadingIssuesHandler;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.util.version.UnsupportedVersionException;

final class DefaultImporter implements Importer {
   private final ProjectManager projectManager;

   public DefaultImporter(ProjectManager var1) {
      this.projectManager = var1;
   }

   @Override
   public JitterPackDescriptor extractDescriptor(File var1, ImportParameters var2) throws JitterPackException, UnsupportedVersionException {
      return JitterPackUtils.extractDescriptor(var1, var2);
   }

   @Override
   public IntegrationProject unpack(JitterPack var1, JitterPackDescriptor var2, File var3, ImportParameters var4, ProjectLoadingIssuesHandler var5) throws JitterPackException, UnsupportedVersionException, ProjectLoadingCancelledException {
      this.logUnpackRequest(var1, var3);
      if (var2.isEncrypted() && var4.getPassword() == null) {
         throw new JitterPackException("This Jitterpak is encrypted. Please provide the correct password.");
      }

      JitterPackFormat var6 = JitterPackUtils.getPackFormat(var2, var4);
      ProjectDescriptor var7 = var6.unpack(var1.getFile(), var3);
      return this.loadProject(var7, var5, var2.isPackedProjectReadOnly());
   }

   private IntegrationProject loadProject(ProjectDescriptor var1, ProjectLoadingIssuesHandler var2, boolean var3) throws ProjectLoadingCancelledException, UnsupportedVersionException, JitterPackException {
      this.logProjectLoadingRequest(var1);
      IntegrationProjectLock.acquire();

      try {
         IntegrationProject var4 = this.projectManager.unpackProject(var1.projectName(), var1.projectFolder(), var2, var3);
         PostUnpackProcessor.processUnpack(var4, var3);
         var4.setJitterPackComment(var1.comment());
         this.projectManager.saveProject(var4);
         return var4;
      } catch (InterchangeLoadingException var10) {
         this.handleProjectLoadingException(var10);
      } catch (InterchangeSavingException var11) {
         this.handleProjectLoadingException(var11);
      } finally {
         IntegrationProjectLock.release();
      }

      return null;
   }

   private void handleProjectLoadingException(Exception var1) throws JitterPackException {
      JitterPackException var2 = new JitterPackException("Failed to load the unpacked project.", var1);
      this.logLoadProjectException(var2);
      throw var2;
   }

   private void logLoadProjectException(Exception var1) {
      JitterPackLog.getLogger().throwing(this.getClass().getName(), "loadProject", var1);
   }

   private void logUnpackRequest(JitterPack var1, File var2) {
      JitterPackLog.getLogger().info("Asked to unpack the Jitterpak " + var1.getFile().getAbsolutePath() + " to the folder " + var2.getAbsolutePath());
   }

   private void logProjectLoadingRequest(ProjectDescriptor var1) {
      JitterPackLog.getLogger().info("About to load the unpacked project: " + var1);
   }
}
