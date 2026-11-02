package org.etlforge.integration.client.ui.interchange.entity.schedule.page.occurrence;

import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;

final class Monthly_A_Panel extends InputPanel {
   private Every_X_Panel dayPanel;
   private Every_X_Panel monthPanel;

   public Monthly_A_Panel(String var1, String var2) {
      this.setOpaque(false);
      this.createComponents(var1, var2);
   }

   private void createComponents(String var1, String var2) {
      this.dayPanel = new Every_X_Panel(var1, ScheduleResources.getString("Label.Day"), ScheduleResources.getString("Label.ofEvery"), 1, 31);
      this.monthPanel = new Every_X_Panel(var2, null, ScheduleResources.getString("Label.Months"), 1, 12);
      this.addInputPanel(this.dayPanel);
      this.addInputPanel(this.monthPanel);
      this.layoutComponents();
   }

   private void layoutComponents() {
      BoxBuilder.horizontal(this.getUi()).of(this.dayPanel.getUi(), this.monthPanel.getUi());
   }

   public int getDay() {
      return this.dayPanel.getValue();
   }

   public void setDay(int var1) {
      this.dayPanel.setValue(var1);
   }

   public void clearDay() {
      this.dayPanel.clearValue();
   }

   public int getMonth() {
      return this.monthPanel.getValue();
   }

   public void setMonth(int var1) {
      this.monthPanel.setValue(var1);
   }

   public void clearMonth() {
      this.monthPanel.clearValue();
   }
}
