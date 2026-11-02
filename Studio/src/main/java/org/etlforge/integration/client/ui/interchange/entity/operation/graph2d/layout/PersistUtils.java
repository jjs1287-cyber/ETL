package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import java.awt.Rectangle;
import org.etlforge.ui.graphics.RectangleUtils;
import org.etlforge.util.persist.Persistor;

final class PersistUtils {
   static void writeBounds(Persistor var0, Rectangle var1) {
      var0.putString("bounds", RectangleUtils.toString(var1));
   }

   static Rectangle readBounds(Persistor var0) {
      return RectangleUtils.fromString(var0.getString("bounds"));
   }

   private PersistUtils() {
   }
}
