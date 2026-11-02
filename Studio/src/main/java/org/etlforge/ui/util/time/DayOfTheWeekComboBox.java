package org.etlforge.ui.util.time;

import javax.swing.JComboBox;
import org.etlforge.util.time.DayOfTheWeek;

public class DayOfTheWeekComboBox extends JComboBox {
   public DayOfTheWeekComboBox() {
      super(DayOfTheWeek.values());
   }

   public DayOfTheWeek getSelectedItem() {
      return (DayOfTheWeek)super.getSelectedItem();
   }
}
