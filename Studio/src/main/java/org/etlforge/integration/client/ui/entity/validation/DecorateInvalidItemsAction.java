package org.etlforge.integration.client.ui.entity.validation;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.ui.action.ToggleAction;

public class DecorateInvalidItemsAction extends AbstractAction implements ToggleAction {
   private final ValidationStatusIndicator indicator;
   private BooleanPreference preference;

   public DecorateInvalidItemsAction(ValidationStatusIndicator var1) {
      super(Strings.get("DecorateInvalid.Name"));
      this.indicator = Preconditions.checkNotNull(var1, "indicator");
      this.putValue("ShortDescription", Strings.get("DecorateInvalid.ToolTip"));
   }

   @Override
   public boolean isSelected() {
      return this.indicator.isIndicatingValidationStatus();
   }

   @Override
   public void setSelected(boolean var1) {
      this.indicator.setIndicateValidationStatus(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      boolean var2 = !this.isSelected();
      this.setSelected(var2);
      if (this.preference != null) {
         this.preference.set(var2);
      }
   }

   public void setPreference(BooleanPreference var1) {
      this.preference = var1;
      this.setSelected(var1.get());
   }
}
