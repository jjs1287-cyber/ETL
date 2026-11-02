package com.etlforge.integration.client.ui.sfdc.soql.browser;

import com.etlforge.integration.client.ui.sfdc.wizard.soql.ChildToParentField;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class ChildToParentFieldNode {
   private final KongaTreeNode childNode;
   private final ChildToParentField childToParentField;

   public ChildToParentFieldNode(KongaTreeNode var1, ChildToParentField var2) {
      this.childNode = var1;
      this.childToParentField = var2;
   }

   public KongaTreeNode getChildNode() {
      return this.childNode;
   }

   public ChildToParentField getField() {
      return this.childToParentField;
   }

   public String toSoql() {
      return this.childToParentField.toSoql();
   }
}
