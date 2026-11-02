package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.Relationship;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.widget.list.FilteredKongaList;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.StringMatcherListFilterFunction;

final class RelationshipSelector implements UiProvider {
   private final FilteredKongaList list;

   public RelationshipSelector(LinkEntitiesModel var1) {
      this.list = createList(var1);
   }

   private static FilteredKongaList createList(LinkEntitiesModel var0) {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setCellRenderer(new RelationshipSelector.Renderer(var0));
      var1.setLabel(new JLabel("Select a relationship for the link. (Grayed out relationships are already in use.)"));
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      var1.setSelectionConverter(new RelationshipSelector.ValidSelectionConverter(var0));
      FilteredListModel var2 = createListModel(var0.getObject());
      KongaList var3 = var1.newList(var2);
      return FilteredKongaList.builder(var3)
         .withFilterLabelStyle(WizardStyleSheet.SMALL_CAPTION_STYLE)
         .withFilterFunction(getFilterFunction())
         .buildWithFilterBelow();
   }

   private static FilteredListModel createListModel(MSCrmObject var0) {
      DefaultListModel var1 = new DefaultListModel();
      addRelationshipsToListModel(var1, "1:N", var0.getOneToManyRelationships());
      addRelationshipsToListModel(var1, "N:1", var0.getManyToOneRelationships());
      addRelationshipsToListModel(var1, "N:N", var0.getManyToManyRelationships());
      return new FilteredListModel(var1);
   }

   private static void addRelationshipsToListModel(DefaultListModel var0, String var1, Iterable<? extends Relationship> var2) {
      var0.addElement(var1);

      for (RelationshipSelector.RelationshipCapsule var5 : encapsulate(var2)) {
         var0.addElement(var5);
      }

      var0.addElement("");
   }

   private static List<RelationshipSelector.RelationshipCapsule> encapsulate(Iterable<? extends Relationship> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (Relationship var3 : var0) {
         var1.add(new RelationshipSelector.RelationshipCapsule(var3));
      }

      Collections.sort(var1);
      return var1;
   }

   private static Function<String, ListFilter> getFilterFunction() {
      Function var0 = new Function<Object, String>() {
         @Nullable
         public String apply(Object var1) {
            return var1 instanceof RelationshipSelector.RelationshipCapsule ? ((RelationshipSelector.RelationshipCapsule)var1).text : null;
         }
      };
      return StringMatcherListFilterFunction.of(StringMatchers.CONTAINS_IGNORE_CASE, var0);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
      this.list.selectFirst();
   }

   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   public Relationship getSelectedRelationship() {
      IndexedSelection var1 = this.list.getSelection();
      return var1.isEmpty() ? null : (Relationship)var1.head();
   }

   public SelectionSource asSelectionSource() {
      return this.list;
   }

   private static class RelationshipCapsule implements Comparable<RelationshipSelector.RelationshipCapsule> {
      public final Relationship relationship;
      public final String text;

      public RelationshipCapsule(Relationship var1) {
         this.relationship = var1;
         this.text = var1.toString();
      }

      public int compareTo(RelationshipSelector.RelationshipCapsule var1) {
         return String.CASE_INSENSITIVE_ORDER.compare(this.text, var1.text);
      }

      @Override
      public String toString() {
         return this.text;
      }
   }

   private static class Renderer extends DefaultListCellRenderer {
      private final LinkEntitiesModel model;

      public Renderer(LinkEntitiesModel var1) {
         this.model = var1;
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof RelationshipSelector.RelationshipCapsule) {
            this.renderRelationship((RelationshipSelector.RelationshipCapsule)var2, var1, var4);
         } else if (var2 instanceof String) {
            this.renderHeader((String)var2, var1, var4);
         }

         return this;
      }

      private void renderRelationship(RelationshipSelector.RelationshipCapsule var1, JList var2, boolean var3) {
         Font var4 = var2.getFont();
         this.setFont(var4);
         if (!var3) {
            Color var5 = this.model.contains(var1.relationship) ? Color.LIGHT_GRAY : Color.BLACK;
            this.setForeground(var5);
         }

         this.setText("  " + var1.text);
      }

      private void renderHeader(String var1, JList var2, boolean var3) {
         Font var4 = Fonts.bold(var2);
         this.setFont(var4);
         if (!var3) {
            this.setForeground(Color.BLACK);
         }

         this.setText(var1);
      }
   }

   private static class ValidSelectionConverter implements SelectionConverter {
      private final LinkEntitiesModel model;

      private ValidSelectionConverter(LinkEntitiesModel var1) {
         this.model = var1;
      }

      @Override
      public Object fromSelection(Object var1) {
         if (var1 instanceof RelationshipSelector.RelationshipCapsule) {
            Relationship var2 = ((RelationshipSelector.RelationshipCapsule)var1).relationship;
            if (!this.model.contains(var2)) {
               return var2;
            }
         }

         return null;
      }

      @Override
      public Object toSelection(Object var1) {
         return null;
      }
   }
}
