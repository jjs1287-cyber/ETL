package org.etlforge.ui.widget.overlay;

import com.google.common.base.Supplier;
import java.awt.Dimension;
import org.etlforge.ui.wait.WaitIndicator;
import org.etlforge.ui.widget.CircleProgressBar;

public final class DefaultWaitIndicatorFactory implements Supplier<WaitIndicator> {
   private Dimension prefSize = new Dimension(75, 75);

   public void setCircleSize(Dimension var1) {
      this.prefSize = new Dimension(var1);
   }

   public CircleProgressBar get() {
      CircleProgressBar var1 = new CircleProgressBar();
      var1.setPreferredSize(this.prefSize);
      return var1;
   }
}
