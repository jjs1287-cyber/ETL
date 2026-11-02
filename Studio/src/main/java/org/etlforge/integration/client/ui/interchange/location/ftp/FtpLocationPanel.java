package org.etlforge.integration.client.ui.interchange.location.ftp;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ranges;
import java.awt.EventQueue;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.DefaultPortDefiner;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.datalocation.cache.FtpLocationCache;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.panel.FileLocationPanel;
import org.etlforge.integration.client.ui.interchange.util.InterchangeViewUtils;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.IntegerRangeFilter;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.net.Port;

public final class FtpLocationPanel extends DataLocationPanel {
   private final HostField hostField;
   private final DefaultPortDefiner portField;
   private final HostPathField hostPathField;
   private final CheckBoxInputField passiveModeOption;
   private final CheckBoxInputField renameOption;
   private final CheckBoxInputField nlstOption;
   private final FtpTransferTypePanel transferTypePanel;
   private final FileLocationPanel fileLocationPanel;
   private final FtpSecurityModePanel securityModePanel;
   private final IntegerInputField transferTimeout;
   private FtpLocationCache cache;
   private final SftpKeyPanel sftpKeys=new SftpKeyPanel();

   public FtpLocationPanel(FtpLocation var1, SourceTarget var2) {
      super(var1, var2);
      byte var3 = 20;
      this.hostField = new HostField(this, var3);
      this.portField = new DefaultPortDefiner();
      this.portField.registerAsChangeSource(this);
      this.hostPathField = new HostPathField(this, var2, var3);
      this.transferTypePanel = new FtpTransferTypePanel(var1.getTransferType());
      this.renameOption = this.createRenameCheck();
      this.passiveModeOption = this.createPassiveModeField();
      this.nlstOption = this.createNLSTCheck();
      this.fileLocationPanel = new FileLocationPanel(var1, var2, true, this, var3);
      this.securityModePanel = this.createSecurityModePanel(var1);
      this.transferTimeout = this.createTransferTimeout();
      this.updateFields(var1);
      this.addInputComponents();
   }

   public FtpLocation getDisplayedObject() {
      return (FtpLocation)super.getDisplayedObject();
   }

   public void setDataCache(FtpLocationCache var1) {
      this.cache = Preconditions.checkNotNull(var1);
      this.fileLocationPanel.setLoginNameCache(var1);
      this.hostField.installAutoCompletion(new CacheBasedHostHistory(var1));
      this.hostPathField.installAutoCompletion(new CacheBasedHostPathHistory(var1));
   }

   @Override
   public void display() {
      this.layoutComponents();
      if(this.securityModePanel.isSFTP())this.sftpKeys.reload();
      super.display();
   }

   @Override
   public void setInitialFocus() {
      this.hostField.requestFocus();
   }

   @Override
   public void apply() {
      FtpLocation var1 = this.getDisplayedObject();
      this.hostField.applyTo(var1);
      this.hostPathField.applyTo(var1);
      var1.setPort(this.getPort());
      var1.setPassiveMode(this.passiveModeOption.isSelected());
      this.securityModePanel.apply(var1);
      this.sftpKeys.apply(var1);
      this.transferTypePanel.apply(var1);
      this.fileLocationPanel.apply(var1);
      var1.setTransferTimeout(this.transferTimeout.getIntValue());
      if (this.getSourceTarget() == SourceTarget.Target) {
         var1.setFtpRenameEnabled(this.renameOption.isEnabled() && this.renameOption.isSelected());
      }

      if (this.getSourceTarget() == SourceTarget.Source) {
         var1.setNLSTEnabled(this.nlstOption.isEnabled() && this.nlstOption.isSelected());
      }

      this.updateCache(var1);
   }

   @Override
   public void reset() {
      FtpLocation var1 = this.getDisplayedObject();
      this.updateFields(var1);
      this.sftpKeys.reset(var1);
      this.fileLocationPanel.reset(var1);
      this.securityModePanel.reset(var1);
      this.transferTypePanel.reset(var1);
      this.checkSftpOption();
   }

