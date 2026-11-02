package org.etlforge.integration.client.ui.interchange.delete;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceUtils;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class WizardItemDeleteChecker {
   private final Set<IntegrationEntity> willBeDeleted;
   private final Set<IntegrationEntity> wizardsToBeDeleted;
   private final Set<IntegrationEntity> allImplementingItems;
   private final ProjectDependencies projectDependencies;
   private Set<IntegrationEntity> orphansToDelete;

   public WizardItemDeleteChecker(Set<IntegrationEntity> var1, ProjectDependencies var2) {
      this.willBeDeleted = var1;
      this.projectDependencies = var2;
      this.wizardsToBeDeleted = Sets.newHashSet();
      this.allImplementingItems = Sets.newHashSet();

      for (IntegrationEntity var4 : var1) {
         if (var4 instanceof SalesforceWizardEntity) {
            this.wizardsToBeDeleted.add(var4);
            WizardItemBrowser var5 = new WizardItemBrowser((SalesforceWizardEntity)var4);
            this.allImplementingItems.addAll(var5.getAllImplementingItems());
         }
      }

      this.allImplementingItems.removeAll(var1);
      Iterables2.removeIfNot(this.allImplementingItems, new Predicate<IntegrationEntity>() {
         public boolean apply(IntegrationEntity var1) {
            return SalesforceUtils.isSalesforceWizardObject(var1);
         }
      });
   }

   public Set<IntegrationEntity> getOrphansToDelete() {
      return this.orphansToDelete;
   }

   public boolean isGreenlighted(EntityFilter var1) {
      Set var2 = this.collectOrphanedImplementingItems();
      if (var2.isEmpty()) {
         this.orphansToDelete = Sets.newHashSet();
         return true;
      } else {
         List var3 = this.visibleOrphans(var2, var1);
         if (var3.isEmpty()) {
            this.orphansToDelete = var2;
            return true;
         } else {
            Answer var4 = this.askUserForPermission(var3);
            if (var4.isYes()) {
               this.orphansToDelete = var2;
               return true;
            } else {
               this.orphansToDelete = Sets.newHashSet();
               return !var4.isCancel();
            }
         }
      }
   }

   private Set<IntegrationEntity> collectOrphanedImplementingItems() {
      HashSet var1 = Sets.newHashSet();

      for (IntegrationEntity var3 : this.allImplementingItems) {
         Set var4 = this.collectDependingItems(var3);
         var4.removeAll(this.willBeDeleted);
         var4.removeAll(this.allImplementingItems);
         if (var4.isEmpty()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   private Set<IntegrationEntity> collectDependingItems(IntegrationEntity var1) {
      HashSet var2 = Sets.newHashSet();
      this.collectDependingItems(var1, var2);
      return var2;
   }

   private void collectDependingItems(IntegrationEntity var1, Set<IntegrationEntity> var2) {
      for (IntegrationEntity var4 : this.projectDependencies.getDependingObjects(var1)) {
         if (var2.add(var4)) {
            this.collectDependingItems(var4, var2);
         }
      }
   }

   private List<IntegrationEntity> visibleOrphans(Set<IntegrationEntity> var1, EntityFilter var2) {
      return Iterables2.sortedCopy(Iterables.filter(var1, var2), new EntityTypeAndNameSorter());
   }

   private Answer askUserForPermission(final List<IntegrationEntity> var1) {
      return ClientInfo.isDataLoader() ? Answer.AFFIRMATIVE : UiThreadSupplier.call(new Supplier<Answer>() {
         public Answer get() {
            WizardItemDeleteChecker.Question var1x = new WizardItemDeleteChecker.Question(var1);
            return var1x.ask();
         }
      });
   }

   private static class Question implements UiProvider {
      private final IntegrationEntityList list;

      public Question(List<IntegrationEntity> var1) {
         this.list = new IntegrationEntityList(var1);
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5).withBorder(Empty.border(10));
         JLabel var3 = new JLabel("The following Salesforce wizard objects will no longer be used:");
         var3.setIcon(EntityIcons.getSalesforceWizardIcon());
         var2.north(var3).center(this.list).south("Do you want to delete them as well?");
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.list.requestFocus();
      }

      public Answer ask() {
         return Ask.yesNoOrCancel(this, "Delete Wizard Items?");
      }
   }
}
