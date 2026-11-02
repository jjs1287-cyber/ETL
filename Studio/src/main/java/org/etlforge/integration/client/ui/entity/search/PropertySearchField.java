package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import javax.swing.Action;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiProvider;

public interface PropertySearchField extends UiProvider {
   SearchableProperty getProperty();

   Object getValue();

   Predicate<IntegrationEntity> getSearcher(SearchOptions var1);

   void setEnabled(boolean var1);

   boolean isEnabled();

   void setSearchAction(Action var1);
}
