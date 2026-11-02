package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public class SaveSearchModel extends AbstractPropertyBasedModel {
   public static final String SELECTOBJECTNAME = "selectedObjectName";
   public static final String SEARCHLIST = "SavedSearchList";
   public static final String SELECTEDSEARCH = "SelectedSavedSearch";
   private final Property<String> selectedObjectName = this.newProperty("selectedObjectName");
   private final Property<SavedSearch> selectedSearch = this.newProperty("SelectedSavedSearch");
   private final ListProperty<SavedSearch> searchList = this.newListProperty("SavedSearchList");
   private Map<String, String> searchMap = Maps.newHashMap();

   public String getSelectedSavedSearchId() {
      return this.selectedSearch.get() == null ? null : this.selectedSearch.get().getId();
   }

   private void updateSavedSearch() {
      this.selectedSearch.set(null);
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.searchMap.entrySet()) {
         var1.add(new SavedSearch((String)var3.getKey(), (String)var3.getValue()));
      }

      this.searchList.set(var1);
   }

   public void setObjectName(String var1) {
      if (!var1.equals(this.selectedObjectName.get())) {
         this.selectedObjectName.set(var1);
      }
   }

   public void setSavedSearch(Map<String, String> var1) {
      this.searchMap = var1;
      this.updateSavedSearch();
   }

   void setSelectionListener(PropertyChangeListener var1) {
      this.selectedSearch.addListener(var1);
   }

   public void setSavedSearchId(String var1) {
      if (!Strings.isNullOrEmpty(var1)) {
         String var2 = this.searchMap.get(var1);
         if (var2 == null) {
            this.selectedSearch.set(null);
         } else {
            this.selectedSearch.set(new SavedSearch(var1, var2));
         }
      }
   }
}
