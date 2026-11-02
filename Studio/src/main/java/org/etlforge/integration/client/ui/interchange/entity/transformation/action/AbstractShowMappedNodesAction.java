package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.ui.action.ToggleAction;

abstract class AbstractShowMappedNodesAction extends AbstractApplicationAction implements ToggleAction {
   protected final TransformationPage page;

   public AbstractShowMappedNodesAction(String var1, TransformationPage var2) {
      super(var1, TransformationActionDictionary.DICTIONARY);
      this.page = var2;
      this.putValue("ToggleAction#ON_OFF", Boolean.FALSE);
      var2.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            AbstractShowMappedNodesAction.this.updateSelectedState();
         }
      });
   }

   protected abstract TreeComponent getTreeComponent(TransformationPage var1);

   private void updateSelectedState() {
      TreeComponent var1 = this.getTreeComponent(this.page);
      boolean var2 = var1.isShowMappedNodesOnly();
      this.setSelected(var2);
   }

   @Override
   public final void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            boolean var1x = AbstractShowMappedNodesAction.this.isSelected();
            boolean var2 = !var1x;
            TreeComponent var3 = AbstractShowMappedNodesAction.this.getTreeComponent(AbstractShowMappedNodesAction.this.page);
            var3.setShowMappedNodesOnly(var2);
            AbstractShowMappedNodesAction.this.setSelected(var2);
         }
      });
   }

   @Override
   public final boolean isSelected() {
      Object var1 = this.getValue("ToggleAction#ON_OFF");
      return var1 instanceof Boolean ? (Boolean)var1 : false;
   }

   @Override
   public final void setSelected(boolean var1) {
      this.putValue("ToggleAction#ON_OFF", var1);
   }
}
