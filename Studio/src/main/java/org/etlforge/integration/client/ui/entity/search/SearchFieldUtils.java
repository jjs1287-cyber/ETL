package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class SearchFieldUtils {
   public static final Predicate<IntegrationEntity> ALL = new Predicate<IntegrationEntity>() {
      public boolean apply(IntegrationEntity var1) {
         return true;
      }
   };
   public static final Predicate<IntegrationEntity> NONE = new Predicate<IntegrationEntity>() {
      public boolean apply(IntegrationEntity var1) {
         return false;
      }
   };

   private SearchFieldUtils() {
   }
}
