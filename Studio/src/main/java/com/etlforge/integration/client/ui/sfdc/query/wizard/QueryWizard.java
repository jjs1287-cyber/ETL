package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflow;
import com.etlforge.integration.client.sfdc.data.SoqlObject;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.QueryBuilderModel;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.relational.Controller;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.relational.RelationalFieldSelector;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.relational.RelationalFieldSelectorModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.wsdl.ObjectSchemaLookup;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class QueryWizard extends SalesforceWizard<SalesforceQuery> {
   private final PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);
   private final QueryWorkflowBuilder workflowBuilder;
   private final QueryBuilderModel queryBuilderModel;

   public QueryWizard(EntityWizardContext var1, SalesforceQuery var2, Folder var3, SalesforceFacade var4, QueryBuilderModel var5) {
      super(var1, var2, var3, SfdcMethod.QUERY, var4);
      this.queryBuilderModel = var5;
      this.workflowBuilder = new QueryWorkflowBuilder(var1.getProject(), var2, var3);
      this.setWorkflowBuilder(this.workflowBuilder);
   }

   protected QueryWizardPageChain getPageChain() {
      return (QueryWizardPageChain)super.getPageChain();
   }

   public ResponseTextStructureFactory getResponseStructureFactory() {
      return this.workflowBuilder.getResponseTextStructureFactory();
   }

   public Transformation getRequestTransformation() {
      return this.workflowBuilder.getRequest();
   }

   public Transformation getResponseTransformation() {
      return this.workflowBuilder.getResponse();
   }

   @Override
   protected void postStarted() {
      this.getDisplayer().setSection("Login");
   }

   @Override
   protected void applyPages() {
      QueryWizardPageChain var1 = this.getPageChain();
      var1.apply();
   }

   public RelationalFieldSelector createRelationalFieldSelector(QueryBuilderModel var1) {
      RelationalFieldSelectorModel var2 = new RelationalFieldSelectorModel(var1);
      Controller var3 = new Controller(var2, ObjectSchemaLookup.fromSchemaProvider(this.getSchemaProvider()));
      return new RelationalFieldSelector(var2, var3);
   }

   public ParsedQuery getParsedQuery() {
      return this.workflowBuilder.getParsedQuery();
   }

   public void setParsedQuery(ParsedQuery var1, boolean var2) {
      this.workflowBuilder.setParsedQuery(var1, var2);
      this.updateWizardName(var1, var2);
   }

   private void updateWizardName(ParsedQuery var1, boolean var2) {
      SObjectSchema var3 = this.lookupObjectDefinition(var1.getObjectName());
      String var4 = var3 != null ? var3.getLabelPlural() : var1.getObjectName();
      String var5 = String.format("Query%s %s", var2 ? " All" : "", var4);
      this.setWizardItemName(var5);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.addPropertyChangeListener(var2);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.removePropertyChangeListener(var2);
   }

   public QueryWorkflowBuilder getWorkflowBuilder() {
      return this.workflowBuilder;
   }

   public SoapWizardWorkflow<SalesforceQuery> buildWorkflow(QueryTarget var1, Target var2, Options var3) {
      this.workflowBuilder.configureTarget(var2, var1);
      SoqlObject var4 = this.queryBuilderModel.getSoqlObject();
      this.workflowBuilder.applyResponseTransformationFilter();
      this.workflowBuilder.applyAutoMappingSetting(this.isAutoMap());
      SoapWizardWorkflow var5 = this.workflowBuilder.buildWorkflow(var4, var3);
      this.setWorkflow(var5);
      return var5;
   }
}
