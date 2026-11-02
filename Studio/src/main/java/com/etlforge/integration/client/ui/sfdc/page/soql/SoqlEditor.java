package com.etlforge.integration.client.ui.sfdc.page.soql;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.soql.TestQueryAction;
import com.etlforge.integration.client.ui.sfdc.soql.browser.SObjectSchemaBrowser;
import com.etlforge.integration.client.ui.sfdc.soql.browser.SchemaBrowserActionListener;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import com.etlforge.integration.salesforce.ui.NameDisplayModes;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.wsdl.ObjectSchemaLookup;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.util.AbstractObservableValue;

public final class SoqlEditor implements Disposable {
   private final SalesforceFacade sfdcFacade;
   private SoqlInputArea soqlArea;
   private SoqlEditor.SchemaLookup schemaLookup;
   private SoqlEditor.SelectedObject selectedObject;
   private SObjectSchemaBrowser schemaBrowser;
   private Optional<QueryTester> tester = Optional.absent();
   private NameDisplayModes nameDisplayModes = NameDisplayModes.api();

   public SoqlEditor(SalesforceFacade var1) {
      this.sfdcFacade = Preconditions.checkNotNull(var1);
   }

   @Override
   public void dispose() {
      this.sfdcFacade.close();
   }

   public SalesforceFacade getSfdcFacade() {
      return this.sfdcFacade;
   }

   public void setNameDisplayModes(NameDisplayModes var1) {
      this.nameDisplayModes = Preconditions.checkNotNull(var1);
   }

   public void setQueryTester(QueryTester var1) {
      this.tester = Optional.of(var1);
   }

   public String edit(String var1, String var2, SalesforceConnector var3) {
      if (this.soqlArea == null) {
         this.createUiComponents();
      }

      this.prepareSoqlArea(var1);
      SoqlEditor.RefreshAction var4 = new SoqlEditor.RefreshAction();
      Optional var5 = this.createTestAction();
      SoqlEditor.Ui var6 = new SoqlEditor.Ui(var4, var5);
      this.prepareObjectTree(var2, var3, var6.startWait());
      return this.openUi(var6);
   }

   private Optional<? extends Action> createTestAction() {
      if (this.tester.isPresent()) {
         TestQueryAction var1 = new TestQueryAction(this.soqlArea.getSoqlProperty(), false);
         var1.setTester(this.tester.get());
         return Optional.of(var1);
      } else {
         return Optional.absent();
      }
   }

   private void createUiComponents() {
      this.soqlArea = new SoqlInputArea();
      this.soqlArea.setIncludeCaption(false);
      this.schemaLookup = new SoqlEditor.SchemaLookup();
      this.selectedObject = new SoqlEditor.SelectedObject(this.schemaLookup);
      this.schemaBrowser = new SObjectSchemaBrowser(this.schemaLookup, this.selectedObject, this.nameDisplayModes).withVisibleRowCount(8);
      this.schemaBrowser.addActionListener(new SchemaBrowserActionListener() {
         @Override
         public void objectSelected(String var1) {
            SoqlEditor.this.soqlArea.insertObjectName(var1);
         }

         @Override
         public void fieldsSelected(Optional<String> var1, List<String> var2) {
            SoqlEditor.this.soqlArea.insertFieldNames(var1.or(""), var2);
         }
      });
   }

   private void prepareSoqlArea(String var1) {
      this.soqlArea.setQueryString(var1);
   }

   private void prepareObjectTree(String var1, SalesforceConnector var2, WaitLock var3) {
      this.schemaLookup.setConnector(var2);
      this.selectedObject.populate(var1, var3);
   }

   private String openUi(SoqlEditor.Ui var1) {
      KongaDialog var2 = this.createDialog(var1);
      var2.setVisible(true);
      return var2.wasOkPressed() ? var1.getSoql() : null;
   }

   private KongaDialog createDialog(SoqlEditor.Ui var1) {
      JComponent var2 = WizardStyleDialog.wrapUi(var1);
      WizardStyleDialog var3 = new WizardStyleDialog(ApplicationUi.getAppWinFrame(), "Edit SOQL", var2);
      var3.getOKButton().setText("Save");
      var3.getOKButton().setIcon(CommonIcons.SAVE_16);
      var3.setFocusedComponent(var1);
      var3.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
      var3.setOkButtonAsDefault();
      var3.manageLocation(SoqlEditor.class);
      return var3;
   }

   private class RefreshAction extends AbstractAction {
      private WaitService waitService = WaitService.NONE;

      public RefreshAction() {
         ActionDecorators.REFRESH.decorate(this);
         this.putValue("ShortDescription", "Refresh the list of fields");
      }

