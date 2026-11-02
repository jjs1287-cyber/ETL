package org.etlforge.integration.client.server;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public final class LatestLoginFirst implements Comparator<KnownServer>, Serializable {
   private final Comparator<KnownServer> impl = Ordering.natural().reverse().<Date>nullsLast().onResultOf(new Function<KnownServer, Date>() {
      public Date apply(KnownServer var1) {
         return var1.getDateOfLatestLogin();
      }
   });

   public int compare(KnownServer var1, KnownServer var2) {
      return this.impl.compare(var1, var2);
   }
}
