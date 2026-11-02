package com.etlforge.integration.license.client.ui.actions;

import com.etlforge.integration.license.client.ui.LicenseRequestEmail;
import java.awt.Color;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.CopyToClipboardButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextArea;

final class InstallationIdViewer implements UiProvider {
   private final KongaTextArea textArea;
   private final UiProvider copyToClipboardButton;

   public InstallationIdViewer(String var1) {
      this.textArea = KongaTextArea.viewer(var1, Size.rows(3), Size.columns(50));
      this.textArea.setSelectAllWhenFocused(true);
      this.copyToClipboardButton = new CopyToClipboardButton(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.north(this.layoutCaption());
      var2.center(this.textArea);
      var2.south(InvisiblePanel.of(this.copyToClipboardButton));
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.copyToClipboardButton.requestFocus();
   }

   private Object layoutCaption() {
      Grid var1 = Grid.oneColumn(10);
      var1.add(TextStyles.BigAdminHintBold.makeLabel("License Request Instructions"));
      var1.add("To request a commercial license, copy the text below and send it to:");
      var1.add(TextStyle.newColor(TextStyles.DefaultText, Color.BLUE).makeLabel(LicenseRequestEmail.EMAIL));
      return var1;
   }
}
