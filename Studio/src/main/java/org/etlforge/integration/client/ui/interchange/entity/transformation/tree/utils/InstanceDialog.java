package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.etlforge.lang.Intervals;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;

final class InstanceDialog extends KongaDialog {
   private final int numberOfInstances;
   private int instance = -1;
   private final JFormattedTextField inputField;

   private InstanceDialog(Window var1, int var2) {
      super(var1, "Find instance");
      this.numberOfInstances = var2;
      this.inputField = this.createInputField();
      this.createLayout();
      this.pack();
      this.setResizable(false);
      this.setLocationRelativeTo(null);
   }

   public static int getInstance(Window var0, int var1) {
      InstanceDialog var2 = new InstanceDialog(var0, var1);
      var2.setVisible(true);
      return var2.wasOkPressed() ? var2.instance : -1;
   }

   @Override
   protected void onOk() {
      try {
         this.collectEnteredValue();
      } finally {
         super.onOk();
      }
   }

   private void collectEnteredValue() {
      Object var1 = this.inputField.getValue();
      if (var1 instanceof Number) {
         int var2 = ((Number)var1).intValue();
         this.instance = Intervals.valueWithin(var2, 1, this.numberOfInstances);
      } else {
         this.instance = -1;
      }
   }

   private void createLayout() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder();
      var1.center(this.layoutInputPanel()).south(this.layoutButtons()).asContentPaneFor(this);
   }

   private UiProvider layoutInputPanel() {
      return Horizontal.box().of("Enter instance number to display: ", this.inputField).glue().withEmptyBorder(10);
   }

   private JFormattedTextField createInputField() {
      JFormattedTextField var1 = new JFormattedTextField(NumberFormat.getInstance());
      var1.setColumns(4);
      var1.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), new Object());
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            InstanceDialog.this.onOk();
         }
      });
      this.setFocusedComponent(var1);
      return var1;
   }

   private JComponent layoutButtons() {
      JPanel var1 = this.getButtonPanel(OK_CANCEL, 10);
      var1.setBorder(Empty.border(10, 10, 20, 10));
      this.setOkButtonAsDefault();
      return var1;
   }
}
