package org.etlforge.integration.client.ui.interchange.testhistory;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.entity.testhistory.TestResultHistoryView;
import org.etlforge.integration.client.ui.interchange.CurrentProjectListenerResource;

public abstract class AbstractTestHistoryViewFactory implements WindowSectionContentProvider {
   private final String id;
   private final CurrentProjectService currentProjectService;

   protected AbstractTestHistoryViewFactory(String var1, CurrentProjectService var2) {
      this.id = var1;
      this.currentProjectService = var2;
   }

   @Override
   public final String getContentId() {
      return this.id;
   }

   @Override
   public final WindowSectionContent getContent(WindowSection var1) {
      TestResultHistoryView var2 = this.createNewView(var1);
      this.installProjectListener(var2);
      this.setCurrentProject(var2);
      return var2;
   }

   protected abstract TestResultHistoryView createNewView(WindowSection var1);

   private void setCurrentProject(TestResultHistoryView var1) {
      ManagedProject var2 = this.currentProjectService.getManagedProject();
      if (var2 != null) {
         var1.setProject(var2);
      }
   }

   private void installProjectListener(final TestResultHistoryView var1) {
      var1.addResource(new CurrentProjectListenerResource(this.currentProjectService) {
         @Override
         public void projectOpened(CurrentProjectEvent var1x) {
            ManagedProject var2 = var1x.getManagedProject();
            var1.setProject(var2);
         }

         @Override
         public void projectClosed(CurrentProjectEvent var1x) {
            var1.setProject(null);
         }
      });
   }
}
