package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public final class MultipleMapAction extends AbstractSingleObjectSelectionAction<Node> {
   private TreeMapper treeMapper;
   private boolean isXml;

   public MultipleMapAction() {
      super(Node.class, "Multiple", ClientIcons.getIcon("MultipleMappings.16"));
      this.putValue("KongaMediumIconKey", ClientIcons.getIcon("MultipleMappings.24"));
      this.putValue("ShortDescription", "Create a copy of the selected folder");
   }

   public MultipleMapAction(TreeMapper var1) {
      this();
      this.setTreeMapper(var1);
   }

   public MultipleMapAction(final TransformationPage var1) {
      this();
      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            MultipleMapAction.this.setTreeMapper(var1.getTreeMapper());
         }
      });
   }

   public void setTreeMapper(TreeMapper var1) {
      this.treeMapper = var1;
      this.isXml = var1.isTargetXml();
      this.setSelectionSource(var1.m_targetTreeComponent);
   }

   protected boolean isEnabledFor(Node var1) {
      return var1.isFolder() && (this.isXml && var1.isLoopNode() || var1.isOptional() || !this.isXml || var1.isChildOfOneOfNode());
   }

   protected void run(Node var1, ActionEvent var2) {
      if (var1.isLoopNode() || var1.isOptional()) {
         this.treeMapper.addOption(var1);
      }
   }
}
