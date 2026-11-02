package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.salesforce.config.MappingTreePath;
import org.etlforge.integration.salesforce.config.SalesforceConfig;
import org.etlforge.integration.structure.mapping.NodePath;

final class StraightMappingSourceExpressionFactory {
   private final Transformation tx;
   private final StraightMapperForSalesforceUcuRequest sfdcUcuRequestMapper;

   public StraightMappingSourceExpressionFactory(Transformation var1) {
      this.tx = var1;
      if (this.isSalesforceUpsertRequest() && var1.isPartOfSalesforcePartnerWizard()) {
         this.sfdcUcuRequestMapper = new StraightMapperForSalesforceUcuRequest(var1);
      } else {
         this.sfdcUcuRequestMapper = null;
      }
   }

   public String getSourceExpression(Node var1, Node var2) {
      if (this.sfdcUcuRequestMapper != null) {
         return this.sfdcUcuRequestMapper.createMappingExpression(var1, var2);
      }

      String var3 = null;
      if (this.isSalesforceUpsertResponse() && this.isSourceNodeInResponsePartOfSfdcResponse(var1)) {
         var3 = this.createInstanceResolvingExpression(var1);
      }

      if (var3 == null) {
         var3 = var1.m_deName;
      }

      return ScriptConstants.wrapInTags(var3);
   }

   private boolean isSalesforceUpsertRequest() {
      return this.isPartOfUpsertCall(SalesforceWizardRole.Position.REQUEST);
   }

   private boolean isPartOfUpsertCall(SalesforceWizardRole.Position var1) {
      SalesforceWizardRole var2 = this.tx.getSalesforceWizardRole();
      if (var2 != null && var2.position == var1) {
         switch (var2.method) {
            case UPSERT:
            case CREATE:
            case UPDATE:
               return true;
            default:
               return false;
         }
      } else {
         return false;
      }
   }

   private boolean isSalesforceUpsertResponse() {
      return this.isPartOfUpsertCall(SalesforceWizardRole.Position.RESPONSE);
   }

   private boolean isSourceNodeInResponsePartOfSfdcResponse(Node var1) {
      String var2 = var1.m_deName;
      String var3 = this.getPathToResponseBody();
      return var2.startsWith(var3);
   }

   private String getPathToResponseBody() {
      SalesforceConfig var1 = SalesforceConfig.getInstance();
      return var1.getMappingTreePath(MappingTreePath.RESPONSE_BODY);
   }

   private String createInstanceResolvingExpression(Node var1) {
      String var2 = this.constructArrayDataElementName(var1);
      String var3 = this.getSourcePathForInstanceResolving(var1);
      String var4 = "if(IsNull({0}), {0} = {1});\n{0}[SourceInstanceCount() - 1]";
      return MessageFormat.format(var4, var2, var3);
   }

   private String constructArrayDataElementName(Node var1) {
      NodePath var2 = new NodePath(var1.m_deName);
      String var3 = var2.getLeafName();
      String var4 = "$etlforge.connect.upsert." + var3;
      if (!var3.endsWith("s")) {
         var4 = var4 + "s";
      }

      return var4;
   }

   private String getSourcePathForInstanceResolving(Node var1) {
      String var2 = var1.m_deName;
      String var3 = this.getPathToResponseBody();
      int var4 = var2.indexOf(".", var3.length());
      assert var4 > 0;
      String var5 = var2.substring(0, var4);
      String var6 = var2.substring(var4 + 1);
      return var5 + "#." + var6;
   }
}
