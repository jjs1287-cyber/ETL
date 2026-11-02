package org.etlforge.integration.client.ui.interchange.location.ftp;

import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.ftp.FtpTransferType;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.widget.label.KongaLabel;

final class FtpTransferTypePanel extends InputPanel {
   private final RadioButtonGroupPanel<FtpTransferType> buttons = RadioButtonGroupPanel.forEnum(FtpTransferType.class);

   public FtpTransferTypePanel(FtpTransferType var1) {
      this.buttons.setUndoRedoPresentationName(Strings.get("Ftp.TransferType.Edit"));
      this.buttons.setSelectedValue(var1);
      this.buttons.registerAsChangeSource(this);
      this.layoutUi();
   }

   private void layoutUi() {
      KongaLabel var1 = TextStyles.SmallBoldCaption.makeLabel(Strings.get("Ftp.TransferType"));
      UiProvider var2 = this.buttons.getUi(var1, RadioButtonGroupPanel.Layout.ONE_COLUMN);
      this.wrap(var2);
   }

   public void apply(FtpLocation var1) {
      var1.setTransferType(this.buttons.getSelection());
   }

   public void reset(FtpLocation var1) {
      this.buttons.setSelectedValue(var1.getTransferType());
   }
}
