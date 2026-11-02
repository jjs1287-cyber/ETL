package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.functors.EntityFilter;

public abstract class AbstractSearchType implements SearchType {
   private final String name;
   private final EntityFilter filter;
   private final boolean typeSpecific;

   protected AbstractSearchType(String var1) {
      this(var1, EntityFilter.ACCEPT_ALL, false);
   }

   protected AbstractSearchType(String var1, EntityFilter var2, boolean var3) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "name cannot be null or empty");
      Preconditions.checkNotNull(var2, "filter");
      this.name = var1;
      this.filter = var2;
      this.typeSpecific = var3;
   }

   @Override
   public final String getName() {
      return this.name;
   }

   @Override
   public final boolean isEntityTypeSpecific() {
      return this.typeSpecific;
   }

   @Override
   public final EntityFilter getFilter() {
      return this.filter;
   }
}
