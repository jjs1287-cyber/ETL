package org.etlforge.integration.server.engine.json;

import java.io.File;

public interface XsdLocator {
   File getSourceXsd(long var1) throws Exception;

   File getTargetXsd(long var1) throws Exception;
}
