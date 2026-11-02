package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.name.NameSorter;

public final class RootObject extends FieldOwner {
   private final SObjectSchema so;

   public RootObject(SObjectSchema var1) {
      super(var1.getName(), RelationshipPath.root(), var1);
      this.so = var1;
   }

   public ImmutableList<Relationship> getChildRelationships() {
      Builder var1 = ImmutableList.builder();

      for (SObjectSchema.Child var3 : NameSorter.naturalOrdering().sortedCopy(this.so.getChildren())) {
         var1.add(new Relationship(var3.getName(), var3.getObjectType()));
      }

      return var1.build();
   }

   @Override
   public String getDisplayName(NameDisplayMode var1) {
      return var1.getDisplayName(this.so);
   }
}
