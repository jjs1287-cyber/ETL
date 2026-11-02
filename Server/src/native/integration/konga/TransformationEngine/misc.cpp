/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string.h>

#ifndef WIN32
	#include <stdarg.h>	                 
#endif

#include <konga/Utils/KString.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Xml/DOMNamedNodeMapPtr.h>

#include "TreeMapper.h"
#include "misc.h"

using namespace konga;
using std::string;
using std::map;

bool GetLevelInfo(KString& sEndPath, KString &sLevelName, int &iInstanceNumber)
{
	iInstanceNumber = 0;
	StringTokenizer tokCur(sEndPath,"$.");
	const char * CurTok;

                                   
          
	                                                                                
	                                                                                 
	                                                                                     
	                                                                                                       
	tokCur.NextToken();
	CurTok = tokCur.token();

	switch(CurTok[0])
	{
		case '$':
		case '.':
			break;
		default:
			KString sLevelStr = CurTok;
			iInstanceNumber = 0;
			int InstPos = sLevelStr.Find('#');
			if (-1 != InstPos)
			{
				sLevelName = sLevelStr.Left(InstPos);
				KString sInstNum = sLevelStr.Right(sLevelStr.GetLength() - InstPos - 1);
				sInstNum.Replace('_', '-');
				if (0 != sInstNum.GetLength())
				{
					iInstanceNumber = atoi(sInstNum);
				}
			}
			else
				sLevelName = sLevelStr;

			KString sMsg;
			sMsg.Format("%s %d", (const char*)sLevelName, iInstanceNumber);
			               

			                                                                                            
			                                       
			sEndPath.Delete(0, sLevelStr.GetLength()+1);
			break;
	}

	return true;
}


                                     
char* FGETS(konga::AutoArrayDelete<char>& buf,int&l,FILE*in)
{
	int i=0;
	char c,*p = 0, *pRetVal = 0;
	bool bFoundCR = false;

	while((p=fgets(&buf[i],(l-i),in))!=0)
	{
		for(int j=i;j<l;j++)
		{
			if ((c=buf[j])==0)
				break;
			if (c=='\n')
			{
				bFoundCR = true;
				break;
			}
		}

		buf[l]=0;
		i=l - 1;
		if (!bFoundCR)
		{
			                                                 
			l=2*l;
			AutoArrayDelete<char> new_buf(new char[l+1]);
                      
                       
			strcpy(new_buf.Get(),buf.Get());
			buf.Reset( new_buf.Release() );
                     
		}
		pRetVal = buf.Get();

		if (bFoundCR)
			break;
	}

	return pRetVal;
}

void RemoveCRFromString(char* const buf0,int buf_len)
{
	if(buf0 == 0 || strlen(buf0) == 0 || strchr(buf0, 13) == 0)
		return;

	int n0=(int)strlen(buf0);
	char*buf1=new char[n0+1];
	int k0=0,k1=0,n;
	while((k0+(n=konga::findk(&buf0[k0],'\r')))<n0){
		if(n>0)strncpy(&buf1[k1],&buf0[k0],n);
		k0+=(1+n);
		k1+=n;
	}

	if(k0>0){
		if(n0>k0)strncpy(&buf1[k1], &buf0[k0], n0-k0);
        strncpy(buf0,buf1,k1+n0-k0);
        buf0[k1+n0-k0]=0;
	}

	delete[]buf1;
	return;

}

char* read_file(const char*filename){
	char *buff;
	FILE*in=fopen(filename,"rb");
	if(in){
		fseek(in,0,SEEK_END);
		long file_length=ftell(in);
		buff=new char[file_length+3];
		fseek(in,0,SEEK_SET);
		fread(buff,sizeof(char),file_length,in);
		fclose(in);
		buff[file_length]='\r';
		buff[file_length+1]='\n';
		buff[file_length+2]=0;
	} else {
		buff=new char[100]; 
		sprintf(buff,"failed to open \"%s\"\n",filename);
	}
	return buff;
}

  
                                                               
                                                               
                               
                                                                    
                                                                   
                                                                  
  
char* get_token(char*& str, char*& token, const char* tok_chrs)
{
	if( !str || !*str ) return token=0;
	char* pc = strpbrk(str,tok_chrs);
	if( pc ) *pc=0;
	if( token ) strcpy(token,str);
	else if(str) token = strnewdup(str);
	else return token = 0;
	if( pc ) str=pc+1;
	else str=0;
	return token;
}

char* get_token(char*& str, const char* tok_chrs)
{
	char*token=0;
	if( !str || !*str ) return 0;
	char* pc = strpbrk(str,tok_chrs);
	if( pc ) *pc=0;
	if(str) token = strnewdup(str);
	else return 0;
	if( pc ) str=pc+1;
	else str=0;
	return token;
}

