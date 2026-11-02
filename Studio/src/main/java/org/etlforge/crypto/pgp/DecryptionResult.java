package org.etlforge.crypto.pgp;

public interface DecryptionResult {
   byte[] message();

   String messageName();

   MessageIntegrity messageIntegrity();

   MessageAuthenticationStatus authenticationStatus();
}
