package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import java.util.Collections;
import java.util.List;
import org.etlforge.integration.salesforce.wsdl.DefaultFieldOrder;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;

final class TargetFieldsTreeModelBuilder {
   public static KongaTreeModel buildTreeModel(HeaderMappingsModel var0) {
      DefaultKongaTreeNode var1 = new DefaultKongaTreeNode("root", true);
      addRelatedObjects(var1, var0);
      addFields(var1, var0);
      return new DefaultKongaTreeModel(var1);
   }

   private static void addRelatedObjects(DefaultKongaTreeNode var0, HeaderMappingsModel var1) {
      List var2 = var1.getRelatedObjects();
      if (!var2.isEmpty()) {
         Collections.sort(var2, RelatedObject.byRelationshipName());

         for (Object __etlItem25 : var2) {
            RelatedObject var4 = (RelatedObject) __etlItem25;
            DefaultKongaTreeNode var5 = new DefaultKongaTreeNode(var0, var4.getRelationshipName(), true);

            for (SObjectSchema.Field var7 : var4.getExternalIds()) {
               new DefaultKongaTreeNode(var5, var7, false);
            }
         }
      }
   }

   private static void addFields(DefaultKongaTreeNode var0, HeaderMappingsModel var1) {
      List var2 = var1.getObjectFields();
      Collections.sort(var2, new DefaultFieldOrder());

      for (Object __etlItem39 : var2) {
         SObjectSchema.Field var4 = (SObjectSchema.Field) __etlItem39;
         new DefaultKongaTreeNode(var0, var4, false);
      }
   }

   private TargetFieldsTreeModelBuilder() {
   }
}
