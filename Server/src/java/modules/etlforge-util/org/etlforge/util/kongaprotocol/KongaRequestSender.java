package org.etlforge.util.kongaprotocol;

public interface KongaRequestSender {
   KongaResponse send(KongaRequest var1) throws KongaProtocolException;
}
