/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <ctype.h>

#include <limits>

#include <konga/Utils/PortDefs.h>	                   
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/StringTokenizer.h>

                                             
#include <konga/TransformationUtils/Definitions.h>

#include "Token.h"
#include "DataElement.h"
#include "TransException.h"

using namespace konga;
using std::string;

const int Token::Pr[]={
	4,4,4,4,		                          
	4,4,4,4,		                                        
	3,3,3,			                  
	7,7,8,8,		                     
	9,				      
	6,6,6,6,6,6,	                         
	1,10,			                         
	0,10,			                 
	10,10,10,10,	                                           
	10,10,10,10,10,	                                         
	9,				      
	4,5,4,			                
	1,2,			              
	4,10,			                    
	3,				            
	8,				      
	11,				              
	11				          
};

const char* Token::OpStr[]={
			"Unknown", "IDE", "ODE", "Num",
			"StrConst", "SourceDE", "GlobalDE", "LocalDE",
			"Set","+=","-=",
			"Add", "Sub", "Mul", "Div",
			"Not",
			"NE", "EQ", "GT", "GE", "LT", "LE",
			"LP", "RP",
			"LBrace", "RBrace",
			"LBrachet", "RBrachet", "StartArray", "EndArray",
			"Neg", "_++", "_--", "++_", "--_"
			"POW",
			"ODBC", "And", "Or",
			"Func", "Comma",
			"BoolConst", "Marker",
			"Semicolon",
			"Mod",
			"WhiteSpaces",
			"Comment"};


                                 
                                 
                                                                             
                                   
                                                                                                               
                                                                      
                    
                   
                   
                 
                  
                  
                
                                                            
                                         
                                                               
                                                                                                
                                                                                      
                                                                                     
                                                                          
                                 

