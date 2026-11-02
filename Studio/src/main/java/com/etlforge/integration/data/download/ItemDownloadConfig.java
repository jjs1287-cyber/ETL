package com.etlforge.integration.data.download;

import com.etlforge.integration.data.download.entity.EntityFactory;
import org.etlforge.integration.data.project.IntegrationProject;

public interface ItemDownloadConfig {
   EntityFactory getFactory();

   void doPostProjectAssemblyProcessing(IntegrationProject var1);
}
