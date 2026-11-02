package org.etlforge.integration.data.entity.script;

public interface GuidPathCache {
   String getGuid(String var1);

   String getPathFromGuid(String var1);

   void addGuidPathCacheListener(GuidPathCacheListener var1);

   void removeGuidPathCacheListener(GuidPathCacheListener var1);
}
