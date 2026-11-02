package org.etlforge.integration.client.runtime.appdir;

import java.io.File;
import org.apache.commons.lang.SystemUtils;

public final class DefaultLocation {
   public static final File LOCATION = new File(SystemUtils.USER_HOME, "ETLForgeStudio");

   private DefaultLocation() {
   }
}
