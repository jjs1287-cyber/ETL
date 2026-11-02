package org.etlforge.integration.client.ui.entity.search;

import java.awt.EventQueue;
import java.awt.Window;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaDataLookup;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.persist.EntityPersistor;

public final class SearchAndReplaceAction extends AbstractSingleActiveObjectAction<Folder> {
   private final Window owner;
   private final SearchAndReplaceMetaDataLookup metaDataLookup;
   private EntityPersistor persistor;

   public SearchAndReplaceAction(String var1, Window var2, ActiveObjectService var3, SearchAndReplaceMetaDataLookup var4) {
      super(var1, var3, "Advanced Search...", null, Folder.class);
      this.owner = var2;
      this.metaDataLookup = var4;
   }

   public void setPersistor(EntityPersistor var1) {
      this.persistor = var1;
   }

   protected boolean isEnabledFor(Folder var1) {
      SearchAndReplaceMetaData var2 = this.metaDataLookup.getTypeMetaData(var1.getItemType());
      return var2.hasProperties();
   }

   protected void runOnObject(final Folder var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SearchAndReplaceMetaData var1x = SearchAndReplaceAction.this.metaDataLookup.getTypeMetaData(var1.getItemType());
            SearchAndReplaceDialog var2 = new SearchAndReplaceDialog(var1, var1x);
            var2.setPersistor(SearchAndReplaceAction.this.persistor);
            var2.open(SearchAndReplaceAction.this.owner);
         }
      });
   }
}
