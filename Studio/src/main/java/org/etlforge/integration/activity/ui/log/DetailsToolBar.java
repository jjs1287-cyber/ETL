package org.etlforge.integration.activity.ui.log;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;

final class DetailsToolBar {
   final JComponent ui = Box.createHorizontalBox();

   public DetailsToolBar(LogMessageDisplayer var1) {
      this.addLabel(this.ui);
      this.addButtons(this.ui, var1);
      this.setBorder(this.ui);
   }

   private void addLabel(JComponent var1) {
      JLabel var2 = new JLabel(PackageResources.Displayer.DETAILS_LABEL);
      var1.add(var2);
   }

   private void addButtons(JComponent var1, LogMessageDisplayer var2) {
      Action var3 = var2.getSearchAction();
      if (var3 != null) {
         var1.add(Box.createHorizontalStrut(10));
         this.addButton(var1, var3);
         var1.add(Box.createHorizontalGlue());
      }
   }

   private void addButton(JComponent var1, Action var2) {
      KongaButton var3 = new KongaButton(var2);
      var3.setText("");
      ButtonUtils.configureButtonForToolBar(var3);
      var1.add(var3);
   }

   private void setBorder(JComponent var1) {
      var1.setBorder(new BorderBuilder().bevel(0).empty(2));
   }
}
