package com.etlforge.integration.client.ui.script.fcn.db;

import com.etlforge.integration.client.ui.script.fcn.TestAction;
import com.etlforge.integration.client.ui.script.fcn.TestConsole;
import com.etlforge.integration.client.ui.script.fcn.VariableSelector;
import com.etlforge.integration.client.ui.script.fcn.db.sql.ReservedWordsControlPanel;
import com.etlforge.integration.client.ui.script.fcn.db.sql.SqlStringArea;
import com.etlforge.integration.client.ui.script.fcn.db.sql.Template;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentListener;
import org.etlforge.integration.client.database.ui.TableIcon;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsController;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsModel;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsView;
import org.etlforge.integration.client.database.ui.sqlbuilder.NameGeneratorInstaller;
import org.etlforge.integration.client.database.ui.sqlbuilder.TransferSelectedNodesAction;
import org.etlforge.integration.client.database.ui.sqlbuilder.tree.DbObjectsTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.SplitPaneCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;

final class BuildSqlStatementPageUi implements UiProvider {
   private final SqlStringArea textArea = new SqlStringArea();
   private final UiProvider reservedWordsControlPanel = new ReservedWordsControlPanel(this.textArea, "WHERE", "AND", "OR");
   private final DbObjectsView dbObjectsView;
   private final VariableSelector variableSelector;
   private TestConsole testConsole;
   private TestAction testAction;
   private BuildSqlStatementPageUi.Layout layout;

   public BuildSqlStatementPageUi(Locatable var1, GlobalDataElements var2, FormulaBuilderSourceTree var3, DbObjectsController var4) {
      this.dbObjectsView = this.createDbObjectsView(var1, var4);
      this.variableSelector = this.createVariableSelector(var2, var3);
   }

   private DbObjectsView createDbObjectsView(Locatable var1, DbObjectsController var2) {
      DbObjectsModel var3 = new DbObjectsModel(var1);
      var3.addModelListener(new NameGeneratorInstaller(this.textArea));
      DbObjectsTree var4 = new DbObjectsTree(var3);
      var4.setController(var2);
      var4.setVisibleRowCount(4);
      var4.setDefaultAction(new TransferSelectedNodesAction(this.textArea));
      return var4;
   }

   private VariableSelector createVariableSelector(GlobalDataElements var1, FormulaBuilderSourceTree var2) {
      VariableSelector var3 = new VariableSelector(var1, var2);
      var3.setGlobalDataElementReceiver(new Receiver<String>() {
         public void handle(String var1) {
            BuildSqlStatementPageUi.this.textArea.insertGlobalDataElement(var1);
         }
      });
      var3.setSourceDataElementReceiver(new Receiver<String>() {
         public void handle(String var1) {
            BuildSqlStatementPageUi.this.textArea.insertSourceDataElement(var1);
         }
      });
      return var3;
   }

   public String getSql() {
      return this.textArea.getSql();
   }

   public void setTemplate(Template var1) {
      this.textArea.setTemplate(var1);
   }

   public boolean isValid() {
      String var1 = this.getSql();
      return !var1.isEmpty();
   }

   public void addChangeListener(DocumentListener var1) {
      this.textArea.addChangeListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layout = new BuildSqlStatementPageUi.Layout();
      this.layout.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.textArea.requestFocus();
   }

   public void installTestConsole(TestConsole var1, TestAction var2) {
      this.testConsole = var1;
      this.testAction = var2;
   }

   public final void showTestSection() {
      if (this.testConsole != null && this.layout != null) {
         this.layout.showTestSection();
      }
   }

   private class Layout {
      private final KongaSplitPane pageSplit = new KongaSplitPane(1);
      private KongaTabbedPane tablesAndTestTabs;

      public Layout() {
         this.layoutLeftSide();
         this.layoutRightSide();
         this.pageSplit.setBorder(new LineBorder(Color.GRAY, 2));
         this.pageSplit.setResizeWeight(0.75);
      }

      private void layoutLeftSide() {
         KongaSplitPane var1 = KongaSplitPane.verticalSplit(this.layoutSqlArea(), this.layoutBottomView());
         var1.setResizeWeight(0.5);
         this.pageSplit.setLeftComponent(var1);
      }

      private UiProvider layoutSqlArea() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder();
         var1.center(BuildSqlStatementPageUi.this.textArea).south(this.layoutControlPanel());
         return var1;
      }

      private UiProvider layoutBottomView() {
         if (BuildSqlStatementPageUi.this.testConsole == null) {
            return BuildSqlStatementPageUi.this.dbObjectsView;
         }

         this.tablesAndTestTabs = new KongaTabbedPane();
         this.tablesAndTestTabs.addTab("Tables", new TableIcon(), BuildSqlStatementPageUi.this.dbObjectsView);
         this.tablesAndTestTabs.addTab("Test", Icons.TEST_ICON_16, BuildSqlStatementPageUi.this.testConsole);
         this.tablesAndTestTabs.setSelectedIndex(0);
         return this.tablesAndTestTabs.asUiProvider();
      }

      private UiProvider layoutControlPanel() {
         if (BuildSqlStatementPageUi.this.testAction == null) {
            return BuildSqlStatementPageUi.this.reservedWordsControlPanel;
         }

         BoxBuilder var1 = BoxBuilder.horizontal();
         var1.add(BuildSqlStatementPageUi.this.reservedWordsControlPanel).glue().add(BuildSqlStatementPageUi.this.testAction);
         return var1;
      }

      private void layoutRightSide() {
         BuildSqlStatementPageUi.this.variableSelector.decorate(SplitPaneCanvas.right(this.pageSplit));
      }

      public void decorate(UiCanvas var1) {
         var1.display(this.pageSplit);
      }

      public final void showTestSection() {
         if (this.tablesAndTestTabs != null) {
            this.tablesAndTestTabs.setSelectedIndex(1);
         }
      }
   }
}
