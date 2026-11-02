package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public final class OpenFormulaBuilderAction extends AbstractSingleObjectSelectionAction<Node> {
   private TreeMapper treeMapper;

   public OpenFormulaBuilderAction(TreeMapper var1) {
      this();
      this.setTreeMapper(var1);
   }

   public OpenFormulaBuilderAction(final TransformationPage var1) {
      this();
      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            OpenFormulaBuilderAction.this.setTreeMapper(var1.getTreeMapper());
         }
      });
   }

   public OpenFormulaBuilderAction() {
      super(Node.class, null, ClientIcons.FORMULA_16);
      this.putValue("KongaMediumIconKey", ClientIcons.FORMULA_24);
      this.putValue("ShortDescription", "Create a formula for the selected target node");
   }

   public void setTreeMapper(TreeMapper var1) {
      this.treeMapper = var1;
      this.setSelectionSource(var1.m_targetTreeComponent);
   }

   protected boolean isEnabledFor(Node var1) {
      return !var1.isFolder() && !var1.mapDisabled();
   }

   protected void run(Node var1, ActionEvent var2) {
      if (this.treeMapper != null) {
         this.treeMapper.doMap(var1, true);
      }
   }
}