   @Override
   public void dispose() {
      this.hostField.dispose();
      this.hostPathField.dispose();
      super.dispose();
   }

   private void updateCache(FtpLocation var1) {
      if (this.cache != null) {
         this.cache.update(var1);
      }
   }

   private Port getPort() {
      return this.portField.getPort();
   }

   private CheckBoxInputField createPassiveModeField() {
      String var1 = Strings.get("Ftp.PassiveMode");
      return InputFieldFactory.getCheckBoxInputField(var1, var1);
   }

   private CheckBoxInputField createRenameCheck() {
      String var1 = Strings.get("Ftp.FtpRename");
      return InputFieldFactory.getCheckBoxInputField(var1, var1);
   }

   private CheckBoxInputField createNLSTCheck() {
      return InputFieldFactory.getCheckBoxInputField(Strings.get("Ftp.NLST.Name"), Strings.get("Ftp.NLST.Label"));
   }

   private FtpSecurityModePanel createSecurityModePanel(FtpLocation var1) {
      FtpSecurityModePanel var2 = new FtpSecurityModePanel(var1);
      var2.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  FtpLocationPanel.this.checkSftpOption();
               }
            });
         }
      });
      return var2;
   }

   private IntegerInputField createTransferTimeout() {
      String var1 = "TransferTimeout";
      KongaTextField var2 = new KongaTextField(6);
      IntegerRangeFilter var3 = new IntegerRangeFilter(Ranges.closed(1, 86400));
      ((AbstractDocument)var2.getDocument()).setDocumentFilter(var3);
      IntegerInputField var4 = new IntegerInputField(var1, var2, 0, 86400, new JLabel("Transfer Timeout:"));
      var2.setToolTipText("If an FTP transfer takes longer than this amount (in seconds), then the connection will be closed");
      return var4;
   }

   private void checkSftpOption() {
      boolean var1 = !this.securityModePanel.isSFTP();
      this.sftpKeys.setSftpVisible(!var1);
      this.renameOption.setEnabled(var1);
      this.nlstOption.setEnabled(var1);
   }

   private void addInputComponents() {
      this.addInputField(this.passiveModeOption);
      this.addInputField(this.renameOption);
      this.addInputField(this.nlstOption);
      this.addInputPanel(this.transferTypePanel);
      this.addInputPanel(this.securityModePanel);
      this.addInputPanel(this.sftpKeys);
      this.addInputField(this.transferTimeout);
   }

   private void layoutComponents() {
      FtpLocationPanel.Layout var1 = this.getSourceTarget() == SourceTarget.Source ? new FtpLocationPanel.SourceLayout() : new FtpLocationPanel.TargetLayout();
      var1.doLayout();
   }

   private static JComponent getInputFieldCapsule(InputField var0) {
      JComponent var1 = InputFieldFactory.getCapsule(var0);
      return InvisiblePanel.of(var1);
   }

   private void updateFields(FtpLocation var1) {
      this.sftpKeys.reset(var1);
      this.hostField.reset(var1);
      this.portField.setPort(var1.getPort());
      this.hostPathField.reset(var1);
      this.passiveModeOption.setSelected(var1.getPassiveMode());
      this.transferTimeout.setValue(var1.getTransferTimeout());
      this.nlstOption.setSelected(var1.isNLSTEnabled());
      if (this.getSourceTarget() == SourceTarget.Target) {
         this.renameOption.setSelected(var1.isFtpRenameEnabled());
      }

      this.securityModePanel.reset(var1);
      this.checkSftpOption();
   }

   private abstract class Layout {
      protected final DataLocationPanelLayout top = new DataLocationPanelLayout();

      private Layout() {
      }

      public final void doLayout() {
         FtpLocationPanel.this.hostField.addToLayout(this.top);
         this.top.addInMiddle(FtpLocationPanel.this.portField);
         FtpLocationPanel.this.hostPathField.addToLayout(this.top);
         FtpLocationPanel.this.fileLocationPanel.layoutLoginControl(this.top);
         this.addAdditionalTopControls(this.top);
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
         var1.north(InvisiblePanel.of(this.top));
         var1.center(this.layoutOptionalFields());
         FtpLocationPanel.this.wrap(var1);
      }

      protected abstract void addAdditionalTopControls(DataLocationPanelLayout var1);

      protected abstract UiProvider getOptionalControls();

      protected final JComponent layoutOptionalFields() {
         UiProvider var1 = this.getOptionalControls();
         JComponent var2 = new BorderLayoutBuilder().north(var1).container();
         var2.setBorder(Empty.border(0, 15, 0, 0));
         InterchangeViewUtils.configurePropertyFields(var2);
         DropDownPanel var3 = new DropDownPanel(var2, "&Options", false);
         return var3.getDisplayer();
      }

      protected final UiProvider layoutTransferTypeAndSecurityOptions() {
         BoxBuilder var1 = BoxBuilder.horizontal();
         var1.topAligned();
         var1.addAllWithSpace(50, this.createTransferOptionsLayout(), new BorderLayoutBuilder(0,8).north(FtpLocationPanel.this.securityModePanel.getUi()).center(FtpLocationPanel.this.sftpKeys.getUi()).container());
         return var1;
      }

      private JComponent createTransferOptionsLayout() {
         OneColumnPanel var1 = new OneColumnPanel();
         var1.withInsets(0, 0, 15, 0);
         var1.add(FtpLocationPanel.this.transferTypePanel.getUi());
         var1.withInsets(0, 0, 10, 0);
         var1.add(TextStyles.SmallBoldCaption.makeLabel("Settings:"));
         var1.withInsets(0, 4, 10, 0);
         var1.add(FtpLocationPanel.this.passiveModeOption.getInputComponent());
         var1.add(FtpLocationPanel.this.fileLocationPanel.skipEmptyTargets);
         var1.add(FtpLocationPanel.this.fileLocationPanel.autoCreateDirectories);
         TwoColumnPanel var2 = FtpLocationPanel.this.fileLocationPanel.getCharacterEncodingPanel();
         var2.addRow(FtpLocationPanel.this.transferTimeout.getLabel(), FtpLocationPanel.this.transferTimeout.getInputComponent());
         var1.add(var2);
         JPanel var3 = InvisiblePanel.newPanel();
         var1.addToParent(var3, null);
         return var3;
      }
   }

   private class SourceLayout extends FtpLocationPanel.Layout {
      private SourceLayout() {
      }

      @Override
      protected void addAdditionalTopControls(DataLocationPanelLayout var1) {
         FtpLocationPanel.this.fileLocationPanel.layoutSourceFileFilterControls(var1);
      }

      @Override
      protected UiProvider getOptionalControls() {
         OneColumnPanel var1 = new OneColumnPanel()
            .withInsets(0, 0, 10, 0)
            .setWeightX(1.0)
            .of(
               InputFieldFactory.getCapsule(FtpLocationPanel.this.nlstOption),
               FtpLocationPanel.this.fileLocationPanel.getAfterProcessingLayout(),
               FtpLocationPanel.this.fileLocationPanel.getIgnoreLineFieldsForLayout()
            );
         UiProvider var2 = this.layoutTransferTypeAndSecurityOptions();
         return new BorderLayoutBuilder(0, 10).north(var1).center(var2);
      }
   }

   private class TargetLayout extends FtpLocationPanel.Layout {
      private TargetLayout() {
      }

      @Override
      protected void addAdditionalTopControls(DataLocationPanelLayout var1) {
         InputField var2 = FtpLocationPanel.this.fileLocationPanel.getFileFilterFieldAsInputFieldForLayout();
         var1.add(var2.getLabel(), var2.getInputComponent(), FtpLocationPanel.getInputFieldCapsule(FtpLocationPanel.this.renameOption));
      }

      @Override
      protected UiProvider getOptionalControls() {
         return this.layoutTransferTypeAndSecurityOptions();
      }
   }
}
