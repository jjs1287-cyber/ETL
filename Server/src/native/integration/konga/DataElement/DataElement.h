/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_DATAELEMENT_H
#define ETLFORGE_DATAELEMENT_H

#define TARGET_MAPPED_TO_NULL -7

#include <string>

#include <konga/Utils/KString.h>
#include <konga/Utils/DateUtils.h>	             
#include <konga/Utils/BinaryData.h>

#include <konga/Xml/DOMNodePtr.h>

#include "Token.h"

namespace konga {

class JbNode;

class DataElement {
	
	friend class Token;	                         

public:
	enum Type{ t_unknown = -1,
			   t_int = 0,
			   t_long = 1,
			   t_float = 2,
			   t_double = 3,
			   t_date = 4,
			   t_string = 5,
			   t_bool = 6,
			   t_timespan = 7,
			   t_bit = 8,
			   t_binary = 9,
			   t_collection = 10,
			   t_map
	};
	                                                                         

	                                                         
	static const char* get_type_name(DataElement::Type t);

	                                                                               
	static Type chooseType(Type t1,Type t2);

	                                                         
	static Type get_type(const char* t, int sqlDataType);

	static bool IsAString(const char*type_str, int sqlDataType);

	static KString ToEdiDate(const char*date_str,int length);
	static KString FromEdiDate(const char*date_str);
	static KString ToOracleDate(const char*date_str, bool* bHasSeconds);

	static const char* monthsAbr[];
	static const char* months[];
	static const char* daysAbr[];
	static const char* days[];

private:
	static const char s_empty_name[];

public:
	DataElement();
	DataElement(const DataElement& de);
	explicit DataElement(const char*name);
	DataElement(const char* input_str, int&count, int&err);

	DataElement(DataElement*d1, DataElement*d2, Token*t);
	DataElement(DataElement*d, Token*t);

	explicit DataElement(const konga::xml::DOMNodePtr& node);
	explicit DataElement(Type type);
	DataElement(Type type, int i);
	DataElement(Type type, long long i);
	DataElement(Type type, float f);
	DataElement(Type type, double d);
	DataElement(Type type, const konga::CrDateTime&t);
	DataElement(Type type, const char*str);	                   
	DataElement(Type type, const char*strValue, const char*strFormat);
	DataElement(konga::BinaryData* pbd);

	virtual ~DataElement(void);

	void doToken(Token*t);

	void initCollection(bool setToNull=false);
	bool IsAString() const;
	bool isCollection() const {
		return (m_type == t_collection);
	}

	bool isMap() const {
		return (m_type == t_map);
	}


	                          
	int addToCollection(DataElement*ds);

	int addToCollection(const konga::xml::DOMNodePtr& node);
	int addToCollection(const KString&value, const KString&type_str, int sqlDataType, const KString&format_str, bool isNull=false);

	bool hasInstanceTag() const;
	size_t instanceCount() const {
		if(m_type!=t_collection||m_value.ds==0)return 0;
		return m_value.ds->size();
	}
		
	DataElement& operator=(const DataElement* const de);
	DataElement& operator=(const DataElement& de);

	void set(const DataElement* const de);

	                                          
	void setName(const char* const name);

	                                        
	                   
	const char* getName() const {
		return ( m_name == 0 ) ? s_empty_name : m_name;
	}

	void setXmlNode(const konga::xml::DOMNodePtr& node);

	void setJbNode(konga::JbNode* jbNode);

	konga::xml::DOMNodePtr getXmlNode();

	konga::JbNode* getJbNode(){return m_jbNode;}
	
	void ProcessString(const char* inp_str,int&count,int&err);

	Type getType() const { return m_type; }

	Type findFinalDataElementType();

	                                                          
	void setType(Type newType);

	const char* getTypeStr() const{
		return get_type_name(m_type);
	}

	                                                                                    
	                                                                                                           
	                                                                                                  
	size_t GetStringLength(int&err);
	size_t GetStringLengthUtf8Character(int&err);

	void redefineType(Type type, const char*sFormat);
	KString GetString(int&err) const;
	double GetDouble(int&err) const;
	bool GetBool(int&err) const;	                                                                  
	int GetInteger(int&err) const;
	long long GetLong(int&err) const;
	float GetFloat(int&err) const;
	std::map<std::string, konga::DataElement*>* GetMap(int&err) const;
	konga::BinaryData* GetBinaryDataPointer(int&err) const;

