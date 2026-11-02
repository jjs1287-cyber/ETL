package org.etlforge.integration.client.ui.structure.editor.ldap.target;

import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.entity.wizard.NewLdapLocationCallback;
import org.etlforge.integration.client.ui.structure.editor.ldap.LdapLocationSelectionPage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.LocatableFilter;
import org.etlforge.integration.data.structure.LdapStructure;

public final class LdapTargetSelectionPage extends EntitySelectionPage<Target> implements LdapLocationSelectionPage {
   public LdapTargetSelectionPage(ApplicationWindowWizard var1, EntityExplorerSupport var2) {
      super(var1, PackageResources.TargetSelector.TITLE, var2, EntityType.Target, Target.class, new LocatableFilter(DataLocationType.LDAP));
      this.setMainCaption(PackageResources.TargetSelector.MAIN_CAPTION);
      this.setSubCaption(PackageResources.TargetSelector.SUB_CAPTION);
      this.setSelectorLabel(PackageResources.TargetSelector.COMBO_LABEL);
      this.setNewEntityCallback(new NewLdapLocationCallback());
   }

   @Override
   public void setSelectedLdapLocation(LdapLocation var1) {
      SourceId var2 = var1.getSourceId();
      if (var2 != null) {
         this.setSelectedEntityId(var2);
      } else {
         this.setSelectedEntityId(var1.getTargetId());
      }
   }

   @Override
   public LdapLocation getSelectedLdapLocation() {
      IntegrationEntity var1 = this.getSelectedEntity();
      if (var1 instanceof Target) {
         DataLocation var2 = ((Target)var1).getLocation();
         return (LdapLocation)var2;
      } else {
         return null;
      }
   }

   @Override
   public void applyTo(LdapStructure var1) {
   }
}
