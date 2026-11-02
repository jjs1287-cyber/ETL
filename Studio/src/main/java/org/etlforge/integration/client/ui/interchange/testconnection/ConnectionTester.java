package org.etlforge.integration.client.ui.interchange.testconnection;

import javax.annotation.Nullable;
import org.etlforge.integration.data.test.TestConnectionResult;

public interface ConnectionTester {
   @Nullable
   TestConnectionResult testConnection();
}
