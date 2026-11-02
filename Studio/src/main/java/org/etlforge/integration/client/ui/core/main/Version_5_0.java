package org.etlforge.integration.client.ui.core.main;

import org.etlforge.integration.client.ui.core.window.VersionBanner;

public final class Version_5_0 implements VersionBanner {
   @Override
   public String getCodeName() {
      return "Coffee";
   }

   @Override
   public String getToolTip() {
      return "<html>\"Coffee, the finest organic suspension ever devised.\"<br>- Star Trek: Voyager</html>";
   }

   @Override
   public String getImageName() {
      return "Version5.0.png";
   }
}
