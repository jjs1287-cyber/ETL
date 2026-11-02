import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;

public final class StudioXsltAssetLifecycleSmoke {
    public static void main(String[] args) throws Exception {
        String stage=args[0];
        Path manifest=Paths.get("C:/ETL/Reports/xslt-asset-lifecycle-manifest.txt");
        List<String> ids;
        if("create".equals(stage)) {
            ids=Arrays.asList(UUID.randomUUID().toString(),UUID.randomUUID().toString());
            Files.write(manifest,ids,StandardCharsets.UTF_8);
        } else ids=Files.readAllLines(manifest,StandardCharsets.UTF_8);
        String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
        StringBuilder hash=new StringBuilder();
        for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8))) hash.append(String.format("%02x",b&255));
        InterchangeDataHolder data=new InterchangeDataHolder();
        data.setInterchangeProjectsTab(StudioXsltAssetOperationSmoke.table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{ids.get(0),"ETLForge XSLT lifecycle "+ids.get(0).substring(0,8)}));
        String[] columns={"EntityType_ID","Entity_ID","KeyName","KeyValue"};
        if("delete".equals(stage)) data.setEntityProperties(StudioXsltAssetOperationSmoke.table(columns,new String[]{"34",ids.get(1),"__deleted","1"}));
        else data.setEntityProperties(StudioXsltAssetOperationSmoke.table(columns,
            new String[]{"34",ids.get(1),"xslttransform_name","Lifecycle "+stage},
            new String[]{"34",ids.get(1),"xslt","<xsl:stylesheet version=\"3.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:template match=\"/\"><result>"+stage+"</result></xsl:template></xsl:stylesheet>"}));
        DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));
        ((org.apache.axis.client.Stub)deploy).setTimeout(30000);
        CDeployResults result=deploy.deployInterchangeData("Admin",hash.toString(),new InterchangeIdHolder(),data);
        if(result==null || result.getDeployStatus()>1) throw new AssertionError("XSLT lifecycle deploy failed: "+stage);
        System.out.println("PASS: XSLT asset "+stage+" SOAP request");
    }
}
