package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.base.Optional;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.tree.AddConnectionAction;
import org.etlforge.ui.widget.tree.ConnectedTrees;
import org.etlforge.ui.widget.tree.DeleteAllConnectionsAction;
import org.etlforge.ui.widget.tree.DeleteConnectionAction;

final class HeaderMappingsUiLayout {
   private final ConnectedTrees widget;
   private final HeaderMappingsController controller;

   public HeaderMappingsUiLayout(ConnectedTrees var1, HeaderMappingsController var2) {
      this.widget = var1;
      this.controller = var2;
   }

   public void decorate(UiCanvas var1) {
      this.addUpdateSourceAndTargetButtons();
      String var2 = "Drag and drop a source header to a target field to link them, or select them and click the Map button.";
      JComponent var3 = this.layoutControls();
      Vertical.borderLayout(5).of(var2, this.widget, var3).decorate(var1);
   }

   private void addUpdateSourceAndTargetButtons() {
      Optional var1 = this.controller.getUpdateSourceHeaderAction();
      if (var1.isPresent()) {
         this.widget.addLeftButtons(new KongaButton((Action)var1.get()));
      }

      Optional var2 = this.controller.getUpdateTargetFieldsAction();
      if (var2.isPresent()) {
         this.widget.addRightButtons(new KongaButton((Action)var2.get()));
      }
   }

   private JComponent layoutControls() {
      Action var1 = this.controller.getAutoMapAction();
      Action var2 = this.controller.getImportMappingsAction();
      Action var3 = this.controller.getExportMappingsAction();
      AddConnectionAction var4 = new AddConnectionAction("&Map", this.widget);
      DeleteConnectionAction var5 = new DeleteConnectionAction("&Remove", this.widget);
      DeleteAllConnectionsAction var6 = new DeleteAllConnectionsAction("Start &Over", this.widget);
      JPanel var7 = Horizontal.grid(10).of(var2, var3).invisible();
      JPanel var8 = Horizontal.box().addAllWithSpace(10, var1, var7).invisible();
      JPanel var9 = Grid.oneRow(10).of(var4, var5, var6).invisible();
      return BoxBuilder.horizontal().add(var8).glue().add(var9).container();
   }
}
