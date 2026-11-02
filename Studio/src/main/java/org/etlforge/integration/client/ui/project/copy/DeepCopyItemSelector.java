package org.etlforge.integration.client.ui.project.copy;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.ui.entity.dependencies.tree.RequiredObjectsTreeUi;
import org.etlforge.integration.client.ui.entity.selector.EntityTableSelector;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RequiredObjectsTree;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.label.UiProviderLabel;

public final class DeepCopyItemSelector implements UiProvider {
   private final RequiredObjectsTree cascade;
   private final EntityTableSelector<IntegrationEntity> selector;
   private final RequiredObjectsTreeUi cascadeUi;

   public DeepCopyItemSelector(IntegrationEntity var1) {
      this(RequiredObjectsTree.fromStartPoint(var1));
   }

   public DeepCopyItemSelector(RequiredObjectsTree var1) {
      this.cascade = var1;
      this.selector = this.createSelector();
      this.cascadeUi = this.createCascadeUi();
      this.selector.getSelectionSource().addSelectionListener(new DeepCopyItemSelector.ConsistentSelectionEnforcer());
   }

   private EntityTableSelector<IntegrationEntity> createSelector() {
      Set var1 = this.getItemsToDisplay();
      var1.remove(this.getStartPoint());
      EntityTableSelector var2 = new EntityTableSelector<>(Iterables2.sortedCopy(var1, new EntityTypeAndNameSorter()), IntegrationEntity.class);
      var2.setSelectionState(true);
      return var2;
   }

   private Set<IntegrationEntity> getItemsToDisplay() {
      Set var1 = this.cascade.getAllItems();
      IntegrationEntity var2 = this.getStartPoint();
      var1.remove(var2);
      Iterables.removeIf(var1, EntityFilters.BUILT_IN);
      return var1;
   }

   private RequiredObjectsTreeUi createCascadeUi() {
      RequiredObjectsTreeUi var1 = new RequiredObjectsTreeUi(this.cascade);
      var1.setVisibleRowCount(12);
      return var1;
   }

   private IntegrationEntity getStartPoint() {
      return this.cascade.getRoot().item;
   }

   public Set<IntegrationEntity> getItemsToExclude() {
      return Sets.newHashSet(this.selector.getUnselectedEntities());
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.northToSouth(this.layoutCaption(), this.layoutSelector(), this.layoutTree());
      var2.decorate(var1);
   }

   private UiProvider layoutCaption() {
      TextStyle var1 = WizardStyleSheet.MEDIUM_CAPTION_STYLE;
      KongaLabel var2 = var1.makeLabel("");
      RendererUtils.decorateRenderer(var2, this.getStartPoint(), EntityIcons.Size.MEDIUM);
      return Horizontal.box().of("Deep copy of ", var2);
   }

   private UiProvider layoutSelector() {
      KongaLabel var1 = UiProviderLabel.create("&Select which of the following items to copy:", this.selector);
      return new BorderLayoutBuilder(0, 3).north(var1).center(this.selector);
   }

   private JComponent layoutTree() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 5);
      var1.north("Dependency graph. An object in the tree depends on the objects under it.").center(this.cascadeUi);
      var1.withBorder(Empty.border(0, 10, 0, 10));
      DropDownPanel var2 = new DropDownPanel(BorderLayoutBuilder.fillWith(var1), "&Why are these objects needed?", false);
      var2.decorateTitle(WizardStyleSheet.MEDIUM_CAPTION_STYLE);
      JComponent var3 = var2.getDisplayer();
      var3.setOpaque(false);
      return var3;
   }

   @Override
   public void requestFocus() {
      this.selector.requestFocus();
   }

   private class ConsistentSelectionEnforcer implements SelectionListener {
      private Set<IntegrationEntity> selection;
      private Set<IntegrationEntity> previousSelection = this.getSelectedItems();
      private boolean blocked;

      public ConsistentSelectionEnforcer() {
      }

      private Set<IntegrationEntity> getSelectedItems() {
         return Sets.newHashSet(DeepCopyItemSelector.this.selector.getSelectedEntities());
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         if (!this.blocked) {
            this.selection = this.getSelectedItems();
            if (this.isFullOrEmpty()) {
               this.previousSelection = this.selection;
            } else {
               this.enforceConsistency();
               this.previousSelection = this.getSelectedItems();
            }
         }
      }

      private boolean isFullOrEmpty() {
         return this.selection.isEmpty() || DeepCopyItemSelector.this.selector.isEverythingSelected();
      }

      private void enforceConsistency() {
         this.blocked = true;
         boolean var1 = this.handleRemoval();
         if (!var1) {
            this.handleAddition();
         }

         this.blocked = false;
      }

      private boolean handleRemoval() {
         Set var1 = this.itemsThatWereRemoved();
         if (var1.isEmpty()) {
            return false;
         }

         Set var2 = DeepCopyItemSelector.this.cascade.getItemsExcluding(var1);
         var2.removeAll(DeepCopyItemSelector.this.selector.getUnselectedEntities());
         DeepCopyItemSelector.this.selector.setSelectedEntities(var2);
         return true;
      }

      private Set<IntegrationEntity> itemsThatWereRemoved() {
         HashSet var1 = Sets.newHashSet(this.previousSelection);
         var1.removeAll(this.selection);
         return var1;
      }

      private void handleAddition() {
         Set var1 = this.itemsThatWereAdded();
         assert !var1.isEmpty();
         Set var2 = this.getAddtionalItemsToAddBack(var1);
         this.selection.addAll(var2);
         DeepCopyItemSelector.this.selector.setSelectedEntities(this.selection);
      }

      private Set<IntegrationEntity> itemsThatWereAdded() {
         HashSet var1 = Sets.newHashSet(this.selection);
         var1.removeAll(this.previousSelection);
         return var1;
      }

      private Set<IntegrationEntity> getAddtionalItemsToAddBack(Set<IntegrationEntity> var1) {
         HashSet var2 = Sets.newHashSet();

         for (IntegrationEntity var4 : var1) {
            var2.addAll(DeepCopyItemSelector.this.cascade.collectAncestors(var4));
            var2.addAll(DeepCopyItemSelector.this.cascade.collectDescendants(var4));
         }

         return var2;
      }
   }
}
