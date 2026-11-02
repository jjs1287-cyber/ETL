package org.etlforge.integration.client.project;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import org.etlforge.io.FileUtils;
import org.etlforge.io.Folder;
import org.etlforge.system.callback.Receiver;

public final class ProjectSearcher {
   private Predicate<String> nameFilter = Predicates.alwaysTrue();
   private volatile Folder currentFolder;
   private volatile boolean stopped;

   public ProjectLocation[] search(Folder var1) {
      final ArrayList var2 = Lists.newArrayList();
      this.processFolder(var1, new Receiver<ProjectLocation>() {
         public void handle(ProjectLocation var1) {
            var2.add(var1);
         }
      }, new ProjectFolderDetector());
      return (ProjectLocation[]) var2.toArray(new ProjectLocation[var2.size()]);
   }

   public void search(Folder var1, Receiver<ProjectLocation> var2) {
      this.processFolder(var1, var2, new ProjectFolderDetector());
   }

   private void processFolder(Folder var1, Receiver<ProjectLocation> var2, ProjectFolderDetector var3) {
      this.currentFolder = var1;
      boolean var4 = var3.isProjectFolder(var1.asJavaFile());
      if (var4) {
         this.handleProjectFolder(var1.asJavaFile(), var2, var3);
      } else if (!this.stopped) {
         this.drillDown(var1, var2, var3);
      }
   }

   private void handleProjectFolder(File var1, Receiver<ProjectLocation> var2, ProjectFolderDetector var3) {
      File var4 = var3.getProjectFile(var1);
      String var5 = FileUtils.stripExtension(var4);
      if (this.nameFilter.apply(var5)) {
         var2.handle(new ProjectLocation(var5, var1));
      }
   }

   private void drillDown(Folder var1, Receiver<ProjectLocation> var2, ProjectFolderDetector var3) {
      for (Folder var7 : var1.listSubFolders()) {
         if (!this.stopped) {
            this.processFolder(var7, var2, var3);
         }
      }
   }

   public void stop() {
      this.stopped = true;
   }

   public void setNameFilter(Predicate<String> var1) {
      this.nameFilter = var1;
   }

   public File getCurrentFolder() {
      return this.currentFolder.asJavaFile();
   }
}
