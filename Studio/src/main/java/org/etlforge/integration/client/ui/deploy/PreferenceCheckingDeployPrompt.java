package org.etlforge.integration.client.ui.deploy;

import java.util.ArrayList;
import java.util.Collection;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.integration.client.deploy.DeployPrompt;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class PreferenceCheckingDeployPrompt implements DeployPrompt {
   private final DeployPrompt adaptee;
   private final BooleanPreference pref;
   private volatile Collection<? extends IntegrationEntity> dirty;

   public PreferenceCheckingDeployPrompt(DeployPrompt var1, BooleanPreference var2) {
      this.adaptee = var1;
      this.pref = var2;
   }

   @Override
   public Collection<? extends IntegrationEntity> getDirtyItemsToDeploy() {
      if (this.isAutomatic()) {
         return this.dirty != null ? this.dirty : new ArrayList<>();
      } else {
         return this.adaptee.getDirtyItemsToDeploy();
      }
   }

   private boolean isAutomatic() {
      return this.pref.get();
   }

   @Override
   public boolean prompt(Collection<? extends IntegrationEntity> var1, Collection<? extends IntegrationEntity> var2) {
      this.dirty = var2;
      return this.isAutomatic() ? true : this.adaptee.prompt(var1, var2);
   }
}
