package com.etlforge.integration.license.client.ui;

import com.google.common.collect.Lists;
import com.etlforge.integration.license.LicenseInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JLabel;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.label.KongaLabel;

final class LicenseDetailsViewer implements UiProvider {
   private static final String TITLE = "License Details";
   private static final String SERVER_LABEL = "ETLForge Server: ";
   private static final String EXPIRATION_LABEL = "Expiration Date (UTC): ";
   private static final String NO_EXPIRATION_DATE = "[This license never expires]";
   private static final String PASSED_EXPIRATION_DATE = "* Expired *";
   private final LicenseInfo info;
   private final List<String[]> options;

   public LicenseDetailsViewer(LicenseInfo var1) {
      this.info = var1;
      this.options = this.createOptions();
   }

   private List<String[]> createOptions() {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.info.getOptions().entrySet()) {
         String[] var4 = this.getOptionKeyValue(var3);
         var1.add(var4);
      }

      return var1;
   }

   private String[] getOptionKeyValue(Entry<String, String> var1) {
      String var2 = StringUtils.capitalize((String)var1.getKey());
      String var3 = (String)var1.getValue();
      if ("true".equalsIgnoreCase(var3)) {
         var3 = "Yes";
      } else if ("false".equalsIgnoreCase(var3)) {
         var3 = "No";
      }

      return new String[]{var2, var3};
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = this.createEmptyGrid();
      this.layoutServer(var2);
      this.layoutExpirationDate(var2);
      this.layoutOptions(var2);
      var2.decorate(var1);
   }

   private TwoColumnPanel createEmptyGrid() {
      return new TwoColumnPanel()
         .withLeftInsets(0, 0, 10, 5)
         .withRightInsets(0, 0, 10, 0)
         .withRightWeights(1.0, 0.0)
         .withBorder(StyleSheet.getDefaultTitleBorder("License Details"));
   }

   private void layoutServer(TwoColumnPanel var1) {
      var1.addRow(new JLabel("ETLForge Server: "), new JLabel(this.info.getServerInfo().getName()));
   }

   private void layoutExpirationDate(TwoColumnPanel var1) {
      JLabel var2 = new JLabel("Expiration Date (UTC): ");
      JLabel var3 = new JLabel(this.getExpirationDate());
      if (var3.getText().equals("[This license never expires]")) {
         var2.setEnabled(false);
         var3.setEnabled(false);
      }

      var1.addRow(var2, var3);
   }

   private String getExpirationDate() {
      if (this.info.isPerpetual()) {
         return "[This license never expires]";
      }

      String var1 = this.info.getExpirationDate();
      if (StringUtils.isEmpty(var1)) {
         assert this.info.isTrial() && this.info.hasExpired() : "An expiration date must be provided unless the license is perpetual or an expired trial.";
         var1 = "* Expired *";
      }

      return var1;
   }

   private void layoutOptions(TwoColumnPanel var1) {
      for (String[] var3 : this.options) {
         var1.addRow(new KongaLabel(var3[0] + ":"), new KongaLabel(var3[1]));
      }
   }

   @Override
   public void requestFocus() {
   }

   public String getTextToCopy() {
      StringBuilder var1 = new StringBuilder();
      var1.append("License Details").append(":").append(SystemUtils.LINE_SEPARATOR);
      var1.append("ETLForge Server: ").append(this.info.getServerInfo().getName()).append(SystemUtils.LINE_SEPARATOR);
      var1.append("Expiration Date (UTC): ").append(this.getExpirationDate());

      for (String[] var3 : this.options) {
         var1.append(SystemUtils.LINE_SEPARATOR).append(var3[0]).append(": ").append(var3[1]);
      }

      return var1.toString();
   }
}
