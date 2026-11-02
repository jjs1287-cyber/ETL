package com.etlforge.integration.client.ui.sfdc;

import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.persist.EntityPersistor;

public final class NameChangeConsequence implements SaveConsequence {
   private final IntegrationEntityPage page;
   private final EntityPersistor persistor;
   private SalesforceWizardRenamer renamer;

   public NameChangeConsequence(IntegrationEntityPage var1, EntityPersistor var2) {
      this.page = var1;
      this.persistor = var2;
   }

   @Override
   public boolean isAllowed() {
      String var1 = this.page.getObject().getName();
      String var2 = this.page.getDisplayedName();
      boolean var3 = !var1.equals(var2);
      if (var3) {
         this.renamer = new SalesforceWizardRenamer(this.persistor);
         this.renamer.setOldWizardName(var1);
      } else {
         this.renamer = null;
      }

      return true;
   }

   @Override
   public void apply() {
      if (this.renamer != null) {
         this.renamer.setNewName(this.page.getObject(), this.page.getDisplayedName());
      }
   }
}
