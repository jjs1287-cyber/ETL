/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;

import org.etlforge.io.KongaIoUtils;
import org.etlforge.io.FileUtils;

import com.google.common.collect.Maps;

   
                                                                                          
                                          
      
                                                                                  
                                                                                                
                                                         
                                                             
      
                                                                                                 
                                                   
      
                                                                                       
                                                                                   
   
                         
   
               
   
   
final class FileBasedJmsProviderStore implements MutableJmsProviderStore {

    private static final String FILE_EXTENSION = ".jms_provider";

    private static final FileFilter FILE_FILTER = new FileFilter() {

        @Override
        public boolean accept(File pathname) {
            if (pathname.isFile()) {
                String ext = FileUtils.getExtension(pathname, true);
                return FILE_EXTENSION.equals(ext);
            }
            return false;
        }
    };

    private final File location;

    private final InstanceMap instances;

    private final FileNameGenerator fileNameGenerator;

       
                                                                                              
                       
       
                      
                                                                                              
       
    public FileBasedJmsProviderStore(File location) {
        checkNotNull(location, "location");
        this.location = location;
        instances = new InstanceMap();
        fileNameGenerator = new FileNameGenerator();
    }

    @Override
    public synchronized boolean contains(String id) {
        return instances.contains(id);
    }

    @Override
    public synchronized JmsProvider getProvider(String id) {
        return instances.get(id);
    }

    @Override
    public synchronized void addProvider(JmsProvider provider) throws JmsProviderStoreException {
        checkNotNull(provider, "provider");
        try {
            saveProviderToDisk(provider);
            instances.add(provider);
        } catch (IOException ex) {
            throw new JmsProviderStoreException("Failed to store the provider " + provider.getName() + " to disk.", ex);
        }
    }

    @Override
    public synchronized boolean removeProvider(JmsProvider provider) {
        checkNotNull(provider, "provider");
        if (instances.contains(provider)) {
            if (deleteProviderFromDisk(provider)) {
                return instances.remove(provider);
            }
        }
        return false;
    }

    @Override
    public synchronized Iterable<JmsProvider> getStoredProviders() {
        return instances.getStoredProviders();
    }

    private void saveProviderToDisk(JmsProvider p) throws IOException {
        ensureParentFolderExists();
        File f = getFileForProvider(p);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
        try {
            out.writeObject(p);
        } finally {
            KongaIoUtils.close(out);
        }
    }

    private void ensureParentFolderExists() throws IOException {
        if (!location.exists()) {
            if (!location.mkdirs()) {
                throw new IOException("The location " + location.getAbsolutePath() + " could not be created.");
            }
        }
    }

    private boolean deleteProviderFromDisk(JmsProvider p) {
        File f = getFileForProvider(p);
        return f.exists() ? f.delete() : true;
    }

    private File getFileForProvider(JmsProvider p) {
        String name = fileNameGenerator.getFileName(p) + FILE_EXTENSION;
        return new File(location, name);
    }


    private final class InstanceMap {

        private final Map<String, JmsProvider> instances;

        private boolean initialized;

        public InstanceMap() {
            instances = Maps.newHashMap();
            initialized = false;
        }

        public Iterable<JmsProvider> getStoredProviders() {
            if (!initialized) {
                initialize();
            }
            return new Iterable<JmsProvider>() {

                @Override
                public Iterator<JmsProvider> iterator() {
                    return instances.values().iterator();
                }
            };
        }

        public void add(JmsProvider provider) {
            if (!initialized) {
                initialize();
            }
            instances.put(provider.getId(), provider);
        }

        public JmsProvider get(String providerId) {
            return contains(providerId) ? instances.get(providerId) : null;
        }

        public boolean contains(String providerId) {
            if (!initialized) {
                initialize();
            }
            return instances.containsKey(providerId);
        }

        public boolean contains(JmsProvider provider) {
            return contains(provider.getId());
        }

        public boolean remove(JmsProvider provider) {
            assert (initialized) : "The instance map must be initialized before a " + "provider can be removed from it";
            return instances.remove(provider.getId()) != null;
        }

        private void initialize() {
            loadStoredProviders();
            initialized = true;
        }

        private void loadStoredProviders() {
            for (File f : location.listFiles(FILE_FILTER)) {
                loadProviderFromFile(f);
            }
        }

        private void loadProviderFromFile(File f) {
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(f));
                Object o = in.readObject();
                if (o instanceof JmsProvider) {
                    addLoadedProvider((JmsProvider) o);
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } finally {
                KongaIoUtils.close(in);
            }
        }

        private void addLoadedProvider(JmsProvider p) {
            instances.put(p.getId(), p);
        }
    }


    private static final class FileNameGenerator {

        private final MessageDigest md5;

        public FileNameGenerator() {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                                                                                
                                                  
                ex.printStackTrace();
            }
            this.md5 = md5;
        }

        public String getFileName(JmsProvider p) {
            String id = p.getId();
            if (md5 == null) {
                return id;
            }
            byte[] bytes = md5.digest(id.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toHexString((0xff & b)));
            }
            return sb.toString();
        }
    }
    
}
