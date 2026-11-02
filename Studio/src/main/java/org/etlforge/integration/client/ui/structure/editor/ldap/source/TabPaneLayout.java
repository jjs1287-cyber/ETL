package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import com.google.common.collect.Lists;
import java.awt.Container;
import java.util.Collection;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.ui.Empty;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.event.BlockableEventListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;

class TabPaneLayout implements DefinerListLayout {
   private final KongaTabbedPane tabs = new KongaTabbedPane();
   private final TabPaneLayout.TabListener tabListener;
   private List<SearchParametersDefiner> definers;

   TabPaneLayout(Container var1, Object var2) {
      this.tabs.setInterpretTitlesAsMnemonicStrings(true);
      this.tabListener = new TabPaneLayout.TabListener();
      this.tabs.addChangeListener(this.tabListener);
      ContainerCanvas.decorate(this.tabs, var1, var2);
   }

   @Override
   public void layoutParameterDefiners(Collection<SearchParametersDefiner> var1) {
      this.tabListener.reset();
      this.tabListener.setBlocked(true);
      this.definers = Lists.newArrayList(var1);
      this.tabs.removeAll();

      for (SearchParametersDefiner var3 : var1) {
         String var4 = var3.getClassName();
         JComponent var5 = this.createDefinerWrapper(var3);
         this.tabs.addTab(var4, var5);
      }

      this.tabListener.setBlocked(false);
      this.tabs.setSelectedIndex(0);
   }

   private JComponent createDefinerWrapper(SearchParametersDefiner var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      UiUtils.setOpaqueRecursivelySkipDefaults(var2, false);
      var2.setBorder(Empty.border(10));
      JPanel var3 = BorderLayoutBuilder.fillWith(var2);
      var3.setBackground(Colors.NEUTRAL_BACKGROUND);
      return var3;
   }

   private class TabListener extends BlockableEventListener implements ChangeListener {
      private int currentIndex = -1;

      private TabListener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         if (!this.isBlocked()) {
            int var2 = TabPaneLayout.this.tabs.getSelectedIndex();
            if (var2 != this.currentIndex) {
               this.currentIndex = var2;
               SearchParametersDefiner var3 = TabPaneLayout.this.definers.get(var2);
               this.giveFocusTo(var3);
            }
         }
      }

      public void reset() {
         this.currentIndex = -1;
      }

      private void giveFocusTo(SearchParametersDefiner var1) {
         FocusUtils.requestFocusLater(var1, 100);
      }
   }
}
