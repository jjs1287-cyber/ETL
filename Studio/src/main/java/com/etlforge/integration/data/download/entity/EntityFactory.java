package com.etlforge.integration.data.download.entity;

import com.etlforge.integration.data.download.InvalidDownloadedDataException;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface EntityFactory {
   IntegrationEntity createItem(StringKeyValuePairs var1) throws InvalidDownloadedDataException;
}
