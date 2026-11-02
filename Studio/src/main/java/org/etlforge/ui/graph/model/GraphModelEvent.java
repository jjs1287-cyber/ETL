package org.etlforge.ui.graph.model;

import java.util.EventObject;

public class GraphModelEvent extends EventObject {
   protected GraphModelEvent(GraphModel var1) {
      super(var1);
   }

   public GraphModel getSource() {
      return (GraphModel)super.getSource();
   }

   public void setSource(GraphModel var1) {
      super.source = var1;
   }
}
