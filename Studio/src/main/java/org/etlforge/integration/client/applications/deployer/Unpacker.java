package org.etlforge.integration.client.applications.deployer;

import java.io.File;
import java.util.Collection;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectLoadingIssue;
import org.etlforge.integration.client.project.ProjectLoadingIssuesHandler;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.version.UnsupportedVersionException;

final class Unpacker {
   private final ProjectManager projectManager;
   private final Importer importer;
   private final File destination;
   private final ConsoleWriter consoleWriter;

   public Unpacker(ProjectManager var1, Importer var2, File var3, ConsoleWriter var4) {
      this.projectManager = var1;
      this.importer = var2;
      this.destination = var3;
      this.consoleWriter = var4;
   }

   public IntegrationProject unpack(JitterPack var1) {
      try {
         this.consoleWriter.log("Unpacking the Jitterpak " + var1.getFile());
         JitterPackDescriptor var2 = var1.getDescriptor();
         ImportParameters var3 = ImportParameters.DEFAULT;
         IntegrationProject var4 = this.importer.unpack(var1, var2, this.destination, var3, new Unpacker.IssuesLogger(this.consoleWriter));
         this.projectManager.setCurrentProject(var4);
         return var4;
      } catch (ProjectLoadingCancelledException var5) {
         this.consoleWriter.log(var5);
      } catch (JitterPackException var6) {
         this.consoleWriter.log(var6);
      } catch (UnsupportedVersionException var7) {
         this.consoleWriter.log(var7);
      }

      return null;
   }

   private static class IssuesLogger implements ProjectLoadingIssuesHandler {
      private final ConsoleWriter consoleWriter;

      IssuesLogger(ConsoleWriter var1) {
         this.consoleWriter = var1;
      }

      @Override
      public void handle(IntegrationProject var1, Collection<ProjectLoadingIssue> var2) {
         for (ProjectLoadingIssue var4 : var2) {
            this.consoleWriter.log(var4.getMessage());
         }
      }
   }
}
