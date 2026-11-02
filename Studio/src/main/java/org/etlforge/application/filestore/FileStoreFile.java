package org.etlforge.application.filestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public interface FileStoreFile {
   String getPath();

   Iterator<FileStoreFile> getChildren();

   File getPhysicalRepresentation();

   String getMD5() throws FileNotFoundException, IOException;
}