bool GetODBCTimeOut(long& dwTimeOut)
{
    dwTimeOut=GetOptionAsInt("Settings","ODBCTimeOut",-1);
    return true;
}

                                   
                                                                                                      
                                                                                            
                                                                         
                                                                      
                                                                                                                
                                                            
                                                             
                                   
                                    
bool GetNameValuePair(const KString&OverRideParams, char Delimiter, char Assignment, const KString&SearchName, KString& Value)
{

	                                                                                      

	                                                          
	KString _SearchName_=Delimiter+SearchName+Assignment;
	int iNamePos = OverRideParams.Find(_SearchName_, 0);
	if(iNamePos<0){                                             
		KString SearchName_=SearchName+Assignment;
		iNamePos = OverRideParams.Find(SearchName_, 0);
		if(iNamePos!=0)return false;
	}
	else {
		iNamePos++;
	}
	
	                                                                     
	int iAsgnPos = iNamePos+(int)SearchName.GetLength();

	if (-1 == iAsgnPos)
		return false;

	                                                                                      
	                           
	int iValPos = OverRideParams.Find(Delimiter, iAsgnPos);

	                                                                               
	if (-1 == iValPos)
	{
		Value = OverRideParams.Right(OverRideParams.GetLength() - (iAsgnPos+1));
	}
	else
	{
		Value = OverRideParams.Mid(iAsgnPos+1, iValPos - (iAsgnPos+1));
	}

	return true;
}

#if 0
void FPRINTF(FILE*out, const char*sMessage, ...)
{
    if(out)
    {
        va_list argList;
        va_start(argList, sMessage);

        KString sDescription;
        sDescription.FormatV(sMessage, argList);
		const char* s=(const char*)sDescription;
		for(int i=0, n=strlen(s);i<n;i++){
			fputc(s[i], out);
		}
                                                               
           
                                                                             
           
        va_end(argList);
    }
}
#endif

bool getXmlValidationRegistryFlag(){
	return GetOptionAsBoolean("TransformationEngine", "ValidateXML", true);
}

int updateTableNameAlias(const char*str, std::map<std::string, std::string>& mapTableAlias, const string& sBeginQuote, const string& sEndQuote)
{
	std::string terminators=" ,.\"" + sBeginQuote + sEndQuote;
	StringTokenizer tk(str, terminators.c_str());
	int pos=0, nTables=0;
	string ownerName, tableName, ownerAlias, tableAlias;
	string beginQuote, endQuote;
	while(tk.NextToken()){
		string token=tk.token();
		char c=token[0];
		switch(c){
		case ' ': pos++; break;
		case ',':
			            
			{
				if(tableAlias.empty()){
					tableAlias=tableName;
					ownerAlias=ownerName;
				}
				string sqlOwnerTable=toSqlTableName(ownerName, tableName, sBeginQuote, sEndQuote);
				string ownerTableAlias=getDETableName(ownerAlias, tableAlias);
				mapTableAlias[ownerTableAlias]=sqlOwnerTable;
				pos=0;
				ownerAlias="";
				tableAlias="";
			}
			ownerName="";
			tableName="";
			break;
		case '.':
			if(pos==0){
				ownerName=tableName;
				tableName="";
			} else {
				ownerAlias=tableAlias;
				tableAlias="";
			}
			break;
		default:
			{
				std::string closingQuote;
				if(token=="\""){
					closingQuote="\"";
				} else if(token==sBeginQuote){
					closingQuote=sEndQuote;
				}
				if(!closingQuote.empty()){
					token="";
					while(tk.NextToken()){
						std::string tmp=tk.token();
						if(tmp==closingQuote)break;
						token+=tmp;
					}
				}
				if(pos==0){
					if(tableName==""&&ownerName=="")nTables++;
					tableName=token;
				} else {
					tableAlias=token;
				}
			}
			break;
		}
	}
	return nTables;
}

std::string toSqlTableName(const string& owner,const string& table, const std::string& beginQuote, const std::string& endQuote)
{
	if(owner.empty()){
		return beginQuote+table+endQuote;
	}
	else {
		return beginQuote+owner+endQuote+"."+beginQuote+table+endQuote;
	}
}



string getDETableName(const string& owner, const string& table)
{
	if (owner.empty()) {
		return table;
	}
	else {
		return owner+":"+table;
	}
}




void parseOwnerTableFieldString(const char* str, string& owner, string& tableName, string& field, const string& sBeginQuote, const string& sEndQuote)
{
	string terminators="\"."+sBeginQuote+sEndQuote;
	StringTokenizer tk(str, terminators.c_str());
	owner="";
	tableName="";
	int pos=0;
	while(tk.NextToken()){
		string token=tk.token();
		char c=token[0];
		switch(c){
		case '.':
			pos++;
			break;
		default:
			{
				string close;
				if(token=="\"")close="\"";
				else if(token==sBeginQuote)close=sEndQuote;
				if(!close.empty()){
					token="";
					while(tk.NextToken()){
						string tmp(tk.token());
						if(tmp==close)break;
						token+=tmp;
					}
				}
				switch(pos)
				{
				case 0:
					field=token;
					break;
				case 1:
					tableName=field;
					field=token;
					break;
				case 2:
					owner=tableName;
					tableName=field;
					field=token;
					break;
				}
				break;
			}
		}
	}
}

