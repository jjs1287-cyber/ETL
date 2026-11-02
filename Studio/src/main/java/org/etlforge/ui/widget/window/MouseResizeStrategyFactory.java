package org.etlforge.ui.widget.window;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.Point;
import java.awt.Window;
import java.util.Map;

public final class MouseResizeStrategyFactory {
   private final Window window;
   private final Map<MouseResizeHotSpot, MouseResizeStrategy> cache;

   public MouseResizeStrategyFactory(Window var1) {
      Preconditions.checkNotNull(var1, "w");
      this.window = var1;
      this.cache = Maps.newHashMap();
   }

   public MouseResizeStrategy getStrategy(Point var1) {
      MouseResizeStrategy var2 = null;
      MouseResizeHotSpot var3 = MouseResizeHotSpot.getHotSpot(this.window, var1);
      if (var3 != null) {
         this.cache.get(var3);
         var2 = var3.createStrategy(this.window);
         this.cache.put(var3, var2);
      }

      return var2;
   }
}
