package org.etlforge.integration.mscrm.query;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.name.Nameable;

public final class ResponseStructure {
   private final ResponseStructure.Record root;

   public ResponseStructure(ResponseStructure.Record var1) {
      this.root = Preconditions.checkNotNull(var1, "root");
   }

   public ResponseStructure.Record getRoot() {
      return this.root;
   }

   public String toXmlSchema(String var1) {
      ArgCheck.checkNotEmpty(var1, "rootName");
      ResponseStructureSchemaBuilder var2 = new ResponseStructureSchemaBuilder(var1, this.root);
      return var2.buildSchema();
   }

   public static final class Record implements Nameable {
      private final String name;
      private final ImmutableList<MSCrmAttribute> attributes;
      private final ImmutableList<ResponseStructure.Record> children;
      private final boolean allAttributes;

      public Record(String var1, ImmutableList<MSCrmAttribute> var2, boolean var3, ImmutableList<ResponseStructure.Record> var4) {
         this.name = ArgCheck.checkNotEmpty(var1, "name");
         this.attributes = Preconditions.checkNotNull(var2, "attributes");
         this.allAttributes = var3;
         this.children = Preconditions.checkNotNull(var4, "children");
      }

      @Override
      public String getName() {
         return this.name;
      }

      public ImmutableList<MSCrmAttribute> getAttributes() {
         return this.attributes;
      }

      public ImmutableList<ResponseStructure.Record> getChildRecords() {
         return this.children;
      }

      public boolean isAllAttributes() {
         return this.allAttributes;
      }
   }
}