const Token::FNC Token::fnc[]={
	{"RecordCount(","",1},
	{"int(","*",2},
	{"long(","*",3},
	{"float(","*",4},
	{"double(","*",5},
	{"string(","*",6},
	{"bool(","*",7},
	{"date(","*",8},
	{"log(","d",9},
	{"log10(","d",10},
	{"pow(","dd",11},
	{"exp(","d",12},
	{"if(","b*.",13},
	{"Trim(","s",14},
	{"RTrim(","s",15},
	{"LTrim(","s",16},
	{"Right(","si",17},
	{"Left(","si",18},
	{"Mid(","sii",19},
	{"Replace(","sss",20},
	{"LPad(","si",21},
	{"RPad(","si",22},
	{"Now(","",23},
	{"GeneralDate(","D",24},
	{"LongDate(","D",25},
	{"MediumDate(","D",26},
	{"ShortDate(","D",27},
	{"LongTime(","D",28},
	{"MediumTime(","D",29},
	{"ShortTime(","D",30},
	{"DBLookup(","ss",31},
	{"DBLookupInt(","ss",32},	                                                                  
	{"quote(","s",33},
	{"dquote(","s",34},
	{"CVTDate(","sss",35},
	{"Format(","s*",36},
	{"AutoNumber(","",37},
	{"Null(","",38},
	{"IsNull(","*",39},
	{"IsValid(","*",40},
	{"Eval(","**",41},
	{"Index(","ss.",42},
	{"CacheLookUp(","ss",43},
	{"FindByValue(","*rr",44},
	{"FindValue(","*rr",100},
	{"Case(",".",45},
	{"InList(",".",46},
	{"Length(","*",47},
	{"Max(","r",48},
	{"Min(","r",49},
	{"Exist(","*r",50},
	{"Sum(","r",51},
	{"Count(","r",52},
	{"FindByPos(","ir",53},
	{"SumString(","r.",54},             
	{"Set(",".",60},
	{"Get(",".",61},
	{"GetXmlString(","s",68},
	{"Truncate(","sii",69},
	{"RaiseError(","*",70},
	{"Eval3(","**s",200},
	{"Md5(","s",201},
	{"IsPresent(","s",202},
	{"IsNil(","s",203},
	{"CountSubString(","ss",204},
	{"Days(","i",205},
	{"Hours(","i",206},
	{"Minutes(","i",207},
	{"Now_(","",209},
	{"LdapConnect(",".",210},
	{"LdapAdd(","ss",211},
	{"LdapRemove(","ss",212},
	{"LdapExecute(","s",213},
	{"LdapSearch(",".",214},
	{"LdapReplace(","ss",215},
	{"SelectNodeFromXmlAny(","sr",220},
	{"GetNodeName(","s",221},
	{"GetNodeValue(","s",222},
	{"CreateNode(",".",223},
	{"Attribute(","ss",224},
	{"SelectSingleNode(",".",225},
	{"FormatDate(","Ds",226},
	{"Guid(","",227},
	{"DayOfWeek(","D",228},
	{"DayOfMonth(","D",229},
	{"MonthOfYear(","D",230},
	{"LastDayOfMonth(","D",231},
	{"SourceFileName(","",232},
	{"ToLower(","s",233},
	{"ToUpper(","s",234},
	{"Base64Encode(","s",235},
	{"Base64Decode(","s",236},
	{"SendSystemEmail(","sss",237},
	{"SendEmail(",".",238},
	{"RegExMatch(",".",239},
	{"DbExecute(",".",240},
	{"RegExReplace(","sss",241},
	{"CallStoredProcedure(",".",242},
	{"LdapDeleteEntry(","s",243},
	{"SqlEscape(","s",244},
	{"WriteToOperationLog(","s",245},
	{"CancelOperationChain(","s",246},
	{"LPadChar(","ssi",247},
	{"RPadChar(","ssi",248},
	{"ReadFile(",".",249},
	{"WriteFile(",".",250},
	{"GetLastError(","",251},
	{"SetLastError(","s",252},
	{"ResetLastError(","",253},
	{"FlushFile(",".",254},
	{"While(","b*",255},
	{"SelectNodesFromXmlAny(","sr.",256},
	{"LdapRename(","ss**",257},
	{"FlushAllFiles(",".",258},
	{"RunOperation(",".",259},
	{"SumCsv(","r",260},
	{"RunScript(","s.",261},
    {"ArgumentList(",".",466},
    {"RunXSLT(","ss.",465},
	{"Split(","ss",262},
	{"GetHostByIp(","s",263},
	{"SelectNodes(",".",264},
	{"SendEmailMessage(","s",265},
	{"ArrayToMultipleValues(",".",266},
	{"DebugBreak(",".",267},
	{"ReadArrayString(","s.",268},
	{"Base64EncodeFile(",".",269},
	{"FileList(","f",270},
	{"Sleep(","i",271},
	{"InitializeDiff(","s",272},
	{"SetDiffChunkSize(","i",273},
	{"DiffKeyList(",".",274},
	{"OrderedDiffKeyList(",".",275},
	{"DiffAdd(","",276},
	{"DiffDelete(","",277},
	{"DiffUpdate(","",278},
	{"DiffComplete(","",279},
	{"ResetDiff(","si",280},
	{"TrimChars(","ss",281},
	{"LTrimChars(","ss",282},
	{"RTrimChars(","ss",283},
	{"ToProper(","s",284},
	{"IsInteger(","*",285},
	{"GetServerName(","",286},
	{"IsValidString(","s",287},
	{"SourceInstanceCount(","",300},
	{"TargetInstanceCount(","",301},
	{"CreateInstance{","",302},
	{"SetDbInsert(","",303},
	{"SetDbUpdate(","",304},
	{"Unmap(","",305},
	{"BinaryToUuid(","b",306},
	{"UuidToBinary(","s",307},
	{"BinaryToHex(","b",308},
	{"HexToBinary(","s",309},
	{"ReRunOperation(",".",310},
	{"MD5AsTwoNumbers(",".",311},
	{"RunPlugin(","s",315},
	{"GetInputString(","s",316},
	{"Mod(","**",317},
	{"UrlEncode(","s.",318},
	{"GetOperationQueue(",".",319},
	{"WaitForOperation(","s.",320},
	{"IfNull(","**",321},
	{"IfEmpty(","**",322},
	{"InitCounter(","*.",323},
	{"RunGroovyScript(","s.",401},
	{"DbCloseConnection(","s",402},
	{"DbRollbackTransaction(","s",403},
	{"Map(","",460},
	{"Collection(","",461},
	{"GetSourceInstanceMap(",".",462},
	{"GetSourceAttrNames(",".",463},
	{"CollectValues","mc",464},
	{0,0,0}
};

