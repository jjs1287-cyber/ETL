package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class QueryBuilderUi implements UiProvider {
   private final QueryBuilderModel model;
   private final AttributeSelector attributeSelector;
   private final OrderListUi orderListUi;
   private final FilterUi filterUi;
   private final LinkEntitiesListUi linkEntitiesUi;
   private final KongaTextArea xmlArea = new KongaTextArea(Size.rows(6), Size.columns(30));
   private final List<QueryBuilderUi.Box> additionalBoxes = Lists.newArrayList();

   public QueryBuilderUi(QueryBuilderModel var1, QueryBuilderController var2) {
      this.model = Preconditions.checkNotNull(var1, "model");
      Preconditions.checkNotNull(var2, "controller");
      this.attributeSelector = new AttributeSelector(var1.getAttributeSelectionModel());
      this.orderListUi = new OrderListUi(var1.getAttributeOrderModel());
      this.filterUi = new FilterUi(var1.getFilterDefinitionModel());
      this.linkEntitiesUi = new LinkEntitiesListUi(var1.getLinkEntitiesModel(), var2);
      this.xmlArea.setEditable(false);
      this.showResultingXml();
      var1.listenToAllChanges(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            QueryBuilderUi.this.showResultingXml();
         }
      });
   }

   private void showResultingXml() {
      final String var1 = this.model.buildXml();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            QueryBuilderUi.this.xmlArea.setText(var1);
            QueryBuilderUi.this.xmlArea.setCaretPosition(0);
         }
      });
   }

   public QueryBuilderModel getModel() {
      return this.model;
   }

   @Override
   public void decorate(UiCanvas var1) {
      QueryBuilderUi.Layout var2 = new QueryBuilderUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.attributeSelector.requestFocus();
   }

   public void addBox(QueryBuilderUi.Box var1) {
      this.additionalBoxes.add(Preconditions.checkNotNull(var1, "box"));
   }

   public static final class Box {
      private final UiProvider ui;
      private final String title;

      public Box(UiProvider var1, String var2) {
         this.ui = Preconditions.checkNotNull(var1, "ui");
         this.title = Preconditions.checkNotNull(var2, "title");
      }
   }

   private class Layout {
      private final KongaSplitPane topLevelSplit;

      public Layout() {
         KongaSplitPane var2 = this.layoutAttributeSelectorAndXmlAreaSplit();
         JComponent var3 = this.layoutFilterOrderAndLinkEntityLists();
         this.setMinSizes(var2, var3);
         this.topLevelSplit = KongaSplitPane.verticalSplit(var2, var3);
         this.topLevelSplit.setProportionalDividerLocation(0.7);
         this.topLevelSplit.setResizeWeight(0.7);
      }

      private KongaSplitPane layoutAttributeSelectorAndXmlAreaSplit() {
         JComponent var1 = new BorderLayoutBuilder()
            .withBorder(StyleSheet.getDefaultTitleBorder("Select Attributes"))
            .center(QueryBuilderUi.this.attributeSelector)
            .nonOpaque()
            .container();
         JComponent var2 = this.withTitleBorder(QueryBuilderUi.this.xmlArea.asUiProvider(), "Resulting Fetch XML");
         this.setMinSizes(var1, var2);
         KongaSplitPane var3 = KongaSplitPane.horizontalSplit(var1, var2);
         var3.setProportionalDividerLocation(0.3);
         var3.setResizeWeight(0.3);
         return var3;
      }

      private void setMinSizes(JComponent var1, JComponent var2) {
         Dimension var3 = new Dimension(100, 100);
         var1.setMinimumSize(var3);
         var2.setMinimumSize(var3);
      }

      private JComponent layoutFilterOrderAndLinkEntityLists() {
         ArrayList var1 = Lists.newArrayList();

         for (QueryBuilderUi.Box var3 : QueryBuilderUi.this.additionalBoxes) {
            var1.add(this.withTitleBorder(var3.ui, var3.title));
         }

         var1.add(this.withTitleBorder(QueryBuilderUi.this.filterUi, "Filter"));
         var1.add(this.withTitleBorder(QueryBuilderUi.this.orderListUi, "Order"));
         var1.add(this.withTitleBorder(QueryBuilderUi.this.linkEntitiesUi, "Link Entities"));
         return Horizontal.grid(12).of(var1).nonOpaque().container();
      }

      private JComponent withTitleBorder(UiProvider var1, String var2) {
         return new BorderLayoutBuilder().withBorder(StyleSheet.getDefaultTitleBorder(var2)).center(var1).nonOpaque().container();
      }

      public void decorate(UiCanvas var1) {
         var1.display(this.topLevelSplit);
      }
   }
}
