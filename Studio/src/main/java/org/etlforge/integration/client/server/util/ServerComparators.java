package org.etlforge.integration.client.server.util;

import java.util.Comparator;
import java.util.Date;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.common.server.ServerGuid;

public final class ServerComparators {
   public static final Comparator<ServerGuid> SORT_BY_GUID = getGuidComparator();
   public static Comparator<KnownServer> SORT_BY_FIRST_LOGIN = getFirstLoginComparator();
   public static final Comparator<KnownServer> SORT_BY_LATEST_LOGIN = getLatestLoginComparator();

   private static Comparator<ServerGuid> getGuidComparator() {
      return new Comparator<ServerGuid>() {
         public int compare(ServerGuid var1, ServerGuid var2) {
            return var1.toString().compareTo(var2.toString());
         }
      };
   }

   private static Comparator<KnownServer> getFirstLoginComparator() {
      return new Comparator<KnownServer>() {
         public int compare(KnownServer var1, KnownServer var2) {
            return ServerComparators.getDateComparator().compare(var1.getDateOfFirstLogin(), var2.getDateOfFirstLogin());
         }
      };
   }

   private static Comparator<KnownServer> getLatestLoginComparator() {
      return new Comparator<KnownServer>() {
         public int compare(KnownServer var1, KnownServer var2) {
            return ServerComparators.getDateComparator().compare(var1.getDateOfLatestLogin(), var2.getDateOfLatestLogin());
         }
      };
   }

   private static Comparator<Date> getDateComparator() {
      return getGeneralComparator();
   }

   private static <T extends Comparable<T>> Comparator<T> getGeneralComparator() {
      return new Comparator<T>() {
         public int compare(T var1, T var2) {
            if (var1 == null) {
               return var2 == null ? 0 : 1;
            } else {
               return var2 == null ? -1 : var1.compareTo(var2);
            }
         }
      };
   }

   private ServerComparators() {
   }
}
