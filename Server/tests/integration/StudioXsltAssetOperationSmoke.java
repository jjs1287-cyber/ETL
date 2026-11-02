import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.io.StringReader;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;

public final class StudioXsltAssetOperationSmoke {
    static InterchangeDataTable table(String[] columns, String[]... values) {
        InterchangeDataRow[] rows = new InterchangeDataRow[values.length];
        for (int i=0;i<values.length;i++) rows[i]=new InterchangeDataRow(values[i]);
        return new InterchangeDataTable(columns, rows);
    }
    static String quote(String value) { return "\""+value.replace("\\","\\\\").replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n")+"\""; }
    static void normalize(Node node) {
        for (Node child=node.getFirstChild(); child!=null;) {
            Node next=child.getNextSibling();
            if (child.getNodeType()==Node.TEXT_NODE && child.getTextContent().trim().isEmpty()) node.removeChild(child);
            else normalize(child);
            child=next;
        }
    }
    public static void main(String[] args) throws Exception {
        String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
        StringBuilder hash=new StringBuilder();
        for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8))) hash.append(String.format("%02x",b&255));
        String project=UUID.randomUUID().toString(), asset=UUID.randomUUID().toString(), script=UUID.randomUUID().toString();
        String scriptOperation=UUID.randomUUID().toString(), graphOperation=UUID.randomUUID().toString();
        String source=UUID.randomUUID().toString(), target=UUID.randomUUID().toString();
        String stylesheet=new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/transform.xsl")),StandardCharsets.UTF_8);
        String input=new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/input.xml")),StandardCharsets.UTF_8);
        Path root=Paths.get("C:/ETL/Samples/XSLT/integration").resolve(project);
        Files.createDirectories(root.resolve("input")); Files.createDirectories(root.resolve("output"));
        Files.write(root.resolve("input/input.xml"),input.getBytes(StandardCharsets.UTF_8));
        Path output=root.resolve("output/result.xml");
        String marker="ETLForge-XSLT-NAMED-"+project;
        String content="<trans>output=RunXSLT(\"xs."+asset+"\","+quote(input)+");WriteToOperationLog("+quote(marker)+"+output[0]);</trans>";
        InterchangeDataHolder data=new InterchangeDataHolder();
        data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"ETLForge XSLT Assets "+project.substring(0,8)}));
        data.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},
            new String[]{"34",asset,"xslttransform_name","Expand Embedded XML"},new String[]{"34",asset,"xslt",stylesheet}));
        data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},new String[]{script,"Named XSLT call",Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8))}));
        data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},
            new String[]{source,"XSLT local input","16",root.resolve("input").toString(),"input.xml",null,null,null,null,null}));
        data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},
            new String[]{target,"XSLT local output","16",root.resolve("output").toString(),"result.xml",null,null,null,null,null,null}));
        String[] columns={"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"};
        data.setOperationsTab(table(columns,
            new String[]{scriptOperation,"Named XSLT script","8",null,null,"1",null,null,null,null,null,null},
            new String[]{graphOperation,"Source to XSLT to Target","23",source,target,"1",null,null,null,null,null,null}));
        data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},
            new String[]{scriptOperation,"1",script},new String[]{graphOperation,"3",source},new String[]{graphOperation,"8",asset},new String[]{graphOperation,"4",target}));
        DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));
        ((org.apache.axis.client.Stub)deploy).setTimeout(30000);
        CDeployResults deployed=deploy.deployInterchangeData("Admin",hash.toString(),new InterchangeIdHolder(),data);
        if(deployed==null || deployed.getDeployStatus()>1) throw new AssertionError("XSLT asset/operation deployment failed");
        JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
        ((org.apache.axis.client.Stub)jobs).setTimeout(30000);
        WsRunOperationResult named=jobs.runOperationInstanceMD5Password("Admin",hash.toString(),scriptOperation);
        WsRunOperationResult graph=jobs.runOperationInstanceMD5Password("Admin",hash.toString(),graphOperation);
        if(named==null || graph==null || named.getOperationInstanceId()<1 || graph.getOperationInstanceId()<1) throw new AssertionError("XSLT submission failed");
        Files.write(Paths.get("C:/ETL/Reports/xslt-assets-live-manifest.txt"),Arrays.asList(project,asset,scriptOperation,graphOperation,Long.toString(named.getOperationInstanceId()),Long.toString(graph.getOperationInstanceId()),output.toString(),marker),StandardCharsets.UTF_8);
        System.out.println("Deployed XSLT asset; queued named script="+named.getOperationInstanceId()+", graph="+graph.getOperationInstanceId());
        long deadline=System.nanoTime()+90000000000L;
        while(!Files.exists(output) && System.nanoTime()<deadline) Thread.sleep(250);
        if(!Files.exists(output)) throw new AssertionError("XSLT graph produced no output within 90 seconds");
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        Document expected=factory.newDocumentBuilder().parse(new InputSource(new StringReader(input)));
        NodeList entries=expected.getElementsByTagName("xml");
        for(int i=0;i<entries.getLength();i++) {
            Element element=(Element)entries.item(i);
            Document embedded=factory.newDocumentBuilder().parse(new InputSource(new StringReader(element.getTextContent())));
            while(element.hasChildNodes()) element.removeChild(element.getFirstChild());
            element.appendChild(expected.importNode(embedded.getDocumentElement(),true));
        }
        Document actual=factory.newDocumentBuilder().parse(output.toFile());
        normalize(expected); normalize(actual);
        if(!expected.getDocumentElement().isEqualNode(actual.getDocumentElement())) throw new AssertionError("XSLT graph output differs from independently expanded XML");
        if(!input.equals(new String(Files.readAllBytes(root.resolve("input/input.xml")),StandardCharsets.UTF_8))) throw new AssertionError("Source XML was changed");
        System.out.println("PASS: queued x64 Source -> XSLT -> Target produced correct XML and preserved its source");
    }
}
