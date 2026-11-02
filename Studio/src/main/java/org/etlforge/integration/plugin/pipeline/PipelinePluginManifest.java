package org.etlforge.integration.plugin.pipeline;

import java.net.URL;
import org.etlforge.integration.plugin.PluginDataElementTemplate;
import org.etlforge.util.version.Version;

public interface PipelinePluginManifest {
   String getDefinition();

   String getName();

   String getDisplayName();

   Version getVersion();

   Version getSpecificationVersion();

   String getAuthor();

   String getDescription();

   String getCommand();

   URL getUpdateUrl();

   URL getDocumentationUrl();

   String getArguments();

   PluginDataElementTemplate[] getDataElements();

   PipelinePosition[] getPipelinePositions();

   @Override
   boolean equals(Object var1);
}
