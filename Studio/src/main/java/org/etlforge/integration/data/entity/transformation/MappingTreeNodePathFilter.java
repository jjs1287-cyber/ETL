package org.etlforge.integration.data.entity.transformation;

public interface MappingTreeNodePathFilter {
   MappingTreeNodePathFilter TRANSPARENT = new MappingTreeNodePathFilter() {
      @Override
      public boolean isHidden(String var1) {
         return false;
      }
   };

   boolean isHidden(String var1);

   @Override
   boolean equals(Object var1);

   @Override
   int hashCode();
}
