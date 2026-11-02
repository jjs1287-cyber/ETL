package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.application.ui.help.DocumentedResource;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public class DocumentedProjectItemNode extends DefaultKongaTreeNode implements DocumentedResource {
   private final String helpId;

   public DocumentedProjectItemNode(KongaTreeNode var1, String var2, String var3) {
      super(var1, var2, true);
      this.helpId = var3;
   }

   @Override
   public String getHelpId() {
      return this.helpId;
   }
}
