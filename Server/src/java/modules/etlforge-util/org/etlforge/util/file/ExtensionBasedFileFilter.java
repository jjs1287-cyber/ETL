package org.etlforge.util.file;

import java.util.Iterator;

public interface ExtensionBasedFileFilter {
   Iterator<String> getExtensions();
}
