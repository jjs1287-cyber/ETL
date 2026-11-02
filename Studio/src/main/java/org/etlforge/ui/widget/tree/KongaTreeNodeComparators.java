package org.etlforge.ui.widget.tree;

import java.util.Comparator;
import org.etlforge.lang.KongaStringUtils;

public final class KongaTreeNodeComparators {
   public static <T extends KongaTreeNode> Comparator<T> byDisplayName() {
      return new Comparator<T>() {
         public int compare(T var1, T var2) {
            return KongaStringUtils.compare(var1.toString().toLowerCase(), var2.toString().toLowerCase());
         }
      };
   }

   private KongaTreeNodeComparators() {
   }
}
