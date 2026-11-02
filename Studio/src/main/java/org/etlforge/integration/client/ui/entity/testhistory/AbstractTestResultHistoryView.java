package org.etlforge.integration.client.ui.entity.testhistory;

import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectTestHistory;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.integration.data.test.TestResultFilter;
import org.etlforge.integration.data.test.TestResultHistory;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;

public abstract class AbstractTestResultHistoryView<T extends EntityTestResult> extends AbstractWindowSectionContent implements TestResultHistoryView {
   private final EntityTestHistoryTable<T> table;
   private final DeleteSelectedHistoryEntriesAction<T> deleteAction;
   private final ViewTestResultDetailsAction detailsAction;
   private final OpenFilterUiAction openFilterUiAction;

   protected AbstractTestResultHistoryView(String var1, Class<T> var2, EntityTestHistoryTable<T> var3, WindowSection var4) {
      super(var1, var4);
      this.table = var3;
      this.detailsAction = new ViewTestResultDetailsAction();
      this.deleteAction = new DeleteSelectedHistoryEntriesAction<>(var2);
      var3.installActions(this.detailsAction, this.deleteAction);
      this.openFilterUiAction = new OpenFilterUiAction(new AbstractTestResultHistoryView.FilterCallback());
      this.addResource(new AbstractTestResultHistoryView.DynamicTableResource());
   }

   @Override
   public final void setProject(ManagedProject var1) {
      if (var1 != null) {
         TestResultHistory var2 = this.getHistory(var1.getTestHistory());
         IntegrationEntityLookup var3 = var1.getProject().getItemLookup();
         this.table.setHistory(var2, var3);
         this.detailsAction.setEntityLookup(var3);
         this.deleteAction.setHistory(var2);
      } else {
         this.table.setHistory(null, null);
         this.detailsAction.setEntityLookup(null);
         this.deleteAction.setHistory(null);
      }
   }

   protected abstract TestResultHistory<T> getHistory(ProjectTestHistory var1);

   @Override
   public final void setFilter(TestResultFilter var1) {
      this.table.setFilter(var1);
   }

   @Override
   public final void decorate(JComponent var1) {
      BorderLayoutBuilder.from(var1).north(this.getTitlePanel()).center(this.table);
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      KongaButton var2 = new KongaButton(this.openFilterUiAction);
      ButtonUtils.configureButtonForToolBar(var2);
      var1.add(var2);
      var1.add(KongaButton.forToolbar(this.detailsAction));
      var1.add(KongaButton.forToolbar(this.deleteAction));
      super.populateToolBar(var1);
   }

   private class DynamicTableResource extends AbstractWindowElementResource {
      private DynamicTableResource() {
      }

      @Override
      public void dispose(WindowElement var1) {
         AbstractTestResultHistoryView.this.table.dispose();
      }
   }

   private class FilterCallback implements Receiver<TestResultFilter> {
      private FilterCallback() {
      }

      public void handle(TestResultFilter var1) {
         AbstractTestResultHistoryView.this.table.setFilter(var1);
      }
   }
}
