package org.etlforge.integration.client.ui.interchange.entity.webservice;

import java.awt.Dimension;
import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.client.ui.MappingStructureTreeWidget;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;

public final class WebServiceCallStructuresPanel implements UiProvider {
   private final MappingStructureTreeWidget requestTree = createTree("Request structure");
   private final MappingStructureTreeWidget responseTree = createTree("Response structure");

   private static MappingStructureTreeWidget createTree(String var0) {
      MappingStructureTreeWidget var1 = new MappingStructureTreeWidget(var0);
      var1.setRootVisible(false);
      return var1;
   }

   public void displayStructures(WebServiceCallStructures var1) {
      this.requestTree.populate(var1.request());
      this.responseTree.populate(var1.response());
   }

   @Override
   public void decorate(UiCanvas var1) {
      Grid var2 = new Grid(1, 2, 5, 0);
      var2.addAll(this.requestTree, this.responseTree).withPreferredSize(new Dimension(800, 400));
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.requestTree.requestFocus();
   }
}
