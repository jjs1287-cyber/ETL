package com.etlforge.integration.license.client.ui;

import com.google.common.collect.Lists;
import com.etlforge.integration.license.LicenseInfo;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.util.clipboard.ClipboardUtils;

abstract class LicenseMessage implements UiProvider {
   private final LicenseInfo info;
   private final String title;
   private final Icon icon;
   private RequestNewLicenseMessage requestNewLicenseBox;
   private LicenseDetailsViewer detailsViewer;
   private Action preferencesAction;
   private Action closeAction;

   protected LicenseMessage(LicenseInfo var1, String var2, Icon var3) {
      this.info = var1;
      this.title = var2;
      this.icon = var3;
   }

   public final String getTitle() {
      return this.title;
   }

   protected final LicenseInfo getInfo() {
      return this.info;
   }

   public void setPreferencesAction(Action var1) {
      this.preferencesAction = var1;
   }

   public void setCloseAction(Action var1) {
      this.closeAction = var1;
   }

   protected abstract String getMessage();

   public final void setRequestNewLicenseAction(Action var1) {
      this.requestNewLicenseBox = new RequestNewLicenseMessage();
      this.requestNewLicenseBox.setRequestLicenseAction(var1);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(10, 0);
      var2.west(this.layoutIcon()).center(this.layoutMessage());
      var2.decorate(var1);
   }

   private Object layoutMessage() {
      OneColumnPanel var1 = this.getEmptyMessageCanvas();
      this.layoutMessage(var1);
      if (this.hasButtons()) {
         BoxBuilder var2 = BoxBuilder.horizontal();
         ArrayList var3 = Lists.newArrayList();
         if (this.preferencesAction != null) {
            var3.add(new KongaButton(this.preferencesAction));
         }

         if (!var3.isEmpty()) {
            var2.addAllWithSpace(10, var3);
         }

         var2.glue();
         if (this.closeAction != null) {
            var2.add(this.closeAction);
         }

         var1.add(var2);
      }

      return var1;
   }

   private boolean hasButtons() {
      return this.preferencesAction != null || this.closeAction != null;
   }

   private OneColumnPanel getEmptyMessageCanvas() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 0, 10, 0);
      var1.setWeightX(1.0);
      var1.add(TextStyles.BigAdminHintBold.makeLabel(this.title));
      return var1;
   }

   protected abstract void layoutMessage(OneColumnPanel var1);

   private JLabel layoutIcon() {
      JLabel var1 = new JLabel(this.icon);
      var1.setVerticalAlignment(1);
      var1.setVerticalTextPosition(3);
      return var1;
   }

   protected final void layoutLicenseDetails(UiCanvas var1) {
      this.detailsViewer = new LicenseDetailsViewer(this.info);
      this.detailsViewer.decorate(var1);
   }

   protected final void layoutRequestNewLicenseBox(UiCanvas var1) {
      if (this.requestNewLicenseBox != null) {
         this.requestNewLicenseBox.decorate(var1);
      }
   }

   @Override
   public final void requestFocus() {
   }

   public final void copyInformationToClipboard() {
      String var1 = this.getTextToCopy();
      ClipboardUtils.copyToSystemClipboard(var1);
   }

   private String getTextToCopy() {
      StringBuilder var1 = new StringBuilder(this.title);
      var1.append(SystemUtils.LINE_SEPARATOR).append(this.getMessage()).append(SystemUtils.LINE_SEPARATOR);
      this.copyDetailsAndRequestNewLicenseText(var1);
      return var1.toString();
   }

   private void copyDetailsAndRequestNewLicenseText(StringBuilder var1) {
      if (this.detailsViewer != null) {
         var1.append(this.detailsViewer.getTextToCopy()).append(SystemUtils.LINE_SEPARATOR);
      }

      if (this.requestNewLicenseBox != null) {
         var1.append(this.requestNewLicenseBox.getTextToCopy()).append(SystemUtils.LINE_SEPARATOR);
      }
   }
}
