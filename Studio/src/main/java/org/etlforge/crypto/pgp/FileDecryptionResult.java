package org.etlforge.crypto.pgp;

import java.io.File;

public interface FileDecryptionResult {
   File decryptedFile();

   MessageIntegrity messageIntegrity();

   MessageAuthenticationStatus authenticationStatus();
}
