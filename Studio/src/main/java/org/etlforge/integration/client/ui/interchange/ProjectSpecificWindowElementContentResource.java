package org.etlforge.integration.client.ui.interchange;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;

public class ProjectSpecificWindowElementContentResource extends CurrentProjectListenerResource {
   private final WindowSection section;
   private final WindowSectionContent content;

   public ProjectSpecificWindowElementContentResource(CurrentProjectService var1, WindowSection var2, WindowSectionContent var3) {
      super(var1);
      this.section = var2;
      this.content = var3;
   }

   @Override
   public final void projectClosed(CurrentProjectEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ProjectSpecificWindowElementContentResource.this.section.removeContent(ProjectSpecificWindowElementContentResource.this.content);
         }
      });
   }
}
