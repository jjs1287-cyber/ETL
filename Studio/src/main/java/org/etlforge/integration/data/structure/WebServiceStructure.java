package org.etlforge.integration.data.structure;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.data.location.DataLocation;

public abstract class WebServiceStructure extends DataStructure implements FunctionCallStructure {
   private WebServiceCallId wsCallId;
   private WebServiceOperationInfo opInfo = new WebServiceOperationInfo();

   protected WebServiceStructure(WebServiceCallId var1, DataStructureType var2) {
      super(var2);
      this.wsCallId = Preconditions.checkNotNull(var1);
   }

   public WebServiceStructure createCopy(WebServiceCallId var1) {
      Preconditions.checkNotNull(var1, "wsCallId");
      WebServiceStructure var2;
      synchronized (this.getDataLock()) {
         var2 = (WebServiceStructure)this.clone();
         var2.setOpInfo(this.opInfo);
      }

      var2.wsCallId = var1;
      return var2;
   }

   public final WebServiceCallId getFunctionCallId() {
      return this.wsCallId;
   }

   @Override
   public final StructureNature getStructureNature() {
      return StructureNature.Tree;
   }

   @Override
   public final boolean isCompatibleWith(DataStructure var1) {
      return super.isCompatibleWith(var1) || var1 != null && var1.getStructureType() == DataStructureType.XML;
   }

   @Override
   public final boolean isCompatibleWith(DataLocation var1) {
      return var1 != null && var1.isFileBased();
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass().equals(var1.getClass())) {
         WebServiceStructure var2 = (WebServiceStructure)var1;
         return this.wsCallId.equals(var2.wsCallId);
      } else {
         return false;
      }
   }

   @Override
   public final int hashCode() {
      return this.wsCallId.hashCode();
   }

   public final WebServiceOperationInfo getOpInfo() {
      WebServiceOperationInfo var1;
      synchronized (this.getDataLock()) {
         var1 = this.opInfo;
      }

      return new WebServiceOperationInfo(var1);
   }

   public final void setOpInfo(WebServiceOperationInfo var1) {
      WebServiceOperationInfo var2 = new WebServiceOperationInfo(var1);
      synchronized (this.getDataLock()) {
         this.opInfo = var2;
      }
   }

   public final String getOperationName() {
      synchronized (this.getDataLock()) {
         return this.opInfo.getOperationName();
      }
   }
}
