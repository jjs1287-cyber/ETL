package org.etlforge.application.ui.search.string;

import com.google.common.base.Predicate;
import java.awt.EventQueue;
import javax.swing.UIManager;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.QuickFrame;

public final class StringFilterFieldTest implements Runnable {
   @Override
   public void run() {
      StringFilterField var1 = new StringFilterField(StringSearchRule.CONTAINS_IGNORE_CASE, StringSearchRule.STARTS_WITH_IGNORE_CASE);
      var1.setLabel("&Filter");
      var1.setCallback(new Receiver<Predicate<String>>() {
         public void handle(Predicate<String> var1) {
            boolean var2 = var1.apply("nabokov");
            System.out.println("Mach: " + var2);
         }
      });
      QuickFrame.show(var1, null);
   }

   public static void main(String[] var0) throws Exception {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      EventQueue.invokeLater(new StringFilterFieldTest());
   }
}
