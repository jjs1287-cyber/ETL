package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import javax.swing.AbstractAction;
import javax.swing.Icon;

abstract class WizardAction extends AbstractAction {
   private final Wizard wizard;

   public WizardAction(Wizard var1, String var2, Icon var3) {
      super(var2, var3);
      this.wizard = Preconditions.checkNotNull(var1);
   }

   protected final Wizard getWizard() {
      return this.wizard;
   }
}
