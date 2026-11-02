package org.etlforge.integration.activity.client.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.activity.client.ActivityEntry;

public final class CompositeActivityEntryFilter implements Predicate<ActivityEntry> {
   private final List<ActivityEntryFilter> parts;

   public CompositeActivityEntryFilter(ActivityEntryFilter... var1) {
      this.parts = Lists.newArrayList(var1);
   }

   public void add(ActivityEntryFilter var1) {
      this.parts.add(var1);
   }

   public boolean apply(ActivityEntry var1) {
      for (ActivityEntryFilter var3 : this.parts) {
         if (!var3.accepts(var1)) {
            return false;
         }
      }

      return true;
   }
}
