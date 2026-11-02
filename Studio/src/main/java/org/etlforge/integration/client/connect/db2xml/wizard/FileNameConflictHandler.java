package org.etlforge.integration.client.connect.db2xml.wizard;

import com.google.common.base.Supplier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.property.ComponentEnabledBinding;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;

final class FileNameConflictHandler {
   private final XsdGeneratorModel model;
   private final String originalFileName;

   public FileNameConflictHandler(XsdGeneratorModel var1) {
      this.originalFileName = var1.getFileName();
      this.model = var1;
   }

   public FileNameConflictHandler.Resolution resolveConflict() {
      return UiThreadSupplier.call(new Supplier<FileNameConflictHandler.Resolution>() {
         public FileNameConflictHandler.Resolution get() {
            FileNameConflictHandler.Ui var1 = FileNameConflictHandler.this.new Ui();
            KongaDialog var2 = FileNameConflictHandler.this.createDialog(var1);
            var2.setLocationRelativeTo(null);
            var2.setVisible(true);
            if (var2.wasOkPressed()) {
               if (var1.isOverwriteSelected()) {
                  FileNameConflictHandler.this.model.setFileName(FileNameConflictHandler.this.originalFileName);
                  return FileNameConflictHandler.Resolution.OVERWRITE;
               } else {
                  return FileNameConflictHandler.Resolution.NEW_NAME;
               }
            } else {
               return FileNameConflictHandler.Resolution.CANCEL;
            }
         }
      });
   }

   private KongaDialog createDialog(UiProvider var1) {
      KongaDialog var2 = new KongaDialog(UiUtils.getActiveWindow(), "File name conflict");
      var2.standardLayout(var1, KongaDialog.CONTINUE_CANCEL);
      var2.setContinueButtonAsDefault();
      var2.setFocusedComponent(var1);
      ComponentEnabledBinding var3 = new ComponentEnabledBinding(this.model, "validSettings", var2.getContinueButton());
      var3.syncUi();
      return var2;
   }

   public enum Resolution {
      CANCEL,
      NEW_NAME,
      OVERWRITE;
   }

   private class Ui implements UiProvider {
      private final JRadioButton renameOption = new KongaRadioButton("&Rename the file", true);
      private final JRadioButton overwriteOption = new KongaRadioButton("&Overwrite the existing file");
      private final FileNameField fileNameField = new FileNameField(Size.columns(30));

      public Ui() {
         ButtonUtils.makeMutuallyExclusive(this.renameOption, this.overwriteOption);
         TextBinding var2 = TextBinding.bind(FileNameConflictHandler.this.model, "filename", this.fileNameField.getInputComponent());
         var2.syncUi();
         this.renameOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               Ui.this.fileNameField.requestFocus();
            }
         });
      }

      public boolean isOverwriteSelected() {
         return this.overwriteOption.isSelected();
      }

      @Override
      public void decorate(UiCanvas var1) {
         OneColumnPanel var2 = new OneColumnPanel();
         var2.setWeightX(1.0);
         var2.withInsets(0, 0, 10, 0);
         JLabel var3 = new JLabel("An XSD file with this name already exists on the server. What do you want to do?");
         var3.setIcon(CommonIcons.WARNING_16);
         var2.add(var3);
         var2.add(this.renameOption);
         var2.withInsets(0, 25, 10, 0);
         var2.add(this.fileNameField.getInputComponent());
         var2.withInsets(0, 0, 10, 0);
         var2.add(this.overwriteOption);
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.fileNameField.requestFocus();
      }
   }
}
