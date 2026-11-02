package org.etlforge.integration.client.ui.admin.audit;
import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;

public final class AuditXsltCaptureLiveTest extends AuditDeploymentLiveTest {
    static String asset=UUID.randomUUID().toString();
    static InterchangeDataHolder assetData(String xml,boolean deleted){
        InterchangeDataHolder data=new InterchangeDataHolder();data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"Audit XSLT Verification "+project.substring(0,8)}));
        data.setEntityProperties(deleted?table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"34",asset,"__deleted","1"}):table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"34",asset,"xslttransform_name","Audit verification XSLT"},new String[]{"34",asset,"xslt",xml}));return data;
    }
    public static void main(String[] args)throws Exception{
        String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder hex=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hex.append(String.format("%02x",b&255));hash=hex.toString();
        deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://localhost:47100/axis/konga_deploy_interchange_data"));
        String before="<xsl:stylesheet version='3.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'><xsl:template match='/'><before/></xsl:template></xsl:stylesheet>",after=before.replace("before/","after/");
        deploy(assetData(before,false));require(entries(34,"").size()==1,"XSLT creation event missing");
        deploy(assetData(before,false));require(entries(34,"").size()==1,"Unchanged XSLT generated an event");
        deploy(assetData(after,false));require(entries(34,"").size()==2,"XSLT edit event missing");
        deploy(assetData(after,true));require(entries(34,"").size()==3,"XSLT deletion event missing");
        System.out.println("PASS "+checks+" XSLT deployment audit checks: create, unchanged, update, delete; isolated project "+project);
    }
}
