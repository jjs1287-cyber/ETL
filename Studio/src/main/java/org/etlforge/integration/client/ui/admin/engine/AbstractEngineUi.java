package org.etlforge.integration.client.ui.admin.engine;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.engine.GetEngineStatusJob;
import org.etlforge.integration.client.admin.engine.ServerEngine;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.ui.admin.core.console.AdminConsolePart;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public abstract class AbstractEngineUi implements AdminConsolePart {
   private final ServerEngine engine;
   private final Admin serverApi;
   private final EngineController controller;
   private final EngineStatusPanel statusPanel;
   private final EngineActionPanel actionPanel;
   private final KongaLayeredPane container;

   protected AbstractEngineUi(ServerEngine var1, Admin var2) {
      this.engine = Preconditions.checkNotNull(var1, "engine");
      this.serverApi = Preconditions.checkNotNull(var2, "serverApi");
      this.controller = new EngineController(var1, var2);
      this.statusPanel = this.createStatusPanel();
      this.actionPanel = this.createActionPanel();
      this.container = new KongaLayeredPane(this.layout());
      this.statusPanel.setWaitService(this.container);
      this.actionPanel.setWaitService(this.container);
      this.getStatus();
   }

   @Override
   public final void serverConnectionChanged(ServerConnectionEvent var1) {
      if (var1.isConnected() && !this.engine.isGettingStatus()) {
         this.controller.getEngineStatus();
      } else {
         this.statusPanel.setDisconnected();
         this.controller.setDisconnected();
      }
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public final void setActive(boolean var1) {
   }

   @Override
   public final void populatePopupMenu(KongaPopupMenu var1) {
      this.statusPanel.addItemsToPopupMenu(var1);
      var1.addSeparator();
      this.actionPanel.addItemsToPopupMenu(var1);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      var1.display(this.container);
   }

   @Override
   public final void requestFocus() {
      this.actionPanel.requestFocus();
   }

   @Override
   public void dispose() {
   }

   private EngineStatusPanel createStatusPanel() {
      return new EngineStatusPanel(this.engine, this.controller, this.getStatusPanelTitle(), this.getStatusPanelIcon());
   }

   protected abstract String getStatusPanelTitle();

   protected abstract Icon getStatusPanelIcon();

   private EngineActionPanel createActionPanel() {
      this.controller.getStartEngineAction().setIcon(this.getStartEngineIcon());
      this.controller.getStopEngineAction().setIcon(this.getStopEngineIcon());
      return new EngineActionPanel(this.getActionPanelTitle(), this.getActionPanelIcon(), this.controller);
   }

   protected abstract String getActionPanelTitle();

   protected abstract Icon getActionPanelIcon();

   protected abstract Icon getStartEngineIcon();

   protected abstract Icon getStopEngineIcon();

   private JComponent layout() {
      BoxBuilder var1 = Vertical.box().leftAligned().withEmptyBorder(5);
      var1.addAll(this.statusPanel, this.actionPanel);
      return new BorderLayoutBuilder().north(var1).deeplyNonOpaque().container();
   }

   private void getStatus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            GetEngineStatusJob var1 = new GetEngineStatusJob(AbstractEngineUi.this.engine, AbstractEngineUi.this.serverApi);
            var1.submit(AbstractEngineUi.this.container, 125);
         }
      });
   }
}
