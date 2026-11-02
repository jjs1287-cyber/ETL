package org.etlforge.integration.client.ui.script.builder;

import com.google.common.collect.Lists;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.split.KongaSplitPane;

class EditingTab {
   private final JComponent ui;

   public EditingTab(UiProvider... var1) {
      List var2 = this.getActiveProviders(var1);
      if (var2.isEmpty()) {
         this.ui = null;
      } else if (var2.size() == 1) {
         this.ui = BorderLayoutBuilder.fillWith(var2.get(0));
      } else {
         KongaSplitPane var3 = this.createSplitPane();
         this.ui = var3;
         this.splitParts(var2, var3);
      }
   }

   public boolean isEmpty() {
      return this.ui == null;
   }

   public JComponent getUi() {
      return this.ui;
   }

   private KongaSplitPane createSplitPane() {
      KongaSplitPane var1 = ComponentFactories.splitPaneFactory().newSplitPane(1);
      var1.setDividerSize(4);
      var1.setResizeWeight(0.5);
      return var1;
   }

   private static JComponent wrap(UiProvider var0) {
      JPanel var1 = BorderLayoutBuilder.fillWith(var0);
      var1.setMinimumSize(new Dimension(150, 0));
      return var1;
   }

   private List<UiProvider> getActiveProviders(UiProvider... var1) {
      ArrayList var2 = Lists.newArrayList();

      for (UiProvider var6 : var1) {
         if (var6 != null) {
            var2.add(var6);
         }
      }

      return var2;
   }

   private void splitParts(List<UiProvider> var1, KongaSplitPane var2) {
      for (int var3 = 0; var3 < var1.size(); var3++) {
         JComponent var4 = wrap((UiProvider)var1.get(var3));
         if (var2.getLeftComponent() == null) {
            var2.setLeftComponent(var4);
         } else if (var2.getRightComponent() == null) {
            var2 = this.addRightComponent(var1, var2, var3, var4);
         }
      }
   }

   private KongaSplitPane addRightComponent(List<UiProvider> var1, KongaSplitPane var2, int var3, JComponent var4) {
      if (var3 == var1.size() - 1) {
         var2.setRightComponent(var4);
      } else {
         KongaSplitPane var5 = this.createSplitPane();
         var5.setLeftComponent(var4);
         var2.setRightComponent(var5);
         var2 = var5;
      }

      return var2;
   }
}