namespace {
	int scan_number(const char*str){
		int k,ip=0,ie=0,id=0,iq=0;
		unsigned char c,cc,c0=0,c_uppercase;
		for(k=0;(c=str[k])!=0;k++){
			if(isdigit(c))id++;
			else if(id==0&&iq==0&&(c=='+'||c=='-'))iq++;
			else if(c=='.'&&ie==0)ip++;
			else if(ie==0&&((c_uppercase=toupper(c))=='E'||c_uppercase=='D')&&
				k>0&&(isdigit(c0)||c0=='.')&&
				(isdigit(cc=str[k+1])||((cc=='+'||cc=='-')&&isdigit(str[k+2])))
				){
				ie++;
				k++;
			}
			else if(c==' '&&((c_uppercase=str[k+1])=='E'||c_uppercase=='D')){
				                                                
				c=c0;
			} else break;
			c0=c;
		}
		if(ip>1||ie>1){
			                                         
			for(k=id=ip=0;(c=str[k])!=0;k++){
				if(isdigit(c))id++;
				else if(id==0&&iq==0&&(c=='+'||c=='-'))iq++;
				else if(ip==0&&c=='.')ip++;
				else break;
			}
		}
		return k;
	}

	bool IsInteger(const char*s){
		while(*s) {
			if(!isdigit(*s++))
				return false;
		}
		return true;
	}
	std::string construct_location_str(const char*str0,int length,long& lineNumber){
		konga::StringTokenizer tk(str0, length, "\n");
		int line=1;
		std::string token;
		while(tk.NextToken()){
			token=tk.token();
			if(token[0]=='\n')line++;
		}
		lineNumber = line;
		if(line==1)return token;
		else return std::string("at line ")+konga::toString(line)+"\n"+token+"\n";
	}
}

Token::Token(Type type, const char*str) :  m_type(type), m_data(-1), m_lineNumber(1), m_de(0), m_stName( str!=0 ? str : "")
{
}

bool Token::isSourceDE(const std::set<std::string>*source_de_set, const std::string& name0, bool isFlat){
	if(isFlat){
		if(name0.find('#')!=std::string::npos){
			return source_de_set!=0 && source_de_set->find(rmInstanceTag(name0))!=source_de_set->end();
		}
		return source_de_set!=0 && source_de_set->find(name0)!=source_de_set->end();
	}
	else {
		const char*name=name0.c_str();
		int n1=strcspn(name, '$'), n2=strcspn(name, '.'), n=n1<n2?n1:n2;
		return name[n]!=0;
	}
}

std::string Token::rmInstanceTag(const std::string& name0){
	StringTokenizer tk(name0.c_str(),"#.$");
	bool isInstanceTag=false;
	std::string str="";
	while(tk.NextToken()){
		const char* t=tk.token();
		switch(t[0]){
			case '#': isInstanceTag=true; break;
			case '.':
			case '$': isInstanceTag=false;
			default:
				if(!isInstanceTag)str+=t;
				isInstanceTag=false;
				break;
		}
	}
	return str;
}

