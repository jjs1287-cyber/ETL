package org.etlforge.integration.client.ui.entity.testhistory;

import java.awt.Color;
import javax.swing.Icon;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.collections.Pair;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.ui.widget.label.KongaLabel;

public final class OutcomeDecorator {
   public static Pair<Icon, Color> getIconAndTextColor(TestResult.Outcome var0) {
      switch (var0) {
         case SUCCESS:
            return Pair.of(CommonIcons.SUCCESS_16, Color.BLACK);
         case ERROR:
            return Pair.of(CommonIcons.ERROR_16, Colors.ERROR_TEXT_COLOR);
         case PARTIAL:
            return Pair.of(CommonIcons.WARNING_16, Color.BLACK);
         default:
            throw new AssertionError();
      }
   }

   public static KongaLabel createLabel(TestResult.Outcome var0) {
      KongaLabel var1 = new KongaLabel(var0.toString());
      Pair var2 = getIconAndTextColor(var0);
      var1.setIcon((Icon)var2.first);
      var1.setForeground((Color)var2.second);
      return var1;
   }

   private OutcomeDecorator() {
   }
}
