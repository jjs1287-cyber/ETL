package org.etlforge.integration.uidelegate.project;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;

public final class ProjectNode extends IntegrationEntityTreeNode {
   private static final String ROOT_NODE_PREFIX = "Project: ";

   @ExtensionObjectConstructor
   public ProjectNode(IntegrationProject var1) {
      super(null, var1, true);
   }

   public IntegrationProject getUserObject() {
      return (IntegrationProject)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return "Project: " + super.getDisplayName();
   }

   @Override
   public boolean sortChildren() {
      return false;
   }
}