Token::Token(const char*str0,int&i,bool IsStart,bool hasValidLHS, const FNC* fnc_array, const std::set<std::string>*source_de_set, bool isFlatSource, int&err) : m_type(Unknown), m_data(-1), m_lineNumber(1), m_de(0)
{
	err=0;
	unsigned char c;
	const char*str=&str0[i];
	int k,ifunc;
	if (strnicmp(str, CR_COM, strlen(CR_COM))==0 
	|| strnicmp(str,CR_JSCRIPT,strlen(CR_JSCRIPT))==0
	|| strnicmp(str,CR_VBSCRIPT,strlen(CR_VBSCRIPT))==0)
	{
		k=konga::strcspn(str,'(');
		m_type=Func;
		m_stName=string(str,k);
		if(str[k])k++;
		goto Exit;
	}
	else if((ifunc=find_func(str, fnc_array))>=0)
	{                                             
		m_type=Func;
		m_stName=fnc[ifunc].fnc_name;
		m_data=ifunc;
		k=m_stName.length();
		goto Exit;
	}
	k=1;
	c=str[0];
	switch(c){
	case ' ':
	case '\t':
	case '\r':
	case '\n':
		m_type=WhiteSpaces;
		k=strspn(str," \t\r\n");
		m_stName=std::string(str,k);
		break;
	case ';':
		m_type=Semicolon;
		m_stName=";";
		break;
	case ',':
		m_type=Comma;
		m_stName=",";
		break;
	case '"':
	case '\'':
		{
			bool bQuoteClosed=false;
			char c1;
			for(k=1;(c1=str[k])!=0&&!bQuoteClosed;k++){
				if(c1==c){
					bQuoteClosed=true;
					k++;
					break;
				}
				else if(c1=='\\'){
					k++;
				}
				m_stName+=c1;
			}
			if(!bQuoteClosed){
				throw konga::TokenSyntaxException("unbalanced quote");
			}
			m_stName=std::string(str,k);
			m_type=StrConst;
		}
		break;
	case '+':
	case '-':
		{
			if(strlen(str)==1){
				throw konga::TokenSyntaxException("Operator missing 2nd operand");
			}
			char c1=str[1];
			if(c1=='='){              
				k++;
				m_type=c=='+'? PSet: MSet;
				m_stName=std::string(str,k);
			}
			else if(c1==c){              
				k++;
				m_type=c=='+'? PrePP: PreMM;
				m_stName=std::string(str,k);
			}
			else if(IsStart&&(c1=='.'||isdigit(c1))){
				k=scan_number(str);
				m_type=Num;
				m_stName=std::string(str,k);
			}
			else if(IsStart&&c=='-'){
				m_type=Neg;
				m_stName="-";
			}
			else if(c=='+'){
				m_type=Add;
				m_stName="+";
			}
			else {
				m_type=Sub;
				m_stName="-";
			}
		}
		break;
	case '*':
		if(str[1]=='*'){
			m_type=POW;
			m_stName="**";
			k=2;
		} else {
			m_type=Mul;
			m_stName="*";
		}
		break;
	case '/':
		if(str[1]=='/'){
			m_type=Comment;
			k=::strcspn(str,"\r\n");
			k+=::strspn(&str[k],"\r\n");
			m_stName=std::string(str,k);
		} else if(str[1]=='*'){
			m_type=Comment;
			char cc=0;
			for(k=2;(cc=str[k])!=0;k++){
				if(cc=='*' && str[k+1]=='/'){
					k+=2;
					break;
				}
			}
			m_stName=std::string(str,k);
		} else {
			m_type=Div;
			m_stName="/";
		}
		break;
	case '(':
		m_type=LP;
		m_stName="(";
		break;
	case ')':
		m_type=RP;
		m_stName=")";
		break;
	case '[':
		m_type=LBRACKET;
		m_stName="[";
		break;
	case ']':
		m_type=RBRACKET;
		m_stName="]";
		break;
	case '{':
		m_type=LBRACE;
		m_stName="{";
		break;
	case '}':
		m_type=RBRACE;
		m_stName="}";
		break;
	case '|':
		m_type=Or;
		if(str[1]=='|')k=2;
		m_stName="||";
		break;
	case '&':
		m_type=And;
		if(str[1]=='&')k=2;
		m_stName="&&";
		break;
	case '^':
		m_type=POW;
		m_stName="^";
		break;
	case '<':
		if(str[1]=='='){
			m_type=LE;
			m_stName="<=";
			k=2;
		} else {
			m_type=LT;
			m_stName="<";
		}
		break;
	case '>':
		if(str[1]=='='){
			m_type=GE;
			m_stName=">=";
			k=2;
		} else {
			m_type=GT;
			m_stName=">";
		}
		break;
	case '!':
		if(str[1]=='='){
			m_type=NE;
			m_stName="!=";
			k=2;
		} else {
			m_type=Not;
			m_stName="!";
		}
		break;
	case '=':
		if(str[1]=='='){
			m_type=EQ;
			m_stName="==";
			k=2;
		} else if(str[1]=='<'||str[1]=='>'){
			throw konga::TokenSyntaxException(std::string("TokenizeRPN syntax error, \"")+std::string(str,2)+"\" should be reversed");
		} else {
			if(hasValidLHS){
				m_type=Set;
				m_stName="=";
			} else {
				throw konga::TokenSyntaxException("TokenizeRPN syntax error, the left hand side of assignment operator must be a local or global variable.");
			}
		}
		break;
	default:
		                                                             
		if((k=scan_de_name(str))>0){
			m_stName=std::string(str,k);
			if(stricmp(m_stName.c_str(),"TRUE")==0||stricmp(m_stName.c_str(),"FALSE")==0){
				m_type=BoolConst;
			} else {
				if(m_stName.at(0)=='$'){
					m_type=GlobalDE;
				}
				else if(isSourceDE(source_de_set, m_stName, isFlatSource)){
					m_type=IDE;
				}
				else {
					m_type=LocalDE;
				}
			}
		} else if(c=='.'||isdigit(c)){
			k=scan_number(str);
			m_type=Num;
			m_stName=std::string(str,k);
		} else {         
			throw konga::TokenSyntaxException("TokenizeRPN syntax error, encountered unrecognized token. \""+std::string(str,100));		}
		break;
	}
Exit:
	i+=k;
	m_stSource=construct_location_str(str0,i,m_lineNumber);
}

