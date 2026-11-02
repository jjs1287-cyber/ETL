package org.etlforge.ui.widget.list;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.text.Position.Bias;

public class KongaListBase extends JList {
   private Function<Object, String> nextMatchFunction = new KongaListBase.DefaultNextMatchFunction();

   public KongaListBase() {
   }

   public KongaListBase(ListModel var1) {
      super(var1);
   }

   public KongaListBase(Object[] var1) {
      super(var1);
   }

   public KongaListBase(Vector<?> var1) {
      super(var1);
   }

   public void setNextMatchFunction(Function<Object, String> var1) {
      this.nextMatchFunction = Preconditions.checkNotNull(var1);
   }

   @Override
   public int getNextMatch(String var1, int var2, Bias var3) {
      ListModel var4 = this.getModel();
      int var5 = var4.getSize();
      if (var1 == null) {
         throw new IllegalArgumentException();
      }

      if (var2 >= 0 && var2 < var5) {
         var1 = var1.toUpperCase();
         int var6 = var3 == Bias.Forward ? 1 : -1;
         int var7 = var2;

         do {
            Object var8 = var4.getElementAt(var7);
            if (var8 != null) {
               String var9 = this.nextMatchFunction.apply(var8);
               if (var9 != null) {
                  var9 = var9.toUpperCase();
                  if (var9.startsWith(var1)) {
                     return var7;
                  }
               }
            }

            var7 = (var7 + var6 + var5) % var5;
         } while (var7 != var2);

         return -1;
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static class DefaultNextMatchFunction implements Function<Object, String> {
      private DefaultNextMatchFunction() {
      }

      public String apply(Object var1) {
         return var1 == null ? null : var1.toString();
      }
   }
}
