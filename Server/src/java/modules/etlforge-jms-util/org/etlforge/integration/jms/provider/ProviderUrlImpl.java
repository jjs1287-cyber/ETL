/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

   
                         
   
               
   
   
final class ProviderUrlImpl implements ProviderUrl {

    public static final int DEFAULT_PORT = 1099;

    private final String protocol;

    private final String server;

    private final int port;

    ProviderUrlImpl(String protocol, String server, int port) {
        if (!isValid(protocol)) {
            throw new IllegalArgumentException("protocol cannot be null or empty.");
        }
        if (!isValid(server)) {
            throw new IllegalArgumentException("server cannot be null or empty.");
        }
        if ((port < 0) || (port > 65535)) {
            throw new IllegalArgumentException("port must be >= 0 and <= 65535 (was " + port + ")");
        }
        this.protocol = protocol.trim().toLowerCase();
        this.server = server;
        this.port = port;
    }

       
                                             
       
       
    @Override
    public String getProtocol() {
        return protocol;
    }

       
                                           
       
       
    @Override
    public String getServer() {
        return server;
    }

       
                                         
       
       
    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return protocol + "://" + server + ":" + port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ProviderUrlImpl) {
            return equals((ProviderUrlImpl) obj);
        }
        return false;
    }

    private boolean equals(ProviderUrlImpl url) {
        return protocol.equals(url.protocol) && server.equals(url.server) && (port == url.port);
    }

    @Override
    public int hashCode() {
        int hash = 31;
        hash = 17 * hash + protocol.hashCode();
        hash = 17 * hash + server.hashCode();
        hash = 17 * hash + port;
        return hash;
    }

    private static final boolean isValid(String s) {
        return (s != null) && s.trim().length() > 0;
    }
}
