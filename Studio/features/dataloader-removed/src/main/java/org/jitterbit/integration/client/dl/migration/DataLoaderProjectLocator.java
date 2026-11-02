package org.jitterbit.integration.client.dl.migration;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jitterbit.collections.LookupMap;
import org.jitterbit.integration.client.project.ProjectFile;
import org.jitterbit.integration.client.project.ProjectFolderDetector;
import org.jitterbit.integration.client.project.ProjectLocation;
import org.jitterbit.io.FileUtils;
import org.jitterbit.io.Folder;

final class DataLoaderProjectLocator {
   private final Folder rootFolder;

   public static DataLoaderProjectLocator forApplicationFolder(Folder var0) {
      return new DataLoaderProjectLocator(var0.subFolder("projects"));
   }

   public DataLoaderProjectLocator(Folder var1) {
      this.rootFolder = var1;
   }

   public Optional<ProjectLocation> locateLatestDataLoaderProject() {
      if (this.rootFolder.exists()) {
         List var1 = this.listCandidates();
         if (!var1.isEmpty()) {
            DataLoaderProjectLocator.LastModifiedFirst var2 = new DataLoaderProjectLocator.LastModifiedFirst();
            ProjectLocation var3 = var2.max(var1);
            return Optional.of(var3);
         }
      }

      return Optional.absent();
   }

   private List<ProjectLocation> listCandidates() {
      ArrayList var1 = Lists.newArrayList();
      ProjectFolderDetector var2 = new ProjectFolderDetector();

      for (File var6 : this.rootFolder.listFiles(new ProjectFolderFilter())) {
         if (var2.isProjectFolder(var6)) {
            File var7 = var2.getProjectFile(var6);
            String var8 = FileUtils.stripExtension(var7);
            var1.add(new ProjectLocation(var8, var6));
         }
      }

      return var1;
   }

   private static class LastModifiedFirst extends Ordering<ProjectLocation> {
      private final LookupMap<ProjectLocation, Long> modificationDates = LookupMap.withSupplier(new Function<ProjectLocation, Long>() {
         public Long apply(ProjectLocation var1) {
            try {
               ProjectFile var2 = ProjectFile.createForExistingProject(var1.getProjectName(), var1.getLocation(), false);
               Optional var3 = var2.getModifiedAt();
               if (var3.isPresent()) {
                  return ((Date)var3.get()).getTime();
               }

               File var4 = var2.getFile();
               return var4.lastModified();
            } catch (Exception var5) {
               return -1L;
            }
         }
      });

      private LastModifiedFirst() {
      }

      public int compare(ProjectLocation var1, ProjectLocation var2) {
         Long var3 = this.modificationDates.getValue(var1);
         Long var4 = this.modificationDates.getValue(var2);
         return var3.compareTo(var4);
      }
   }
}
