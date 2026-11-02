package org.etlforge.integration.client.ui.operation.options;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

public final class ChunkNodeSelectorDialog {
   public static CROM selectNode(JFrame var0, Transformation var1, SourceTarget var2, String var3) {
      ChunkNodeSelector var4 = new ChunkNodeSelector(var3);
      KongaDialog var5 = createDialog(var0, var4, var1, var2);
      var5.setVisible(true);
      return var5.wasOkPressed() ? var4.getSelectedNode() : null;
   }

   private static KongaDialog createDialog(JFrame var0, final ChunkNodeSelector var1, final Transformation var2, final SourceTarget var3) {
      final KongaDialog var4 = new KongaDialog(var0, "Select the Chunk Node", true);
      var4.standardLayout(var1, KongaDialog.OK_CANCEL);
      var4.manageLocation(ChunkNodeSelectorDialog.class);
      var4.addWindowListener(new WindowAdapter() {
         @Override
         public void windowOpened(WindowEvent var1x) {
            var1.createStructures(var2, var3);
         }
      });
      var1.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1x) {
            ChunkNodeSelectorDialog.updateOkButton(var1, var4);
         }
      });
      var1.setDefaultAction(new AbstractSelectionAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            var4.getOkActionListener().actionPerformed(var1);
         }
      });
      updateOkButton(var1, var4);
      var4.setMinimumSize(new Dimension(400, 300));
      var4.setOkButtonAsDefault();
      return var4;
   }

   private static void updateOkButton(ChunkNodeSelector var0, KongaDialog var1) {
      var1.getOKButton().setEnabled(var0.getSelectedNode() != null);
   }

   private ChunkNodeSelectorDialog() {
   }
}
