package com.etlforge.integration.client.ui.sfdc.soql.browser;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.ChildToParentField;
import java.util.LinkedList;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class FieldNode extends DefaultKongaTreeNode {
   public FieldNode(SObjectSchema.Field var1) {
      super(var1, false);
   }

   public SObjectSchema.Field getUserObject() {
      return (SObjectSchema.Field)super.getUserObject();
   }

   public SObjectSchema.Field getField() {
      return this.getUserObject();
   }

   public String getSoqlRepresentation() {
      return this.isChildToParentField() ? this.asChildToParentFieldNode().toSoql() : this.getDisplayName();
   }

   public boolean isChildToParentField() {
      return this.getParent() instanceof ParentRelationshipNode;
   }

   public ChildToParentFieldNode asChildToParentFieldNode() {
      KongaTreeNode var1 = this.getParent();
      if (!(var1 instanceof ParentRelationshipNode)) {
         throw new UnsupportedOperationException("Not a child-to-parent field");
      }

      LinkedList var2 = Lists.newLinkedList();
      var2.addFirst(this.getDisplayName());

      while (var1 instanceof ParentRelationshipNode) {
         var2.addFirst(var1.getDisplayName());
         var1 = var1.getParent();
      }

      assert var1 instanceof RootObjectNode || var1 instanceof ChildRelationshipNode : "Unexpected node representing the child object for a child-to-parent field: "
         + (var1 == null ? null : var1.getClass().getName());
      return new ChildToParentFieldNode(var1, new ChildToParentField(var2));
   }
}
