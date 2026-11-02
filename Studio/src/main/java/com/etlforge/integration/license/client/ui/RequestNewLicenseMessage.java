package com.etlforge.integration.license.client.ui;

import java.awt.Color;
import javax.swing.Action;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class RequestNewLicenseMessage implements UiProvider {
   private Action requestLicenseAction;
   private final String caption = "To request a new license, open the Request License dialog, or send an email to:";

   public void setRequestLicenseAction(Action var1) {
      this.requestLicenseAction = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(0, 0, 10, 0).withBorder(StyleSheet.getDefaultTitleBorder("Request New License"));
      var2.setWeightX(1.0);
      var2.add("To request a new license, open the Request License dialog, or send an email to:");
      var2.add(TextStyle.newColor(TextStyles.DefaultText, Color.BLUE).makeLabel(LicenseRequestEmail.EMAIL));
      if (this.requestLicenseAction != null) {
         var2.add(InvisiblePanel.of(this.requestLicenseAction));
      }

      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   public String getTextToCopy() {
      return "To request a new license, open the Request License dialog, or send an email to:" + SystemUtils.LINE_SEPARATOR + LicenseRequestEmail.EMAIL;
   }
}
