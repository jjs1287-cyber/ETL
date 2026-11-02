package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectLoadingIssuesHandler;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.copy.ProjectCopier;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.util.version.UnsupportedVersionException;

final class NewGuidGeneratingImporter implements Importer {
   private final ProjectManager projectManager;
   private final Importer wrappee;

   public NewGuidGeneratingImporter(ProjectManager var1, Importer var2) {
      this.projectManager = var1;
      this.wrappee = var2;
   }

   @Override
   public JitterPackDescriptor extractDescriptor(File var1, ImportParameters var2) throws JitterPackException, UnsupportedVersionException {
      return this.wrappee.extractDescriptor(var1, var2);
   }

   @Override
   public IntegrationProject unpack(JitterPack var1, JitterPackDescriptor var2, File var3, ImportParameters var4, ProjectLoadingIssuesHandler var5) throws JitterPackException, UnsupportedVersionException, ProjectLoadingCancelledException {
      NewGuidGeneratingImporter.Workflow var6 = new NewGuidGeneratingImporter.Workflow(var1, var2, var4, var3, var5);
      return var6.unpack();
   }

   private final class Workflow {
      private final JitterPack jitterpack;
      private final JitterPackDescriptor descriptor;
      private final ImportParameters params;
      private final File destinationFolder;
      private final ProjectLoadingIssuesHandler issuesHandler;
      private final File unpackFolder;
      private File locationOfUnpackedProject;
      private IntegrationProject unpackedProject;

      public Workflow(JitterPack var2, JitterPackDescriptor var3, ImportParameters var4, File var5, ProjectLoadingIssuesHandler var6) {
         this.jitterpack = var2;
         this.descriptor = var3;
         this.params = var4;
         this.destinationFolder = var5;
         this.issuesHandler = var6;
         this.unpackFolder = ApplicationTempFolder.newLikelyUniqueFolder("P", true);
         this.locationOfUnpackedProject = new File(this.unpackFolder, var3.getFolderName());
      }

      public IntegrationProject unpack() throws JitterPackException, UnsupportedVersionException, ProjectLoadingCancelledException {
         IntegrationProjectLock.acquire();

         try {
            return this.unpackImpl();
         } finally {
            IntegrationProjectLock.release();
            this.deleteFolder(this.unpackFolder);
         }
      }

      private IntegrationProject unpackImpl() throws JitterPackException, UnsupportedVersionException, ProjectLoadingCancelledException {
         this.unpackedProject = this.unpackJitterPack();
         if (this.unpackedProject != null) {
            String var1 = this.processProjectName();
            return this.createNewGUIDs(var1);
         } else {
            return this.unpackedProject;
         }
      }

      private IntegrationProject unpackJitterPack() throws JitterPackException, UnsupportedVersionException, ProjectLoadingCancelledException {
         return NewGuidGeneratingImporter.this.wrappee.unpack(this.jitterpack, this.descriptor, this.unpackFolder, this.params, this.issuesHandler);
      }

      private String processProjectName() throws JitterPackException {
         return this.isRenamingNecessary() ? this.renameProject(this.unpackFolder) : this.descriptor.getProjectName();
      }

      private boolean isRenamingNecessary() {
         String var1 = this.params.getNewProjectName();
         return var1 != null && !var1.equalsIgnoreCase(this.descriptor.getProjectName());
      }

      private String renameProject(File var1) throws JitterPackException {
         try {
            String var2 = this.params.getNewProjectName();
            assert var2 != null;
            File var3 = new File(var1, var2);
            ManagedProject var4 = this.unpackedProject.getExtensionObject(ManagedProject.class);
            ProjectFile var5 = var4.getProjectFile();
            var5.setNewName(var2);
            FileUtils.copyDirectory(var5.getParentFolder(), var3);
            this.locationOfUnpackedProject = var3;
            return var2;
         } catch (IOException var6) {
            throw new JitterPackException("Could not rename the unpacked project: " + var6.getMessage(), var6);
         }
      }

      private IntegrationProject createNewGUIDs(String var1) throws JitterPackException {
         try {
            this.closeUnpackedProject();
            this.copyProject(var1);
            return NewGuidGeneratingImporter.this.projectManager
               .loadProject(var1, new File(this.destinationFolder, this.locationOfUnpackedProject.getName()), ProjectLoadingIssuesHandler.IGNORE);
         } catch (Exception var3) {
            throw new JitterPackException("An error occurred when generating new GUIDs for the imported project.", var3);
         }
      }

      private void copyProject(String var1) throws Exception {
         ProjectCopier var2 = new ProjectCopier(NewGuidGeneratingImporter.this.projectManager);
         ProjectLocation var3 = new ProjectLocation(var1, this.locationOfUnpackedProject);
         ProjectLocation var4 = new ProjectLocation(var1, new File(this.destinationFolder, this.locationOfUnpackedProject.getName()));
         var2.copy(var3, var4);
      }

      private void closeUnpackedProject() {
         try {
            ManagedProject var1 = this.unpackedProject.getExtensionObject(ManagedProject.class);
            NewGuidGeneratingImporter.this.projectManager.closeProject(var1);
         } catch (CloseProjectVetoException var2) {
            throw new RuntimeException("This should not have happened. This is a bug.", var2);
         }
      }

      private void deleteFolder(File var1) {
         try {
            FileUtils.deleteDirectory(var1);
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }
   }
}
