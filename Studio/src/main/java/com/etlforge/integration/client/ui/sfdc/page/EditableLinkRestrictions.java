package com.etlforge.integration.client.ui.sfdc.page;

import java.util.EnumSet;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.entity.wizard.NewFileLocationCallback;
import org.etlforge.integration.client.ui.entity.wizard.NewLocatableCallback;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LocatableFilter;
import org.etlforge.integration.data.structure.DataStructureType;

public final class EditableLinkRestrictions {
   private final EntityFilter filter;
   private final UiEntityFactoryCallback newEntityCallback;

   public static EditableLinkRestrictions forSource(WizardItemBrowser var0) {
      Transformation var1 = var0.getRequestTransformation();
      if (var1 != null) {
         DataStructureType var2 = var1.getInputStructure().getStructureType();
         return forStructureType(var2);
      } else {
         return isBulkLoad(var0) ? forStructureType(DataStructureType.Text) : notEditable();
      }
   }

   private static EditableLinkRestrictions forStructureType(DataStructureType var0) {
      return var0.isFileBased() ? fileBased() : notFileBased(var0);
   }

   private static EditableLinkRestrictions fileBased() {
      LocatableFilter var0 = new LocatableFilter(DataLocationType.getFileBasedTypes());
      NewFileLocationCallback var1 = new NewFileLocationCallback();
      return new EditableLinkRestrictions(var0, var1);
   }

   private static EditableLinkRestrictions notFileBased(DataStructureType var0) {
      EnumSet var1 = var0.getCompatibleLocationTypes();
      if (var1.size() == 1) {
         DataLocationType var2 = (DataLocationType)var1.iterator().next();
         return forLocationType(var2);
      } else {
         return notEditable();
      }
   }

   private static EditableLinkRestrictions forLocationType(DataLocationType var0) {
      LocatableFilter var1 = new LocatableFilter(var0);
      NewLocatableCallback var2 = new NewLocatableCallback(var0);
      return new EditableLinkRestrictions(var1, var2);
   }

   private static boolean isBulkLoad(WizardItemBrowser var0) {
      SalesforceWizardEntity var1 = var0.getWizardItem();
      return var1 instanceof SalesforceBulkLoad;
   }

   private static EditableLinkRestrictions notEditable() {
      return new EditableLinkRestrictions(EntityFilter.REJECT_ALL, null);
   }

   public static EditableLinkRestrictions forTarget(WizardItemBrowser var0) {
      Transformation var1 = var0.getResponseTransformation();
      if (var1 == SystemTransformation.PASS_THROUGH) {
         return fileBased();
      } else if (var1 != null) {
         DataStructureType var2 = var1.getOutputStructure().getStructureType();
         return forStructureType(var2);
      } else {
         return isBulkLoad(var0) ? forStructureType(DataStructureType.Text) : notEditable();
      }
   }

   public EditableLinkRestrictions(EntityFilter var1, UiEntityFactoryCallback var2) {
      this.filter = var1;
      this.newEntityCallback = var2;
   }

   public EntityFilter getFilter() {
      return this.filter;
   }

   public UiEntityFactoryCallback getNewEntityCallback() {
      return this.newEntityCallback;
   }

   public boolean isEditable() {
      return this.newEntityCallback != null;
   }
}
