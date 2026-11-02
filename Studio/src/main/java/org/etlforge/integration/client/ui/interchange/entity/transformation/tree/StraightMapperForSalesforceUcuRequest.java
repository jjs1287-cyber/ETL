package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.base.Optional;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.entity.sfdc.DateConversionMapping;
import org.etlforge.integration.data.entity.sfdc.WizardLookup;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.xml.xsd.SchemaTypes;

public final class StraightMapperForSalesforceUcuRequest {
   private final AbstractSalesforceUCU wizard;

   public StraightMapperForSalesforceUcuRequest(Transformation var1) {
      this.wizard = this.findWizard(var1);
   }

   private AbstractSalesforceUCU findWizard(Transformation var1) {
      WizardLookup var2 = WizardLookup.fromRequest(var1);
      var2.setWizardFilter(EntityFilters.oneOfTypes(EntityType.SalesforceUpsert, EntityType.SalesforceCreate, EntityType.SalesforceUpdate));
      return (AbstractSalesforceUCU)var2.findWizard().orNull();
   }

   public String createMappingExpression(Node var1, Node var2) {
      Optional var3 = this.lookupDateParams();
      if (var3.isPresent()) {
         String var4 = var1.m_deName;
         String var5 = this.lookupTargetType(var2);
         if (SchemaTypes.DATE.toString().equals(var5)) {
            return DateConversionMapping.forDate(var4, var3);
         }

         if (SchemaTypes.DATE_TIME.toString().equals(var5)) {
            return DateConversionMapping.forDateTime(var4, var3);
         }
      }

      return ScriptConstants.wrapInTags(var1.m_deName);
   }

   private Optional<DateConversionParams> lookupDateParams() {
      return this.wizard != null ? this.wizard.getDateConversionParams() : Optional.absent();
   }

   private String lookupTargetType(Node var1) {
      CROM var2 = var1.getCROM();
      CROM var3 = var2.isValueLeaf() ? var2.getParent() : var2;
      return var3.getSchemaTypeExpandedName();
   }
}
