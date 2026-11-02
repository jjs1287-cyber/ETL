package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.StraightMapperForSalesforceUcuRequest;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.mapping.PropertiesToMappingFactory;
import org.etlforge.integration.salesforce.mapping.SdlToSoapMappingConverter;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.lang.KongaStringUtils;

final class SdlMappingsImporter {
   private final Transformation tx;
   private final TreeMapper treeMapper;

   public SdlMappingsImporter(Transformation var1, TreeMapper var2) {
      this.tx = var1;
      this.treeMapper = var2;
   }

   public List<String> getMappings(File var1) throws IOException {
      List var2 = PropertiesToMappingFactory.fromFile(var1);
      SalesforceWizardRole var3 = this.tx.getSalesforceWizardRole();
      SfdcMethod var4 = var3.method;
      String var5 = this.tx.getSalesforceObject().getLocalName();
      SdlToSoapMappingConverter var6 = SdlToSoapMappingConverter.forRequest(var4, var5);
      ImmutableList var7 = var6.toSoap(var2);
      List var8 = this.addNecessaryConversions(var7);
      return KongaListUtils.transform(var8, MapStringFormat.MAPPING_TO_STRING);
   }

   private List<Mapping> addNecessaryConversions(List<Mapping> var1) {
      StraightMapperForSalesforceUcuRequest var2 = new StraightMapperForSalesforceUcuRequest(this.tx);
      ArrayList var3 = Lists.newArrayList();

      for (Mapping var5 : var1) {
         String var6 = var5.getExpression();
         String var7 = KongaStringUtils.removeStartAndEnd(var6, "<trans>", "</trans>").trim();
         Node var8 = this.treeMapper.findSourceNode(var7);
         if (var8 != null) {
            NodePath var9 = var5.getTargetPath();
            Node var10 = this.treeMapper.findTargetNode(var9);
            if (var10 != null) {
               String var11 = var2.createMappingExpression(var8, var10);
               Mapping var12 = MappingFactory.newMapping(var9, var11);
               var3.add(var12);
            }
         }
      }

      return var3;
   }
}
