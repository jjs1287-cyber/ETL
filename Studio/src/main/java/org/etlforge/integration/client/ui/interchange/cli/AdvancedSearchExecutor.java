package org.etlforge.integration.client.ui.interchange.cli;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaDataLookup;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.search.SearchAndReplaceDialog;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.project.IntegrationProject;

public final class AdvancedSearchExecutor implements CommandExecutor {
   private final CurrentProjectService currentProject;
   private final SearchAndReplaceMetaDataLookup metaDataLookup;
   private final EntityPersistor persistor;

   public AdvancedSearchExecutor(CurrentProjectService var1, SearchAndReplaceMetaDataLookup var2, EntityPersistor var3) {
      this.currentProject = var1;
      this.metaDataLookup = var2;
      this.persistor = var3;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      IntegrationProject var2 = this.currentProject.getProject();
      if (var2 != null) {
         RootFolder var3 = this.getRootFolder(var2, var1);
         if (var3 != null) {
            this.openDialog(var3);
         }
      }
   }

   private RootFolder getRootFolder(IntegrationProject var1, String[] var2) {
      EntityType var3 = this.findMatchingType(var1, var2);
      return var3 == null ? null : var1.getRootFolder(var3);
   }

   private EntityType findMatchingType(IntegrationProject var1, String[] var2) {
      String var3 = Joiner.on(" ").join(var2).trim().toLowerCase();
      if (var3.isEmpty()) {
         return null;
      }

      ImmutableList var4 = var1.getSupportedEntityTypes();

      for (Object __etlItem52 : var4) {
         EntityType var6 = (EntityType) __etlItem52;
         if (var6.getDisplayName().equalsIgnoreCase(var3) || var6.getPlural().equalsIgnoreCase(var3)) {
            return var6;
         }
      }

      for (Object __etlItem58 : var4) {
         EntityType var8 = (EntityType) __etlItem58;
         if (var8.getDisplayName().toLowerCase().startsWith(var3) || var8.getPlural().toLowerCase().startsWith(var3)) {
            return var8;
         }
      }

      return null;
   }

   private void openDialog(RootFolder var1) {
      SearchAndReplaceMetaData var2 = this.metaDataLookup.getTypeMetaData(var1.getItemType());
      SearchAndReplaceDialog var3 = new SearchAndReplaceDialog(var1, var2);
      var3.setPersistor(this.persistor);
      var3.open(ApplicationUi.getAppWinFrame());
   }
}
