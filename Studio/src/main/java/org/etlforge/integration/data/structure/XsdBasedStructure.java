package org.etlforge.integration.data.structure;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.location.DataLocation;

public abstract class XsdBasedStructure extends DataStructure implements FunctionCallStructure {
   private final IntegrationEntityId callId;
   private final CallStructureSchema schema;

   protected XsdBasedStructure(DataStructureType var1, IntegrationEntityId var2, CallStructureSchema var3) {
      super(var1);
      this.callId = Preconditions.checkNotNull(var2);
      this.schema = var3;
   }

   @Override
   public final IntegrationEntityId getFunctionCallId() {
      return this.callId;
   }

   @Override
   public final StructureNature getStructureNature() {
      return StructureNature.Tree;
   }

   public final String getXsdPath() {
      return this.schema == null ? null : this.schema.getSchemaName();
   }

   public final String getRootName() {
      return this.schema == null ? null : this.schema.getRootElement();
   }

   public final CallStructureSchema getSchema() {
      return this.schema;
   }

   @Override
   public final boolean isCompatibleWith(DataLocation var1) {
      return var1 != null && var1.isFileBased();
   }

   @Override
   public final boolean isCompatibleWith(DataStructure var1) {
      if (var1 == null) {
         return false;
      }

      if (super.isCompatibleWith(var1)) {
         return true;
      }

      if (var1 instanceof XmlStructure) {
         CallStructureSchema var2 = this.getSchema();
         if (var2 == null) {
            return false;
         }

         XmlStructure var3 = (XmlStructure)var1;
         CallStructureSchema var4 = new CallStructureSchema(var3.getFilePath(), var3.getRootNodeName());
         return var2.equals(var4);
      } else {
         return false;
      }
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (var1 == null) {
         return false;
      }

      if (!this.getClass().equals(var1.getClass())) {
         return false;
      }

      XsdBasedStructure var2 = (XsdBasedStructure)var1;
      return this.getStructureType() == var2.getStructureType() && this.callId.equals(var2.callId) && Objects.equal(this.schema, var2.schema);
   }

   @Override
   public final int hashCode() {
      return Objects.hashCode(this.getStructureType(), this.callId, this.schema);
   }
}
