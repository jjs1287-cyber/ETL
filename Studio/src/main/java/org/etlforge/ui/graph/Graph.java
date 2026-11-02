package org.etlforge.ui.graph;

import org.etlforge.ui.UiProvider;
import org.etlforge.ui.graph.layout.GraphLayout;
import org.etlforge.ui.graph.model.GraphModel;

public interface Graph extends UiProvider {
   GraphModel getModel();

   GraphLayout getLayout();

   GraphSelectionModel getSelectionModel();

   void addGraphInputListener(GraphInputListener var1);

   void removeGraphInputListener(GraphInputListener var1);
}
