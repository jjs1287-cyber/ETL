 /*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __TRANSFORM_H_
#define __TRANSFORM_H_

#include <math.h>
#include <stack>
#include <vector>
#include <string>
#include <set>
#include "NativeJavaScript.h"

#include <konga/Utils/KString.h>
#include <konga/Utils/HashTable.h>  
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/AutoPtrCleanup.h>

#include <konga/DataElement/DataElement.h>
#include <konga/DataElement/Token.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/TransformationUtils/TransformationDataHolder.h>

#include "resource.h"                      

#define PAGE_SIZE 10000
#define RECORD_SIZE (64*1024)

class TreeMapper;
class CTransform;
class CRXmlData;
class CROMData;
class DTDParser;
class CRData;
class CROM;

namespace konga {
	class Debuggee;
	class LocalVariables;
}

class TokenArray{
public:
	int m_start_pos,m_end_pos;
	konga::Token**m_ts;
	bool m_javascript = false;
	std::string m_javascriptSource;
	std::map<std::string,konga::Token**> m_javascriptSources;
	TokenArray(int start_pos,int end_pos,const std::string& javascriptSource);
	TokenArray(CTransform*detrans,const char*buf,int start_pos,int end_pos,int&err);
	~TokenArray();
};

typedef std::vector<TokenArray*> TALIST;

class TokenArrayListItem {
private:
	std::string expr;
	TALIST* talist;

public:
	TokenArrayListItem(const char* source_expr, TALIST* talist0)
	  : expr(source_expr), talist(talist0) {}
	~TokenArrayListItem();

	const char*getSourceExpr() const {return expr.c_str();}
	TALIST* getTAList() const {return talist;}
};

class ParenthesisStack {
	CTransform* pTrans;

	konga::Token* token_start;
	int n_comma;
	int n_data_start;

	std::stack<konga::Token*> token_start_stack;
	std::stack<int> n_comma_stack;
	std::stack<int> n_data_start_stack;

	std::vector<konga::Token*>& rpn_tokens;
	std::vector<konga::Token*>& unused_tokens;

public:
	ParenthesisStack(CTransform* pTrans0,
		int n_data_start0, 
		std::vector<konga::Token*>&rpn_tokens0,
		std::vector<konga::Token*>&unused_tokens0);

	void push(konga::Token* token_start1);

	void pop();

	void add_comma(){
		n_comma++;
	}
	int get_nComma(){return n_comma;}
	int get_expected_nData(){return n_comma+n_data_start;}
	int checkSemicolon(konga::Token*close, konga::Token::Type lastId);
};


class CTransform {
	TreeMapper* m_pTreeMapper;
	konga::Debuggee* m_pDebuggee;
	std::map<konga::DataElement*,CRData*> de_CRData_map;
	std::stack<konga::LocalVariables*>local_variable_map_stack;
	std::map<std::string,konga::DataElement*> source_de_map;
    std::set<std::string> validatedLocals;

	friend class TransformationEngine;
	friend class TreeMapper;
	friend class StreamingTransform;
	friend class ParenthesisStack;

public:
	CTransform(TreeMapper*p, CROM* pTargetNode, const std::set<std::string>* source_de_set, bool isFlatSource);
	CTransform(TreeMapper*p, long lScriptId);

	~CTransform();
	TreeMapper*getTreeMapper() const {return m_pTreeMapper;}

	                           
	FILE* getDebugLog() const;

	TALIST* retrieveTokenArrayList(){
		TALIST* save=m_TokenArrayList;
		m_TokenArrayList=0;
		return save;
	}

	int Evaluate_dbg(const char*sExpression, TALIST*ta_list, const char* dataTypeStr, int sqlDataType, const char* sFormat, KString& sResult, CROM* pTargetAttribute);
	void Evaluate(const char*sExpression, TALIST*ta_list, konga::DataElement::Type target_data_type, const char* sFormat, CROM* pTargetAttribute,KString& sResult, int& err);

	int TokenizeTemplate(const char*strTemplate);
	etlforge::javascript::Value callJavaScriptHost(const std::string& name,const std::vector<etlforge::javascript::Value>& args,TokenArray* block);
	konga::Token**TokenizeToRPN(const char*str,int&err_code);
	TALIST* getCurrentTokenArrayList() const {return m_TokenArrayList;}
	size_t getInputDEListSize() const {return m_InputDEList.size();}
	konga::DataElement*getInputDEOf(size_t i) const {return m_InputDEList.at(i);}

	void createTAListItemForCrom(const char*templateStr, CROM*pAttribute, int&err);

	                  
	konga::LocalVariables* local_variable_map;
	konga::DataElement* getLocalVariable(const std::string& deName);
	void push_LocalVariables();
	void pop_LocalVariables();
    class LocalScope {
        CTransform& owner;
        TALIST* savedTokens;
        LocalScope(const LocalScope&);
        LocalScope& operator=(const LocalScope&);
    public:
        explicit LocalScope(CTransform& value):owner(value),savedTokens(value.m_TokenArrayList){
            owner.push_LocalVariables();
            owner.m_TokenArrayList=0;
        }
        ~LocalScope(){
            if(owner.m_TokenArrayList){
                for(size_t i=0;i<owner.m_TokenArrayList->size();++i) delete owner.m_TokenArrayList->at(i);
                delete owner.m_TokenArrayList;
            }
            owner.m_TokenArrayList=savedTokens;
            owner.pop_LocalVariables();
        }
    };
    
	int SetAutoNumber();
	int SetAutoNumber(int startIndex);

	konga::DataElement* Evaluate(konga::Token*ts[], bool& bFormatApplied,int&err);

public:
	KString m_lastErr;

protected:
	int m_partial_err;
	int m_record;                                                                                                      
	std::vector<konga::DataElement*>m_InputDEList;
	ParenthesisStack* n_comma;

private:
	TALIST* m_TokenArrayList;
	bool m_bFileTarget;
	int n_data;

	                                 
	CRData*m_sourceData;
	int m_AutoNumber;
	int m_sourceInstanceCount;
	konga::HashTable<const char*,konga::DataElement*>* m_table;

	                                                                                         
	CROM* const m_pTargetNode;	                               
	CROM* m_pTargetAttribute;		                                  
	const long m_lScriptId;				                                   
	  

	CTransform*m_pTrans_targetLoopNode;

	const std::set<std::string>* source_de_set;
	bool isFlatSource;

public:
	CRData* getSourceCRData() const {return m_sourceData;}
	void setCount(CROM*targetNode);
	void setCount(int iInstance);
	int GetRecord() const {return m_AutoNumber;}

private:
	void Init();
	void ResetDE();
	int ExecScriptMethod(const char*funcName,konga::DataElement*&de,
		konga::Token*ts[],std::vector<int>&ind, konga::AutoDeleteStack<konga::DataElement>&tmp);
	int ExecFunction(konga::Token*t, konga::DataElement*& de,
		konga::Token*ts[], std::vector<int>& ind, konga::AutoDeleteStack<konga::DataElement>& tmp);
	int createArray(konga::Token*t, konga::DataElement*& de,
		konga::Token*ts[], std::vector<int>& ind, konga::AutoDeleteStack<konga::DataElement>& tmp);

	int DefineSourceDE(const char*input_str);

	int Tokenize(const char*str, std::vector<konga::Token*>&tokens);
	int RPNOrdering(const std::vector<konga::Token*>&tokens, std::vector<konga::Token*>&rpn_tokens, std::vector<konga::Token*>&markers, std::vector<konga::Token*>&unused_tokens);
	konga::DataElement* processOneArg(int ifcn, konga::DataElement*de1, int&err, KString&err_msg);
	konga::DataElement* processTwoArgs(int ifcn, konga::DataElement*de1, konga::DataElement*de2, int&err, KString&err_msg);
	konga::DataElement* processThreeArgs(int ifcn, konga::DataElement*de1, konga::DataElement*de2, konga::DataElement*de3, int&err, KString&err_msg);
	konga::DataElement* if_Array(konga::DataElement*b1, konga::DataElement*r2, konga::DataElement* r3, int&err, KString& err_msg);

protected:
	konga::DataElement* find_de(const std::string& name);

	                                                                                 
	int IntegrationDebugBreakpointHitHelper(konga::Token* pCurrentToken, const std::string& stResult = "") const;

private:

	void HandleMarker(int& i, const int n, konga::Token*& t, konga::Token*ts[],
		konga::DataElement*& de, konga::DataElement*& de1, konga::DataElement*& de2,
		std::stack<konga::DataElement*>& st,
		konga::AutoDeleteStack<konga::DataElement>& tmp, int& err);

	         
	                                                                           
	                                                                               
	                                   
	         
	konga::DataElement* eval(konga::Token*ts[],const int n,int&err, konga::DataElement*de=0);

	                                                                             
	                                 
	konga::DataElement* getDataElement(int i,const konga::Token::FNC*f,konga::Token*ts[],std::vector<int>&ind,konga::AutoDeleteStack<konga::DataElement>&tmp);

	bool processRunOperationCall(int argn, konga::OperationContext* const pOperationContext, const std::string& opTag, int syncArgPos,
		const konga::Token::FNC*f, konga::Token*ts[], std::vector<int>& ind, konga::AutoDeleteStack<konga::DataElement>& tmp, int& err);

	                                          
	                                                                                                                                                          
	konga::DataElement* CallStoredProcedure(const int argn, const konga::Token::FNC*f, konga::Token*ts[], std::vector<int>& ind,
		konga::AutoDeleteStack<konga::DataElement>&tmp, const konga::OperationDataElementHolder* const pDEHolder);

	                                                                                      
	                                                                                                         
	void handle_err(konga::DataElement*const de, int& err) const;

	std::string getInstanceString(std::string& sGeneratorPath) const;

	template<typename T>
	T GetDataElementValue(const std::string& sName, T def, T (konga::DataElement::*getter_func)(int&) const ) const {
		konga::DataElement* de0 = getTransformationDataHolder()->getOperationDataElementHolder()->getOperationDataElement(sName);
		T v;
		int err=0;
		if( de0->IsNull() )
			return def;
		else
			v = static_cast<T>((de0->*getter_func)(err));
		if (err) handle_err(de0, err);
		return v;
	}

	std::string sourceTargetDescription();
	std::string inputArgumentsDescription(int nArg,...);

public:
	void CacheLookup(const std::string& stSourceTargetGuid, const std::string& stSQL, std::string& stValue, bool& bValueIsNull);
	std::string updateFlatDatabaseExpr(const char*strTemplate);
	std::string updateTokenList(const char*str, int n);

	                                                        
	static bool useInstanceResolvingFunction(TALIST* talist);

	                                              
	void expandTreeForDataElements(const char*str, int n, DTDParser*sourceDTDParser, bool& bForbidStreaming); 
	konga::TransformationDataHolder*getTransformationDataHolder() const;
	std::string readArrayString(konga::DataElement*de, const std::string& str, konga::DataElement::Type type, const std::string&format="", const std::string& header="", int i=0);
	std::string readStringToDE(konga::DataElement*dsi, const std::string&value, konga::DataElement::Type type, const std::string&format="", const std::string& header="", int i=0);
	std::string readArrayString(konga::DataElement*de, konga::StringTokenizer& tk, konga::DataElement::Type type, const std::string&format="", const std::string& header="", int i=0);
	long getInstanceNumber() const {return m_sourceInstanceCount;};
	void enterSourceDeMap(konga::DataElement*de, const std::string& path){
		source_de_map[path]=de;
	}
};



int find_END_KEY_position(const char*chars,int start);

#endif                  
