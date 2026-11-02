package org.etlforge.ui.util;

import java.awt.Point;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.text.KongaTextField;

public class PointInputPanel extends InputPanel {
   private final IntegerInputField xField = this.createField("X");
   private final IntegerInputField yField = this.createField("Y");

   public PointInputPanel(Point var1, String var2) {
      if (var1 != null) {
         this.xField.setValue(var1.x);
         this.yField.setValue(var1.y);
      }

      this.layout(var2);
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
   }

   public Point getNewCoordinates() {
      int var1 = this.xField.getIntValue(-1);
      int var2 = this.yField.getIntValue(-1);
      return var1 >= 0 && var2 >= 0 ? new Point(var1, var2) : null;
   }

   public void addActionListener(ActionListener var1) {
      ((JTextField)this.xField.getInputComponent()).addActionListener(var1);
      ((JTextField)this.yField.getInputComponent()).addActionListener(var1);
   }

   public void setInitialFocus() {
      this.xField.requestFocus();
   }

   private void layout(String var1) {
      TwoColumnPanel var2 = new TwoColumnPanel().withEmptyBorder(10);
      if (var1 != null) {
         var2.addRow(new JLabel(var1));
      }

      this.xField.layoutIn(var2);
      this.yField.layoutIn(var2);
      this.wrap(var2);
   }

   private IntegerInputField createField(String var1) {
      IntegerInputField var2 = new IntegerInputField(var1, new KongaTextField(4), 0, Integer.MAX_VALUE, new JLabel(var1 + ":"));
      var2.setSelectAllWhenFocused(true);
      this.addInputField(var2, var2, var2);
      return var2;
   }
}
