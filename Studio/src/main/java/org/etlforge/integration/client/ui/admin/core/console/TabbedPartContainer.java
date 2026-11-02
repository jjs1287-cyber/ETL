package org.etlforge.integration.client.ui.admin.core.console;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.tabbedpane.DefaultTabReorderHandler;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;

final class TabbedPartContainer implements AdminConsolePartContainer {
   private final KongaTabbedPane tabs;
   private final List<AdminConsolePart> parts;
   private final Map<JComponent, AdminConsolePart> tabUiToPart;
   private final AdminConsolePartDisplayOrder displayOrder;

   public TabbedPartContainer(List<? extends AdminConsolePart> var1, AdminConsolePartDisplayOrder var2) {
      Preconditions.checkArgument(var1.size() > 1, "parts must contain at least 2 items");
      this.displayOrder = var2;
      this.parts = var2.getDisplayOrder(var1);
      this.tabUiToPart = Maps.newHashMap();
      this.tabs = this.createTabs();
      this.setInitialActiveState();
   }

   @Override
   public AdminConsolePart getActivePart() {
      return this.parts.get(this.tabs.getSelectedIndex());
   }

   @Override
   public JComponent getToolBarContribution() {
      return null;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tabs.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tabs.requestFocus();
   }

   @Override
   public void serverConnectionChanged(ServerConnectionEvent var1) {
      for (AdminConsolePart var3 : this.parts) {
         var3.serverConnectionChanged(var1);
      }
   }

   private KongaTabbedPane createTabs() {
      KongaTabbedPane var1 = new KongaTabbedPane();
      var1.setInterpretTitlesAsMnemonicStrings(true);

      for (AdminConsolePart var3 : this.parts) {
         JComponent var4 = this.createPartUi(var3);
         var1.addTab(var3.getTitle(), var3.getIcon(), var4);
         this.tabUiToPart.put(var4, var3);
      }

      var1.enableTabReordering(new TabbedPartContainer.TabOrderHandler());
      var1.setSelectedIndex(0);
      var1.addChangeListener(new TabbedPartContainer.TabListener());
      return var1;
   }

   private JComponent createPartUi(AdminConsolePart var1) {
      return new BorderLayoutBuilder().withBackground(Colors.NEUTRAL_BACKGROUND).center(var1).container();
   }

   private void setInitialActiveState() {
      boolean var1 = true;

      for (AdminConsolePart var3 : this.parts) {
         var3.setActive(var1);
         var1 = false;
      }
   }

   private class TabListener implements ChangeListener {
      private TabListener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         int var2 = TabbedPartContainer.this.tabs.getSelectedIndex();

         for (int var3 = 0; var3 < TabbedPartContainer.this.parts.size(); var3++) {
            AdminConsolePart var4 = TabbedPartContainer.this.parts.get(var3);
            var4.setActive(var3 == var2);
         }
      }
   }

   private class TabOrderHandler extends DefaultTabReorderHandler {
      private TabOrderHandler() {
      }

      @Override
      public void handle(KongaTabbedPane var1, int var2, int var3) {
         super.handle(var1, var2, var3);
         ArrayList var4 = Lists.newArrayList();

         for (int var5 = 0; var5 < var1.getTabCount(); var5++) {
            var4.add(TabbedPartContainer.this.tabUiToPart.get(var1.getComponentAt(var5)));
         }

         TabbedPartContainer.this.displayOrder.updateOrder(var4);
      }
   }
}
