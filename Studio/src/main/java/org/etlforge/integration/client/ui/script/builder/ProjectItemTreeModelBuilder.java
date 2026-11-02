package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.tree.KongaTreeModel;

public interface ProjectItemTreeModelBuilder {
   KongaTreeModel buildModel(IntegrationProject var1);
}