string getNameFromSqlTableName(const char*str, const string& sBeginQuote, const string& sEndQuote)
{
	std::string owner, tableName;
	separateOwnerTableNames(str, owner, tableName, sBeginQuote, sEndQuote);
	if(owner.empty())return tableName;
	else return owner+":"+tableName;
}

void separateOwnerTableNames(const string& str, string& owner, string& tableName, const string& sBeginQuote, const string& sEndQuote)
{
	string terminators="\"."+sBeginQuote+sEndQuote;
	StringTokenizer tk(str.c_str(),terminators.c_str());
	owner.clear();
	tableName.clear();
	while(tk.NextToken()){
		string token=tk.token();
		if(token=="."){
			owner=tableName;
			tableName="";
		}
		else {
			string closingQuote;
			if(token=="\""){
				closingQuote="\"";
			}
			else if(token==sBeginQuote){
				closingQuote=sEndQuote;
			}
			if(!closingQuote.empty()){
				token.clear();
				while(tk.NextToken()){
					string tmp=tk.token();
					if(tmp==closingQuote)break;
					token+=tmp;
				}
			}
			unsigned int n=token.size();
			if(n>1 && !sBeginQuote.empty() && !sEndQuote.empty() && token.at(0)==sBeginQuote.at(0) && token.at(n-1)==sEndQuote.at(0)){
				tableName=token.substr(1,n-2);
			}
			else {
				tableName=token;
			}
		}
	}
}


KString findTableNameFromSelectSql(const KString& csSourceSQL){
	const char sFrom[]="FROM ";
	const char sFilterStart[]=" WHERE";
	int i1=csSourceSQL.Find(sFrom);
	int i2=csSourceSQL.Find(sFilterStart);
	if(i2<0)i2=(int)csSourceSQL.GetLength();
	if(i1>=0&&i2>i1){
		i1+=(int)strlen(sFrom);
		return csSourceSQL.Mid(i1,i2-i1);
	} else {
		return KString(csSourceSQL);
	}
}

void getBeginEndStringForTable(TreeMapper*p, const KString& sSourceSQL, string&sBeginStr, string&sEndStr)
{
	sBeginStr=p->getSqlBeginSourceName();
	sEndStr=p->getSqlEndSourceName();
	if(sBeginStr.empty()&&sEndStr.empty()){
		KString csTableName=(const char*)findTableNameFromSelectSql(sSourceSQL);
		char c1=csTableName.GetAt(0), c2=csTableName.GetAt(csTableName.GetLength()-1);
		if((c1=='\''&&c2=='\'')||(c1=='"'&&c2=='"')||(c1=='['&&c2==']')){
			sBeginStr=string(&c1,1);
			sEndStr=string(&c2,1);
		}
	}
}

void printDOMNode(konga::xml::DOMNodePtr node, FILE*out, const std::string&header, const std::string&dh)
{
	konga::xml::DOMNodeType type=node->getNodeType();
	if(type==konga::xml::NODE_ELEMENT)
	{
		if(node->getNodeValue()!="")
			fprintf(out,"%s[E]%s value = %s\n", header.c_str(), node.getNodeName().c_str(), node->getNodeValue().c_str());
		else
			fprintf(out,"%s[E]%s\n", header.c_str(), node.getNodeName().c_str());
		std::string header1=header+dh;

		konga::xml::DOMElementPtr element=(konga::xml::DOMElementPtr)node;
		konga::xml::DOMNamedNodeMapPtr attributes=element->getAttributes();
		unsigned int n=attributes->getLength();
		if(n>0)fprintf(out,"%s",header1.c_str());
		for(unsigned int i=0;i<n;i++){
			konga::xml::DOMNodePtr attr=attributes->getItem(i);
			if(i>0)fprintf(out,",");
			fprintf(out,"[A]%s=\"%s\"", attr->getNodeName().c_str(), attr->getNodeValue().c_str());
		}
		if(n>0)fprintf(out,"\n");

		konga::xml::DOMNodeListPtr children=node->getChildNodes();
		n=children.getLength();
		for(unsigned int i=0;i<n;i++){
			konga::xml::DOMNodePtr child=children->getItem(i);
			printDOMNode(child,out,header1,dh);
		}
	}
	else if(type==konga::xml::NODE_TEXT){
		fprintf(out,"%s[V] %s\n", header.c_str(), node->getText().c_str());
	}
}




