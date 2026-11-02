package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

public enum EdgeType {
   STRAIGHT_LINE {
      @Override
      public String toString() {
         return "Straight lines";
      }
   },
   RIGHT_ANGLES {
      @Override
      public String toString() {
         return "90\u00b0 angles";
      }
   };

   EdgeType() {
   }
}
