package org.etlforge.integration.activity.ui.queue;

import javax.swing.JComponent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.CloseEditorAction;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.activity.ui.component.page.AbstractActivityPage;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.integration.activity.ui.log.SortStatus;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.ActionEnabledBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.WaitStateBinding;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.focus.FocusUtils;

public final class OperationQueuePage extends AbstractActivityPage {
   private final OperationQueueModel model;
   private final OperationQueueController controller;
   private final QueueNavigationControl navigationControl;
   private final QueueDisplayer displayer;
   private final DeleteQueueAction deleteAction;

   public OperationQueuePage(ActivitySubject var1, OperationQueueProvider var2) {
      super(EditorIdGenerator.get(OperationQueuePage.class), var1);
      this.model = new OperationQueueModel(var1);
      this.model.setSortStatus(SortStatus.descending(OperationLogTableColumn.TIME_RECEIVED));
      this.controller = new OperationQueueController(this.model, var2);
      this.deleteAction = new DeleteQueueAction(this.controller);
      this.navigationControl = this.createNavigationControl();
      this.displayer = new QueueDisplayer(this.model, this.navigationControl.getFilters(), this.deleteAction);
      this.setIcon(PackageResources.Page.ICON);
      this.setCategory(new EditorCategory("Operation Queues", this.getSmallIcon()));
      this.addKeyBinding(KeyUtils.REFRESH, this.navigationControl.getRefreshAction());
   }

   private QueueNavigationControl createNavigationControl() {
      QueueNavigationControl var1 = new QueueNavigationControl(this.model, this.controller);
      var1.addControl(this.createHelpButton("activity.operation.queue"), 2147483646);
      var1.setCloseAction(new CloseEditorAction(this));
      var1.applyFiltersToModel();
      var1.setDisplayedSubject(this.getObject());
      return var1;
   }

   @Override
   public String getTitle() {
      String var1 = this.navigationControl.getTitle();
      ActivitySubject var2 = this.getObject();
      if (var2 != null) {
         var1 = var1 + " " + var2.getName();
      } else {
         var1 = PackageResources.Page.getEmptyPageTitle(var1);
      }

      return var1;
   }

   @Override
   public void retrieveRecords() {
      this.controller.downloadQueue();
   }

   @Override
   protected void displayedEntityNameChanged() {
      this.navigationControl.setDisplayedSubject(this.getObject());
   }

   @Override
   protected String getMementoPathLeaf() {
      return "OperationQueue";
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.navigationControl.setSubjectIcon(this.getSubjectIcon());
      this.bindProperties();
      this.layoutPage();
   }

   private void bindProperties() {
      Bindings var1 = new Bindings(
         new WaitStateBinding(this.model.getProperty("talkingToServer"), this), ActionEnabledBinding.forBoolean(this.deleteAction, this.model, "deleteEnabled")
      );
      var1.syncUi();
   }

   private void layoutPage() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder();
      JComponent var2 = this.navigationControl.getDisplayer();
      FocusUtils.makeComponentMouseFocusable(var2);
      var1.north(var2).center(this.displayer);
      this.setDisplayPanel(var1.container());
   }

   @Override
   public void requestFocus() {
      this.displayer.requestFocus();
   }
}
