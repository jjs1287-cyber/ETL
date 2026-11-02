package com.etlforge.integration.client.ui.sfdc.wizard;

import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.operation.OperationPreferences;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public interface WorkflowBuilder {
   void setConnector(SalesforceConnector var1);

   void setSalesforceObject(SObjectSchema var1);

   void setOperationPreferences(OperationPreferences var1);
}
