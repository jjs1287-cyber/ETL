package org.etlforge.integration.client.ui.entity.tree;

import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public interface TopLevelNode {
   KongaTreeNode buildTree(IntegrationProject var1);
}
