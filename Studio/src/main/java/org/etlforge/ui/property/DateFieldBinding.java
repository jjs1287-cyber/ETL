package org.etlforge.ui.property;

import java.text.DateFormat;
import java.util.Date;
import javax.swing.text.JTextComponent;
import org.etlforge.util.Property;

public class DateFieldBinding extends AbstractBinding<Date> {
   private final JTextComponent component;
   private final DateFormat dateFormat;

   public DateFieldBinding(Property<Date> var1, JTextComponent var2, DateFormat var3) {
      super(var1);
      this.component = var2;
      this.dateFormat = var3;
   }

   protected void updateUiWithValue(Date var1) {
      String var2 = this.getText(var1);
      this.component.setText(var2);
   }

   private String getText(Date var1) {
      return var1 == null ? "" : this.dateFormat.format(var1);
   }
}
