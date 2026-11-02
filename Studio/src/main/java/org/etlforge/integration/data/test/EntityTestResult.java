package org.etlforge.integration.data.test;

import org.etlforge.integration.data.entity.IntegrationEntityId;

public interface EntityTestResult extends TestResult {
   IntegrationEntityId getSubjectId();
}
