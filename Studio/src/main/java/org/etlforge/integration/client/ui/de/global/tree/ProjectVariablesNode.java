package org.etlforge.integration.client.ui.de.global.tree;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.de.resources.DataElementResources;
import org.etlforge.integration.uidelegate.project.ProjectIcons;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

final class ProjectVariablesNode extends DefaultKongaTreeNode {
   public ProjectVariablesNode(KongaTreeNode var1) {
      super(var1, DataElementResources.getString("DataElement.Tree.Project.Name"), true);
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return ProjectIcons.PROJECT_16;
   }
}
