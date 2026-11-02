package org.etlforge.integration.data.entity.factory;

import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface IntegrationEntityFactory {
   IntegrationEntity createEntity(Folder var1) throws IntegrationEntityFactoryException;
}
