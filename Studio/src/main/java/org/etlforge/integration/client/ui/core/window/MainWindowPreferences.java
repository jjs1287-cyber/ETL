package org.etlforge.integration.client.ui.core.window;

import java.awt.Dimension;
import java.awt.Point;
import org.etlforge.util.preference.KongaPreferences;

class MainWindowPreferences {
   private static final String MAXIMIZED_PREF_KEY = "Maximized";
   private static final String SIZE_PREF_KEY = "Size";
   private static final String LOCATION_PREF_KEY = "Location";
   private final KongaPreferences prefs = KongaPreferences.getPreferences(MainWindowFrame.class);

   Dimension getSize() {
      return this.prefs.getDimension("Size", new Dimension(1250, 750));
   }

   void setSize(Dimension var1) {
      this.prefs.putDimension("Size", var1);
   }

   Point getLocation() {
      return this.prefs.getPoint("Location", null);
   }

   void setLocation(Point var1) {
      this.prefs.putPoint("Location", var1);
   }

   boolean isMaximized() {
      return this.prefs.getBoolean("Maximized", false);
   }

   void setMaximized(boolean var1) {
      this.prefs.putBoolean("Maximized", var1);
   }
}
