package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.bulkload.TargetFields;

public interface TargetFieldsUpdater {
   TargetFields getTargetFields() throws SalesforceException;
}
