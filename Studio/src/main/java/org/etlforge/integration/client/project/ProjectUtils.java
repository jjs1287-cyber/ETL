package org.etlforge.integration.client.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFunctions;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ProjectUtils {
   public static void prepareRootDirectory(File var0) throws InterchangeSavingException {
      if (var0.exists()) {
         if (var0.isDirectory()) {
            try {
               FileUtils.cleanDirectory(var0);
            } catch (IOException var2) {
               throw new InterchangeSavingException("Failed to delete the old contents under the specified project directory " + var0.getAbsolutePath() + ".");
            }
         } else if (!var0.delete()) {
            throw new InterchangeSavingException(
               "Failed to create the root directory for the new project because the parent directory contains a file having the same name that could not be deleted."
            );
         }
      }

      if (!var0.isDirectory() && !var0.mkdirs()) {
         throw new InterchangeSavingException("Failed to create the directory " + var0.getAbsolutePath() + ".");
      }
   }

   public static void saveProjectFile(IntegrationProject var0, ProjectFile var1) throws InterchangeSavingException {
      var1.update(var0);

      try {
         var1.save();
      } catch (FileNotFoundException var3) {
         throw new InterchangeSavingException(var3);
      } catch (IOException var4) {
         throw new InterchangeSavingException(var4);
      }
   }

   public static IntegrationProject getProject(IntegrationEntity var0) {
      if (var0 instanceof IntegrationProject) {
         return (IntegrationProject)var0;
      }

      if (var0 == null) {
         return null;
      }

      IntegrationEntity var1 = var0.getParent();

      while (var1 != null && !(var1 instanceof IntegrationProject)) {
         var1 = var1.getParent();
      }

      return (IntegrationProject)var1;
   }

   public static ManagedProject getManagedProject(IntegrationEntity var0) {
      IntegrationProject var1 = getProject(var0);
      return var1 != null ? var1.getExtensionObject(ManagedProject.class) : null;
   }

   public static EntityResources getProjectResources(IntegrationProject var0) {
      ManagedProject var1 = getManagedProject(var0);
      return var1 != null ? var1.getEntityResources() : null;
   }

   public static List<EntityDescriptor> getRootFolderDescriptors(IntegrationProject var0) {
      return KongaListUtils.transform(var0.getAllRootFolders(), EntityFunctions.TO_DESCRIPTOR);
   }

   private ProjectUtils() {
   }
}
