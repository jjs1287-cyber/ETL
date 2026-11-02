/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <stdio.h>

#include <konga/Utils/StringUtils.h>	          
#include <konga/Utils/PortDefs.h>		           

void get_header_string(char*line,char*buf){
	int k1,k2;
	char c;
	k1 = konga::findk(line,':');
	for(k1++;(c=line[k1])==' ';k1++){;}
	for(k2=k1+1;(c=line[k2])!='\n'&&c!='\0';k2++){if(c=='\'')line[k2]='"';}
	while(line[k2-1]==' ')k2--;
	line[k2]=0;
	strcpy(buf,&line[k1]);
}

void parse_smtp(const char*filename,char*to,char*from,char*subject,char*process_vars){
	FILE*in=fopen(filename,"r");
	to[0]=from[0]=subject[0]=process_vars[0]=0;
	char c,line[1024];
	                                             
	int i,k1,k2;
	if(in!=0){
		while(fgets(line,1024,in)){
			if(line[0]=='\n'||line[0]=='\r')break;
			else if(strnicmp(line,"to:",3)==0){
				get_header_string(line,to);
			} else if(strnicmp(line,"from:",5)==0){
				get_header_string(line,from);
			} else if(strnicmp(line,"subject:",8)==0){
				get_header_string(line,subject);
			} 
		}
		long lCurPos = ftell(in);
		fseek(in,0,SEEK_END);
		long lEndPos = ftell(in);
		fseek(in,lCurPos,SEEK_SET);
		if( lEndPos > lCurPos ){
			char* buf = new char[lEndPos - lCurPos + 1], *pc;
			if( fread(buf,sizeof(char),lEndPos - lCurPos,in) > 0 &&
				(pc=strstr(buf,"<!--- ETLForge")) != 0 )
			{
				k1=konga::findk(pc,'{');
				k2=konga::findk(pc,'}');
				if(k2>k1){
					pc[k2+1]=0;
					for(i=0;k1<=k2;k1++){if((c=pc[k1])=='\'')c='"'; process_vars[i++]=c;}	                
					process_vars[i]=0;
				}
			}
			delete[] buf;
		}
		fclose(in);
	}
	if(to[0]==0)strcpy(to,"Not found");
	if(from[0]==0)strcpy(from,"Not found");
	if(subject[0]==0)strcpy(subject,"Not found");
	if(process_vars[0]==0)strcpy(process_vars,"Not found");
}
