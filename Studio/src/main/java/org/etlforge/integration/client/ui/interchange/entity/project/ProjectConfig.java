package org.etlforge.integration.client.ui.interchange.entity.project;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.project.page.ProjectPage;
import org.etlforge.integration.client.ui.interchange.entity.project.page.ProjectPageConfig;
import org.etlforge.integration.client.ui.project.ProjectRenamer;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class ProjectConfig extends AbstractEntityConfig {
   private final ApplicationAction newProjectAction;
   private final ProjectRenamer projectRenamer;
   private final ImmutableList<Action> pageActions;

   public ProjectConfig(ApplicationAction var1, ProjectRenamer var2, List<Action> var3) {
      super(EntityType.Project);
      this.newProjectAction = var1;
      this.projectRenamer = var2;
      this.pageActions = ImmutableList.copyOf(var3);
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new ProjectPage((IntegrationProject)var1);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      return new ProjectPageConfig(var1, var3, this.projectRenamer, this.pageActions);
   }

   @Override
   protected ApplicationAction createNewEntityAction() {
      return this.newProjectAction;
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new IntegrationProjectToolTip((IntegrationProject)var1);
   }
}
