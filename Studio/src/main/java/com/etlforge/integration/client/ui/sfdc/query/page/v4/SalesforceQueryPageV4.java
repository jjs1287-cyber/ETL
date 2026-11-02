package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.ui.sfdc.DefaultRetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.SfdcWizardPageCategory;
import com.etlforge.integration.client.ui.sfdc.page.v4.AbstractSalesforceItemPageV4;
import com.etlforge.integration.client.ui.sfdc.soql.v4.QuerySyntaxValidator;
import com.etlforge.integration.client.ui.sfdc.soql.v4.QueryValidator;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.config.SalesforceConfig;
import org.etlforge.ui.text.contentassist.CompletionContext;

public final class SalesforceQueryPageV4 extends AbstractSalesforceItemPageV4<SalesforceQuery> {
   private QueryValidator queryValidator = new QuerySyntaxValidator();
   private CompletionContext completionContext;
   private final BatchSizeEditor batchSizeEditor;
   private final DefaultRetryPolicyEditor retryPolicyEditor;

   public SalesforceQueryPageV4(SalesforceQuery var1) {
      super(var1, SfdcMethod.QUERY);
      this.retryPolicyEditor = this.createRetryPolicyEditor(var1);
      this.batchSizeEditor = new BatchSizeEditor(var1, SalesforceConfig.getInstance().getMethodConfig(SfdcMethod.QUERY));
      this.addResource(new SalesforceQueryPageV4.FieldSelectorCloser());
   }

   private DefaultRetryPolicyEditor createRetryPolicyEditor(SalesforceQuery var1) {
      DefaultRetryPolicyEditor var2 = new DefaultRetryPolicyEditor(var1);
      var2.addUndoableEditListener(new UndoableEditListener() {
         @Override
         public void undoableEditHappened(UndoableEditEvent var1) {
            SalesforceQueryPageV4.this.undoableEditHappened(var1);
            SalesforceQueryPageV4.this.setDirty();
         }
      });
      return var2;
   }

   @Override
   protected EditorCategory createCategory() {
      return SfdcWizardPageCategory.CATEGORY;
   }

   public void setQueryValidator(QueryValidator var1) {
      SalesforceQueryPanelV4 var2 = this.getDetailsPanel();
      if (var2 == null) {
         this.queryValidator = var1;
      } else {
         var2.setQueryValidator(var1);
      }
   }

   public void setCompletionContext(CompletionContext var1) {
      SalesforceQueryPanelV4 var2 = this.getDetailsPanel();
      if (var2 == null) {
         this.completionContext = var1;
      } else {
         var2.setCompletionContext(var1);
      }
   }

   public SalesforceQueryPanelV4 getDetailsPanel() {
      return (SalesforceQueryPanelV4)super.getDetailsPanel();
   }

   protected SalesforceQueryPanelV4 createDataPanel() {
      SalesforceQueryPanelV4 var1 = new SalesforceQueryPanelV4(this.getWindow(), this.getObject(), this.getLinks());
      this.doDefaultPanelConfiguration(var1);
      var1.setBatchSizeEditor(this.batchSizeEditor);
      var1.setRetryPolicyEditor(this.retryPolicyEditor);
      var1.setQueryValidator(this.queryValidator);
      if (this.completionContext != null) {
         var1.setCompletionContext(this.completionContext);
      }

      return var1;
   }

   boolean hasQueryStringOrBatchSizeBeenModified() {
      SalesforceQuery var1 = this.getObject();
      String var2 = var1.getQueryString();
      String var3 = this.getQueryString();
      return this.batchSizeEditor.hasChanged() || !var3.equals(var2);
   }

   String getQueryString() {
      return this.getDetailsPanel().getQueryString();
   }

   int getBatchSize() {
      return this.batchSizeEditor.getBatchSize();
   }

   RetryPolicy getRetryPolicy() {
      return this.retryPolicyEditor.getPolicy();
   }

   private class FieldSelectorCloser extends AbstractWindowElementResource {
      public FieldSelectorCloser() {
         super(WindowElementStateSpan.WHEN_ACTIVE);
      }

      @Override
      public void dispose(WindowElement var1) {
         SalesforceQueryPageV4.this.getDetailsPanel().closeFieldSelector();
      }
   }
}
