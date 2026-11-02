package org.etlforge.integration.client.ui.interchange.entity.webservice.xsd;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.button.KongaButton;

final class ConflictsDialog {
   private final SoapSchemaController controller;
   private KongaDialog dialog;

   public ConflictsDialog(SoapSchemaController var1) {
      this.controller = var1;
   }

   public void open(JTextField var1) {
      this.dialog = new KongaDialog(SwingUtilities.getWindowAncestor(var1), "File Conflict");
      Grid var2 = this.createText();
      KongaButton var3 = new KongaButton(new ConflictsDialog.RenameAction(var1));
      KongaButton var4 = new KongaButton(new ConflictsDialog.OverwriteAction());
      BorderLayoutBuilder var5 = new BorderLayoutBuilder(10, 20);
      BoxBuilder var6 = BoxBuilder.horizontal().glue().addAllWithSpace(10, var3, var4);
      JLabel var7 = new JLabel(AlertIcons.WARNING);
      var7.setVerticalAlignment(1);
      var5.center(var2).west(var7).south(var6);
      this.dialog.standardLayout(var5, null);
      this.dialog.getRootPane().setDefaultButton(var3);
      this.dialog.setResizable(false);
      this.dialog.setLocationRelativeTo(null);
      this.dialog.setVisible(true);
   }

   private Grid createText() {
      Grid var1 = Grid.oneColumn(2);
      var1.addAll(
         "The server already contains XSD files with the same name.",
         "You can overwrite the existing files, or try another name.",
         "Overwriting the existing files will affect Transformations that use those files.",
         " ",
         "What do you want to do?"
      );
      return var1;
   }

   private class OverwriteAction extends AbstractAction {
      public OverwriteAction() {
         super("&Overwrite existing files");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ConflictsDialog.this.dialog.dispose();
               ConflictsDialog.this.controller.generateWithForce();
            }
         });
      }
   }

   private class RenameAction extends AbstractAction {
      private final JTextField nameField;

      public RenameAction(JTextField var2) {
         super("&Use a different name");
         this.nameField = var2;
         this.putValue("ShortDescription", "Return to the prevoius screen and enter a new name.");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ConflictsDialog.this.dialog.dispose();
               FocusUtils.changeFocusAcrossWindows(RenameAction.this.nameField, 125);
               RenameAction.this.nameField.selectAll();
            }
         });
      }
   }
}
