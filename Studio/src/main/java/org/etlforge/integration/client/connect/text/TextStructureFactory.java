package org.etlforge.integration.client.connect.text;

import org.etlforge.integration.data.entity.TextDocument;

public interface TextStructureFactory<T> {
   TextStructureFactory NOT_AVAILABLE = new TextStructureFactory() {
      @Override
      public boolean isAvailableFor(Object var1) {
         return false;
      }

      @Override
      public TextDocument createStructure(Object var1) {
         throw new UnsupportedOperationException();
      }
   };

   boolean isAvailableFor(T var1);

   TextDocument createStructure(T var1);
}
