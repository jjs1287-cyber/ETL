import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.entity.id.XsltTransformId;
import org.etlforge.integration.data.entity.metadata.EntityTypeConfig;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.*;
import org.etlforge.integration.client.ui.script.builder.DefaultProjectItemTreeModelBuilder;
import org.etlforge.util.persist.XmlPersistor;

public final class XsltAssetSmoke {
    public static void main(String[] args) throws Exception {
        org.etlforge.integration.data.entity.operation.OperationType.load(
            org.etlforge.integration.data.entity.operation.XsltOperationType.TYPE,
            org.etlforge.integration.data.entity.operation.ArchiveOperationType.TYPE,
            org.etlforge.integration.data.entity.operation.ScriptOperationType.TYPE);
        XsltTransform asset = (XsltTransform)EntityTypeConfig.createNewEntity(EntityType.XsltTransform, "Expand Embedded XML");
        String stylesheet = new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/transform.xsl")), StandardCharsets.UTF_8);
        asset.setXslt(stylesheet);
        XsltTransform.validateStylesheet(stylesheet);
        XmlPersistor store = XmlPersistor.newRoot("entity");
        asset.writeTo(store);
        XsltTransform restored = new XsltTransform(asset.getID(), "");
        restored.restoreFrom(XmlPersistor.load(store.toXml()));
        if (!stylesheet.equals(restored.getXslt()) || !asset.getName().equals(restored.getName()) || !asset.getID().equals(restored.getID()))
            throw new AssertionError("XSLT asset persistence lost data");
        if (!(EntityType.XsltTransform.createIntegrationEntityId() instanceof XsltTransformId))
            throw new AssertionError("XSLT ID registration");
        if (!"xs.".equals(GrammarUtils.idToPrefix(XsltTransformId.class))) throw new AssertionError("XSLT function prefix");
        if (!GrammarUtils.getAllIdsFromScript(new Transform(), "<trans>output=RunXSLT(\"xs."+asset.getID()+"\",\"<root/>\");</trans>", "XSLT smoke").contains(asset.getID()))
            throw new AssertionError("Named XSLT dependency resolution");
        IntegrationProject project = IntegrationProject.createNewProject("XSLT smoke", Arrays.asList(EntityType.Source, EntityType.Target, EntityType.Operation, EntityType.Script, EntityType.XsltTransform));
        if (project.getRootFolder(EntityType.XsltTransform) == null) throw new AssertionError("Missing XSLT root folder");
        project.getRootFolder(EntityType.XsltTransform).addItem(asset);
        org.etlforge.integration.data.entity.script.ProjectGuidPathCache paths = new org.etlforge.integration.data.entity.script.ProjectGuidPathCache(project,
            Arrays.asList(new ProjectItemArgDescriptor("xs", XsltTransformId.class, XsltTransform.class)));
        String displayed = "<trans>output=RunXSLT("+ScriptConstants.getProjectItemTagString(asset)+",FileContent);</trans>";
        String translated = org.etlforge.integration.data.entity.script.GuidPathTranslator.translateToGuid(displayed, paths, null);
        if (!translated.contains("\"xs."+asset.getID()+"\"")) throw new AssertionError("Named TAG was not translated for deployment");
        if (!displayed.equals(org.etlforge.integration.data.entity.script.GuidPathTranslator.translateToPath(translated, paths))) throw new AssertionError("Named TAG display round trip");
        paths.dispose();
        new DefaultProjectItemTreeModelBuilder().buildModel(project);
        org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline graph = org.etlforge.integration.data.entity.operation.XsltOperationType.TYPE.createNewTemplate();
        if (graph.getActivities().size() != 3 || !(graph.getActivities().get(1) instanceof org.etlforge.integration.data.entity.operation.pipeline.XsltTransformActivity))
            throw new AssertionError("XSLT operation graph");
        org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineMemento memento = new org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineMemento(graph);
        XmlPersistor graphStore = XmlPersistor.newRoot("Pipeline");
        memento.writeTo(graphStore);
        if (!graphStore.toXml().contains("XSLT_TRANSFORM")) throw new AssertionError("XSLT pipeline persistence");
        org.etlforge.collections.StringKeyValuePairs downloaded = new org.etlforge.collections.StringKeyValuePairs();
        downloaded.put("xslttransform_id", asset.getID().toString());
        downloaded.put("xslttransform_name", asset.getName());
        downloaded.put("xslt", stylesheet);
        XsltTransform checkedOut = (XsltTransform)new com.etlforge.integration.data.download.entity.DefaultEntityFactory(EntityType.XsltTransform).createItem(downloaded);
        if (!stylesheet.equals(checkedOut.getXslt())) throw new AssertionError("Checkout factory");
        Object wrapper = new org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl.DeployInterchangeModelWsImplWrapper(null, null);
        java.lang.reflect.Method encode = wrapper.getClass().getDeclaredMethod("updateEntity", IntegrationEntity.class);
        encode.setAccessible(true); asset.setDeployDirty(true); encode.invoke(wrapper, asset);
        java.lang.reflect.Field encoded = wrapper.getClass().getDeclaredField("m_extendedEntityProperties"); encoded.setAccessible(true);
        java.util.List<?> rows = (java.util.List<?>)encoded.get(wrapper);
        if (rows.size()!=2) throw new AssertionError("XSLT deploy wrapper lost properties");
        boolean rejected = false;
        try { XsltTransform.validateStylesheet("<not-a-stylesheet/>"); } catch (Exception expected) { rejected = true; }
        if (!rejected) throw new AssertionError("Non-stylesheet accepted");
        System.out.println("PASS: XSLT asset factory, ID, XML round-trip, named dependency and optional project tree");
    }
}
