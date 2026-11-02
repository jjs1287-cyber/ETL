package org.etlforge.integration.client.ui.interchange.component.tree;

import javax.swing.Icon;
import org.etlforge.integration.uidelegate.project.ProjectIcons;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public final class NoProjectNode extends DefaultKongaTreeNode {
   public NoProjectNode() {
      super("", false);
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return ProjectIcons.PROJECT_16;
   }
}
