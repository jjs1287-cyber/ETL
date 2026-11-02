package org.etlforge.ui.widget.scroll;

import java.io.Serializable;
import javax.swing.JScrollPane;

public final class ScrollPosition implements Serializable {
   public static final ScrollPosition TOP_LEFT = new ScrollPosition(0, 0);
   private final int horizontal;
   private final int vertical;

   public ScrollPosition(int var1, int var2) {
      this.horizontal = var1;
      this.vertical = var2;
   }

   public ScrollPosition(JScrollPane var1) {
      this(var1.getHorizontalScrollBar().getValue(), var1.getVerticalScrollBar().getValue());
   }

   public void restore(JScrollPane var1) {
      var1.getHorizontalScrollBar().setValue(this.horizontal);
      var1.getVerticalScrollBar().setValue(this.vertical);
   }

   public int getHorizontal() {
      return this.horizontal;
   }

   public int getVertical() {
      return this.vertical;
   }
}
