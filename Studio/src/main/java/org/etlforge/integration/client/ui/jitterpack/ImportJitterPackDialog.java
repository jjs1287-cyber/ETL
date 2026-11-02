package org.etlforge.integration.client.ui.jitterpack;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.io.File;
import javax.swing.JButton;
import org.etlforge.application.ui.help.WindowHelpButton;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.ButtonOrder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.FileSelectionListener;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.KongaButton;

public final class ImportJitterPackDialog {
   private final ImportJitterPackUi ui;
   private final DoImportAction importAction;
   private KongaDialog dialog;

   public ImportJitterPackDialog(ImportJitterPackUi var1, ImporterFactory var2, UnpackCallback var3) {
      Preconditions.checkNotNull(var1, "ui");
      Preconditions.checkNotNull(var3, "callback");
      this.ui = var1;
      this.importAction = new DoImportAction(var1, var2, var3);
   }

   public void show() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ImportJitterPackDialog.this.openDialog();
         }
      });
   }

   private void openDialog() {
      if (this.dialog == null) {
         this.dialog = this.createDialog();
         this.importAction.setWaitService(this.dialog);
         this.listen();
      }

      this.dialog.setVisible(true);
   }

   private KongaDialog createDialog() {
      KongaScrollPane var1 = WizardStyleDialog.wrapInScroll(this.ui);
      WizardStyleDialog var2 = new WizardStyleDialog(UiUtils.getActiveWindow(), Strings.getJitterPackString("Import.Title"), var1, null, true);
      this.layoutDialogButtons(var2);
      JButton var3 = var2.getOKButton();
      var3.setAction(this.importAction);
      if (var3 instanceof KongaButton) {
         ((KongaButton)var3).setUseSmallActionIcon(true);
      }

      var2.pack();
      var2.manageLocation(ImportJitterPackDialog.class);
      var2.setOkButtonAsDefault();
      return var2;
   }

   private void layoutDialogButtons(WizardStyleDialog var1) {
      var1.setFixedButtonSize(false);
      JButton var2 = var1.createOKButton();
      JButton var3 = var1.createCancelButton();
      JButton var4 = new WindowHelpButton(var1, "Import_Jitterpak", true).getButton();
      ImmutableList var5 = ButtonOrder.okAndCancelOrder(var2, var3);
      BoxBuilder var6 = Horizontal.box().withEmptyBorder(10).nonOpaque().add(var4).glue().addAllWithSpace(10, var5);
      var1.getDefaultLayout().south(var6);
   }

   private void listen() {
      this.ui.setDoImportAction(this.importAction);
      this.ui.addJitterPackSelectionListener(new JitterPackSelectionListener() {
         @Override
         public void selectionChanged(JitterPack var1) {
            ImportJitterPackDialog.this.updateImportActionEnabledState();
         }
      });
      this.ui.addDestinationFolderListener(new FileSelectionListener() {
         @Override
         public void selectionChanged(File var1) {
            ImportJitterPackDialog.this.updateImportActionEnabledState();
         }
      });
      this.updateImportActionEnabledState();
   }

   private void updateImportActionEnabledState() {
      this.importAction.setEnabled(this.ui.isInputValid());
   }

   public void close() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (ImportJitterPackDialog.this.dialog != null) {
               ImportJitterPackDialog.this.dialog.removeFromLocationManager();
               ImportJitterPackDialog.this.dialog.setVisible(false);
            }
         }
      });
   }
}

