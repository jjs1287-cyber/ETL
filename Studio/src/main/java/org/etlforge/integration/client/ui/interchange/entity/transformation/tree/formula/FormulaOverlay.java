package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.integration.client.ui.interchange.entity.transformation.prefs.CancelFormulaBuilderOnEscapePreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.text.undo.TextUndoRedoControllerFactory;

final class FormulaOverlay extends AbstractFormulaBuilder {
   private final OverlayContainer container;
   private final OverlayDecorator overlay;
   public boolean visible;
   private UndoRedoController undoRedo;

   public FormulaOverlay(
      OverlayContainer var1, IntegrationProject var2, TreeMapper var3, FormulaBuilderUiFactory var4, String var5, String var6, NodeMapping var7
   ) {
      super(var2, var3, var4, var5, var6, var7);
      this.container = var1;
      this.overlay = this.createOverlay();
   }

   private OverlayDecorator createOverlay() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(this.getUi()).withEmptyBorder(10);
      OverlayDecorator var2 = new OverlayDecorator(var1, this.getSuggestedTitle()) {
         @Override
         protected void onCancel() {
            FormulaOverlay.this.handleCancel();
         }

         @Override
         protected void onHidden() {
            FormulaOverlay.this.undoRedo.dispose();
            FormulaOverlay.this.visible = false;
         }

         @Override
         protected void onDisplayed() {
            this.installUndoRedo();
         }

         private void installUndoRedo() {
            TextUndoRedoControllerFactory var1x = UndoRedoControllers.textFactory();
            FormulaOverlay.this.undoRedo = var1x.newController(FormulaOverlay.this.getUi().getExpressionArea().getTextPane());
            FormulaOverlay.this.undoRedo.alwaysConsumeKeyboardTriggers();
         }
      };
      var2.setFillOut();
      var2.setTitleStyle(TextStyles.DefaultBold);
      FormulaOverlay.OkAction var3 = new FormulaOverlay.OkAction();
      var2.setOkAction(var3);
      JComponent var4 = this.createOverlayButtons(var2, var3);
      this.getUi().setButtonPanel(var4);
      var2.setOkAsDefault(true);
      var2.setBlocking(true);
      var2.setFocusReceiverWhenClosed(this.getTreeMapper());
      this.installShortcuts(var2);
      return var2;
   }

   private JComponent createOverlayButtons(OverlayDecorator var1, Action var2) {
      Grid var3 = Grid.oneRow(10);
      var3.addAll(var2, var1.getCancelAction());
      BoxBuilder var4 = BoxBuilder.horizontal();
      var4.glue().add(var3);
      return var4.container();
   }

   private void installShortcuts(OverlayDecorator var1) {
      if (CancelFormulaBuilderOnEscapePreference.get()) {
         var1.installOkAndCancelShortCuts();
      } else {
         var1.installOkAndCancelShortCuts(var1.getOkAction(), null);
      }
   }

   @Override
   public void open() {
      this.overlay.show(this.container, new OverlayCenterPositioner());
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            FormulaOverlay.this.getUi().requestFocus();
         }
      });
   }

   @Override
   public void hide() {
      this.overlay.hide();
   }

   @Override
   public void showFormula() {
      this.restoreCaretPosition();
      this.getUi().showFormula();
      this.open();
      this.visible = true;
   }

   @Override
   public final boolean isVisible() {
      return this.visible;
   }

   @Override
   protected void mappingChanged() {
      String var1 = this.getSuggestedTitle();
      this.overlay.setTitle(var1);
   }

   private final class OkAction extends AbstractAction {
      public OkAction() {
         super("OK");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (FormulaOverlay.this.apply()) {
            FormulaOverlay.this.rememberCaretPosition();
            FormulaOverlay.this.hide();
         }
      }
   }
}
