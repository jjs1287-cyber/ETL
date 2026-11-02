package org.etlforge.crypto.pgp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public interface Key extends Closeable {
   InputStream getKeyData() throws IOException;

   Long getKeyId();

   String getUserId();
}
