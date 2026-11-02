package org.etlforge.ui.graph2d;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import org.apache.commons.lang.StringUtils;
import org.etlforge.lang.Intervals;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.text.KongaTextField;

public final class GraphZoomer implements UiProvider {
   private static final int BUTTON_ZOOM_VALUE = 20;
   private static final int MIN_VALUE = 10;
   private static final int MAX_VALUE = 200;
   private final Graph2D graph;
   private int value;
   private final KongaTextField valueField;
   private final JButton zoomInButton;
   private final JButton zoomOutButton;

   public GraphZoomer(Graph2D var1) {
      this.graph = var1;
      this.value = (int)(var1.getZoomLevel() * 100.0);
      this.valueField = new KongaTextField(5);
      this.valueField.setSelectAllWhenFocused(true);
      this.zoomInButton = new JButton("+");
      this.zoomOutButton = new JButton("-");
      ButtonUtils.addActionListener(new GraphZoomer.ButtonListener(), this.zoomInButton, this.zoomOutButton);
      this.addFieldListener();
      this.updateValueField();
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(5, this.zoomOutButton, this.zoomInButton, this.valueField);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.valueField.requestFocusInWindow();
   }

   public void addActionListener(ActionListener var1) {
      this.valueField.addActionListener(var1);
   }

   private void change(int var1) {
      this.set(this.value += var1);
      this.valueField.requestFocusInWindow();
   }

   private void set(int var1) {
      this.value = var1;
      this.ensureWithinLimits();
      this.updateValueField();
      this.updateButtonsEnabledState();
      this.graph.setZoomLevel(this.value / 100.0);
   }

   private void ensureWithinLimits() {
      this.value = Intervals.valueWithin(this.value, 10, 200);
   }

   private void updateButtonsEnabledState() {
      this.zoomOutButton.setEnabled(this.value >= 30);
      this.zoomInButton.setEnabled(this.value <= 180);
   }

   private void updateValueField() {
      this.valueField.setText(this.value + "%");
   }

   private void addFieldListener() {
      GraphZoomer.FieldListener var1 = new GraphZoomer.FieldListener();
      this.valueField.addActionListener(var1);
      this.valueField.addFocusListener(var1);
   }

   private class ButtonListener implements ActionListener {
      private ButtonListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (var1.getSource() == GraphZoomer.this.zoomInButton) {
            GraphZoomer.this.change(20);
         } else {
            GraphZoomer.this.change(-20);
         }
      }
   }

   private class FieldListener extends FocusAdapter implements ActionListener {
      private FieldListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.apply();
      }

      @Override
      public void focusLost(FocusEvent var1) {
         this.apply();
      }

      private void apply() {
         try {
            String var1 = StringUtils.removeEnd(GraphZoomer.this.valueField.getText().trim(), "%");
            int var2 = Integer.parseInt(var1);
            GraphZoomer.this.set(var2);
         } catch (NumberFormatException var3) {
            GraphZoomer.this.updateValueField();
         }
      }
   }
}
