package org.etlforge.integration.client.ui.admin.engine;

import java.util.EnumSet;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.ServerStatus;
import org.etlforge.integration.client.admin.engine.ServerEngine;
import org.etlforge.ui.Empty;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.panel.CaptionTitlePanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.util.Property;

public final class EngineStatusPanel implements UiProvider {
   private final ServerEngine engine;
   private final EngineController controller;
   private final JTextArea statusArea;
   private final StatusIndicator indicator;
   private final JComponent ui;

   public EngineStatusPanel(ServerEngine var1, EngineController var2, String var3, Icon var4) {
      this.engine = var1;
      this.controller = var2;
      this.statusArea = KongaTextArea.viewer(var1.getDetails(), Size.rows(4), Size.columns(40));
      this.indicator = new StatusIndicator(var1.getStatus());
      this.addBindings();
      this.ui = this.layoutComponents(var3, var4);
   }

   private void addBindings() {
      Bindings var1 = new Bindings();
      Property var2 = this.engine.getProperty("details");
      var1.add(new TextBinding(var2, this.statusArea));
      Property var3 = this.engine.getProperty("status");
      var1.add(new EngineStatusPanel.StatusBinding(var3));
      var1.syncUi();
   }

   public void setDisconnected() {
      this.statusArea.setText("[Not connected]");
      this.indicator.setStatus(EnumSet.of(ServerStatus.UNDEFINED));
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      EngineControlAction var1 = this.controller.getStatusAction(Admin.StatusDetail.NORMAL);
      JButton var2 = UiUtils.findButtonForAction(this.ui, var1);
      if (var2 != null) {
         var2.requestFocusInWindow();
      }
   }

   public void addItemsToPopupMenu(JPopupMenu var1) {
      KongaMenu var2 = new KongaMenu(Strings.get("StatusPanel.Menu"), (Icon)this.controller.getStatusAction(Admin.StatusDetail.SHORT).getValue("SmallIcon"));

      for (EngineControlAction var4 : this.controller.getAllStatusActions()) {
         JMenuItem var5 = var2.add(var4);
         var5.setIcon(null);
      }

      var1.add(var2);
   }

   public void setWaitService(WaitService var1) {
      for (EngineControlAction var3 : this.controller.getAllStatusActions()) {
         var3.setWaitService(var1);
      }
   }

   private JComponent layoutComponents(String var1, Icon var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder();
      var3.north(this.layoutTitle(var1, var2)).center(this.getContent());
      return var3.container();
   }

   private Object layoutTitle(String var1, Icon var2) {
      KongaLabel var3 = TextStyles.DropDownPanelTitle.makeLabel(var1);
      var3.setIcon(var2);
      return new CaptionTitlePanel(var3, 25);
   }

   private JComponent getContent() {
      OneColumnPanel var1 = new OneColumnPanel().withInsets(5, 5, 5, 15);
      this.layoutButtons(var1);
      var1.add(this.statusArea);
      return new TwoColumnPanel().withAnchor(TwoColumnPanel.NORTHWEST).withBorder(Empty.border(5, 20, 5, 5)).addRow(var1, this.indicator).invisible();
   }

   private void layoutButtons(OneColumnPanel var1) {
      EngineControlAction var2 = this.controller.getStatusAction(Admin.StatusDetail.FULL);
      KongaButton var3 = new KongaButton(var2);
      var3.setText(Strings.get("StatusPanel.Label"));
      var1.add(InvisiblePanel.of(var3));
   }

   private class StatusBinding extends AbstractBinding<EnumSet<ServerStatus>> {
      public StatusBinding(Property<EnumSet<ServerStatus>> var2) {
         super(var2);
      }

      protected void updateUiWithValue(EnumSet<ServerStatus> var1) {
         EngineStatusPanel.this.indicator.setStatus(var1);
      }
   }
}
