package org.etlforge.integration.client.ui.admin.core.console;

import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.util.preference.StringKeyValue;

public class PartBasedConsolePage extends AbstractConsolePage {
   private final AdminConsolePartContainer partsContainer;

   public PartBasedConsolePage(String var1, String var2, List<? extends AdminConsolePart> var3) {
      super(var1, var2);
      this.partsContainer = this.createPartsContainer(var3);
   }

   private AdminConsolePartContainer createPartsContainer(List<? extends AdminConsolePart> var1) {
      this.addResource(new DisposableWindowElementResource(var1));
      if (var1.isEmpty()) {
         return new NoPartContainer(this.getTitle());
      } else {
         return var1.size() == 1
            ? new SinglePartContainer((AdminConsolePart)var1.get(0))
            : new TabbedPartContainer(var1, new AdminConsolePartDisplayOrder(new StringKeyValue(this.getClass())));
      }
   }

   @Override
   protected void populatePopupMenu(KongaPopupMenu var1) {
      AdminConsolePart var2 = this.partsContainer.getActivePart();
      if (var2 != null) {
         this.partsContainer.getActivePart().populatePopupMenu(var1);
      }
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.setAdditionalTitleDecoration(this.partsContainer.getToolBarContribution());
      this.createTitlePanel();
      this.setToolBar(this.createToolBar());
      this.setMainPanel(this.createDisplayer());
   }

   @Override
   protected final void serverConnectionChanged(ServerConnectionEvent var1) {
      if (this.partsContainer != null) {
         this.partsContainer.serverConnectionChanged(var1);
      }
   }

   private JComponent createDisplayer() {
      JPanel var1 = BorderLayoutBuilder.fillWith(this.partsContainer);
      var1.setOpaque(false);
      return var1;
   }
}
