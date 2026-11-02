package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import java.awt.EventQueue;
import javax.swing.JLabel;
import javax.swing.UIManager;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.ui.Size;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.text.KongaTextArea;

final class WarningsDisplayer {
   public static void display(final String var0, final String var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            BorderLayoutBuilder var1x = WarningsDisplayer.createLayout(var0);
            UiProviderDialogDisplayer.display(ApplicationUi.getAppWinFrame(), var1x, var1);
         }
      });
   }

   private static BorderLayoutBuilder createLayout(String var0) {
      KongaTextArea var1 = KongaTextArea.viewer(var0, Size.rows(10), Size.columns(70));
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(10, 0);
      JLabel var3 = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
      var3.setVerticalAlignment(1);
      var2.west(var3).center(var1);
      return var2;
   }

   private WarningsDisplayer() {
   }
}
