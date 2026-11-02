package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.collect.ImmutableList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class ImplementingObjectsLinkPanel implements Disposable, UiProvider {
   private final ImmutableList<LinkToImplementingObject> links;
   private final Disposable disposable;

   public ImplementingObjectsLinkPanel(ImmutableList<LinkToImplementingObject> var1) {
      this.links = var1;
      this.disposable = new DisposableCollection(var1);
   }

   public void registerAsChangeSource(InputPanel var1) {
      for (LinkToImplementingObject var3 : this.links) {
         var3.registerAsChangeSource(var1);
      }
   }

   public void validate() throws IntegrationDataPanelException {
      for (LinkToImplementingObject var2 : this.links) {
         var2.validate();
      }
   }

   public void layoutIn(NColumnPanel var1) {
      for (LinkToImplementingObject var3 : this.links) {
         var3.layoutIn(var1);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = new ImplementingObjectsLinkPanel.Layout().get();
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public void dispose() {
      this.disposable.dispose();
   }

   private class Layout {
      private final TwoColumnPanel grid = this.emptyGrid();

      public Layout() {
         for (LinkToImplementingObject var3 : ImplementingObjectsLinkPanel.this.links) {
            var3.layoutIn(this.grid);
         }
      }

      private TwoColumnPanel emptyGrid() {
         TwoColumnPanel var1 = new TwoColumnPanel();
         byte var2 = 10;
         var1.withLeftInsets(0, 0, var2, 5);
         var1.withRightInsets(0, 0, var2, 0);
         var1.withAnchor(GridBagLayoutPanel.Anchor.WEST);
         var1.withRightFill(GridBagLayoutPanel.Fill.BOTH);
         var1.withRightWeights(1.0, 0.0);
         var1.setOpaque(false, false);
         return var1;
      }

      public JComponent get() {
         JPanel var1 = InvisiblePanel.of(this.grid);
         var1.setBorder(StyleSheet.getDefaultTitleBorder("Review / Edit"));
         return var1;
      }
   }
}
