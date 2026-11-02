package org.etlforge.integration.client.project.backup;

import java.util.Date;
import org.etlforge.integration.client.project.ProjectLocation;

public interface AvailableBackup {
   ProjectLocation getLocation();

   Date getTimestamp();
}