	konga::CrDateTime GetDate(int&err) const;
    konga::CrDateTime GetDate(const char*str0,const char*formatStr,int&err) const;

	const char* getDefaultStr() const {
		return (const char*)m_defaultValueStr;
	}
	
	                                                                     
	                                   
	void setTypedValue(double d);
	void setTypedValue(bool b);
	void setTypedValue(const char* s,const char* typeStr, int sqlDataType);

	                                                             
	void setLong(long long l);

	void setError(int error){m_err=error;}
	void setDefaultString(const KString&str){m_defaultValueStr=str;}
	void setValue(const char* s);
	int getError() const {return m_err;}

	size_t getDECollectionSize() const;

	                                                
	DataElement*getDECollectionOf(size_t index) const;

	KString Format(const char*format_str,int&err) const;

	std::string getXmlNodeName();
	std::string getXmlNodeValue();

	void setFormatStr(const KString& str){m_formatStr=str;}
	const char* getFormatStr(){return (const char*)m_formatStr;}

	int getPosBegin() {
		return m_pos_begin;
	}
	void setPosBegin(int iPosBegin) {
		m_pos_begin = iPosBegin;
	}

	int getPosLength() {
		return m_pos_len;
	}
	void setPosLength(int iPosLen) {
		m_pos_len = iPosLen;
	}

	const KString& input() const { return m_input; }
	bool IsNull() const {return m_bNull;}
	void setNull(bool b=true);
	bool isGlobalDE();
	bool isTokenDE();
	Type getDataElementTypeForArray(int&err, KString& errmsg) const;


private:
	const char* m_name;
	KString m_input;
	Type m_type;
	int m_pos_begin,m_pos_len;
	mutable int m_err;
	bool m_bNull;

	typedef std::vector<konga::DataElement*> DEVector;

	union VALUE { int i; long long l; float f; double d; std::string*stdstr; bool b;
	konga::CrDateTime*t; long dt; DEVector*ds; BinaryData*binary; std::map<std::string,DataElement*>*map; } m_value;

	                                                                
	konga::xml::DOMNodePtr* m_pXmlNode;
	konga::JbNode* m_jbNode;

	int m_precision;
	KString m_defaultValueStr;
	KString m_formatStr;
	Token*m_token;

	                                           
	void adoptName(char* const name);

	                                                                       
	                                                                          
	                                                                 
	                                                                                 
	void setStringValueInternal(const char* val);

	                                                                                                
	void initDE();

	                                                                  
	                                                          
	                                                                
	void initXmlNode(const konga::xml::DOMNodePtr* const pNode);

public:
	                                                         
	void clearValue();

	                                                                                     
	                                                                                                         
	bool appendStringValue(const char* val);

	void copyToken(DataElement*de){m_token=de->m_token;}
	void setToken(Token*token){m_token=token;}
	std::string toString() const;

private:
	                               
	void initUnion();

	                                                             
	                                      
	bool OpEQ(Type t1, Type t2,
		const DataElement& d1,
		const DataElement& d2) const;
	bool OpLT(Type t1, Type t2,
		const DataElement& d1,
		const DataElement& d2) const;

	bool HasDomain() const {
		return (m_formatStr.Find('|')>=0);
	}

	bool HasFormat() const {
		return (!m_formatStr.IsEmpty()&&!HasDomain());
	}

public:
	                                        
	bool operator<(const DataElement& de) const;
	bool operator>(const DataElement& de) const;
	bool operator==(const DataElement& de) const;
};

                                                                     
KString CVTDate(DataElement* inputDE, KString input_form_str, KString output_form_str, int&err, std::string& errMsg);

                                                                
bool RepresentsAnInteger(const DataElement& de);
}                   

#ifdef _DEBUG
namespace {
	void printDE(konga::DataElement* de, const std::string& comment, const std::string& ind = "") {
		int err;
		printf("%s%s %p Name=%s; Value=%s\n", ind.c_str(), comment.c_str(), de, de->getName(), (const char*)de->GetString(err));
		if ( de->isCollection() ) {
			const size_t size = de->getDECollectionSize();
			for(size_t i = 0; i < size; ++i ) {
				printDE(de->getDECollectionOf(i), "", ind + " ");
			}
		}
	}
}
#endif

#endif                          

