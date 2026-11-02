package org.etlforge.integration.client.project;
import java.io.File;
import java.util.Collections;
import java.util.UUID;
import org.etlforge.io.Folder;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.client.entity.settings.FileResource;
public final class XmlStorageSmoke {
 private static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);}
 public static void run(){
  File base=new File("C:/ETL/Studio/build/storage-smoke/"+UUID.randomUUID());
  Folder source=new Folder(new File(base,"source"));Folder target=new Folder(new File(base,"target"));
  TextDocument entity=new TextDocument("storage test");
  EntityResourcesImpl store=new EntityResourcesImpl(source);
  store.getSettings().getEntitySettings(entity).putString("sample","ETLForge XML");
  store.getFileResource(entity,"retained.txt").write("file payload");
  store.getFileResource(entity,"retained.txt").setLifeTime(FileResource.LifeTime.ENTITY);
  FileResource temporary=store.getFileResource(entity,"session.txt");temporary.write("temporary");temporary.setLifeTime(FileResource.LifeTime.PROJECT_SESSION);
  store.close(Collections.emptyList());
  store=new EntityResourcesImpl(source);
  check("ETLForge XML".equals(store.getSettings().getEntitySettings(entity).getString("sample")),"XML settings round trip");
  check("file payload".equals(store.getFileResource(entity,"retained.txt").read()),"file resource round trip");
  check(!store.getFileResource(entity,"session.txt").exists(),"session file cleanup");
  EntityResourcesImpl destination=new EntityResourcesImpl(target);store.export(entity,destination);destination.close(Collections.emptyList());
  destination=new EntityResourcesImpl(target);
  check("ETLForge XML".equals(destination.getSettings().getEntitySettings(entity).getString("sample")),"XML settings export");
  check("file payload".equals(destination.getFileResource(entity,"retained.txt").read()),"file resource export");
  destination.close(Collections.emptyList());store.close(Collections.emptyList());
  System.out.println("PASS: XML settings, file resources, export and session cleanup");
 }
}
