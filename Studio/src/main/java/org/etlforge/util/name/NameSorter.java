package org.etlforge.util.name;

import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Comparator;
import org.etlforge.lang.KongaStringUtils;

public final class NameSorter<T extends Nameable> implements Comparator<T>, Serializable {
   private static final long serialVersionUID = -761455170163107428L;
   private final Comparator<String> order;

   public static <T extends Nameable> Comparator<T> ignoreCase() {
      return new NameSorter<>(true);
   }

   public static <N extends Nameable> Ordering<N> naturalOrdering() {
      return Ordering.from(new NameSorter<>(false));
   }

   public NameSorter(boolean var1) {
      this.order = var1 ? String.CASE_INSENSITIVE_ORDER : KongaStringUtils.NATURAL_ORDER;
   }

   public int compare(T var1, T var2) {
      return this.order.compare(getName(var1), getName(var2));
   }

   private static String getName(Nameable var0) {
      String var1 = var0.getName();
      return var1 != null ? var1 : "";
   }
}
