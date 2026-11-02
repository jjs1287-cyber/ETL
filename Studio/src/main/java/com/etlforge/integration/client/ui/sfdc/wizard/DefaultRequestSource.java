package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceModifier;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;

final class DefaultRequestSource implements RequestSource {
   private final DataStructure dataStruct;

   public DefaultRequestSource(DataStructure var1) {
      this.dataStruct = Preconditions.checkNotNull(var1);
   }

   @Override
   public void applyTo(Transformation var1) {
      var1.setInputStructure(this.dataStruct);
   }

   @Override
   public void applyTo(SalesforceModifier var1) {
   }

   public Set<IntegrationEntity> getItemsCreatedByWizard() {
      return Sets.newHashSet();
   }
}
