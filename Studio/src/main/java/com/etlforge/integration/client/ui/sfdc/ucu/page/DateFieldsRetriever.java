package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Optional;

interface DateFieldsRetriever {
   Optional<DateFields> getDateFields() throws Exception;
}
