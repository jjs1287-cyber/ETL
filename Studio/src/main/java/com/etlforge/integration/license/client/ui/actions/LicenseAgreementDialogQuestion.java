package com.etlforge.integration.license.client.ui.actions;

import com.google.common.base.Charsets;
import com.google.common.base.Supplier;
import com.google.common.io.Files;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.widget.DefaultFileChooserFactory;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.RtfViewer;

class LicenseAgreementDialogQuestion implements LicenseAgreementQuestion, Supplier<Boolean> {
   private volatile String agreement;
   private JRadioButton accept;
   private JRadioButton doNotAccept;
   private KongaDialog dialog;

   @Override
   public boolean doYouAgree(String var1) {
      this.agreement = var1;
      return UiThreadSupplier.call(this);
   }

   public Boolean get() {
      this.dialog = this.createDialog();
      this.dialog.pack();
      this.dialog.setLocationRelativeTo(null);
      this.dialog.setVisible(true);
      return this.dialog.wasOkPressed() ? this.accept.isSelected() : Boolean.FALSE;
   }

   private KongaDialog createDialog() {
      KongaDialog var1 = new KongaDialog(UiUtils.getActiveFrame(), "ETLForge License Agreement", true);
      BorderLayoutBuilder var2 = this.createDialogLayout(var1);
      var2.asContentPaneFor(var1);
      var1.getContinueButton().setEnabled(false);
      UiUtils.makeDefaultButton(var1.getContinueButton());
      return var1;
   }

   private BorderLayoutBuilder createDialogLayout(KongaDialog var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.center(this.createMessageLayout());
      JComponent var3 = this.createButtons(var1);
      var2.south(var3);
      var2.withBorder(Empty.border(10));
      return var2;
   }

   private UiProvider createMessageLayout() {
      this.createOptions();
      KongaLabel var1 = TextStyles.BigAdminHintBold.makeLabel("License Agreement");
      RtfViewer var2 = new RtfViewer(this.agreement);
      var2.setPreferredSize(new Dimension(600, 400));
      Grid var3 = Grid.oneColumn(5);
      var3.addAll(this.accept, this.doNotAccept);
      return Vertical.borderLayout(10).of(var1, var2, var3);
   }

   private void createOptions() {
      LicenseAgreementDialogQuestion.CheckListener var1 = new LicenseAgreementDialogQuestion.CheckListener();
      this.accept = new KongaRadioButton("I &accept the terms in the license agreement");
      this.doNotAccept = new KongaRadioButton("I &do not accept the terms in the license agreement");
      ButtonUtils.makeMutuallyExclusive(this.accept, this.doNotAccept);
      this.accept.addActionListener(var1);
      this.doNotAccept.addActionListener(var1);
   }

   private JComponent createButtons(KongaDialog var1) {
      KongaButton var2 = new KongaButton(new LicenseAgreementDialogQuestion.SaveToFileAction(var1));
      JButton var3 = var1.createContinueButton();
      JButton var4 = var1.createCancelButton();
      return BoxBuilder.horizontal().add(var2).glue().addAllWithSpace(10, var3, var4).container();
   }

   private class CheckListener implements ActionListener {
      private CheckListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         LicenseAgreementDialogQuestion.this.dialog
            .getContinueButton()
            .setEnabled(LicenseAgreementDialogQuestion.this.accept.isSelected() || LicenseAgreementDialogQuestion.this.doNotAccept.isSelected());
      }
   }

   private class SaveToFileAction extends AbstractAction {
      private final Component owner;

      public SaveToFileAction(Component var2) {
         super("&Save...", ApplicationResources.ICONS.getIcon("Save.16"));
         this.putValue("ShortDescription", "Save the license agreement to a file");
         this.owner = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SaveToFileAction.this.runAction();
            }
         });
      }

      private void runAction() {
         DefaultFileChooser var1 = new DefaultFileChooser(new DefaultFileChooserFactory());
         var1.setSelectedFile(new File(SystemUtils.USER_HOME, "ETLForge_License_Agreement.rtf"));
         File var2 = var1.getFileToSave(this.owner, new FileNameExtensionFilter("Rich Text Format", "rtf"), "Save License Agreement");
         if (var2 != null) {
            try {
               Files.write(LicenseAgreementDialogQuestion.this.agreement, var2, Charsets.UTF_8);
            } catch (IOException var4) {
               ErrorLog.attention(this.getClass(), "The license agreement could not be saved to file.", var4);
            }
         }
      }
   }
}
