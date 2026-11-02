package org.etlforge.integration.client.ui.admin.engine;

import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.CaptionTitlePanel;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class EngineActionPanel implements UiProvider {
   private final EngineControlAction startAction;
   private final EngineControlAction stopAction;
   private final JComponent ui;

   public EngineActionPanel(String var1, Icon var2, EngineController var3) {
      this.startAction = var3.getStartEngineAction();
      this.stopAction = var3.getStopEngineAction();
      this.ui = this.layoutComponents(var1, var2);
   }

   private JComponent layoutComponents(String var1, Icon var2) {
      return Vertical.borderLayout().of(this.title(var1, var2), this.buttons()).container();
   }

   private Object title(String var1, Icon var2) {
      KongaLabel var3 = TextStyles.DropDownPanelTitle.makeLabel(var1);
      var3.setIcon(var2);
      return new CaptionTitlePanel(var3, 25);
   }

   private JComponent buttons() {
      return Horizontal.box().withBorder(Empty.border(15, 20, 5, 5)).addAllWithSpace(10, this.startAction, this.stopAction).container();
   }

   public void addItemsToPopupMenu(KongaPopupMenu var1) {
      var1.addActions(this.startAction, this.stopAction);
   }

   public void setWaitService(WaitService var1) {
      this.startAction.setWaitService(var1);
      this.stopAction.setWaitService(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
   }
}
