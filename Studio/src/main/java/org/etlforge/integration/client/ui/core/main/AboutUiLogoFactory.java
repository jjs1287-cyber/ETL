package org.etlforge.integration.client.ui.core.main;

import com.google.common.base.Supplier;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.SplashScreenUi;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class AboutUiLogoFactory implements Supplier<JComponent> {
   public JComponent get() {
      SplashScreenUi var1 = new SplashScreenUi();
      var1.setDisplayVersion(false);
      var1.setUseBorder(false);
      var1.setUseReflectedLogo(true);
      var1.installMagic();
      JPanel var2 = InvisiblePanel.of(var1);
      var2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
      return var2;
   }
}
