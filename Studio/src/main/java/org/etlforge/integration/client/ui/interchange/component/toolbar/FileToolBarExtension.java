package org.etlforge.integration.client.ui.interchange.component.toolbar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.toolbar.AbstractToolBarExtension;
import org.etlforge.application.ui.window.toolbar.ToolBarEntry;
import org.etlforge.application.ui.window.toolbar.ToolBarExtensionPoint;
import org.etlforge.application.ui.window.toolbar.ToolbarMode;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.event.ProjectManagerEvent;
import org.etlforge.integration.client.project.event.ProjectManagerListener;
import org.etlforge.integration.client.ui.interchange.core.action.InterchangeActionDictionary;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;

public final class FileToolBarExtension extends AbstractToolBarExtension {
   private final EntityConfigLookup entityConfigs;
   private final ImmutableList<NewEntityToolBarEntry> newEntityEntries;
   private final ProjectManager projectManager;
   private ImmutableList<? extends ApplicationAction> actions = ImmutableList.of();

   public FileToolBarExtension(EntityConfigLookup var1, Iterable<NewEntityToolBarEntry> var2, ProjectManager var3) {
      super("toolbar.File");
      this.entityConfigs = var1;
      this.newEntityEntries = ImmutableList.copyOf(var2);
      this.projectManager = var3;
   }

   public void setAdditionalActions(List<? extends ApplicationAction> var1) {
      this.actions = ImmutableList.copyOf(var1);
   }

   @Override
   protected List<ToolBarEntry> createExtension(ToolBarExtensionPoint var1) {
      if (var1 != ToolBarExtensionPoint.EXTENSION_POINT_1) {
         return null;
      }

      ArrayList var2 = Lists.newArrayList();
      this.addButtonsForCreatingNewItems(var2);
      this.addOtherButtons(var2);
      return var2;
   }

   private void addButtonsForCreatingNewItems(List<ToolBarEntry> var1) {
      var1.add(ToolBarEntry.separator());
      if (ToolbarMode.getConfiguredMode() == ToolbarMode.MINIMAL) {
         throw new NotImplementedYetException();
      }

      List var2 = this.createNewEntityButtons();
      this.projectManager.addProjectManagerListener(new FileToolBarExtension.ButtonEnabler(var2));
      var1.addAll(var2);
   }

   private List<ToolBarEntry> createNewEntityButtons() {
      ArrayList var1 = Lists.newArrayList();

      for (NewEntityToolBarEntry var3 : this.newEntityEntries) {
         var1.addAll(var3.getNewEntityToolBarButtons(this.entityConfigs));
      }

      KongaButton var4 = this.createNewProjectButton();
      var1.add(ToolBarEntry.of(var4));
      return var1;
   }

   private KongaButton createNewProjectButton() {
      EntityConfig var1 = this.entityConfigs.getEntityConfig(EntityType.Project);
      ApplicationAction var2 = var1.newEntityAction();
      return KongaButton.forToolbar(var2).withMediumActionIcon();
   }

   private void addOtherButtons(List<ToolBarEntry> var1) {
      if (!this.actions.isEmpty()) {
         var1.add(ToolBarEntry.separator());

         for (ApplicationAction var3 : this.actions) {
            var1.add(ToolBarEntry.of(var3, InterchangeActionDictionary.DICTIONARY));
         }
      }
   }

   private static class ButtonEnabler implements ProjectManagerListener {
      private final ImmutableList<ToolBarEntry> entries;

      public ButtonEnabler(Iterable<ToolBarEntry> var1) {
         this.entries = ImmutableList.copyOf(var1);
      }

      @Override
      public void projectOpened(ProjectManagerEvent var1) {
         this.updateButton(true);
      }

      private void updateButton(final boolean var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               for (ToolBarEntry var2 : ButtonEnabler.this.entries) {
                  var2.setEnabled(var1);
               }
            }
         });
      }

      @Override
      public void projectClosed(ProjectManagerEvent var1) {
         this.updateButton(false);
      }
   }
}
