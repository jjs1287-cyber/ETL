package com.etlforge.integration.client.ui.sfdc;

import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.label.KongaLabel;

public final class InvalidWizardObjectStateUi implements UiProvider {
   @Override
   public void decorate(UiCanvas var1) {
      KongaLabel var2 = WizardStyleSheet.caption("Invalid Modification");
      var2.setIcon(AlertIcons.ERROR);
      Grid var3 = Grid.oneRow(3).nonOpaque();
      var3.add("One or more of the project items that implement this wizard has been modified in");
      var3.add("such a way that this wizard is no longer valid.");
      Vertical.borderLayout().of(var2, var3).withGap(20).decorate(var1);
   }

   @Override
   public void requestFocus() {
   }
}
