/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.client.project;

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.entity.settings.EntityFileResourceManager;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.client.entity.settings.EntityXmlSettings;
import org.etlforge.integration.client.entity.settings.FileResource;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.io.Folder;
import org.etlforge.util.persist.XmlPersistor;

   
                                                                                
  
                                           
  
                         
  
               
   
final class EntityResourcesImpl implements org.etlforge.integration.client.entity.settings.CloseableEntityResources {

    private final EntitySettings settings;

    private final EntityFileResourceManager fileResources;

    EntityResourcesImpl(Folder location) {
        fileResources = new EntityFileResourceManager(location);
        this.settings = new EntityXmlSettings(location);
    }

    @Override
    public FileResource getFileResource(IntegrationEntity entity, String name) {
        return fileResources.getResource(entity, name);
    }

    @Override
    public EntitySettings getSettings() {
        return settings;
    }

    @Override
    public void export(IntegrationEntity entity, EntityResources target) {
        checkNotNull(target, "target");
                                                          
        checkArgument(target instanceof EntityResourcesImpl, "I do not know how to transfer resources to the class "
                        + target.getClass());
        exportImpl(entity, (EntityResourcesImpl) target);
    }

    @Override
    public void exportAs(IntegrationEntity source, IntegrationEntity destination, EntityResources target) {
        checkArgument(target instanceof EntityResourcesImpl, "Unsupported resource store");
        EntityResourcesImpl store = (EntityResourcesImpl) target;
        try {
            File sourceRoot = fileResources.getEntityLocation(source);
            if (sourceRoot.exists()) {
                FileUtils.copyDirectory(sourceRoot, store.fileResources.getEntityLocation(destination));
                store.fileResources.refresh(destination);
            }
            XmlPersistor from = (XmlPersistor) settings.getEntitySettings(source);
            XmlPersistor to = (XmlPersistor) store.settings.getEntitySettings(destination);
            from.exportTo(to);
            store.settings.persist();
        } catch (IOException failure) {
            throw new IllegalStateException("Could not copy object resources for " + source.getName(), failure);
        }
    }

    private void exportImpl(IntegrationEntity entity, EntityResourcesImpl target) {
        exportFiles(entity, target);
        exportSettings(entity, target);
    }

    private void exportFiles(IntegrationEntity entity, EntityResourcesImpl target) {
        try {
            File sourceRoot = fileResources.getEntityLocation(entity);
            if (sourceRoot.exists()) {
                File targetRoot = target.fileResources.getEntityLocation(entity);
                FileUtils.copyDirectory(sourceRoot, targetRoot);
                target.fileResources.refresh(entity);
            }
        } catch (IOException ex) {
            ErrorLog.log(getClass(), "Failed to copy file resources associated with the entity " + entity.getName() +
                            " to the target resource store.", ex);
        }
    }

    private void exportSettings(IntegrationEntity entity, EntityResourcesImpl target) {
        XmlPersistor from = (XmlPersistor) settings.getEntitySettings(entity);
        XmlPersistor to = (XmlPersistor) target.settings.getEntitySettings(entity);
        from.exportTo(to);
        target.settings.persist();
    }

    public void close(Collection<IntegrationEntity> deleted) {
        fileResources.cleanup();
        deleteSettings(deleted);
        getSettings().persist();
    }

    private void deleteSettings(Collection<IntegrationEntity> deleted) {
        for (IntegrationEntity e : deleted) {
            settings.deleteSettings(e);
        }
    }
}
