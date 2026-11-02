package org.etlforge.crypto.aes.bc;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;

final class PKCS5S2AesCipher extends AbstractAesCipher {
   @Override
   protected PBEParametersGenerator createPBEParametersGenerator() {
      return new PKCS5S2ParametersGenerator();
   }
}
