package org.etlforge.integration.client.ui.project.browser;

import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileView;
import org.etlforge.integration.client.project.ProjectFolderDetector;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

public class ProjectFileView extends FileView {
   private static final Icon JITTERPACK = ClientIcons.APPLICATION_16;
   private static final Icon PROJECT = EntityIcons.forType(EntityType.Project).getDefaultIcon(EntityIcons.Size.SMALL);
   private final ProjectFolderDetector projectFolderDetector;

   public ProjectFileView(ProjectFolderDetector var1) {
      this.projectFolderDetector = var1;
   }

   @Override
   public Icon getIcon(File var1) {
      String var2 = var1.getName().toLowerCase();
      if (var2.endsWith(".jpk")) {
         return JITTERPACK;
      } else {
         return !var2.endsWith(".jip") && !this.isProjectFolder(var1) ? super.getIcon(var1) : PROJECT;
      }
   }

   private boolean isProjectFolder(File var1) {
      return this.projectFolderDetector.isProjectFolder(var1);
   }

   @Override
   public Boolean isTraversable(File var1) {
      if (var1 != null && this.isProjectFolder(var1)) {
         return Boolean.FALSE;
      }
                                                                            
      return super.isTraversable(var1);
   }
}
