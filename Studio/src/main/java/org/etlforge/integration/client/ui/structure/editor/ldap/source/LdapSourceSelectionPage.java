package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.entity.wizard.NewLdapLocationCallback;
import org.etlforge.integration.client.ui.structure.editor.ldap.LdapLocationSelectionPage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.LocatableFilter;
import org.etlforge.integration.data.structure.LdapStructure;

public final class LdapSourceSelectionPage extends EntitySelectionPage<Source> implements LdapLocationSelectionPage {
   public LdapSourceSelectionPage(ApplicationWindowWizard var1, EntityExplorerSupport var2) {
      super(var1, PackageResources.SourceSelector.TITLE, var2, EntityType.Source, Source.class, new LocatableFilter(DataLocationType.LDAP));
      this.setMainCaption(PackageResources.SourceSelector.MAIN_CAPTION);
      this.setSubCaption(PackageResources.SourceSelector.SUB_CAPTION);
      this.setHelpId("integration.transformation.wizard.ldap");
      this.setSelectorLabel(PackageResources.SourceSelector.COMBO_LABEL);
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
      if (var1 instanceof Source) {
         DataLocation var2 = ((Source)var1).getLocation();
         return (LdapLocation)var2;
      } else {
         return null;
      }
   }

   @Override
   public void applyTo(LdapStructure var1) {
   }
}
