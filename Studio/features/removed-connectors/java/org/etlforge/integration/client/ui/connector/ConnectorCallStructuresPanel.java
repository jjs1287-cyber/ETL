package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Objects;
import java.awt.Dimension;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.client.ui.MappingStructureTreeWidget;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.wait.AbstractWaitService;
import org.etlforge.ui.wait.WaitService;

@ParametersAreNonnullByDefault
public final class ConnectorCallStructuresPanel implements UiProvider {
   @Nullable
   private final MappingStructureTreeWidget requestTree;
   private final MappingStructureTreeWidget responseTree;

   public ConnectorCallStructuresPanel(ConnectorCall var1) {
      this.requestTree = var1.hasRequestTransformation() ? createTree("Request structure") : null;
      this.responseTree = createTree("Response structure");
   }

   private static MappingStructureTreeWidget createTree(String var0) {
      MappingStructureTreeWidget var1 = new MappingStructureTreeWidget(var0);
      var1.setRootVisible(true);
      return var1;
   }

   public void displayStructures(@Nullable MappingTreeStructure var1, MappingTreeStructure var2) {
      if (this.requestTree != null) {
         this.requestTree.populate(var1);
      }

      this.responseTree.populate(var2);
   }

   public WaitService asWaitService() {
      return new AbstractWaitService() {
         @Override
         protected void goToWaitingState() {
            if (ConnectorCallStructuresPanel.this.requestTree != null) {
               ConnectorCallStructuresPanel.this.requestTree.displayWaitNode();
            }

            ConnectorCallStructuresPanel.this.responseTree.displayWaitNode();
         }

         @Override
         protected void stopWaiting() {
            if (ConnectorCallStructuresPanel.this.requestTree != null) {
               ConnectorCallStructuresPanel.this.requestTree.removeWaitNode();
            }

            ConnectorCallStructuresPanel.this.responseTree.removeWaitNode();
         }
      };
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.requestTree != null) {
         Grid var2 = new Grid(1, 2, 5, 0).nonOpaque();
         var2.addAll(this.requestTree, this.responseTree).withPreferredSize(new Dimension(800, 400));
         var2.decorate(var1);
      } else {
         this.responseTree.decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      Objects.firstNonNull(this.requestTree, this.responseTree).requestFocus();
   }
}
