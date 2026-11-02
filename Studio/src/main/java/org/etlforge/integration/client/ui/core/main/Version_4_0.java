package org.etlforge.integration.client.ui.core.main;

import org.etlforge.integration.client.ui.core.window.VersionBanner;

public final class Version_4_0 implements VersionBanner {
   @Override
   public String getCodeName() {
      return "Pynchon";
   }

   @Override
   public String getToolTip() {
      return "Danger's over, Banana Breakfast is saved.";
   }

   @Override
   public String getImageName() {
      return "Version4.0.png";
   }
}
