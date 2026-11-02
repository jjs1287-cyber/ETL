package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.MapAreaDisplayOption;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.menu.ToggleActionCheckBoxMenuItem;

public final class MapAreaSettingsMenu extends AbstractMenu {
   private final TransformationPage page;
   private final Action hideAll;
   private final Action showAll;
   private final EnumMap<MapAreaDisplayOption, ToggleAction> individualActions;

   public MapAreaSettingsMenu(TransformationPage var1) {
      super("menu.ic.tx.MapArea");
      this.page = var1;
      this.hideAll = new MapAreaSettingsMenu.HideAllAction();
      this.showAll = new MapAreaSettingsMenu.ShowAllAction();
      this.individualActions = Maps.newEnumMap(MapAreaDisplayOption.class);

      for (MapAreaDisplayOption var5 : MapAreaDisplayOption.values()) {
         this.individualActions.put(var5, new MapAreaSettingsMenu.IndividualSettingAction(var5));
      }

      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            MapAreaSettingsMenu.this.setActionsEnabledState();
         }
      });
   }

   private void setActionsEnabledState() {
      TreeMapper var1 = this.page.getTreeMapper();

      for (Entry var3 : this.individualActions.entrySet()) {
         ((ToggleAction)var3.getValue()).setSelected(var1.isMapDisplayFlagOn((MapAreaDisplayOption)var3.getKey()));
         ((ToggleAction)var3.getValue()).setEnabled(true);
      }

      this.hideAll.setEnabled(true);
      this.showAll.setEnabled(true);
   }

   @Override
   protected List<? extends JMenuItem> getMenuItems() {
      ArrayList var1 = Lists.newArrayList();

      for (ToggleAction var3 : this.individualActions.values()) {
         var1.add(new ToggleActionCheckBoxMenuItem(var3));
      }

      var1.add(null);
      var1.add(new KongaMenuItem(this.hideAll));
      var1.add(new KongaMenuItem(this.showAll));
      return var1;
   }

   private class HideAllAction extends AbstractAction {
      public HideAllAction() {
         super("&Hide All");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         MapAreaSettingsMenu.this.page.getTreeMapper().setMapAreayDisplay(EnumSet.noneOf(MapAreaDisplayOption.class));

         for (ToggleAction var3 : MapAreaSettingsMenu.this.individualActions.values()) {
            var3.setSelected(false);
         }
      }
   }

   private class IndividualSettingAction extends AbstractAction implements ToggleAction {
      public IndividualSettingAction(MapAreaDisplayOption var2) {
         super("&" + var2.toString());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.updateIndividualSettings();
      }

      private void updateIndividualSettings() {
         EnumSet var1 = EnumSet.noneOf(MapAreaDisplayOption.class);

         for (Entry var3 : MapAreaSettingsMenu.this.individualActions.entrySet()) {
            if (((ToggleAction)var3.getValue()).isSelected()) {
               var1.add(var3.getKey());
            }
         }

         MapAreaSettingsMenu.this.page.getTreeMapper().setMapAreayDisplay(var1);
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
         super("&Show All");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         MapAreaSettingsMenu.this.page.getTreeMapper().setMapAreayDisplay(EnumSet.allOf(MapAreaDisplayOption.class));

         for (ToggleAction var3 : MapAreaSettingsMenu.this.individualActions.values()) {
            var3.setSelected(true);
         }
      }
   }
}
