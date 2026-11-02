/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

import org.etlforge.lang.IntParser;

   
                                                                  
   
               
   
   
final class ProviderUrlParser {

    private final String defaultProtocol;

    private final int defaultPort;

    public ProviderUrlParser(String defaultProtocol, int defaultPort) {
        this.defaultProtocol = defaultProtocol;
        this.defaultPort = defaultPort;
    }

    public ProviderUrl parse(String url) {
        int protocolPos = url.indexOf("://");
        int portPos = url.indexOf(':', protocolPos + 1);
        String protocol = (protocolPos > 0) ? url.substring(0, protocolPos) : defaultProtocol;
        String path = getPath(url, protocolPos, portPos);
        int port = getPort(url, portPos);
        return new ProviderUrlImpl(protocol, path, port);
    }

    private String getPath(String url, int protocolPos, int portPos) {
        if ((protocolPos < 0) && (portPos < 0)) {
            return url;
        }
        if (protocolPos >= 0) {
            int endIndex = (portPos > 0) ? portPos : url.length();
            return url.substring(protocolPos + 3, endIndex);
        }
        return url.substring(0, portPos);
    }

    private int getPort(String url, int portPos) {
        if (portPos >= 0) {
            String s = url.substring(portPos + 1);
            return IntParser.parse(s, defaultPort);
        }
        return defaultPort;
    }

}
