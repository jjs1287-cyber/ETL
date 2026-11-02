package org.etlforge.crypto.aes.bc;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.OpenSSLPBEParametersGenerator;

final class OpenSslAesCipher extends AbstractAesCipher {
   @Override
   public void setIterationCount(int var1) {
      super.setIterationCount(1);
   }

   @Override
   protected PBEParametersGenerator createPBEParametersGenerator() {
      return new OpenSSLPBEParametersGenerator();
   }
}
