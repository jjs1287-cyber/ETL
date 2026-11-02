package org.etlforge.integration.client.ui.interchange.entity.schedule.page.occurrence;

import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.ui.input.DayOfTheWeekInputField;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputFieldValidator;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.time.DayOfTheWeekCheckBoxPanel;

class WeeklyPanel extends InputPanel implements InputFieldValidator {
   private final Every_X_Panel everyXPanel;
   private final DayOfTheWeekCheckBoxPanel daysPanel;

   public WeeklyPanel(Every_X_Panel var1, DayOfTheWeekCheckBoxPanel var2) {
      this.everyXPanel = var1;
      this.daysPanel = var2;
      this.daysPanel.setOpaque(false);
      this.setOpaque(false);
      this.addInputPanel(var1);
      this.addInputField(new DayOfTheWeekInputField(ScheduleResources.getString("Label.Weeks"), this.daysPanel), this);
      this.layoutComponents();
   }

   private void layoutComponents() {
      BoxBuilder var1 = BoxBuilder.horizontal().add(this.everyXPanel.getUi()).glue().nonOpaque();
      BoxBuilder.vertical(this.getUi()).glue().add(var1).add(this.daysPanel).glue();
   }

   @Override
   public boolean isFieldRequired(InputField var1) {
      return true;
   }

   @Override
   public boolean isFieldValid(InputField var1) {
      return this.daysPanel.getSelectedDays().length > 0;
   }

   @Override
   public void validateField(InputField var1) throws InputFieldException {
      if (!this.isFieldValid(var1)) {
         throw new InputFieldException(var1, ScheduleResources.getString("Error.WeeklyPanel.Empty"));
      }
   }
}