Token::~Token(){
	if(m_type==Num||m_type==StrConst||m_type==BoolConst) {
		delete m_de;
	}
}

int Token::AddNumberOfData(int n_comma,Type last_type, const FNC* fnc_array){
	switch(m_type){
	case StrConst:
	case Num:
	case BoolConst:
	case IDE:
	case GlobalDE:
	case LocalDE:
		return 1;
	case Set:
	case PSet:
	case MSet:
		return -1;
	case Or:
	case And:
	case NE:
	case EQ:
	case GT:
	case GE:
	case LT:
	case LE:
	case Add:
	case Sub:
	case Mul:
	case Div:
	case POW:
		return -1;
	case Neg:
	case Not:
	case PreMM:
	case PrePP:
	case PostMM:
	case PostPP:
		return 0;
	case Func:
		{
			const Token::FNC*f=&Token::fnc[m_data];
			if(strncmp(GetName().c_str(),CR_COM, strlen(CR_COM))==0
			||strncmp(GetName().c_str(),CR_JSCRIPT,strlen(CR_JSCRIPT))==0
			||strncmp(GetName().c_str(),CR_VBSCRIPT,strlen(CR_VBSCRIPT))==0
			||!f->isFixedArgumentCount()){
				return 1-(n_comma+((last_type!=Func)?1:0));
			} else {
				int i=find_func(GetName().c_str(), fnc_array);
				if(i<0)return 0;
				return 1-strlen(fnc[i].fnc_args);
			}
		}
	case LBRACE:
		return 1-(n_comma+((last_type!=LBRACE)?1:0));
	case Semicolon:
		return -1;
	default:
		return 0;	            
	}
}

int Token::find_func(const char* str, const FNC* fnc_array){
	const char* tmp;
	for(int i=0;(tmp=fnc_array[i].fnc_name);i++){
		if(strnicmp(tmp,str,strlen(tmp))==0)return i;	
	}
	return -1;
}

