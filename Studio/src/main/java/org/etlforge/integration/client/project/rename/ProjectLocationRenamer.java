package org.etlforge.integration.client.project.rename;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.client.project.ProjectFileLock;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

public class ProjectLocationRenamer {
   private final ProjectLocation originalLocation;
   private ProjectNameConflictResolver conflictResolver;

   public ProjectLocationRenamer(ProjectLocation var1) {
      Preconditions.checkNotNull(var1, "originalLocation");
      this.originalLocation = var1;
      this.conflictResolver = new ProjectNameConflictResolver() {
         @Override
         public ProjectNameConflictResolver.Resolution resolve(ProjectLocation var1) {
            return ProjectNameConflictResolver.Resolution.CANCEL;
         }
      };
   }

   public void setConflictResolver(ProjectNameConflictResolver var1) {
      Preconditions.checkNotNull(var1, "conflictResolver");
      this.conflictResolver = var1;
   }

   public ProjectLocation rename(String var1) throws IllegalNameException, IOException {
      this.checkOriginalProjectLock();
      this.validateName(var1);
      ProjectLocation var2 = this.createNewLocation(var1);
      if (!this.isNewLocationOk(var2)) {
         return null;
      }

      this.checkProjectLockForDestination(var2);
      this.originalLocation.updateProjectFileWithName(var1);
      this.transferData(var2);
      var2.updateProperties();
      return var2;
   }

   private ProjectLocation createNewLocation(String var1) {
      File var2 = new File(this.originalLocation.getLocation().getParentFile(), var1);
      return new ProjectLocation(var1, var2);
   }

   private void validateName(String var1) throws IllegalNameException {
      NameValidator var2 = NameValidatorStore.getInstance().get(IntegrationProject.class);
      var2.validateName(var1);
   }

   private boolean isNewLocationOk(ProjectLocation var1) {
      if (var1.getLocation().exists()) {
         ProjectNameConflictResolver.Resolution var2 = this.conflictResolver.resolve(var1);
         return var2 == ProjectNameConflictResolver.Resolution.OVERWRITE;
      } else {
         return true;
      }
   }

   private void checkOriginalProjectLock() throws IOException {
      if (this.originalLocation.getLocation().exists() && this.isLocked(this.originalLocation)) {
         throw new IOException("Cannot rename the project at \"" + this.originalLocation.getLocationPath() + "\" because it is open for editing.");
      }
   }

   private boolean isLocked(ProjectLocation var1) {
      ProjectFileLock var2 = new ProjectFileLock();
      return var2.isLocked(var1.getLocation());
   }

   private void checkProjectLockForDestination(ProjectLocation var1) throws IOException {
      if (var1.getLocation().exists() && this.isLocked(var1)) {
         throw new IOException("Cannot overwrite the project at \"" + var1.getLocationPath() + "\" because it is open for editing.");
      }
   }

   private void transferData(ProjectLocation var1) throws IOException {
      this.clearDestination(var1);
      this.renameFolder(var1);
   }

   private void clearDestination(ProjectLocation var1) throws IOException {
      if (var1.exists()) {
         var1.deleteFromDisk();
      }
   }

   private void renameFolder(ProjectLocation var1) throws IOException {
      if (!this.originalLocation.getLocation().renameTo(var1.getLocation())) {
         throw new IOException("Failed to rename the folder \"" + this.originalLocation.getLocationPath() + "\" to \"" + var1.getLocationPath() + "\".");
      }
   }
}
