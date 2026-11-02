package org.etlforge.integration.client.server.ui.info;

import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.Date;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.util.ServerComparators;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.util.version.Version;

final class TableComparators {
   public static Comparator<ServerGuid> getGuidComparator() {
      return ServerComparators.SORT_BY_GUID;
   }

   public static Comparator<KnownServer> getLatestLoginComparator() {
      return ServerComparators.SORT_BY_LATEST_LOGIN;
   }

   public static Comparator<Date> getDateComparator() {
      return getGeneralComparator();
   }

   public static Comparator<Version> getVersionComparator() {
      return getGeneralComparator();
   }

   private static <T extends Comparable<T>> Comparator<T> getGeneralComparator() {
      return Ordering.natural().nullsLast();
   }

   private TableComparators() {
   }
}