void Token::DefineDE(LocalVariables* local_variable_map) {
	if(m_type==LocalDE){
		if(local_variable_map==0)return;
		m_de=local_variable_map->get(m_stName);
		if(m_de==0){
			m_de=new DataElement();
			m_de->setName(m_stName.c_str());
			m_de->setNull();
			m_de->setToken(this);
			local_variable_map->put(m_de);
		}
		return;
	}
	if(m_type!=Num&&m_type!=StrConst&&m_type!=BoolConst)return;
	m_de=new DataElement();
	m_de->setToken(this);
	m_de->setNull(false);
	switch(m_type){
	case Num:
		{
		DataElement::Type deType = DataElement::t_string;	                                                                                 
		const string::size_type nDigits = m_stName.size();
		if ( IsInteger(m_stName.c_str()) ) {
			if ( nDigits <= std::numeric_limits<long>::digits10 )
				deType = DataElement::t_int;
			else if ( nDigits <= std::numeric_limits<long long>::digits10 )
				deType = DataElement::t_long;
		}
		else {
			if ( nDigits <= std::numeric_limits<double>::digits10 ) {
				deType = DataElement::t_double;
			}
		}
		m_de->setType(deType);
		switch(deType){
		case DataElement::t_int:
			m_de->m_value.i=atoi(m_stName.c_str());
			break;
		case DataElement::t_long:
			m_de->m_value.l=atoll(m_stName.c_str());
			break;
		case DataElement::t_double:
			if(m_stName.find('d')!=std::string::npos || m_stName.find('D')!=std::string::npos){
				std::string str1(m_stName);
				konga::ReplaceCharWithChar(str1,'d','e');
				konga::ReplaceCharWithChar(str1,'D','e');
				sscanf(str1.c_str(),"%lg",&m_de->m_value.d);
				break;
			}
			sscanf(m_stName.c_str(),"%lg",&m_de->m_value.d);
			break;
		default:
			m_de->setValue(m_stName.c_str());
			break;
		}
		break;
		}
	case StrConst:
		{
		const size_t len=m_stName.size();
		const string str1(m_stName.substr(1,len-2));
		const string str2( konga::restoreEscapedCharacters(str1) );
		m_de->setType(DataElement::t_string);
		m_de->setValue(str2.c_str());
		}
		break;
	case BoolConst:
		m_de->m_type=DataElement::t_bool;
		m_de->m_value.b=(stricmp(m_stName.c_str(),"TRUE")==0);
		break;
	default:
		break;
	}
}
                                                                      


int Token::priority() const
{
	return Pr[(int)m_type];
}

int konga::scan_de_name(const char*str)
{
	int i,k=0;
	unsigned char c;
	if(str==0)return -1;
	if(isdigit((unsigned char)str[0]))return -1;
	k=(int)::strcspn(str,"+-/*()<>,;\\{} \t\n\r&|=!^[]");
	for(i=0;i<k;i++){
		c=str[i];
		if(c!='.'&&!isdigit(c))return k;
	}
	return -1;
}

LocalVariables::~LocalVariables(){
	for(std::map<string,DataElement*>::iterator it=locaVar_map.begin();
		it!=locaVar_map.end(); ++it){
			DataElement*de=it->second;
			delete de;
	}
}

void LocalVariables::put(DataElement* de){
	locaVar_map[de->getName()]=de;
    assigned.insert(de);
}

void LocalVariables::loadArguments(const std::vector<konga::DataElement*>&arguments){
	for(std::vector<konga::DataElement*>::const_iterator it=arguments.begin(); it!=arguments.end(); ++it){
		put(*it);
	}
}


DataElement*LocalVariables::get(const string& name){
	std::map<string,DataElement*>::iterator it=locaVar_map.find(name);
		if(it!=locaVar_map.end())return it->second;
		DataElement*de=new DataElement(name.c_str());
		locaVar_map[name]=de;
		return de;
}


void LocalVariables::markAssigned(DataElement* de){
    std::map<string,DataElement*>::const_iterator it=locaVar_map.find(de->getName());
    if(it!=locaVar_map.end() && it->second==de) assigned.insert(de);
}
