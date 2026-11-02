package org.etlforge.integration.client.ui.entity.search;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.thread.AbstractInterruptible;

final class SearchJob extends AbstractInterruptible {
   private final EntitySearchModel model;

   public SearchJob(EntitySearchModel var1) {
      this.model = var1;
   }

   @Override
   protected boolean canBeStopped() {
      return true;
   }

   @Override
   protected final void runImpl() {
      ArrayList var1 = Lists.newArrayList();
      if (this.model.hasSearchString()) {
         this.searchImpl(var1);
      }

      if (!this.isStopping()) {
         Collections.sort(var1, new EntityTypeAndNameSorter());
         this.model.setMatchingEntites(var1);
      }
   }

   private void searchImpl(List<IntegrationEntity> var1) {
      for (IntegrationEntity var3 : this.model.getEntities()) {
         if (this.isStopping()) {
            return;
         }

         if (this.isMatch(var3)) {
            var1.add(var3);
         }
      }
   }

   private boolean isMatch(IntegrationEntity var1) {
      return this.isCorrectType(var1) && this.hasMatchingValue(var1);
   }

   private boolean isCorrectType(IntegrationEntity var1) {
      return this.model.isEntityTypeSelected(var1.getEntityType());
   }

   private boolean hasMatchingValue(IntegrationEntity var1) {
      for (String var3 : this.getStringsToCheck(var1)) {
         if (this.model.isMatch(var3)) {
            return true;
         }
      }

      return false;
   }

   private Iterable<String> getStringsToCheck(IntegrationEntity var1) {
      SearchType var2 = this.model.getSearchType();
      return var2.getStringsToSearch(var1);
   }
}
