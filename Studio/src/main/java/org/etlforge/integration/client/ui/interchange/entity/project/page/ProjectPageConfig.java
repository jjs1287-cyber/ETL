package org.etlforge.integration.client.ui.interchange.entity.project.page;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.client.ui.project.ProjectRenamer;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class ProjectPageConfig extends DefaultPageConfig {
   private final ProjectRenamer projectRenamer;
   private final ImmutableList<Action> pageActions;

   public ProjectPageConfig(ApplicationWindow var1, ProjectPersistor var2, ProjectRenamer var3, List<Action> var4) {
      super(var1, var2);
      this.projectRenamer = var3;
      this.pageActions = ImmutableList.copyOf(var4);
   }

   @Override
   protected void postConfiguration(IntegrationEntityPage var1) {
      ProjectPage var2 = (ProjectPage)var1;
      var2.setProjectRenamer(this.projectRenamer);
      var2.setProjectActions(this.pageActions.toArray(new Action[this.pageActions.size()]));
   }
}
