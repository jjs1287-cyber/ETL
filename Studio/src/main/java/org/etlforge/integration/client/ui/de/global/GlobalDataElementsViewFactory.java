package org.etlforge.integration.client.ui.de.global;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.data.project.IntegrationProject;

public class GlobalDataElementsViewFactory implements WindowSectionContentProvider {
   private final IntegrationProject project;
   private ExpressionSourceDisplayer sourceDisplayer;

   public GlobalDataElementsViewFactory(IntegrationProject var1) {
      this.project = var1;
   }

   public GlobalDataElementsView getContent(WindowSection var1) {
      GlobalDataElementsViewImpl var2 = new GlobalDataElementsViewImpl(var1);
      var2.setProject(this.project);
      var2.setExpressionSourceDisplayer(this.sourceDisplayer);
      return var2;
   }

   @Override
   public String getContentId() {
      return GlobalDataElementsView.CONTENT_ID;
   }

   public void setExpressionSourceDisplayer(ExpressionSourceDisplayer var1) {
      this.sourceDisplayer = var1;
   }
}
