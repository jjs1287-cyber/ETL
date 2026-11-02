package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;

interface BulkOptions {
   void applyTo(SalesforceBulkLoad var1);

   void applyTo(Operation var1);
}
