package com.etlforge.integration.client.ui.sfdc.soql.browser;

import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;

abstract class RelationshipNode extends DefaultKongaTreeNode {
   private final String objectType;

   public RelationshipNode(String var1, String var2) {
      super(var1, true);
      this.objectType = var2;
   }

   public final String getObjectType() {
      return this.objectType;
   }

   public final String getRelationshipName() {
      return (String)this.getUserObject();
   }

   @Override
   public boolean isFolder() {
      return true;
   }

   @Override
   public boolean isLeaf() {
      return false;
   }
}
