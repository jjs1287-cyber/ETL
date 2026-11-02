package org.etlforge.integration.activity.ui.component.filter;

import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.ActivityEntryFilter;
import org.etlforge.ui.UiProvider;

public interface ActivityEntryFilterUi<T extends ActivityEntry> extends UiProvider {
   ActivityEntryFilter getFilter();

   void apply();

   void reset();

   boolean isActive();

   String getFilterString();

   void addFilterListener(FilterListener var1);

   void removeFilterListener(FilterListener var1);
}
