package org.etlforge.ui.widget.text.marker;

import com.google.common.base.Preconditions;
import java.awt.Color;

public final class MarkerColor {
   public final String name;
   public final Color color;

   public MarkerColor(String var1, Color var2) {
      Preconditions.checkNotNull(var1, "name");
      Preconditions.checkNotNull(var2, "color");
      this.name = var1;
      this.color = var2;
   }
}
