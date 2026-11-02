package com.etlforge.integration.client.project.share;

public interface ConflictResolver {
   ConflictResolution resolve(NameConflict var1);

   ConflictResolution resolve(IdConflict var1);
}
