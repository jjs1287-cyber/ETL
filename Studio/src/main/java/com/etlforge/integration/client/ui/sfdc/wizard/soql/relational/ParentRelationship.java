package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.ChildToParentField;
import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import java.util.LinkedList;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class ParentRelationship extends FieldOwner {
   private final FieldOwner child;

   public ParentRelationship(String var1, FieldOwner var2, SObjectSchema var3) {
      super(var1, var2.getPath(), var3);
      this.child = var2;
   }

   @Override
   public String getDisplayName(NameDisplayMode var1) {
      return this.getName();
   }

   public FieldOwner getChild() {
      return this.child;
   }

   public boolean isOwnerOf(ChildToParentField var1) {
      ImmutableList var2 = var1.getParentPath();
      if (var2.equals(this.getPath().toNameList())) {
         return true;
      }

      RelationshipPath var3 = this.getChainOfParentRelationAncestors();
      return var3.toNameList().equals(var2);
   }

   private RelationshipPath getChainOfParentRelationAncestors() {
      LinkedList var1 = Lists.newLinkedList();
      var1.add(this);

      for (RelationshipPath var2 = this.getPath().getParentPath(); !var2.isEmpty() && var2.getLeaf() instanceof ParentRelationship; var2 = var2.getParentPath()) {
         var1.addFirst(var2.getLeaf());
      }

      return RelationshipPath.of(var1);
   }

   public ChildToParentField getChildToParentField(String var1) {
      LinkedList var2 = Lists.newLinkedList();
      var2.add(this.getName());
      var2.add(var1);

      for (RelationshipPath var3 = this.getPath().getParentPath(); var3.getLeaf() instanceof ParentRelationship; var3 = var3.getParentPath()) {
         var2.addFirst(var3.getLeaf().getName());
      }

      return new ChildToParentField(var2);
   }

   public boolean belongsToRootObject() {
      for (RelationshipPath var1 = this.getPath().getParentPath(); !var1.isEmpty(); var1 = var1.getParentPath()) {
         FieldOwner var2 = var1.getLeaf();
         if (var2 instanceof RootObject) {
            return true;
         }

         if (var2 instanceof ChildRelationship) {
            return false;
         }
      }

      throw new RuntimeException("Illegal path: " + this.getPath());
   }

   public ChildRelationship getChildRelation() {
      for (RelationshipPath var1 = this.getPath().getParentPath(); !var1.isEmpty(); var1 = var1.getParentPath()) {
         FieldOwner var2 = var1.getLeaf();
         if (var2 instanceof ChildRelationship) {
            return (ChildRelationship)var2;
         }
      }

      throw new UnsupportedOperationException("This ParentRelationship belongs to the RootObject.");
   }
}
