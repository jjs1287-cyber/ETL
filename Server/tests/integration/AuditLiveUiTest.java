package org.etlforge.integration.client.ui.admin.audit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.swing.*;

public final class AuditLiveUiTest {
    static final ObjectMapper JSON=new ObjectMapper();
    static int checks;
    static String hash;
    static void require(boolean ok,String message){checks++;if(!ok)throw new AssertionError(message);}
    static ObjectNode request(String action){return JSON.createObjectNode().put("action",action);}
    static JsonNode call(ObjectNode request)throws Exception{return AuditLogService.invokeRequest("http://localhost:47100/axis/konga_admin","Admin",hash,request);}
    static void reject(ObjectNode request)throws Exception{boolean rejected=false;try{call(request);}catch(org.apache.axis.AxisFault fault){rejected=true;}require(rejected,"Invalid request accepted");}
    static <T> java.util.List<T> find(java.awt.Container parent,Class<T> type){java.util.List<T> result=new java.util.ArrayList<>();for(java.awt.Component child:parent.getComponents()){if(type.isInstance(child))result.add(type.cast(child));if(child instanceof java.awt.Container)result.addAll(find((java.awt.Container)child,type));}return result;}
    public static void main(String[] args)throws Exception {
        System.setProperty("java.awt.headless","true");
        byte[] password=Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt"));
        byte[] digest=MessageDigest.getInstance("MD5").digest(new String(password,StandardCharsets.UTF_8).trim().getBytes(StandardCharsets.UTF_8));
        StringBuilder hex=new StringBuilder();for(byte item:digest)hex.append(String.format("%02x",item&255));hash=hex.toString();java.util.Arrays.fill(password,(byte)0);
        JsonNode settings=call(request("settings"));
        require(settings.path("projects").size()>0,"Project list missing");
        require(settings.path("detailDays").asInt()==30 && settings.path("headerDays").asInt()==365,"Defaults differ");
        require(settings.path("canManageSettings").asBoolean(),"Administrator cannot manage settings");
        require(settings.path("captureEnabled").asBoolean() && settings.path("captureScope").asText().contains("Scripts"),"Capture state is incorrect");
        String project=settings.path("projects").get(0).path("guid").asText();
        JsonNode list=call(request("list").put("projectGuid",project).put("days",30).put("objectType",23).put("search","test").put("offset",0).put("limit",50));
        require(list.path("entries").isArray(),"History response missing entries");
        call(request("saveSettings").put("detailDays",30).put("headerDays",365).put("retentionEnabled",false));
        require(call(request("settings")).path("headerDays").asInt()==365,"Settings save failed");
        reject(request("list").put("projectGuid","11111111-1111-4111-8111-111111111111"));
        reject(request("detail").put("eventId",Long.MAX_VALUE));
        reject(request("saveSettings").put("detailDays",366).put("headerDays",365));
        reject(request("saveSettings").put("detailDays",30).put("headerDays",365).put("retentionEnabled",true));
        boolean denied=false;try{AuditLogService.invokeRequest("http://localhost:47100/axis/konga_admin","Admin","incorrect",request("settings"));}catch(org.apache.axis.AxisFault fault){denied=true;}require(denied,"Invalid credentials accepted");
        final AuditLogPanel[] panel=new AuditLogPanel[1];
        SwingUtilities.invokeAndWait(()->{panel[0]=new AuditLogPanel(payload->call(payload));panel[0].start();});
        long deadline=System.currentTimeMillis()+15000;boolean loaded=false;
        while(System.currentTimeMillis()<deadline){final boolean[] ready={false};SwingUtilities.invokeAndWait(()->{java.util.List<JComboBox> combos=find(panel[0],JComboBox.class);ready[0]=combos.get(0).isEnabled() && combos.get(0).getItemCount()>1;});if(ready[0]){loaded=true;break;}Thread.sleep(30);}
        require(loaded,"Live panel did not load selectable projects");
        SwingUtilities.invokeAndWait(()->{require(find(panel[0],JButton.class).stream().anyMatch(button->"Refresh".equals(button.getText()) && button.isEnabled()),"Refresh disabled");panel[0].disconnect();});
        System.out.println("PASS "+checks+" live audit transport/UI checks; "+settings.path("projects").size()+" real projects; no test events inserted");
        System.exit(0);
    }
}