      public void setWaitService(WaitService var1) {
         this.waitService = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               RefreshAction.this.refresh();
            }
         });
      }

      private void refresh() {
         WaitLock var1 = this.waitService.startWait();
         SoqlEditor.this.selectedObject.refresh(var1);
      }
   }

   private class SchemaLookup extends ObjectSchemaLookup {
      private boolean loggedIn;
      private final Object lock = new Object();
      private volatile SalesforceConnector connector;
      private volatile boolean refresh;

      private SchemaLookup() {
      }

      public void setConnector(SalesforceConnector var1) {
         this.connector = var1;
      }

      public void setRefresh(boolean var1) {
         this.refresh = var1;
         if (var1) {
            this.reset();
         }
      }

      @Override
      protected SObjectSchema lookup(String var1) throws Exception {
         synchronized (this.lock) {
            if (!this.loggedIn) {
               SoqlEditor.this.sfdcFacade.login(this.connector);
               this.loggedIn = true;
            }
         }

         if (this.refresh) {
            return SoqlEditor.this.sfdcFacade.refreshFlatObject(var1);
         }

         SObjectSchemaProvider var5 = SoqlEditor.this.sfdcFacade.getSchemaProvider();
         return var5.describeObject(var1);
      }
   }

   private class SelectedObject extends AbstractObservableValue<SObjectSchema> {
      private final SoqlEditor.SchemaLookup schemaLookup;
      private String objectName;
      private volatile SObjectSchema so;

      public SelectedObject(SoqlEditor.SchemaLookup var2) {
         this.schemaLookup = var2;
      }

      public SObjectSchema get() {
         return this.so;
      }

      public void populate(String var1, WaitLock var2) {
         this.objectName = var1;
         this.populate(false, var2);
      }

      private void populate(final boolean var1, final WaitLock var2) {
         Callable var3 = new Callable<SObjectSchema>() {
            public SObjectSchema call() throws Exception {
               TimeUnit.MILLISECONDS.sleep(250L);
               SelectedObject.this.schemaLookup.setRefresh(var1);
               return SelectedObject.this.schemaLookup.apply(SelectedObject.this.objectName);
            }
         };
         Receiver var4 = new Receiver<SObjectSchema>() {
            public void handle(SObjectSchema var1) {
               SelectedObject.this.set(var1);
            }
         };
         Computation.create(var3, var4).swingWorkerBuilder().runWhenFinished(new Runnable() {
            @Override
            public void run() {
               var2.release();
            }
         }).buildAndExecute();
      }

      private void set(SObjectSchema var1) {
         if (var1 != this.so) {
            SObjectSchema var2 = this.so;
            this.so = var1;
            this.fireValueChange(var2, var1);
         }
      }

      public void refresh(WaitLock var1) {
         this.set(null);
         this.populate(true, var1);
      }
   }

   private class Ui implements UiProvider {
      private final KongaLayeredPane treePane = new KongaLayeredPane();
      private final KongaSplitPane splitPane;

      public Ui(SoqlEditor.RefreshAction var2, Optional<? extends Action> var3) {
         var2.setWaitService(this.treePane);
         this.splitPane = KongaSplitPane.verticalSplit(
            this.layoutSoqlArea(SoqlEditor.this.soqlArea, var3), this.layoutTree(this.treePane, SoqlEditor.this.schemaBrowser, var2)
         );
         this.splitPane.setResizeWeight(0.5);
         ActionKeyBinding.install(var2, this.splitPane, 1);
      }

      private UiProvider layoutSoqlArea(SoqlInputArea var1, Optional<? extends Action> var2) {
         var1.setLabel("&Query string:");
         return var2.isPresent()
            ? new BorderLayoutBuilder(0, 5).withEmptyBorder(0, 0, 5, 0).center(var1).south(Horizontal.Align.east(var2.get())).nonOpaque()
            : var1;
      }

      private UiProvider layoutTree(KongaLayeredPane var1, SObjectSchemaBrowser var2, SoqlEditor.RefreshAction var3) {
         JComponent var4 = new BorderLayoutBuilder(0, 5).center(var2).south(Horizontal.Align.west(var3)).container();
         var1.setBaseComponent(var4);
         return var1.asUiProvider();
      }

      public WaitLock startWait() {
         return this.treePane.startWait();
      }

      @Override
      public void decorate(UiCanvas var1) {
         var1.display(this.splitPane);
      }

      @Override
      public void requestFocus() {
         SoqlEditor.this.soqlArea.requestFocus();
      }

      public String getSoql() {
         return SoqlEditor.this.soqlArea.getQueryString();
      }
   }
}
