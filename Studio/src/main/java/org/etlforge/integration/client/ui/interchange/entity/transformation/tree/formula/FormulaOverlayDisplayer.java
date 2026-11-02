package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.overlay.OverlayAlert;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public final class FormulaOverlayDisplayer extends AbstractFormulaDisplayer {
   private final OverlayContainer container;

   public FormulaOverlayDisplayer(FormulaBuilderUiFactory var1, OverlayContainer var2) {
      super(var1);
      this.container = var2;
   }

   @Override
   protected AbstractFormulaBuilder createFormulaBuilder(TreeMapper var1, String var2, String var3, NodeMapping var4) {
      FormulaOverlay var5 = new FormulaOverlay(this.container, var1.getTreeMapperContainer().getProject(), var1, this.getUiFactory(), var2, var3, var4);
      var5.getUi().setPathChangeReaction(new FormulaOverlayDisplayer.PathChangeReaction());
      return var5;
   }

   @Override
   protected WaitService getWaitService(TreeMapper var1) {
      return this.container instanceof WaitService ? (WaitService)this.container : super.getWaitService(var1);
   }

   @Override
   protected boolean isBlocking() {
      return false;
   }

   private final class PathChangeReaction implements Runnable {
      private PathChangeReaction() {
      }

      @Override
      public void run() {
         if (FormulaOverlayDisplayer.this.getFormulaBuilder() != null) {
            FormulaOverlayDisplayer.this.getFormulaBuilder().applySilently();
            this.showMessage();
         }
      }

      private void showMessage() {
         String var1 = "<html>One or more project items referred to in this formula were renamed or moved to a new folder.<br><br>The script has been updated accordingly.</html>";
         OverlayAlert var2 = new OverlayAlert(var1, AlertIcons.INFO, "Automatic Change");
         var2.setAlpha(0.75F);
         var2.show(FormulaOverlayDisplayer.this.container);
      }
   }
}
