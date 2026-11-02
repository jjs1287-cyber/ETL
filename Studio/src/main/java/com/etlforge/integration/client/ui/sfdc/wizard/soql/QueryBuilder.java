package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.soql.TestQueryAction;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.relational.RelationalFieldSelector;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;

public final class QueryBuilder implements UiProvider {
   private final FieldSelector flatFieldSelector;
   private final ConditionBuilder conditionBuilder;
   private final SoqlArea soqlArea;
   private final TestQueryAction testAction;
   private Optional<RelationalFieldSelector> relationalFieldSelector = Optional.absent();

   public QueryBuilder(QueryBuilderModel var1) {
      this.flatFieldSelector = new FieldSelector(var1);
      this.conditionBuilder = new ConditionBuilder(var1).withBorder(new TitledBorder("Conditions"));
      this.soqlArea = new SoqlArea(var1);
      this.testAction = new TestQueryAction(var1.getProperty("soql"), var1.isQueryAll());
   }

   public void setRelationalFieldSelector(RelationalFieldSelector var1) {
      this.relationalFieldSelector = Optional.of(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Vertical.VerticalBorderLayoutBuilder var2 = this.layoutConditionBuilderAndSoqlArea();
      if (this.relationalFieldSelector.isPresent()) {
         KongaTabbedPane var3 = new KongaTabbedPane();
         var3.addTab("Simple Query", this.flatFieldSelector);
         var3.addTab("Relationship Query", this.relationalFieldSelector.get());
         KongaSplitPane var4 = KongaSplitPane.horizontalSplit(var3, var2.withBorder(Empty.border(0, 8, 0, 0)));
         var4.setDividerLocation(300);
         var1.display(var4);
      } else {
         new BorderLayoutBuilder(12, 0).west(this.flatFieldSelector).center(var2).decorate(var1);
      }
   }

   private Vertical.VerticalBorderLayoutBuilder layoutConditionBuilderAndSoqlArea() {
      return Vertical.borderLayout().withGap(12).of(this.conditionBuilder, this.layoutSoqlArea(), null);
   }

   private JComponent layoutSoqlArea() {
      return new BorderLayoutBuilder(0, 5).center(this.soqlArea).south(Horizontal.Align.east(this.testAction)).container();
   }

   public void setQueryTester(QueryTester var1) {
      this.testAction.setTester(var1);
   }

   @Override
   public void requestFocus() {
      this.flatFieldSelector.requestFocus();
   }

   public void disableQueryAll(String var1) {
      this.soqlArea.disableQueryAll(var1);
   }
}
