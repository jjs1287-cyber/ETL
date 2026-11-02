import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.project.jitterpack.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.entity.id.*;
import org.etlforge.integration.data.entity.operation.*;
import org.etlforge.integration.data.entity.operation.pipeline.*;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.persist.XmlPersistor;

public final class ExportXsltStudioProject {
    public static void main(String[] args) throws Exception {
        OperationType.load(XsltOperationType.TYPE, ScriptOperationType.TYPE, ArchiveOperationType.TYPE);
        final String name="ETLForge XSLT Examples";
        final Path root=Paths.get("C:/ETL/Samples/XSLT/StudioProject").resolve(name+"-"+UUID.randomUUID().toString().substring(0,8));
        Files.createDirectories(root);
        Path result=root.resolve("output/result.xml"); Files.createDirectories(result.getParent());
        Source source=new Source("Sample XML"); source.setLocation(new LocalFileLocation(new File("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/input.xml")));
        Target target=new Target("Expanded XML"); target.setLocation(new LocalFileLocation(result.toFile()));
        XsltTransform transform=new XsltTransform("Expand Embedded XML");
        transform.setXslt(new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/transform.xsl")),StandardCharsets.UTF_8));
        Script script=new Script("Run named XSLT");
        script.setScript("<trans>\nFileContent=ReadFile(\"s."+source.getID()+"\");\noutput=RunXSLT(\"xs."+transform.getID()+"\",FileContent);\nFileContent=output[0];\nWriteToOperationLog(FileContent);\n</trans>");
        Operation graph=new Operation("Source to XSLT to Target");
        graph.setPipeline(new OperationPipeline(XsltOperationType.TYPE,Arrays.asList(SourceActivity.newInstance(source,"Source"),XsltTransformActivity.newInstance(transform,"XSLT"),TargetActivity.newInstance(target,"Target"))));
        Operation named=new Operation("Run XSLT from script");
        named.setPipeline(new OperationPipeline(ScriptOperationType.TYPE,Arrays.asList(ScriptActivity.newInstance(script,"Script"))));
        IntegrationEntity[] entities={source,target,transform,script,graph,named};
        List<EntityType> types=Arrays.asList(EntityType.Operation,EntityType.Source,EntityType.Target,EntityType.Script,EntityType.XsltTransform);
        final IntegrationProject project=IntegrationProject.createNewProject(name,types);
        for(IntegrationEntity entity:entities) {
            project.getRootFolder(entity.getEntityType()).addItem(entity);
            Path directory=root.resolve("Data/"+entity.getEntityType().name()); Files.createDirectories(directory);
            entity.setDeployed(false); entity.setDeployDirty(true); entity.setTransient(false);
            XmlPersistor stored=XmlPersistor.newRoot("Entity"); stored.putString("type",entity.getEntityType().name()); entity.writeTo(stored);
            stored.store(directory.resolve(entity.getID()+".xml").toFile());
        }
        XmlPersistor structure=XmlPersistor.newRoot("Project"); structure.putString("name",name); structure.putString("projectId",project.getID().toString());
        for(EntityType type:types) {
            org.etlforge.util.persist.Persistor folder=structure.createChild("EntityType"); folder.putString("name",type.name());
            for(IntegrationEntity entity:entities) if(entity.getEntityType()==type) {
                org.etlforge.util.persist.Persistor item=folder.createChild("Entity"); item.putString("entityId",entity.getID().toString()); item.putString("name",entity.getName());
            }
        }
        structure.store(root.resolve(name+".xml").toFile());
        Properties properties=new Properties();
        properties.setProperty("project-id",project.getID().toString()); properties.setProperty("saved-version","42.0.0.0");
        properties.setProperty("client-version","42.0.0.0"); properties.setProperty("requires_version","42.0.0.0");
        properties.setProperty("saved-edition","enterprise"); properties.setProperty("structure-file",name+".xml");
        properties.setProperty("deployed","false"); properties.setProperty("deployable","true"); properties.setProperty("deploy-dirty","true");
        Path projectPath=root.resolve(name+".jip");
        try(OutputStream output=Files.newOutputStream(projectPath)){properties.store(output,"ETLForge XSLT examples");}
        final ProjectFile projectFile=ProjectFile.createForExistingProject(name,root.toFile(),false);
        ManagedProject managed=(ManagedProject)Proxy.newProxyInstance(ManagedProject.class.getClassLoader(),new Class[]{ManagedProject.class},(proxy,method,values)->{
            switch(method.getName()) {
                case "getProject": return project;
                case "getProjectFile": return projectFile;
                case "getLocation": return root.toFile();
                default: throw new UnsupportedOperationException(method.getName());
            }
        });
        Path backup=root.resolveSibling(root.getFileName()+".jpk");
        org.etlforge.integration.jitterpack.impl.DefaultExporter exporter=new org.etlforge.integration.jitterpack.impl.DefaultExporter();
        ExportParameters parameters=new ExportParameters(PackingFileFilter.DEFAULT,null,false,false);
        JitterPack packed=exporter.pack(managed,"XSLT 3.0 named script and Source to XSLT to Target examples",backup.toFile(),parameters);
        Class<?> formats=Class.forName("org.etlforge.integration.jitterpack.impl.JitterPackUtils");
        Method select=formats.getDeclaredMethod("getPackFormat",JitterPackDescriptor.class,JitterPackParameters.class); select.setAccessible(true);
        Object format=select.invoke(null,packed.getDescriptor(),parameters);
        Method unpack=format.getClass().getSuperclass().getDeclaredMethod("unpack",File.class,File.class); unpack.setAccessible(true);
        Path restoredRoot=Paths.get("C:/ETL/Studio/build/xslt-backup-roundtrip").resolve(UUID.randomUUID().toString()); Files.createDirectories(restoredRoot);
        unpack.invoke(format,backup.toFile(),restoredRoot.toFile());
        Path restored=restoredRoot.resolve(root.getFileName());
        for(IntegrationEntity entity:entities) {
            Path relative=Paths.get("Data",entity.getEntityType().name(),entity.getID()+".xml");
            if(!Arrays.equals(Files.readAllBytes(root.resolve(relative)),Files.readAllBytes(restored.resolve(relative)))) throw new AssertionError("Backup round-trip lost "+entity.getEntityType());
        }
        XsltTransform reloaded=new XsltTransform(transform.getID(),"");
        reloaded.restoreFrom(XmlPersistor.load(restored.resolve("Data/XsltTransform/"+transform.getID()+".xml").toFile()));
        if(!transform.getXslt().equals(reloaded.getXslt())) throw new AssertionError("Restored stylesheet changed");
        IntegrationProject loaded=IntegrationProject.createExistingProjectShell(name,project.getID(),types);
        ProjectFile restoredProjectFile=ProjectFile.createForExistingProject(name,restored.toFile(),false);
        ProjectDataLoader loader=new org.etlforge.integration.client.project.persistor.v20.ProjectDataLoaderFactoryImpl().newLoader(loaded,restoredProjectFile);
        java.util.Set<ProjectLoadingIssue> issues=loader.loadProject(ProjectLoadingTracker.SILENT);
        if(!issues.isEmpty()) throw new AssertionError("Restored project loading issues: "+issues);
        if(loaded.getAllEntitiesOfClass(XsltTransform.class).size()!=1 || loaded.getAllEntitiesOfClass(Operation.class).size()!=2)
            throw new AssertionError("Studio project loader lost XSLT entities or operations");
        Operation restoredGraph=null;
        for(Operation candidate:loaded.getAllEntitiesOfClass(Operation.class)) if(candidate.getID().equals(graph.getID())) restoredGraph=candidate;
        if(restoredGraph==null) throw new AssertionError("Restored graph operation missing");
        if(restoredGraph.getPipeline()==null) throw new AssertionError("Restored graph pipeline missing");
        if(restoredGraph.getPipeline().getFirstActivityOfType(XsltTransformActivity.class)==null) throw new AssertionError("Restored XSLT activity missing");
        if(restoredGraph.getPipeline().getFirstActivityOfType(XsltTransformActivity.class).getContent()==null)
            throw new AssertionError("Studio project loader lost the graph-to-XSLT reference");
        Files.write(Paths.get("C:/ETL/Reports/xslt-studio-example-paths.txt"),Arrays.asList(projectPath.toString(),backup.toString(),result.toString()),StandardCharsets.UTF_8);
        System.out.println("PASS: native Studio backup export/import preserved all XSLT assets, scripts and operation XML");
        System.out.println("Studio project: "+projectPath);
        System.out.println("Backup: "+backup);
    }
}
