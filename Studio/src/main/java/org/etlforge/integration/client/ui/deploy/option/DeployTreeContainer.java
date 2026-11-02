package org.etlforge.integration.client.ui.deploy.option;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.tree.check.CheckBoxTreeListener;

public interface DeployTreeContainer extends CheckBoxTreeListener {
   void setTree(DeployTree var1);

   IntegrationProject createPartialProject(List<IntegrationEntity> var1);

   void openPage(IntegrationEntity var1);
}
