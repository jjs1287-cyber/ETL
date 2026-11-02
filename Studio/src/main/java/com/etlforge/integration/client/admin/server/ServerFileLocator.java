package com.etlforge.integration.client.admin.server;

import java.util.Date;

public interface ServerFileLocator {
   String getPath();

   Date getModifiedDate();

   long getSizeInBytes();
}
