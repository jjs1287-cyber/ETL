/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <string.h>

#include "RCStringTable.h"
#include "FileUtils.h"
#include "StringUtils.h"
#include "ErrorResolver.h"
#include "PortDefs.h"		                                         


namespace {
	const char def[] = "#define";
	const char def1[] = "STRINGTABLE";
	const char def2[] = "BEGIN";
	const char def3[] = "END";
	const char ws[] = " \t\r\n";
}

konga::RCStringTable::RCStringTable(void) : m_in(0), m_lineNumber(0) {
	line[0]=0;
}

konga::RCStringTable::~RCStringTable(void) {
	close_file();
}

bool konga::RCStringTable::Parse(const std::string&stRCFileName,std::map<int,std::string>&mapStringTable)
{
    std::string stDrive, stDir, stFN, stExt;
    konga::_tsplitpath(stRCFileName.c_str(), stDrive, stDir, stFN, stExt);
    if(stExt!=".rc"){
        m_stErr=std::string("The rc file path must end with \".rc\"\nfile name = ")+stRCFileName;
        return false;
    }
	if(!stDrive.empty())stDrive+=":";
    std::string stResourceFN= stDrive+stDir+"resource.h";

	close_file();
    m_in=fopen(stResourceFN.c_str(),"r");
    m_lineNumber=0;
    if(m_in==0){
		const unsigned long lErr = GetLastError();
		const std::string& reason = ErrorResolver().Resolve(lErr);
        m_stErr=std::string("Cannot open resource.h\nfile name = ")+stResourceFN;
		m_stErr += "\nReason: " + reason;
        return false;
    }

    std::map<std::string,int>mapResourceH;

    while(fgets_()){
       char*s=strstr(line, def);
       if(s==0)continue;
       s=skipws(s+strlen(def));
       if(s==0){
           m_stErr=std::string("Parse resource.h error, unexpected EOF uncountered.\nline number = ")+konga::toString(m_lineNumber)+"\nfile name = "+stResourceFN;
           return false;
       }
       size_t i1=::strcspn(s, ws);
       std::string id(s,i1);
       s=skipws(s+i1);
       size_t i2=::strcspn(s, ws);
       if(s==0){
           m_stErr=std::string("Parse resource.h error, unexpected EOF uncountered.\nline number = ")+konga::toString(m_lineNumber)+"\nfile name = "+stResourceFN;
           return false;
       }
       std::string value(s,i2);
       size_t k=strspn(s,"0123456789");

       if(k==i2){
           mapResourceH[id]=atoi(value.c_str());
       } else {
           printf("??? %s %s\n",id.c_str(),value.c_str());
       }
    }

	close_file();

    m_in=fopen(stRCFileName.c_str(),"r");
    m_lineNumber=0;
    if(m_in==0){
		const unsigned long lErr = GetLastError();
		const std::string& reason = ErrorResolver().Resolve(lErr);
        m_stErr=std::string("Cannot open rc file\nfile name = ")+stRCFileName;
		m_stErr += "\nReason: " + reason;

        return false;
    }
    size_t len_def2=strlen(def2);
    size_t len_def3=strlen(def3);
    while(fgets_()){
        if(strstr(line,def1)!=0){
            char*s0;
            while((s0=fgets_())!=0){
                char c=line[len_def2];
                if(strncmp(line,def2,len_def2)==0&&(c==' '||c=='\t'||c=='\r'||c=='\n')){
                    while((s0=fgets_())!=0){
                        c=line[len_def3];
                        if(strncmp(line,def3,len_def3)==0&&(c==' '||c=='\t'||c=='\r'||c=='\n'))break;
                        char*s=skipws(line);
                        if(s==0){
                            m_stErr=std::string("Unexpected EOF!\nline number = ")+konga::toString(m_lineNumber)+"\nfile name = "+stRCFileName;
                            return false;
                        }
                        size_t i1=::strcspn(s, ws);
                        std::string id(s, i1);
                        s=skipws(s+i1);
                        if(s==0||s[0]!='"'){
                            m_stErr=std::string("Unexpected char encountered, format string should starts with '\"'\nline number = ")+konga::toString(m_lineNumber)+"\nfile name = "+stRCFileName;
                            return false;
                        }
                        s++;
                        size_t i2=strlen(s)-1;
                        while(s[i2]=='\r'||s[i2]=='\n')i2--;
                        if(s[i2]!='"'){
                            m_stErr=std::string("Unexpected char encountered, format string should end with '\"'\nline number = ")+konga::toString(m_lineNumber)+"\nfile name = "+stRCFileName;
                            return false;
                        }
                        std::string stFormat(s,i2);

						std::map<std::string,int>::iterator it=mapResourceH.find(id);
                        if(it==mapResourceH.end()){
                            m_stErr=std::string("Cannot find label from resource.h\nid = ")+id+"\nline number = "+konga::toString(m_lineNumber)+"\nfile name = "+stRCFileName;
                            return false;
                        }
                        int i=it->second;

						                                                                         
						                           
						std::string stFormat_;
						konga::ReplaceStringWithString(stFormat, stFormat_, "\\\\", "\\");
						konga::ReplaceStringWithString(stFormat_, stFormat, "\"\"", "\"");
						konga::ReplaceStringWithString(stFormat, stFormat_, "\\\"", "\"");
						konga::ReplaceStringWithString(stFormat_, stFormat, "\\n", "\n");
                        mapStringTable[i]=stFormat;
                                                                                
                    }
                    break;
                }
            }
            if(s0==0)break;
        }
    }

	close_file();

    return true;
}

char* konga::RCStringTable::skipws(char*s){
    if(s==0)return 0;
    s+=strspn(s, ws);
    if(s[0]==0){
        s=fgets_();
        s=skipws(s);
    }
    return s;
}
