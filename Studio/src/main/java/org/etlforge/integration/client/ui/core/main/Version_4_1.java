package org.etlforge.integration.client.ui.core.main;

import org.etlforge.integration.client.ui.core.window.VersionBanner;

public final class Version_4_1 implements VersionBanner {
   @Override
   public String getCodeName() {
      return "Chocolate";
   }

   @Override
   public String getToolTip() {
      return "<html>\"All you need is love. But a little chocolate now and then doesn't hurt.\"<br>- Charles M. Schulz</html>";
   }

   @Override
   public String getImageName() {
      return null;
   }
}
