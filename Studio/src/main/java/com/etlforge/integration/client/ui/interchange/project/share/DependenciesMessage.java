package com.etlforge.integration.client.ui.interchange.project.share;

import com.google.common.collect.Iterables;
import java.util.List;
import javax.swing.JCheckBox;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RequiredObjectsCollector;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class DependenciesMessage {
   private final List<IntegrationEntity> requiredItems;

   public DependenciesMessage(ManagedProject var1, IntegrationEntity var2) {
      Iterable var3 = Iterables.filter(new RequiredObjectsCollector(var1.getDependencyStore()).collect(var2), new DependenciesMessage.RequiredItemsFilter());
      this.requiredItems = Iterables2.sortedCopy(var3, new EntityTypeAndNameSorter());
   }

   public boolean isOk() {
      return this.isWarningNeded() ? this.askUser() : true;
   }

   private boolean askUser() {
      DependenciesMessage.Ui var1 = new DependenciesMessage.Ui();
      boolean var2 = Ask.continueOrCancel(var1, "Additional Items Will Be Copied").isContinue();
      if (var2 && var1.doNotShowAgainCheck.isSelected()) {
         ShowDependenciesWarningPreference.INSTANCE.set(false);
      }

      return var2;
   }

   private boolean isWarningNeded() {
      return !this.requiredItems.isEmpty() && ShowDependenciesWarningPreference.get();
   }

   private static class RequiredItemsFilter implements EntityFilter {
      private RequiredItemsFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return !(var1 instanceof BuiltInIntegrationEntity);
      }
   }

   private class Ui implements UiProvider {
      private final JCheckBox doNotShowAgainCheck = new KongaCheckBox(ApplicationResources.STRINGS.getString("DoNotShowAgain"));

      private Ui() {
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
         var2.north("The following items are required by the selected items and will also be copied:");
         IntegrationEntityList var3 = new IntegrationEntityList(DependenciesMessage.this.requiredItems);
         var2.center(var3);
         this.doNotShowAgainCheck.setBorder(Empty.border(10, 0, 0, 0));
         var2.south(this.doNotShowAgainCheck);
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
      }
   }
}
