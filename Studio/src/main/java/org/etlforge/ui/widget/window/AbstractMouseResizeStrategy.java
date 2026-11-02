package org.etlforge.ui.widget.window;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Window;

abstract class AbstractMouseResizeStrategy implements MouseResizeStrategy {
   private final Window window;
   private final Cursor cursor;

   protected AbstractMouseResizeStrategy(Window var1, MouseResizeHotSpot var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("w cannot be null.");
      }

      this.window = var1;
      this.cursor = var2.getCursor();
   }

   @Override
   public final void updateCursor() {
      this.window.setCursor(this.cursor);
   }

   protected final Rectangle getBounds() {
      return this.window.getBounds();
   }

   protected final void setBounds(int var1, int var2, int var3, int var4) {
      this.window.setBounds(var1, var2, var3, var4);
   }

   protected final void setSize(int var1, int var2) {
      this.window.setSize(var1, var2);
   }
}
