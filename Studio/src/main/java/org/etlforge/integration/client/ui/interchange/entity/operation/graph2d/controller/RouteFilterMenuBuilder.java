package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.widget.menu.KongaMenu;

final class RouteFilterMenuBuilder {
   private final OperationGraph2D graph;
   private final Action hideAll;
   private final Action showAll;
   private final EnumMap<RouteType, ToggleAction> individualActions;

   public RouteFilterMenuBuilder(OperationGraph2D var1) {
      this.graph = var1;
      this.hideAll = new RouteFilterMenuBuilder.HideAllAction();
      this.showAll = new RouteFilterMenuBuilder.ShowAllAction();
      this.individualActions = Maps.newEnumMap(RouteType.class);
      EnumSet var2 = var1.getLayout().getRouteTypesToShow();

      for (RouteType var6 : RouteType.values()) {
         this.individualActions.put(var6, new RouteFilterMenuBuilder.IndividualSettingAction(var6, var2.contains(var6)));
      }
   }

   public KongaMenu buildMenu() {
      KongaMenu var1 = new KongaMenu("Routes To Show");

      for (RouteType var5 : RouteType.values()) {
         var1.add(this.individualActions.get(var5));
      }

      var1.addActions(null, this.showAll, this.hideAll);
      return var1;
   }

   private void updateIndividualSettings() {
      EnumSet var1 = EnumSet.noneOf(RouteType.class);

      for (Entry var3 : this.individualActions.entrySet()) {
         if (((ToggleAction)var3.getValue()).isSelected()) {
            var1.add(var3.getKey());
         }
      }

      this.setSelectedTypes(var1);
   }

   private void setSelectedTypes(EnumSet<RouteType> var1) {
      this.graph.getLayout().setRouteTypesToShow(var1);
      this.graph.repaint();
   }

   private class HideAllAction extends AbstractAction {
      public HideAllAction() {
         super(Strings.get("RouteFilterMenu.HideAll"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         RouteFilterMenuBuilder.this.setSelectedTypes(EnumSet.noneOf(RouteType.class));

         for (ToggleAction var3 : RouteFilterMenuBuilder.this.individualActions.values()) {
            var3.setSelected(false);
         }
      }
   }

   private class IndividualSettingAction extends AbstractAction implements ToggleAction {
      public IndividualSettingAction(RouteType var2, boolean var3) {
         super(Strings.get("RouteFilterMenu." + var2.name()));
         this.setSelected(var3);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         RouteFilterMenuBuilder.this.updateIndividualSettings();
      }

      @Override
      public boolean isSelected() {
         Object var1 = this.getValue("SwingSelectedKey");
         return var1 instanceof Boolean ? (Boolean)var1 : false;
      }

      @Override
      public void setSelected(boolean var1) {
         this.putValue("SwingSelectedKey", var1);
      }
   }

   private class ShowAllAction extends AbstractAction {
      public ShowAllAction() {
         super(Strings.get("RouteFilterMenu.ShowAll"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         RouteFilterMenuBuilder.this.setSelectedTypes(EnumSet.allOf(RouteType.class));

         for (ToggleAction var3 : RouteFilterMenuBuilder.this.individualActions.values()) {
            var3.setSelected(true);
         }
      }
   }
}
