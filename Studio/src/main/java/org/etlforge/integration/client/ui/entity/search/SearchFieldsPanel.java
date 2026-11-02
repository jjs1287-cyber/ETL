package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList.Builder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.client.entity.search.NestedProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class SearchFieldsPanel implements UiProvider {
   private final ImmutableList<SearchFieldsPanel.Entry> entries;

   public SearchFieldsPanel(List<? extends PropertySearchField> var1) {
      this.entries = buildEntries(var1);
   }

   private static ImmutableList<SearchFieldsPanel.Entry> buildEntries(List<? extends PropertySearchField> var0) {
      Builder var1 = ImmutableList.builder();

      for (PropertySearchField var3 : var0) {
         if (var3.getProperty() instanceof NestedProperty) {
            var1.add(new SearchFieldsPanel.NestedPropertyEntry((EnumPropertySearchField)var3));
         } else {
            var1.add(new SearchFieldsPanel.SingleFieldEntry(var3));
         }
      }

      return var1.build();
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = new NColumnPanel(4, GridBagLayoutPanel.Anchor.EAST);

      for (SearchFieldsPanel.Entry var4 : this.entries) {
         var4.layoutInFourColumns(var2);
      }

      var2.fillWith(" ");
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.entries.get(0).requestFocus();
   }

   public void setSearchAction(Action var1) {
      for (SearchFieldsPanel.Entry var3 : this.entries) {
         var3.setSearchAction(var1);
      }
   }

   public void setReplaceService(ReplaceService var1) {
      for (SearchFieldsPanel.Entry var3 : this.entries) {
         var3.setReplaceService(var1);
      }
   }

   public Predicate<IntegrationEntity> getSearcher(SearchOptions var1) {
      ArrayList var2 = Lists.newArrayList();

      for (SearchFieldsPanel.Entry var4 : this.entries) {
         var4.contributeSearchers(var2, var1);
      }

      return var1.getCombinedSearch(var2);
   }

   private static final class Enabler implements ActionListener {
      private final PropertySearchField target;
      private final SearchFieldsPanel.ReplaceAction replaceAction;

      public Enabler(PropertySearchField var1, SearchFieldsPanel.ReplaceAction var2) {
         this.target = var1;
         this.replaceAction = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         boolean var2 = ((JCheckBox)var1.getSource()).isSelected();
         this.target.setEnabled(var2);
         if (this.replaceAction != null) {
            this.replaceAction.updateEnabledState();
         }

         if (var2) {
            this.target.requestFocus();
         }
      }
   }

   private interface Entry {
      void setSearchAction(Action var1);

      void setReplaceService(ReplaceService var1);

      void contributeSearchers(List<Predicate<IntegrationEntity>> var1, SearchOptions var2);

      void layoutInFourColumns(NColumnPanel var1);

      void requestFocus();
   }

   private static class NestedPropertyEntry implements SearchFieldsPanel.Entry {
      private static final String BLANK_CARD = "blank";
      private final EnumPropertySearchField field;
      private final SearchFieldsPanel.SingleFieldEntry fieldEntry;
      private final CardPanel cards = new CardPanel();
      private final Map<Enum, SearchFieldsPanel> nestedPanels = Maps.newHashMap();
      private SearchFieldsPanel currentNestedPanel;
      private Action searchAction;
      private ReplaceService replaceService;

      public NestedPropertyEntry(EnumPropertySearchField var1) {
         this.field = var1;
         this.fieldEntry = new SearchFieldsPanel.SingleFieldEntry(var1);
         this.cards.add(new JLabel(" "), "blank");
         var1.addListener(new SearchFieldsPanel.NestedPropertyEntry.TypeFieldListener());
      }

      @Override
      public void setSearchAction(Action var1) {
         this.searchAction = var1;
         this.fieldEntry.setSearchAction(var1);

         for (SearchFieldsPanel var3 : this.nestedPanels.values()) {
            var3.setSearchAction(var1);
         }
      }

      @Override
      public void setReplaceService(ReplaceService var1) {
         this.replaceService = var1;
         this.fieldEntry.setReplaceService(var1);

         for (SearchFieldsPanel var3 : this.nestedPanels.values()) {
            var3.setReplaceService(var1);
         }
      }

      @Override
      public void contributeSearchers(List<Predicate<IntegrationEntity>> var1, SearchOptions var2) {
         this.fieldEntry.contributeSearchers(var1, var2);
         if (this.currentNestedPanel != null) {
            var1.add(this.currentNestedPanel.getSearcher(var2));
         }
      }

      @Override
      public void layoutInFourColumns(NColumnPanel var1) {
         this.fieldEntry.layoutInFourColumns(var1);
         var1.fillWith(BorderLayoutBuilder.fillWith(this.cards));
      }

      @Override
      public void requestFocus() {
         this.fieldEntry.requestFocus();
      }

      private final class TypeFieldListener implements EnumPropertySearchField.Listener {
         private TypeFieldListener() {
         }

         @Override
         public void enabledStateChanged(boolean var1) {
            if (var1) {
               this.displayTypeSpecificFields(NestedPropertyEntry.this.field.getValue());
            } else {
               this.hideTypeControls();
            }
         }

         private void displayTypeSpecificFields(Enum var1) {
            NestedPropertyEntry.this.currentNestedPanel = this.getNestedPanelFor(var1);
            NestedPropertyEntry.this.currentNestedPanel.setReplaceService(NestedPropertyEntry.this.replaceService);
            NestedPropertyEntry.this.cards.show(var1.name());
         }

         private SearchFieldsPanel getNestedPanelFor(Enum var1) {
            SearchFieldsPanel var2 = NestedPropertyEntry.this.nestedPanels.get(var1);
            if (var2 == null) {
               var2 = this.createNewNestedPanel(var1);
               JPanel var3 = BorderLayoutBuilder.fillWith(var2);
               var3.setOpaque(true);
               var3.setBorder(Empty.border(10, 5, 5, 5));
               UiUtils.setPanelBackgrounds(var3, Colors.NEUTRAL_BACKGROUND);
               NestedPropertyEntry.this.cards.add(var3, var1.name());
               NestedPropertyEntry.this.nestedPanels.put(var1, var2);
            }

            return var2;
         }

         private SearchFieldsPanel createNewNestedPanel(Enum var1) {
            List var2 = this.collectNestedSearchFields(var1);
            SearchFieldsPanel var3 = new SearchFieldsPanel(var2);
            if (NestedPropertyEntry.this.searchAction != null) {
               var3.setSearchAction(NestedPropertyEntry.this.searchAction);
            }

            return var3;
         }

         private List<PropertySearchField> collectNestedSearchFields(Enum var1) {
            List var2 = ((NestedProperty)NestedPropertyEntry.this.field.getProperty()).getAssociatedProperties(var1);
            ArrayList var3 = Lists.newArrayList();

            for (Object __etlItem232 : var2) {
               SearchableProperty var5 = (SearchableProperty) __etlItem232;
               PropertySearchField var6 = PropertySearchFieldFactory.createField(var5);
               if (var6 != null) {
                  var3.add(var6);
               }
            }

            return var3;
         }

         private void hideTypeControls() {
            NestedPropertyEntry.this.currentNestedPanel = null;
            NestedPropertyEntry.this.cards.show("blank");
         }

         @Override
         public void selectionChanged(Enum var1) {
            this.displayTypeSpecificFields(var1);
         }
      }
   }

   private static final class ReplaceAction extends AbstractAction {
      private final PropertySearchField field;
      private ReplaceService replaceService;

      public ReplaceAction(PropertySearchField var1) {
         super("Replace...");
         this.field = var1;
         this.setEnabled(false);
      }

      public void setReplaceService(ReplaceService var1) {
         this.replaceService = var1;
         this.updateEnabledState();
      }

      public void updateEnabledState() {
         this.setEnabled(this.field.isEnabled() && this.replaceService != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.replaceService.startReplace(this.field);
      }
   }

   private static class SingleFieldEntry implements SearchFieldsPanel.Entry {
      private final PropertySearchField field;
      private final KongaCheckBox activator;
      private final SearchFieldsPanel.ReplaceAction replaceAction;

      public SingleFieldEntry(PropertySearchField var1) {
         this.field = var1;
         if (var1.getProperty() instanceof ReplaceableProperty) {
            this.replaceAction = new SearchFieldsPanel.ReplaceAction(var1);
         } else {
            this.replaceAction = null;
         }

         this.activator = new KongaCheckBox();
         this.activator.addActionListener(new SearchFieldsPanel.Enabler(var1, this.replaceAction));
         var1.setEnabled(false);
      }

      @Override
      public void setSearchAction(Action var1) {
         this.field.setSearchAction(var1);
         this.activator.addActionListener(var1);
      }

      @Override
      public void setReplaceService(ReplaceService var1) {
         if (this.replaceAction != null) {
            this.replaceAction.setReplaceService(var1);
         }
      }

      @Override
      public void contributeSearchers(List<Predicate<IntegrationEntity>> var1, SearchOptions var2) {
         if (this.field.isEnabled()) {
            var1.add(this.field.getSearcher(var2));
         }
      }

      @Override
      public void layoutInFourColumns(NColumnPanel var1) {
         var1.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL);
         var1.setWeights(0, 0.0, 0.0);
         var1.setWeights(1, 0.0, 0.0);
         var1.setWeights(2, 1.0, 0.0);
         var1.setWeights(3, 0.0, 0.0);
         if (this.field instanceof TwoRowSearchField) {
            TwoRowSearchField var2 = (TwoRowSearchField)this.field;
            var1.setInsets(0, 0, 5, 5);
            var1.addRow(this.field.getProperty().getName(), this.activator, var2.getTopRow(), this.replaceAction);
            var1.setInsets(0, 0, 10, 5);
            var1.addRow(null, null, var2.getBottomRow(), null);
         } else {
            var1.setInsets(0, 0, 10, 5);
            var1.addRow(this.field.getProperty().getName(), this.activator, this.field, this.replaceAction);
         }
      }

      @Override
      public void requestFocus() {
         this.activator.requestFocusInWindow();
      }
   }
}
