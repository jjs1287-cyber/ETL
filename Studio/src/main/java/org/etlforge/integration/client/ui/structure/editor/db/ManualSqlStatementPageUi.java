package org.etlforge.integration.client.ui.structure.editor.db;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;
import org.etlforge.application.ui.widget.HideableSplitPane;
import org.etlforge.application.ui.widget.SectionedSplitPane;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.ui.TableIcon;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsController;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsModel;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsView;
import org.etlforge.integration.client.database.ui.sqlbuilder.NameGeneratorInstaller;
import org.etlforge.integration.client.database.ui.sqlbuilder.SqlTextArea;
import org.etlforge.integration.client.database.ui.sqlbuilder.TransferSelectedNodesAction;
import org.etlforge.integration.client.database.ui.sqlbuilder.tree.DbObjectsTree;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;

final class ManualSqlStatementPageUi implements UiProvider {
   private final SqlTextArea textArea = new SqlTextArea();
   private final DbObjectsView dbObjectsView;
   private final TestManualSqlStatementAction testAction;
   private ManualSqlStatementPageUi.Layout layout;

   public ManualSqlStatementPageUi(Source var1, DatabaseLocation var2, DatabaseInfoProvider var3, Receiver<ManualSqlStatement> var4) {
      this.testAction = new TestManualSqlStatementAction(var2, var1.getID(), this.textArea.asStringSupplier());
      this.testAction.setCallback(var4);
      this.textArea.installActionShortCut(this.testAction);
      this.dbObjectsView = this.createDbObjectsView(var1, var3);
   }

   private DbObjectsView createDbObjectsView(Source var1, DatabaseInfoProvider var2) {
      DbObjectsModel var3 = new DbObjectsModel(var1);
      var3.addModelListener(new NameGeneratorInstaller(this.textArea));
      DbObjectsTree var4 = new DbObjectsTree(var3);
      DbObjectsController var5 = new DbObjectsController(var2);
      var4.setController(var5);
      var4.setVisibleRowCount(4);
      var4.setDefaultAction(new TransferSelectedNodesAction(this.textArea));
      return var4;
   }

   public void setSql(String var1) {
      this.textArea.setSql(var1);
   }

   public String getSql() {
      return this.textArea.getSql();
   }

   public void setTestActionEnabled(boolean var1) {
      this.testAction.setEnabled(var1);
   }

   public void addChangeListener(DocumentListener var1) {
      this.textArea.addChangeListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layout = new ManualSqlStatementPageUi.Layout();
      this.layout.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.textArea.requestFocus();
   }

   private class Layout {
      private final SectionedSplitPane sections;

      public Layout() {
         HideableSplitPane var2 = this.createSplitPane();
         this.sections = new SectionedSplitPane(var2);
         this.layoutTextArea();
         this.layoutAvailableTables();
         this.sections.setControlPanelBorder(null);
      }

      private HideableSplitPane createSplitPane() {
         HideableSplitPane var1 = new HideableSplitPane(0, 1);
         var1.setDividerSize(4);
         var1.setResizeWeight(0.5);
         return var1;
      }

      private void layoutTextArea() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 3);
         var1.north(this.layoutToolbar()).center(ManualSqlStatementPageUi.this.textArea);
         this.sections.getSplitPane().setTopComponent(var1);
      }

      private UiProvider layoutToolbar() {
         return Horizontal.box().bottomAligned().add(ManualSqlStatementPageUi.this.testAction).glue();
      }

      private void layoutAvailableTables() {
         JPanel var1 = BorderLayoutBuilder.fillWith(ManualSqlStatementPageUi.this.dbObjectsView);
         var1.setMinimumSize(new Dimension(0, 0));
         this.sections.addSection("Available &Tables", new TableIcon(), var1);
      }

      public void decorate(UiCanvas var1) {
         this.sections.decorate(var1);
      }
   }
}
