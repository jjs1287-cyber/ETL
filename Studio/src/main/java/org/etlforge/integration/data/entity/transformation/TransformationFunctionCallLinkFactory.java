package org.etlforge.integration.data.entity.transformation;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.StrongEntityLink;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.FunctionCallStructure;

final class TransformationFunctionCallLinkFactory {
   private final IntegrationEntityLookup dataLookup;
   private final Transformation transformation;

   public TransformationFunctionCallLinkFactory(Transformation var1, IntegrationEntityLookup var2) {
      this.transformation = var1;
      this.dataLookup = var2;
   }

   public List<? extends EntityLink> getLinks() {
      IntegrationEntity var1 = this.getFunctionFromStructure(this.transformation.getInputStructure());
      IntegrationEntity var2 = this.getFunctionFromStructure(this.transformation.getOutputStructure());
      if (var1 == null && var2 == null) {
         return null;
      }

      ArrayList var3 = Lists.newArrayList();
      if (var1 != null) {
         var3.add(new StrongEntityLink(var1, this.transformation));
      }

      if (var2 != null && var2 != var1) {
         var3.add(new StrongEntityLink(var2, this.transformation));
      }

      return var3;
   }

   private IntegrationEntity getFunctionFromStructure(DataStructure var1) {
      if (var1 instanceof FunctionCallStructure) {
         IntegrationEntityId var2 = ((FunctionCallStructure)var1).getFunctionCallId();
         return this.dataLookup.getEntity(var2);
      } else {
         return null;
      }
   }
}
