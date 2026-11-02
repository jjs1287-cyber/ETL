package com.etlforge.integration.client.ui.sfdc.soql.browser;

import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class RootObjectNode extends DefaultKongaTreeNode {
   public RootObjectNode(KongaTreeNode var1, SObjectSchema var2) {
      super(var1, var2, true);
   }

   @Override
   public String getDisplayName() {
      return this.getUserObject().toString();
   }

   public SObjectSchema getUserObject() {
      return (SObjectSchema)super.getUserObject();
   }
}
