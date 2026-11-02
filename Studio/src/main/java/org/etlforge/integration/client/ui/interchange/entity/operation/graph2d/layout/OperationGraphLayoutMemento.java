package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import org.etlforge.ui.graph.layout.GraphLayoutMemento;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;

class OperationGraphLayoutMemento implements GraphLayoutMemento {
   private final OperationGraphLayout layout;
   private final Persistor state;

   OperationGraphLayoutMemento(OperationGraphLayout var1) {
      this.layout = var1;
      this.state = XmlPersistor.newRoot("OperationGraphLayoutMemento");
      var1.writeTo(this.state);
   }

   @Override
   public void restore() {
      this.layout.readFrom(this.state);
   }
}
