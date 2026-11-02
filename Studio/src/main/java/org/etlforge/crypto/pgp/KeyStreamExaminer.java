package org.etlforge.crypto.pgp;

import java.io.IOException;
import java.io.InputStream;
import org.etlforge.crypto.CryptoException;

public interface KeyStreamExaminer {
   long[] findDecryptionKeyIds(InputStream var1) throws IOException, CryptoException;
}
