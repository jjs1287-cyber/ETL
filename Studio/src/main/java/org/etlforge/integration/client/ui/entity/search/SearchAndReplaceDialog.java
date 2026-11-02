package org.etlforge.integration.client.ui.entity.search;

import com.google.common.collect.Lists;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.entity.search.NameProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.ui.dialog.KongaDialog;

public final class SearchAndReplaceDialog {
   private final Folder root;
   private final List<SearchableProperty> properties;
   private EntityPersistor persistor;

   public SearchAndReplaceDialog(Folder var1, SearchAndReplaceMetaData var2) {
      this(var1, var2.getSearchableProperties(), var2.includeName());
   }

   public SearchAndReplaceDialog(Folder var1, List<? extends SearchableProperty> var2, boolean var3) {
      this.root = var1;
      this.properties = Lists.newArrayList();
      if (var3) {
         this.properties.add(new NameProperty());
      }

      this.properties.addAll(var2);
   }

   public void setPersistor(EntityPersistor var1) {
      this.persistor = var1;
   }

   public void open(Window var1) {
      EntitySearchPanel var2 = this.createSearchUi();
      if (var2 != null) {
         KongaDialog var3 = this.createDialog(var1, var2);
         var3.setVisible(true);
      }
   }

   private EntitySearchPanel createSearchUi() {
      SearchFieldsPanel var1 = this.createSearchFieldsPanel();
      if (var1 == null) {
         return null;
      }

      List var2 = this.root.getAllItemsRecursively();
      EntitySearchPanel var3 = new EntitySearchPanel(var2, var1);
      if (this.persistor != null) {
         var3.enableReplace(this.persistor);
      }

      return var3;
   }

   private SearchFieldsPanel createSearchFieldsPanel() {
      ArrayList var1 = Lists.newArrayList();

      for (SearchableProperty var3 : this.properties) {
         PropertySearchField var4 = PropertySearchFieldFactory.createField(var3);
         if (var4 != null) {
            var1.add(var4);
         }
      }

      if (var1.isEmpty()) {
         Alert.error("This functionality is not quite ready yet.", "Not Yet Supported");
         return null;
      } else {
         return new SearchFieldsPanel(var1);
      }
   }

   private KongaDialog createDialog(Window var1, EntitySearchPanel var2) {
      WizardStyleDialog var3 = new WizardStyleDialog(var1, "Search & Replace", var2, KongaDialog.CLOSE_ONLY, false);
      var3.manageLocation(SearchAndReplaceAction.class);
      return var3;
   }
}
