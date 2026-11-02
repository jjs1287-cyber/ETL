package org.etlforge.integration.client.project.jitterpack;

import java.util.Date;

public interface JitterPackDescriptor {
   JitterPackDescriptor UNKNOWN = new DefaultJitterPackDescriptor("", VersionInfo.UNKNOWN, "", null);
   JitterPackDescriptor UNKNOWN_ENCRYPTED = new DefaultJitterPackDescriptor("?", VersionInfo.UNKNOWN, "?", null, "", true, false);

   VersionInfo getVersionInfo();

   String getProjectName();

   Date createdAt();

   String getComment();

   String getFolderName();

   boolean isEncrypted();

   boolean isPackedProjectReadOnly();
}
