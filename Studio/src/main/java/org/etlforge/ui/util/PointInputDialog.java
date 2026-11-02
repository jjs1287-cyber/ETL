package org.etlforge.ui.util;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanelDialog;

public class PointInputDialog {
   private final Window owner;
   private final String title;
   private Point initialValue;
   private String caption;

   public PointInputDialog(Window var1, String var2) {
      this.owner = var1;
      this.title = var2;
   }

   public void setInitialValue(Point var1) {
      this.initialValue = var1;
   }

   public void setCaption(String var1) {
      this.caption = var1;
   }

   public Point open() {
      PointInputPanel var1 = new PointInputPanel(this.initialValue, this.caption);
      InputPanelDialog var2 = this.createDialog(var1);
      var2.setVisible(true);
      return var2.wasOkPressed() ? var1.getNewCoordinates() : null;
   }

   private InputPanelDialog createDialog(PointInputPanel var1) {
      InputPanelDialog var2 = new InputPanelDialog(this.owner, var1, this.title);
      var2.setResizable(false);
      var2.pack();
      var2.setOkButtonAsDefault();
      var2.setLocationRelativeTo(UiUtils.getActiveWindow());
      this.bindDialogWithInputPanel(var1, var2);
      var2.getOKButton().setEnabled(var1.isInputValid());
      return var2;
   }

   private void bindDialogWithInputPanel(final PointInputPanel var1, final InputPanelDialog var2) {
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            var2.getOKButton().doClick();
         }
      });
      var1.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1x) {
            var2.getOKButton().setEnabled(var1.isInputValid());
         }
      });
   }
}
