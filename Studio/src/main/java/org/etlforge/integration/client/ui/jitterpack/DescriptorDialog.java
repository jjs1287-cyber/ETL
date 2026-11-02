package org.etlforge.integration.client.ui.jitterpack;

import java.awt.EventQueue;
import java.io.File;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.ui.jitterpack.table.JitterPackTable;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class DescriptorDialog {
   private final ApplicationWindow window;
   private final JitterPack jp;

   private DescriptorDialog(ApplicationWindow var1, File var2, JitterPackDescriptor var3) {
      this.window = var1;
      this.jp = new JitterPack(var2, var3);
   }

   public static void show(ApplicationWindow var0, File var1, JitterPackDescriptor var2) {
      final DescriptorDialog var3 = new DescriptorDialog(var0, var1, var2);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            KongaDialog var1x = var3.createDialog();
            var1x.setVisible(true);
         }
      });
   }

   private KongaDialog createDialog() {
      return this.createDialog(this.window.getFrame(), this.getDescriptorUi());
   }

   private OneColumnPanel getDescriptorUi() {
      return new OneColumnPanel().nonOpaque().withInsets(5, 5, 10, 5).add(this.getTableUi(this.jp)).fillOut(this.getCommentUi(this.jp));
   }

   private JComponent getTableUi(JitterPack var1) {
      JitterPackTable var2 = new JitterPackTable(Collections.singletonList(var1), true);
      var2.setNumberOfVisibleRows(2);
      var2.disableSelections();
      return InvisiblePanel.of(var2);
   }

   private UiProvider getCommentUi(JitterPack var1) {
      KongaTextArea var2 = KongaTextArea.viewer(var1.getDescriptor().getComment(), Size.rows(8), Size.columns(40));
      var2.setLabel(Strings.get("DescriptorDialog.Comment"));
      return var2.asUiProvider();
   }

   private KongaDialog createDialog(JFrame var1, OneColumnPanel var2) {
      KongaDialog var3 = new KongaDialog(var1, Strings.getJitterPackString("DescriptorDialog.Title"), true);
      this.createContentPane(var3, var2);
      var3.manageLocation();
      return var3;
   }

   private void createContentPane(KongaDialog var1, OneColumnPanel var2) {
      var1.standardLayout(var2, KongaDialog.OK_ONLY);
      var1.setOkButtonAsDefault();
   }
}

