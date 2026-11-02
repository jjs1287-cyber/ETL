package org.etlforge.integration.client.ui.core.main;

import java.awt.Color;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.Application;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.ui.widget.text.KongaTextField;

class SessionDetailsUi implements UiProvider {
   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = new TwoColumnPanel();
      var2.withInsets(0, 0, 10, 5);
      var2.withRightFill(TwoColumnPanel.BOTH);
      var2.withRightWeights(1.0, 0.0);
      this.addStartupTime(var2);
      this.addApplicationDirectory(var2);
      var2.withAnchor(TwoColumnPanel.NORTHWEST);
      this.addJavaPlatform(var2);
      this.addSystemVariables(var2);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   private void addStartupTime(TwoColumnPanel var1) {
      JLabel var2 = new JLabel(PackageResources.SessionDetails.STARTUP);
      Date var3 = Application.getStartupTime();
      String var4 = DateFormat.getDateTimeInstance().format(var3);
      KongaTextField var5 = createValueField(var4);
      var1.addRow(var2, var5);
   }

   private void addApplicationDirectory(TwoColumnPanel var1) {
      JLabel var2 = new JLabel(PackageResources.SessionDetails.APPDIR);
      File var3 = Application.getApplicationDirectory().getLocation();
      KongaTextField var4 = createValueField(var3.getAbsolutePath());
      var1.addRow(var2, var4);
   }

   private static KongaTextField createValueField(String var0) {
      KongaTextField var1 = new KongaTextField(var0);
      var1.setEditable(false);
      var1.setBackground(javax.swing.UIManager.getColor("Panel.background"));
      return var1;
   }

   private void addJavaPlatform(TwoColumnPanel var1) {
      JLabel var2 = new JLabel(PackageResources.SessionDetails.PLATFORM);
      String var3 = SystemUtils.OS_NAME + "\nJava " + SystemUtils.JAVA_VERSION;
      JComponent var4 = createTextArea(var3, Size.rows(3));
      var1.addRow(var2, var4);
   }

   private void addSystemVariables(TwoColumnPanel var1) {
      JLabel var2 = new JLabel(PackageResources.SessionDetails.SYSTEM);
      String var3 = org.etlforge.system.SystemUtils.getSystemProperties("\n");
      JComponent var4 = createTextArea(var3, Size.rows(10));
      var1.withRightWeights(1.0, 1.0);
      var1.addRow(var2, var4);
   }

   private static JComponent createTextArea(String var0, Size.Rows var1) {
      KongaTextArea var2 = KongaTextArea.viewer(var0, var1, Size.columns(60));
      return new JScrollPane(var2);
   }
}

