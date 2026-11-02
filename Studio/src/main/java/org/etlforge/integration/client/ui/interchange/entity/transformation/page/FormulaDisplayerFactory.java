package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import org.etlforge.integration.client.ui.interchange.entity.transformation.prefs.FormulaBuilderDisplayMode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.prefs.FormulaBuilderDisplayModePreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderUiFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaDialogDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaOverlayDisplayer;

public final class FormulaDisplayerFactory {
   private final FormulaBuilderUiFactory uiFactory;

   public FormulaDisplayerFactory(FormulaBuilderUiFactory var1) {
      this.uiFactory = var1;
   }

   public FormulaDisplayer newDisplayer(TransformationPage var1) {
      FormulaBuilderDisplayMode var2 = FormulaBuilderDisplayModePreference.get();
      switch (var2) {
         case DIALOG:
            return new FormulaDialogDisplayer(this.uiFactory);
         case OVERLAY:
            return new FormulaOverlayDisplayer(this.uiFactory, var1.getOverlayContainer());
         default:
            throw new RuntimeException("I do not know how to create a formula displayer for the mode " + var2.name());
      }
   }
}
