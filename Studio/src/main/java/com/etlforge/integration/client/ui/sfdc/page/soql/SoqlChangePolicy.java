package com.etlforge.integration.client.ui.sfdc.page.soql;

import org.etlforge.integration.salesforce.parser.ParsedQuery;

public interface SoqlChangePolicy {
   void verifyChange(ParsedQuery var1) throws EditSoqlException;
}
