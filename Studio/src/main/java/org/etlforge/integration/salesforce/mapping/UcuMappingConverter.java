package org.etlforge.integration.salesforce.mapping;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SalesforceWsdlConstants;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;

final class UcuMappingConverter extends SdlToSoapMappingConverter {
   private final String soapRoot;

   public UcuMappingConverter(SfdcMethod var1, String var2) {
      this.soapRoot = SalesforceWsdlConstants.getPathToObjectNodeInUcuRequest(var1, var2);
   }

   @Override
   public ImmutableList<Mapping> toSoap(List<? extends Mapping> var1) {
      Builder var2 = ImmutableList.builder();

      for (Mapping var4 : var1) {
         String var5 = var4.getExpression();
         String var6 = var4.getTargetPath().path;
         NodePath var7 = this.toSoapPath(var6);
         var2.add(MappingFactory.newMapping(var7, var5));
      }

      return var2.build();
   }

   private NodePath toSoapPath(String var1) {
      if (var1.contains(":")) {
         var1 = var1.replace(':', '.');
      }

      return new NodePath(this.soapRoot + var1 + "$");
   }
}
