/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifdef WIN32
#include <windows.h>
#endif
#include <sqlext.h>

#include <math.h>
#include <string.h>	          
#include <stdlib.h>          

#include <cassert>
#include <set>
#include <map>

#include <boost/scoped_array.hpp>
#include <boost/thread/mutex.hpp>

#include <konga/Utils/PortDefs.h>	                                   
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/date.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/JbParser/JbNode.h>

#include <konga/CharacterEncodingConversion/EncodingStringUtils.h>

#include <konga/TransformationUtils/Definitions.h>

#include <konga/TransformationEngine/resource.h>
#include <konga/TransformationEngine/ErrorCodes/resource.h>


#include "Token.h"
#include "DataElement.h"
#include "TransException.h"


using namespace konga;
using std::string;

const char* konga::DataElement::monthsAbr[]=
	{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",0};
const char* konga::DataElement::months[]=
	{"January","February","March","April","May","June","July","August","September",
		"October","November","December",0};
const char* konga::DataElement::daysAbr[]=
	{"Sun","Mon","Tues","Wed","Thur","Fri","Sat",0};	
const char* konga::DataElement::days[]=
	{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday",0};


namespace {
	const char*	type_str[]={"int","long","float","double",
										 "date","string","bool",
										 "timespan", "bit", "binary" ,"collection", "map", 0};

	long long read_long_long(const char*s,const KString&formatStr,int&err, bool strict)
	{
		err=0;
		if(s==0)return 0;
		if(stricmp(s,"t")==0 || stricmp(s,"true")==0)return 1;
		if(stricmp(s,"f")==0 || stricmp(s,"false")==0)return 0;
		long long ll=0;
		if(!formatStr.IsEmpty()){
			if(sscanf(s,formatStr,&ll)!=1){
				err=TRANSFORM_DE_READ_ERR;
				return 0;
			} else {
				return ll;
			}
		}

		bool positive=true;
		char c=*s;
		if(c=='-'||c=='+'){
			positive=(c=='+');
			s++;
		}
		while((c=*s++)!=0&&isdigit(c))ll=ll*10+(c-'0');
		if ( strict ) {
			if(c!=0&&c!=' '&&c!='\t'&&c!='\r'&&c!='\n'){
				err=TRANSFORM_DE_READ_ERR;
				return 0;
			}
		}
		return positive?ll:(-ll);
	}

	                                  
	                                               
	                                                
	int checkDomain(const KString&domain,const char* str){
		while(*str==' ')str++;
		int k=domain.Find(str),l=(int)strlen(str),m=(int)domain.GetLength();
		if(k<0)return DOMAIN_VIOLATION;
		l+=k;
		if((k==0||domain.GetAt(k-1)=='|')&&(l==m||domain.GetAt(l)=='|'))return 0;
		return DOMAIN_VIOLATION;
	}

	KString getReadFormat(const char*formatStr){
		                                                                 
		const char*s0;
		KString s;
		char c;
		for(s0=formatStr;(c=*s0)!=0&&c==' ';s0++);                  
		for(;(c=*s0)!=0;s0++){
			s+=c;
			if(c=='%'){
				s0++;
				if((c=*s0)=='%')s+=c;
				else {
					for(;(c=*s0)!=0;s0++)if(!isdigit(c)&&c!='.')break;
					s+=s0;
					break;
				}
			}
		}
		return s;
	}

	bool DataHasError(int err){
		return err==INTEGER_OVER_FLOW || err== TRANSFORM_DE_READ_ERR;
	}

	bool readBoolData(const char*s){
		                                                      
		bool bBoolNumeric = true;
		char c;
		while(*s==' ')s++;
		if(*s==0)return false;

		for (int i = 0; (c=s[i])!=0; i++){
			if (isalpha(c)){
				bBoolNumeric = false;
				break;
			}
		}

		                                                        
		if (bBoolNumeric)
		{
			                 
			return strtod(s, 0)!=0.0;
		}
		else
		{
			                                                                                             
			return (stricmp(s,"true")==0) || (strnicmp(s,"true ",5)==0)|| (stricmp(s,"t")==0)|| (strnicmp(s,"t ",2)==0);
		}
	}
	void handle_err(int err, const KString&value, konga::DataElement::Type type_saved, konga::DataElement::Type type_new){
		std::string sErr;
		switch(err){
			case TRANSFORM_DE_READ_ERR:
				sErr+="Cannot read string \""+value+"\", for data type:"+ konga::DataElement::get_type_name(type_new);
				break;
			case INTEGER_OVER_FLOW:
				sErr+=std::string("Overflow occurred when reading \"")+value+"\", for data type:"+ konga::DataElement::get_type_name(type_new);
				break;
			case TRANSFORM_DATA_TYPE_CONVERT_ERROR:
				sErr=std::string("Failed to convert data type from ")+
					konga::DataElement::get_type_name(type_saved)+" to "+
					konga::DataElement::get_type_name(type_new)+
					", the input: \""+value+"\"";
			case DOMAIN_VIOLATION:
				sErr+=std::string("DOMAIN VIOLATION type:")+
					konga::DataElement::get_type_name(type_new)+
					", the input: \""+value+"\"";
				break;
		}
		throw konga::CrException(sErr);
	}

}


const char* konga::DataElement::get_type_name( konga::DataElement::Type t) {
	switch(t) {
		case t_unknown:
			return "unknown";
		default:
			if ( t < t_unknown || t > t_map ) {
				throw konga::EvalException("Internal error: Unknown data element type (integer value "
					+ konga::toString((int)t) + ").");
			}
			return type_str[static_cast<int>(t)];
	}
}

const char konga::DataElement::s_empty_name[] = "";

konga::DataElement::DataElement() {
	initDE();
	setNull(true);
}

konga::DataElement::DataElement(const char* input_str,int&count,int&err)
{
	initDE();
	ProcessString(input_str,count,err);
	count++;
}

konga::DataElement::DataElement(const konga::xml::DOMNodePtr& node)
{
	initDE();
	initXmlNode(&node);
	m_type=t_string;
	setNull(node==0);
}

konga::DataElement::DataElement(DataElement::Type type,int i)
{
	initDE();
	m_type=type;
	switch(m_type)
	{
	case t_int: m_value.i=i; break;
	case t_long: m_value.l=i; break;
	case t_float: m_value.f=i; break;
	case t_double: m_value.d=i; break;
	case t_bool: m_value.b=(i!=0.0); break;
	case t_string:
		setStringValueInternal(konga::toString(i).c_str());
		break;
	default:
		m_err=TRANSFORM_DATA_TYPE_CONVERT_ERROR;
	}
}

konga::DataElement::DataElement(konga::DataElement::Type type, long long i)
{
	initDE();
	m_type=type;
	switch(m_type)
	{
	case t_int: m_value.i=i; break;
	case t_long: m_value.l=i; break;
	case t_float: m_value.f=i; break;
	case t_double: m_value.d=i; break;
	case t_bool: m_value.b=(i!=0.0); break;
	case t_string:
		setStringValueInternal(konga::toString(i).c_str());
		break;
	default:
		m_err=TRANSFORM_DATA_TYPE_CONVERT_ERROR;
	}
}

konga::DataElement::DataElement(konga::DataElement::Type type, float f)
{
	initDE();
	m_type=type;
	switch(m_type)
	{
	case t_int: m_value.i=static_cast<int>(f); break;
	case t_long: m_value.l=static_cast<long long>(f); break;
	case t_float: m_value.f=f; break;
	case t_double: m_value.d=f; break;
	case t_bool: m_value.b=(f!=0.0f); break;
	case t_string:
		setStringValueInternal(konga::toString(f).c_str());
		break;
	default:
		m_err=TRANSFORM_DATA_TYPE_CONVERT_ERROR;
	}
}

konga::DataElement::DataElement(konga::DataElement::Type type, double d)
{
	initDE();
	m_type=type;
	switch(m_type)
	{
	case t_int: m_value.i=static_cast<int>(d); break;
	case t_long: m_value.l=static_cast<long>(d); break;
	case t_float: m_value.f=static_cast<float>(d); break;
	case t_double: m_value.d=d; break;
	case t_bool: m_value.b=(d!=0.0); break;
	case t_string:
		setStringValueInternal(konga::toString(d).c_str());
		break;
	default:
		m_err=TRANSFORM_DATA_TYPE_CONVERT_ERROR;
	}
}

konga::DataElement::DataElement(konga::DataElement::Type type,const CrDateTime&t)
{
	initDE();
	m_type=type;
	m_value.t=new CrDateTime(t);
	if(type!=t_date)
		m_err=TRANSFORM_DATA_TYPE_CONVERT_ERROR;
}

konga::DataElement::DataElement(const char* name)
{
	initDE();
	setName(name);
	m_type=t_string;
}

konga::DataElement::DataElement(konga::BinaryData* pbd)
{
	initDE();
	m_type=t_binary;
	m_value.binary=pbd;
}


bool konga::DataElement::OpEQ(Type t1, Type t2,
					   const DataElement& d1,
					   const DataElement& d2) const
{
	if(t1==t_bool&&t2==t_bool){
		return d1.m_value.b==d2.m_value.b;
	} else if(t1==t_string&&t2==t_string){
		if ( d1.m_value.stdstr && d2.m_value.stdstr )
			return (*d1.m_value.stdstr)==(*d2.m_value.stdstr);
		else {
			int err1 = 0, err2 = 0;
			const KString& s1 = d1.GetString(err1), s2 = d2.GetString(err2);
			if ( err1 || err2 ) return false;	                                                                   
			return s1 == s2;
		}
	} else if(t1==t_binary && t2==t_binary){
		const konga::BinaryData* d1b=d1.m_value.binary, *d2b=d2.m_value.binary;
		if(d1b == d2b)return true;
		if(!d1b || !d2b)return false;
		return *d1b==*d2b;
	} else if(t1==t_date&&t2==t_date){
		if(d1.m_value.t&&d2.m_value.t)
			return ( (*d1.m_value.t) == (*d2.m_value.t) );
		else
			return false;
	} else if(t1==t_bool||t2==t_bool){
		int err1, err2;
		bool b = d1.GetBool(err1)==d2.GetBool(err2);
		if(DataHasError(err1))
			throw konga::EvalException("Error: d1 has error in exec d1 == d2.");
		if(DataHasError(err2))
			throw konga::EvalException("Error: d2 has error in exec d1 == d2.");
		return b;
	} else if(t1==t_binary || t2==t_binary) {
		throw konga::EvalException("Error: compare(==) binary data with data of other type.");
	} else {
		int err1, err2;
		bool b = d1.GetDouble(err1)==d2.GetDouble(err2);
		if(DataHasError(err1))
			throw konga::EvalException("Error: d1 has error in exec d1 == d2.");
		if(DataHasError(err2))
			throw konga::EvalException("Error: d2 has error in exec d1 == d2.");
		return b;
	}
}


bool konga::DataElement::OpLT(Type t1, Type t2,
					   const DataElement& d1,
					   const DataElement& d2) const {
	if(t1==t_bool&&t2==t_bool){
		throw konga::EvalException("Illegal operation: A boolean can only be compared to another boolean " \
			" using the equals or not-equals operators. You need to convert it to a type for which the less-than operator is defined.");
	} else if(t1==t_string&&t2==t_string){
		if ( d1.m_value.stdstr && d2.m_value.stdstr )
			return (*d1.m_value.stdstr) < (*d2.m_value.stdstr);
		else {
			                                                                        
			int err1 = 0, err2 = 0;
			const KString& s1 = d1.GetString(err1), s2 = d2.GetString(err2);
			if ( err1 || err2 ) return false;	                                                                              
			return s1 < s2;
		}
	} else if(t1==t_date&&t2==t_date){
		assert(d1.m_value.t);
		assert(d2.m_value.t);
		if(d1.m_value.t&&d2.m_value.t)
			return ( (*d1.m_value.t) < (*d2.m_value.t) );
		else
			throw konga::EvalException("Internal Error: d1.m_value.t should not be null in OpLT. Please report this bug.");
	} else if(t1==t_binary || t2==t_binary){
		throw konga::EvalException("Error: compare (<) with binary data.");
	} else {
		int err1, err2;
		bool b = d1.GetDouble(err1) < d2.GetDouble(err2);
		if(DataHasError(err1))
			throw konga::EvalException("Error: d1 has error in exec d1 < d2.");
		if(DataHasError(err2))
			throw konga::EvalException("Error: d2 has error in exec d1 < d2.");
		return b;

	}
}

bool konga::DataElement::operator<(const DataElement& de) const {
	return this->OpLT(this->getType(), de.getType(),
		*this, de);
}

bool konga::DataElement::operator==(const DataElement& de) const {
	return this->OpEQ(this->getType(), de.getType(), *this, de);
}

bool konga::DataElement::operator>(const DataElement& de) const {
	return !( *this == de || *this < de );
}

namespace {
	bool isComparisionUndefined(konga::DataElement::Type t1, konga::DataElement::Type t2,
		const DataElement& de1, const DataElement& de2) {
			return ( t1==konga::DataElement::t_unknown || t2==konga::DataElement::t_unknown ||
				de1.IsNull() || de2.IsNull() );
	}

	bool IsErrorNull(int err) {
		return ( err == TRANSFORM_INPUT_EMPTY ||
			err == TARGET_MAPPED_TO_NULL );
	}
}

konga::DataElement::DataElement(DataElement*d1, DataElement*d2, Token*token)
{
	initDE();
	assert(d1);
	assert(d2);
	Token::Type tokenType=token->getType();
	Type t1=d1->m_type,t2=d2->m_type;
	if(t1==t_collection||t2==t_collection){
		int n1=0, n2=0;
		if(t1==t_collection){
			n1=(int)d1->getDECollectionSize();
		}
		if(t2==t_collection){
			n2=(int)d2->getDECollectionSize();
		}
		if(n1!=0 && n2!=0 && n1!=n2)
			throw konga::CrException(std::string("Error: the operator:")+token->GetName()+
			" operated on two data elements with inconsistent array sizes n1/n2:"+
			konga::toString(n1)+"/"+konga::toString(n2)+".");

		initCollection();
		DataElement*ds1=d1,*ds2=d2;
		for(int i=0, n=n1?n1:n2;i<n;i++){
			if(t1==t_collection)ds1=d1->getDECollectionOf(i);
			if(t2==t_collection)ds2=d2->getDECollectionOf(i);
			addToCollection(new DataElement(ds1,ds2,token));
		}
		return;
	}
	else {
		                                                   
		                                                          
		                                                                         
		m_type=chooseType(t1,t2);
		int err1=0, err2=0;

		switch(tokenType){
			case Token::Add:
			case Token::PSet:
				if(t1==t_bool && t2==t_bool)
					throw konga::EvalException(string("Illegal operation, ADDITION with incompatible data types: ")+get_type_name(t1)+" + "+get_type_name(t2));
				if(t1==t_unknown||t2==t_unknown){
					switch(t1){
					case t_string:
						m_type=t_string;
						setStringValueInternal((const char*)(d1->GetString(err1)));
						break;
					case t_binary:
						m_type=t_binary;
						set(d1);
						break;
					case t_date:
						m_type=t_date;
						m_value.t=new CrDateTime(d1->m_value.t->get_ptime());
						break;
					case t_unknown:
						switch(t2){
						case t_string:
							m_type=t_string;
							setStringValueInternal((const char*)(d2->GetString(err1)));
							break;
						case t_date:
							m_type=t_date;
							m_value.t=new CrDateTime(d2->m_value.t->get_ptime());
							break;
						case t_binary:
							m_type=t_binary;
							set(d2);
							break;
						case t_unknown:                                    
							m_type=t_unknown;
							m_bNull = true;
							return;                                         
						default: 
							m_type=t2;
							setTypedValue(d2->GetDouble(err2));
							break;
						}
						break;
					default:
						m_type=t1;
						setTypedValue(d1->GetDouble(err1));
						break;
					}
				}
				else {
					if(m_type==t_date){
						if(m_value.t)delete m_value.t;
						if(t1==t_date&&(t2==t_int||t2==t_long||t2==t_float||t2==t_double))
							m_value.t=new CrDateTime(d1->m_value.t->get_ptime()+boost::posix_time::seconds(d2->GetInteger(err2)));
						else if((t1==t_int||t1==t_long||t1==t_float||t1==t_double)&&d2->m_type==t_date)
							m_value.t=new CrDateTime(d2->m_value.t->get_ptime()+boost::posix_time::seconds(d1->GetInteger(err1)));
						else
							throw konga::EvalException(string("Illegal operation, ADDITION with incompatible data types: ")+get_type_name(t1)+" + "+get_type_name(t2));
					} else if(m_type==t_string){
						setStringValueInternal((const char*)(d1->GetString(err1)+d2->GetString(err2)));
						                                                                                  
						if ( IsErrorNull(err1) ) err1 = 0;
						if ( IsErrorNull(err2) ) err2 = 0;
					} else {
						setTypedValue(d1->GetDouble(err1)+d2->GetDouble(err2));
					}
				}
				break;
			case Token::Sub:
			case Token::MSet:
				if(t1==t_bool||t2==t_bool)
					throw konga::EvalException(string("Illegal operation, SUBSTRACT with incompatible data types: ")+get_type_name(t1)+" - "+get_type_name(t2));
				if(t1==t_unknown||t2==t_unknown){
					switch(t1){
					case t_string:
						m_type=t_string;
						setStringValueInternal((const char*)(d1->GetString(err1)));
						break;
					case t_date:
						m_type=t_date;
						m_value.t=new CrDateTime(d1->m_value.t->get_ptime());
						break;
					case t_binary:
						m_type=t_binary;
						set(d1);
						break;
					case t_unknown:
						switch(t2){
						case t_string:
						case t_date:
						case t_binary:
							throw konga::EvalException(string("Illegal operation, SUBSTRACT with incompatible data types: ")+get_type_name(t1)+" - "+get_type_name(t2));
						case t_unknown:                                    
							m_type=t_unknown;
							return;                                         
						default:
							m_type=t2;
							setTypedValue(-d2->GetDouble(err2));
							break;
						}
						break;
					default: m_type=t1; setTypedValue(d1->GetDouble(err1)); break;
					}
				}
				else {
					if(t1==t_date){
						if(t2==t_int||t2==t_long||t2==t_float||t2==t_double){
							if(m_value.t)delete m_value.t;
							m_type=t_date;
							m_value.t=new CrDateTime(d1->m_value.t->get_ptime()-boost::posix_time::seconds(d2->GetInteger(err2)));
						}
						else if ( t2==t_date) {
							                                                                     
							if ( d1->m_value.t && d2->m_value.t ) {
								m_type = t_long;
								const CrTimeDuration dt = (*d1->m_value.t - *d2->m_value.t);
								setTypedValue( static_cast<double>(dt.total_seconds()) );
							}
						}
						else {
							throw konga::EvalException(string("Illegal operation, SUBSTRACT with incompatible data types: ")+get_type_name(t1)+" - "+get_type_name(t2));
						}
					}
					else if(t1==t_date||t2==t_date||t1==t_string||t2==t_string||t1==t_bool||t2==t_bool||t1==t_binary||t2==t_binary){
						throw konga::EvalException(string("Illegal operation, SUBSTRACT with incompatible data types: ")+get_type_name(t1)+" - "+get_type_name(t2));
					}
					else {
						setTypedValue(d1->GetDouble(err1)-d2->GetDouble(err2));
					}
				}
				break;
			case Token::Mul:
				if(t1==t_date||t2==t_date||t1==t_string||t2==t_string||t1==t_bool||t2==t_bool||(t1==t_unknown&&t2==t_unknown)||t1==t_binary||t2==t_binary){
					throw konga::EvalException(string("Illegal operation, MULTIPLICATION with incompatible data types: ")+get_type_name(t1)+" * "+get_type_name(t2));
				} else {
					                                                                                                      
					if ( t1==t_unknown || t2==t_unknown ) {
						m_type = t1==t_unknown ? t2 : t1;
					}
					setTypedValue(d1->GetDouble(err1)*d2->GetDouble(err2));
				}
				break;
			case Token::Div:
				if(t1==t_date||t2==t_date||t1==t_string||t2==t_string||t1==t_bool||t2==t_bool||(t1==t_unknown&&t2==t_unknown)||t1==t_binary||t2==t_binary){
					throw konga::EvalException(string("Illegal operation, DIVISION with incompatible data types: ")+get_type_name(t1)+" / "+get_type_name(t2));
				} else {
					const double f1=d1->GetDouble(err1),f2=d2->GetDouble(err2);
					if(DataHasError(err1))
						throw konga::EvalException("Error: d1 has error in exec d1/d2 operator.");
					if(DataHasError(err2))
						throw konga::EvalException("Error: d2 has error in exec d1/d2 operator.");
					if(t2==t_unknown)
						throw konga::EvalException("Illegal operation, division by Null.");
					if(f2==0.)
						throw konga::EvalException("Illegal operation: division by 0.");
					if(t1==t_unknown)
						m_type = t2;
					setTypedValue(f1/f2);
				}
				break;
			                                                                   
			                                                                         
			                
			case Token::EQ:
				m_type=t_bool;
				if( isComparisionUndefined(t1, t2, *d1, *d2) )
					m_value.b = false;
				else
					m_value.b = OpEQ(t1, t2, *d1, *d2);
				break;
			case Token::NE:
				m_type=t_bool;
				if( isComparisionUndefined(t1, t2, *d1, *d2) )
					m_value.b = true;
				else
					m_value.b = !OpEQ(t1, t2, *d1, *d2);
				break;
			case Token::GT:
				m_type=t_bool;
				if( isComparisionUndefined(t1, t2, *d1, *d2) )
					m_value.b = false;
				else
					m_value.b = OpLT(t2, t1, *d2, *d1);
				break;
			case Token::GE:
				m_type=t_bool;
				if( isComparisionUndefined(t1, t2, *d1, *d2) )
					m_value.b = false;
				else
					m_value.b = !OpLT(t1, t2, *d1, *d2);
				break;
			case Token::LT:
				m_type=t_bool;
				if( isComparisionUndefined(t1, t2, *d1, *d2) )
					m_value.b = false;
				else
					m_value.b = OpLT(t1, t2, *d1, *d2);
				break;
			case Token::LE:
				m_type=t_bool;
				if( isComparisionUndefined(t1, t2, *d1, *d2) )
					m_value.b = false;
				else
					m_value.b = !OpLT(t2, t1, *d2, *d1);
				break;
			case Token::POW:
				if(t1==t_date||t1==t_string||t1==t_bool||t2==t_date||t2==t_string||t2==t_bool||t1==t_binary||t2==t_binary){
					throw konga::EvalException(string("Illegal operation, POW with incompatible data types, ")+get_type_name(t1)+" ^ "+get_type_name(t2));
				} else {
					setTypedValue(pow(d1->GetDouble(err1),d2->GetDouble(err2)));
				}
				break;
			case Token::And:
				{
					m_type=t_bool;
					bool b = d1->GetBool(err1) && d2->GetBool(err2);
					if(DataHasError(err1))
						throw konga::EvalException("Error: d1 has error in evaluating And operator.");
					if(DataHasError(err2))
						throw konga::EvalException("Error: d2 has error in evaluating And operator.");
					m_value.b=b;
				}
				break;
			case Token::Or:
				{
					m_type=t_bool;
					bool b = d1->GetBool(err1) || d2->GetBool(err2);
					if(DataHasError(err1))
						throw konga::EvalException("Error: d1 has error in evaluating And operator.");
					if(DataHasError(err2))
						throw konga::EvalException("Error: d2 has error in evaluating And operator.");
					m_value.b=b;
				}
				break;
			default:
				m_type=t_unknown;
				break;
		}                       
		if(m_type==t_unknown){
			throw konga::EvalException(string("Illegal operation: ")+get_type_name(t1)+" "+Token::GetTypeString(tokenType)+" "+get_type_name(t2));
		}
		if(err1){
			KString tmp;
			if(!tmp.LoadString(err1))tmp=konga::toString(err1).c_str();
			throw konga::EvalException(string("err1=")+(const char*)tmp);
		}
		if(err2){
			KString tmp;
			if(!tmp.LoadString(err2))tmp=konga::toString(err2).c_str();
			throw konga::EvalException(string("err2=")+(const char*)tmp);
		}
	}
}

konga::DataElement::DataElement(const DataElement&de)
{
	initDE();
	if(de.isCollection()){
		initCollection();
		for(int i=0,n=de.getDECollectionSize(); i<n; i++){
			DataElement* de_i=de.getDECollectionOf(i);
			if(!de_i->isTokenDE())de_i=new DataElement(*de_i);
			addToCollection(de_i);
		}
	}
	else {
		this->set(&de);
	}
}

konga::DataElement::DataElement(Type type)
{
	initDE();
	m_type=type;
	switch(type){
	case t_collection:
		m_value.ds=new DEVector();
		break;
	case t_map:
		m_value.map=new std::map<std::string, konga::DataElement*>();
		break;
	}

}

                                                                                   
konga::DataElement::DataElement(DataElement*d, Token*token)
{
	if(d->isCollection()){
		initCollection();
		for(int i=0,n=d->getDECollectionSize(); i<n; i++){
			addToCollection(new DataElement(d->getDECollectionOf(i), token));
		}
	}
	else {
		initDE();
		Token::Type type=token->getType();
		int err=0;
		switch(type){
			case Token::Not:
				m_type=t_bool;
				setTypedValue(!d->GetBool(err));
				break;
			case Token::Neg:
				m_type=d->m_type;
				setTypedValue(-(d->GetDouble(err)));
				break;
			default:
				m_type=t_unknown;
				break;
		}
		if(m_type==t_unknown){
			m_err=TRANSFORM_UNKNOWN_DE_TYPE;
		}
		else 
			m_err=err;
		if(m_err!=0){
			KString tmp;
			if(!tmp.LoadString(m_err))tmp=konga::toString(m_err).c_str();
			string msg((const char*)tmp);
			msg+="\nThe problematic token is at the end of the following expression\n"+token->source_location();
			throw konga::EvalException(msg);
		}
	}
}

void konga::DataElement::doToken(Token*token){
	if(isCollection()){
		for(int i=0,n=getDECollectionSize(); i<n; i++){
			getDECollectionOf(i)->doToken(token);
		}
	}
	else {
		Token::Type type=token->getType();
		int err=0;
		switch(type){
			case Token::Not:
				m_type=t_bool;
				setTypedValue(!GetBool(err));
				break;
			case Token::Neg:
				setTypedValue(-(GetDouble(err)));
				break;
			case Token::PrePP:
			case Token::PostPP:
				setTypedValue(GetDouble(err)+1);
				break;
			case Token::PreMM:
			case Token::PostMM:
				setTypedValue(GetDouble(err)-1);
				break;
			default:
				m_type=t_unknown;
				break;
		}
		if(m_type==t_unknown){
			m_err=TRANSFORM_UNKNOWN_DE_TYPE;
		}
		else 
			m_err=err;
		if(m_err!=0){
			KString tmp;
			if(!tmp.LoadString(m_err))tmp=konga::toString(m_err).c_str();
			string msg((const char*)tmp);
			msg+="\nThe problematic token is at the end of the following expression\n"+token->source_location();
			throw konga::EvalException(msg);
		}
	}
}

konga::DataElement::DataElement(konga::DataElement::Type type, const char*str)
{
	if(type==t_collection)
		throw konga::CrException(std::string("Error: cannot construct a data element with type:t_collection, strValue:\"")+str+"\".");
	initDE();
	m_type=type;
	int	iNumFieldsConv = 0;

	switch(m_type){
		case t_int:
			iNumFieldsConv = sscanf(str,"%d",&m_value.i);
			if (0 == iNumFieldsConv || EOF == iNumFieldsConv)
				m_err = ID_TYPECAST_FAILED;
			break;
		case t_long:
			m_value.l = ::atoll(str);
			break;
		case t_float:
			iNumFieldsConv = sscanf(str,"%f",&m_value.f);
			if (0 == iNumFieldsConv || EOF == iNumFieldsConv)
				m_err = ID_TYPECAST_FAILED;
			break;
		case t_double:
			iNumFieldsConv = sscanf(str,"%lf",&m_value.d);
			if (0 == iNumFieldsConv || EOF == iNumFieldsConv)
				m_err = ID_TYPECAST_FAILED;
			break;
		case t_string:
			m_value.stdstr=new string(str?str:"");
			break;
		case t_date:
			m_value.t=new CrDateTime(GetDate(str, m_formatStr, m_err));
			break;
		case t_bool:
			m_value.b=readBoolData(str);
			break;
		case t_binary:
			{
				size_t len;
				unsigned char*pbd=konga::fromHexStringPtr(str, len);
				m_value.binary=new konga::BinaryData((const char*)pbd, len, false);
			}
			break;
		default:
			m_type=t_unknown;
			m_err=TRANSFORM_UNKNOWN_DE_TYPE;
			break;
	}
}

konga::DataElement::DataElement(konga::DataElement::Type type, const char* strValue, const char* strFormat)
{
	if(type==t_collection)
		throw konga::CrException(std::string("Error: cannot construct a data element with type:t_collection, strValue:\"")+strValue+"\", strFormat:\""+strFormat+"\".");
	initDE();
	m_type = type;
	if(strFormat==0 || strlen(strFormat)==0){
		                                                                 
		DataElement de(type, strValue);
		*this = DataElement(de);
	} else {
		m_formatStr = strFormat;
		setTypedValue(strValue, 0, 0);
	}
}

konga::DataElement::~DataElement(void) {
	if(m_name) {
		delete[]m_name;
		m_name = 0;
	}
	clearValue();
}

void konga::DataElement::setType(Type newType){
	clearValue();
	m_type=newType;
}

void konga::DataElement::initCollection(bool setToNull)
{
	m_err=0;
	if(m_type!=t_collection){
		clearValue();
		m_value.ds=new DEVector();
		m_type=t_collection;
	} else {
		if(m_value.ds){
			for(size_t i=0, n=getDECollectionSize(); i<n; i++) {
				DataElement*de=getDECollectionOf(i);
				if(!de->isTokenDE())delete de;
			}
			m_value.ds->clear();
		}
		else
			m_value.ds = new DEVector();
	}
	m_bNull=setToNull;
}

int konga::DataElement::addToCollection(DataElement*ds){
	assert(m_type==t_collection);
	if(m_type!=t_collection){
		throw konga::EvalException("WRONG_TYPE_ADD_TO_COLLECTION");
	}

	if(ds==0)ds=new DataElement();
	if(m_value.ds==0){
		m_value.ds=new DEVector();
	}
	m_value.ds->push_back(ds);
	return m_value.ds->size();
}

int konga::DataElement::addToCollection(const konga::xml::DOMNodePtr& node){
	assert(m_type==t_collection);
	if(m_type!=t_collection){
		throw konga::EvalException("WRONG_TYPE_ADD_TO_COLLECTION");
	}

	if(m_value.ds==0){
		m_value.ds=new DEVector();
	}
	m_value.ds->push_back( new DataElement(node) );
	return m_value.ds->size();
}

int konga::DataElement::addToCollection(const KString&value,const KString&type_str, int sqlDataType, const KString&format_str, bool isNull          ){
	assert(m_type==t_collection);
	if(m_type!=t_collection){
		throw konga::EvalException("WRONG_TYPE_ADD_TO_COLLECTION");
	}

	if(m_value.ds==0){
		m_value.ds=new DEVector();
	}

	DataElement *de;
	if( isNull ) {
		de = new DataElement();
	} else if(format_str.IsEmpty()){
		de = new DataElement(get_type(type_str, sqlDataType),value);
	} else {
		de = new DataElement(get_type(type_str, sqlDataType),value,format_str);
	}
	m_value.ds->push_back(de);
	return 0;
}

bool konga::DataElement::hasInstanceTag() const{
	char c;
	for(int i=0;(c=m_name[i])!=0;i++){
		if(c=='#')return true;
	}
	return false;
}

void konga::DataElement::setTypedValue(double d){
	m_err = 0;
	setNull(false);
	clearValue();

	switch(m_type){
		case t_int:
			m_value.i = static_cast<int>(d);
			break;
		case t_long:
			m_value.l = static_cast<long long>(d);
			break;
		case t_float:
			m_value.f = static_cast<float>(d);
			break;
		case t_double:
			m_value.d = d;
			break;
		case t_bool:
			m_value.b = (d!=0);
			break;
		case t_string:
			{
				const string& value = konga::toString(d);
				setStringValueInternal(value.c_str());
				break;
			}
		default: m_err=TRANSFORM_CANNOT_CVT_DE_TYPE; break;
	}
}

void konga::DataElement::setTypedValue(bool b){
	m_err = 0;
	setNull(false);
	clearValue();

	const double d=b?1.0:0.0;
	switch(m_type){
		case t_int:
			m_value.i = static_cast<int>(d);
			break;
		case t_long:
			m_value.l = static_cast<long long>(d);
			break;
		case t_float:
			m_value.f = static_cast<float>(d);
			break;
		case t_double:
			m_value.d = d;
			break;
		case t_bool:
			m_value.b = b;
			break;
		case t_string:
			setStringValueInternal(b ? "true" : "false");
			break;
		default:
			m_err=TRANSFORM_CANNOT_CVT_DE_TYPE;
	}
}

namespace {
	const long long max_int = std::numeric_limits<long long>::max();
	const long long min_int = std::numeric_limits<long long>::min();
}

void konga::DataElement::setTypedValue(const char* s, const char* typeStr, int sqlDataType){
	if(m_type==t_collection)
		throw konga::CrException(std::string("Error: cannot set the data element of array type with a string:\"")+s+"\", typeStr:"+typeStr+", sqlDataType:"+konga::toString(sqlDataType));
	m_err = 0;
	m_input=s;
	setNull(false);
	clearValue();

	if(typeStr==0){
		if(m_type==t_unknown)m_type=t_string;
	} else {
		m_type=get_type(typeStr,sqlDataType);
	}

	if(HasDomain()){
		if((m_err=checkDomain(m_formatStr,s))!=0)return;		
	}
	bool bHasFormat=HasDomain()||HasFormat();
	KString formatStr(!bHasFormat ? "" : m_formatStr);

	char szPrec[256] = {0};

	switch(m_type){
	case t_int:
		{
			while(*s==' ')s++;
			const long long ll=read_long_long(s,formatStr,m_err,false);
			if(ll>max_int||ll<min_int){
				m_err=INTEGER_OVER_FLOW;
			} else {
				m_value.i=(int)ll;
			}
		}
		break;
	case t_long:
		while(*s==' ')s++;
		m_value.l=read_long_long(s,formatStr,m_err,false);
		break;
	case t_float:
		if(typeStr && strncmp(typeStr,"N", 1)==0 && strlen(typeStr) > 1)
		{
			strcpy(szPrec, typeStr + 1);
			m_precision = atoi(szPrec);
		}

		while(*s==' ')s++;
		if(!bHasFormat)formatStr="%g";
		else formatStr=getReadFormat((const char*)formatStr);
		if(sscanf(s,formatStr,&m_value.f)!=1)
			m_err=TRANSFORM_DE_READ_ERR;
		break;
	case t_double:
		while(*s==' ')s++;
		if(!bHasFormat)formatStr="%lg";
		else formatStr=getReadFormat((const char*)formatStr);
		if(sscanf(s,formatStr,&m_value.d)!=1)
			m_err=TRANSFORM_DE_READ_ERR;
		break;
	case t_date:
		m_value.t=new CrDateTime(GetDate(s, formatStr, m_err));
		break;
	case t_string:
		setStringValueInternal(s);
		break;
	case t_bool:
		{
			                                                      
			bool bBoolNumeric = true;
			char c;
			while((c=*s)!=0&&c==' ')s++;

			for (int i = 0; (c=s[i])!=0; i++)
			{
				if (isalpha(s[i]))
				{
					bBoolNumeric = false;
					break;
				}
			}

			                                                        
			if (bBoolNumeric)
			{
				int n=0;
				if(*s)n=atoi(s);
				if(n==0) 
					m_value.b = false;
				else 
					m_value.b = true;
			}
			else
			{
				                                                                                             
				if(stricmp(s,"true")==0){
					m_value.b=true;
				} else if(strnicmp(s,"true ",5)==0){
					m_value.b=true;
				} else if(stricmp(s,"t")==0){
					m_value.b=true;
				} else if(strnicmp(s,"t ",2)==0){
					m_value.b=true;
				} else {
					m_value.b=false;
				}
			}
		}
		break;
	case t_binary:
		{
			size_t len = 0;
			unsigned char* bytes = fromHexStringPtr(s, len);
			if ( m_value.binary ) delete m_value.binary;
			m_value.binary=new konga::BinaryData((const char*)bytes, len, false);
		}
		break;
	default:
		m_type=t_unknown;
		m_err=TRANSFORM_DE_READ_ERR;
		break;
	}
}

void konga::DataElement::setValue(const char* s){
	if(m_type==t_collection)
		throw konga::CrException(std::string("Error: cannot set the data element of array type with a string:\"")+s+"\"");
	m_err = 0;
	m_input=s;
	setNull(false);
	clearValue();

	if(HasDomain()){
		if((m_err=checkDomain(m_formatStr,s))!=0)return;		
	}
	const bool bHasFormat=HasFormat();
	KString formatStr( !bHasFormat ? "" : m_formatStr);

	switch(m_type){
	case t_int:
		{
			while(*s==' ')s++;
			const long long ll=read_long_long(s,formatStr,m_err,false);
			if(ll>max_int||ll<min_int){
				m_err=INTEGER_OVER_FLOW;
			} else {
				m_value.i=(int)ll;
			}
		}
		break;
	case t_long:
		while(*s==' ')s++;
		m_value.l=read_long_long(s,formatStr,m_err,false);
		break;
	case t_float:
		{
			while(*s==' ')s++;
			if(!bHasFormat)formatStr="%g";
			if(sscanf(s,(const char*)formatStr,&m_value.f)!=1)
				m_err=TRANSFORM_DE_READ_ERR;
		}
		break;
	case t_double:
		while(*s==' ')s++;
		if(!bHasFormat)formatStr="%lg";
		if(sscanf(s,formatStr,&m_value.d)!=1)
			m_err=TRANSFORM_DE_READ_ERR;
		break;
	case t_date:
		m_value.t=new CrDateTime(GetDate(s, formatStr, m_err));
		break;
	case t_string:
		setStringValueInternal(s);
		break;
	case t_bool:
		{
			                                                      
			bool bBoolNumeric = true;
			char c;
			while((c=*s)!=0&&c==' ')s++;

			for (int i = 0; (c=s[i])!=0; i++)
			{
				if (isalpha(c))
				{
					bBoolNumeric = false;
					break;
				}
			}

			                                                        
			if (bBoolNumeric)
			{
				int n=0;
				if(*s)n=atoi(s);
				if(n==0) 
					m_value.b = false;
				else 
					m_value.b = true;
			}
			else
			{
				                                                                                             
				if(stricmp(s,"true")==0){
					m_value.b=true;
				} else if(strnicmp(s,"true ",5)==0){
					m_value.b=true;
				} else if(stricmp(s,"t")==0){
					m_value.b=true;
				} else if(strnicmp(s,"t ",2)==0){
					m_value.b=true;
				} else {
					m_value.b=false;
				}
			}
		}
		break;
	case t_binary:
		{
			size_t len = 0;
			unsigned char* bytes = fromHexStringPtr(s, len);
			if ( m_value.binary ) delete m_value.binary;
			m_value.binary=new konga::BinaryData((const char*)bytes, len, false);
		}
		break;
	default:
		m_type=t_unknown;
		m_err=TRANSFORM_DE_READ_ERR;
		break;
	}
}

void konga::DataElement::setLong(long long lv) {
	m_err = 0;
	setNull(false);
	clearValue();
	if ( getType() != t_long ) {
		setType(t_long);
	}
	m_value.l = lv;
}

konga::DataElement::Type konga::DataElement::chooseType(konga::DataElement::Type t1,konga::DataElement::Type t2){
	if(t1==t2)return t1;

	if(t1==t_collection||t2==t_collection)return t_collection;
	if(t1==t_date&&(t2==t_int||t2==t_long))return t_date;
	if(t2==t_date&&(t1==t_int||t1==t_long))return t_date;
	if(t1==t_date&&t2==t_date)return t_int;
	if(t1==t_date&&t2==t_string)return t_string;
	if(t2==t_date&&t1==t_string)return t_string;
	if(t1==t_date||t2==t_date)return t_unknown;

	                                              
	if ( (t1 == t_bool && t2 == t_string) || (t1 == t_string && t2 == t_bool) ) return t_string;

	if(t1==t_bool||t2==t_bool||t1==t_unknown||t2==t_unknown)return t_unknown;
	if((int)t1>(int)t2)return t1;
	return t2;
}

DataElement& konga::DataElement::operator=(const DataElement* const de) {
	this->set(de);
	return *this;
}

DataElement& konga::DataElement::operator=(const DataElement& de) {
	this->set(&de);
	return *this;
}

void konga::DataElement::set(const DataElement* const de){
	                            
	if(this==de) return;

	clearValue();
	m_err = 0;
	if ( !de )
		return;
	m_err=de->m_err;
	setName(de->m_name);
	m_bNull=de->m_bNull;

	m_pos_begin=de->m_pos_begin;
	m_pos_len=de->m_pos_len;
	m_precision=de->m_precision;
	m_type=de->m_type;

	m_defaultValueStr=de->m_defaultValueStr;
	if ( de->m_pXmlNode ) initXmlNode(de->m_pXmlNode);

	m_formatStr = de->m_formatStr;

	                                                 

	                                                                
	                        
	m_value=de->m_value;
	if( m_type==t_string && m_value.stdstr!=0 ){
		m_value.stdstr=new string(*m_value.stdstr);
	}
	else if( m_type==t_date && m_value.t!=0 ){
		m_value.t=new CrDateTime(*m_value.t);
	}
	else if( m_type==t_collection ){
		                                                                
		m_value.ds=new DEVector();
		const size_t n=de->getDECollectionSize();
		for(size_t i=0; i<n; i++) {
			DataElement* de_i=de->getDECollectionOf(i);
			if(!de_i->isTokenDE()) de_i=new DataElement(*de_i);
			m_value.ds->push_back(de_i);
		}
	}
	else if( m_type==t_binary ){
		m_value.binary=new konga::BinaryData(*m_value.binary);
	}
	else if(m_type==t_map){
		m_value.map=new std::map<std::string, konga::DataElement*>();
		m_value.map->swap(*de->m_value.map);
	}
	m_token=0;
	                                                                                        
}

void konga::DataElement::setName(const char* const name) {
	                                                
	if ( name == m_name || ( name != 0 && m_name != 0 && strcmp(name, m_name) == 0 ) )
		return;

	if ( m_name ) {
		delete[] m_name;
		m_name = 0;
	}
	if ( name )
		m_name = strsave(name);
}

void konga::DataElement::adoptName(char* const name) {
	if ( m_name ) {
		delete[] m_name;
		m_name = 0;
	}
	m_name = name;
}

void konga::DataElement::setXmlNode(const konga::xml::DOMNodePtr& node){
	initXmlNode(&node);
	m_type=t_string;
	setNull(false);
}

void konga::DataElement::setJbNode(konga::JbNode* jbNode){
	m_jbNode = jbNode;
	m_type=t_string;
	setNull(false);
}

konga::xml::DOMNodePtr konga::DataElement::getXmlNode(){
	if(m_pXmlNode==0)initXmlNode(0);
	return *m_pXmlNode;
}

void konga::DataElement::setStringValueInternal(const char* value) {
	assert(m_type == t_string);
	if( m_type != t_string ) return;

	if ( m_value.stdstr != 0 ) {
		delete m_value.stdstr;
	}

	if ( value ) {
		setNull(false);
		m_value.stdstr = new string(value);
	}
	else {
		setNull(true);
		m_value.stdstr = 0;
	}
}

bool konga::DataElement::appendStringValue(const char* value) {
	if(m_type != t_string || value==0) return false;
	m_err=0;

	if(m_value.stdstr==0) {
		setStringValueInternal(value);
	}
	else {
		m_value.stdstr->append(value);
	}
	return true;
}

void konga::DataElement::initUnion() {
	memset(&m_value, 0, sizeof(m_value));
}

void konga::DataElement::initDE(){
	m_type=t_unknown, m_name=0, m_pos_begin=-1, m_pos_len=-1, m_err=0;
	m_precision = 0;
	m_bNull = false;
	m_token=0;
	m_pXmlNode=0;
	m_jbNode=0;
	initUnion();
}

void konga::DataElement::initXmlNode(const konga::xml::DOMNodePtr* const pNode) {
	if ( pNode == 0 ) {
		if( m_pXmlNode != 0 ) delete m_pXmlNode;
		m_pXmlNode = new konga::xml::DOMNodePtr();
	}
	else {
		if( m_pXmlNode != 0 ) {
			*m_pXmlNode = *pNode;
		}
		else {
			m_pXmlNode = new konga::xml::DOMNodePtr(*pNode);
		}
	}
}

void konga::DataElement::clearValue() {
	if( m_type==t_string && m_value.stdstr!=0 ) {
		delete m_value.stdstr;
	}
	else if( m_type==t_date && m_value.t!=0 ) {
		delete m_value.t;
	}
	else if( m_type==t_collection && m_value.ds!=0 ){
		const size_t n = getDECollectionSize();
		for(size_t i=0; i<n; i++){
			DataElement*de=getDECollectionOf(i);
			if(de && !de->isTokenDE())delete de;
		}
		delete m_value.ds;
	}
	else if( m_type==t_binary && m_value.binary!=0){
		delete m_value.binary;
	}
	else if( m_type==t_map && m_value.map!=0){
		std::map<std::string,DataElement*>*map=m_value.map;
		for(std::map<std::string,DataElement*>::iterator it=map->begin(); it!=map->end(); ++it){
			delete it->second;
		}
		delete m_value.map;
	}
	if(m_pXmlNode!=0) {
		delete m_pXmlNode;
		m_pXmlNode = 0;
	}
	                                                                              
	initUnion();
}

                     
             
                                              
   

                                                                                           
void konga::DataElement::ProcessString(const char* inp_str,int&count,int&err){
	err=0;
	setNull(false);
	char* t=(char*)&inp_str[count],*t2;
	                                                                 
	int k,kk,kkk;
	                             
	if(scan_de_name(t)<0){                    
		k=strcspn(t,' ');
		kk=k+1+strcspn(&t[k+1],' ');
		if(sscanf(t,"%d %d ",&m_pos_begin,&m_pos_len)==2){
			count+=kk+1;
			t+=kk+1;
			k=strcspn(t,'(');
			kk=strcspn_(t,')');
			if(t[k]!=0&&t[kk]!=0){
				adoptName(strsave(t,k));

				t2=strsave(&t[k+1],kk-k-1);
				int comma_pos = strcspn(t2,',');
				if(t2[comma_pos]==','){
					char* t3=strsave(&t2[comma_pos+1],kk-k-comma_pos-2);
					int p=strcspn(t3,'"');
					int pp=strcspn_(t3,'"');
					if(t3[p]!='"'||t3[pp]!='"'){
						err=TRANSFORM_INPUT_STR_PARSING_ERR;
						m_formatStr.Empty();	                   
					}
					m_formatStr=strsave(&t3[p+1],pp-p-1);
					delete[]t3;
					t2[comma_pos]='\0';
				}
				  
				m_type=get_type(t2,0);
				delete[]t2;
				count+=kk+1;                      
				if(t[kk+1]=='='){
					if(t[kk+2]=='"'){
						kkk=strcspn(&t[kk+3],'"');
						if(t[kk+3+kkk]=='"'){
							m_defaultValueStr=KString(&t[kk+3],kkk);
							count+=kkk+3;
						}
					}
				}
			} else {
				err=TRANSFORM_INPUT_STR_PARSING_ERR;
			}
		} else {
			err=TRANSFORM_INPUT_STR_PARSING_ERR;
		}
	} else {
		k=strcspn(t,'(');
		kk=strcspn_(t,')');
		if(t[k]!=0&&t[kk]!=0){
			adoptName(strsave(t,k));
			t2=strsave(&t[k+1],kk-k-1);                     
			int comma_pos = strcspn(t2,',');
			if(t2[comma_pos]==','){
				char* t3=strsave(&t2[comma_pos+1],kk-k-comma_pos-2);
				int p=strcspn(t3,'"');
				int pp=strcspn_(t3,'"');
				if(t3[p]!='"'||t3[pp]!='"'){
					err=TRANSFORM_INPUT_STR_PARSING_ERR;
					m_formatStr.Empty();	                   
				}
				m_formatStr=KString(&t3[p+1],pp-p-1);
				delete[]t3;
			}
			  
			int space_pos = ::strcspn(t2," ,");
			t2[space_pos]='\0';
			int angle_bracket_pos1=::strcspn(t2,"<"),angle_bracket_pos2=::strcspn(t2,">"), sqlDataType=0;

			if(angle_bracket_pos2>(angle_bracket_pos1+1) && t2[angle_bracket_pos1]=='<' && t2[angle_bracket_pos2]=='>'){
				t2[angle_bracket_pos1]=0;
				t2[angle_bracket_pos2]=0;
				sqlDataType=atoi(&t2[angle_bracket_pos1+1]);
			}
			m_type=get_type(t2,sqlDataType);
			delete[]t2;
			count+=kk+1;
			if(t[kk+1]=='='){
				if(t[kk+2]=='"'){
					kkk=strcspn(&t[kk+3],'"');
					if(t[kk+3+kkk]=='"'){
						m_defaultValueStr=KString(&t[kk+3],kkk);
						count+=kkk+3;
					}
				}
			}
		} else {
			err=TRANSFORM_INPUT_STR_PARSING_ERR;
			setName("?");
		}
	}
	if(err)
		m_err=err;	
}

namespace {
	std::map<string, DataElement::Type> g_datatype_map;
	boost::mutex fill_map_mutex;

	const int SQL_BLOB = 2004;	                                                                                                         
}

DataElement::Type DataElement::get_type(const char* typeName, int sqlDataType){
	                                                  
	if(sqlDataType!=SQL_UNKNOWN_TYPE){
		                                                        
		switch(sqlDataType){
		case SQL_CHAR:
			return konga::DataElement::t_string;
		case SQL_DOUBLE:
		case SQL_NUMERIC:
		case SQL_DECIMAL:
			return konga::DataElement::t_double;
		case SQL_INTEGER:
		case SQL_SMALLINT:
		case SQL_TINYINT:
		case SQL_BIT:
			return konga::DataElement::t_int;
		case SQL_BIGINT:
			return konga::DataElement::t_long;
		case SQL_FLOAT:
		case SQL_REAL:
			return konga::DataElement::t_double;
		case SQL_VARCHAR: 
		case SQL_LONGVARCHAR:
			return konga::DataElement::t_string;
		case SQL_TIME:
		case SQL_TIMESTAMP:
			return konga::DataElement::t_date;
		case SQL_BINARY:
		case SQL_VARBINARY:
		case SQL_LONGVARBINARY:
		case SQL_BLOB:
			return konga::DataElement::t_binary;
#if (ODBCVER >= 0x0300)
		case SQL_DATETIME:
		case SQL_TYPE_DATE:
		case SQL_TYPE_TIME:
		case SQL_TYPE_TIMESTAMP:
			return konga::DataElement::t_date;
#endif
		}
	}
	if(typeName==0)return t_string;

	{                                   
	boost::mutex::scoped_lock lock(fill_map_mutex);
	if(g_datatype_map.empty()){

		const string stMapFileName( konga::GetServerHomeWithSlash()+"Resources/TransformationEngine/DataTypeMappings.conf" );
		FILE*in=fopen(stMapFileName.c_str(),"r");
		if(in==0){
			throw CrException("Can't open configuration file \""+stMapFileName+"\"");
		}
		konga::AutoFileHandle adopt_in(in);
		g_datatype_map["t_unknown"]=t_unknown;
		g_datatype_map["t_int"]=t_int;
		g_datatype_map["t_long"]=t_long;
		g_datatype_map["t_bit"]=t_bit;
		g_datatype_map["t_bool"]=t_bool;
		g_datatype_map["t_date"]=t_date;
		g_datatype_map["t_collection"]=t_collection;
		g_datatype_map["t_string"]=t_string;
		g_datatype_map["t_double"]=t_double;
		g_datatype_map["t_float"]=t_float;
		g_datatype_map["t_binary"]=t_binary;
		char buf[1024];
		while(fgets(buf,1023,in)){
			int i1=::strcspn(buf,"\t"), i2=::strcspn(buf,";");
			if(strncmp(buf,"t_",2)==0&&i1>0&&i2>(i1+1)){
				for(int i=0;i<i2;i++)buf[i]=tolower(buf[i]);
				buf[i2]=0;
				string s1(buf,i1), s2(&buf[i1+1]);
				std::map<string, DataElement::Type>::const_iterator cit=g_datatype_map.find(s1);
				if(cit!=g_datatype_map.end()){
					g_datatype_map[s2]=cit->second;
				}
			}
		}
	}
	}                   

	char buf[100];
	int i=0,n=strlen(typeName);
	if(n>99){
		throw konga::EvalException("Unexpected long data type string: \""+string(typeName)+"\"");
	}
	for(;i<n;i++){
		buf[i]=tolower(typeName[i]);                                                   
	}
	for(i=n-1;i>0&&typeName[i]==' ';i--){
		n--;
	}
	buf[n]=0;
	std::map<string, DataElement::Type>::const_iterator cit=g_datatype_map.find(buf);
	if(cit!=g_datatype_map.end()){
		return cit->second;
	}
	                        
	  
                                                                                                                    
                                                 
                                           
                                             
                                            
   
	if(stricmp(typeName,"Auto")==0)return t_int;
	                                                                       
	return t_string;

}

bool konga::DataElement::IsAString(const char*type_str, int sqlDataType){
	Type type=get_type(type_str, sqlDataType);
	if(type==t_int||type==t_long||type==t_float||type==t_double||type==t_bool||type==t_timespan||type==t_collection)return false;
	return true;
}

bool konga::DataElement::IsAString() const {
	if(m_type==t_int||m_type==t_long||m_type==t_float||m_type==t_double||m_type==t_bool||m_type==t_timespan||m_type==t_collection||m_type==t_binary)return false;
	return true;
}

bool konga::DataElement::GetBool(int&err) const {
	err=m_err;
	if(m_err||IsNull())return false;

	switch(m_type){
		case t_int: return m_value.i!=0;
		case t_long: return m_value.l!=0;
		case t_float: return m_value.f!=0;
		case t_double: return m_value.d!=0;
		case t_bool: return m_value.b;
		case t_string:
			{
				if(m_value.stdstr==0)return false;
				if(stricmp(m_value.stdstr->c_str(),"true")==0 ||stricmp(m_value.stdstr->c_str(),"t")==0||stricmp(m_value.stdstr->c_str(),"yes")==0)return true;
				if(stricmp(m_value.stdstr->c_str(),"false")==0||stricmp(m_value.stdstr->c_str(),"f")==0||stricmp(m_value.stdstr->c_str(),"no")==0) return false;
				return atof(m_value.stdstr->c_str())!=0;
			}

		default: 
			err=TRANSFORM_CANNOT_CVT_DE_TYPE;
	}
	return false;
}

size_t konga::DataElement::GetStringLengthUtf8Character(int&err) {
	if( m_type != t_string || HasFormat() || m_value.stdstr==0 )
		return GetString(err).GetLength();

	const string& utf8string=*m_value.stdstr;
	const size_t charactersize = konga::EncodingStringUtils::Utf8CharacterSize(utf8string);
	return charactersize;
}

size_t konga::DataElement::GetStringLength(int&err) {
	if( m_type != t_string || HasFormat() || m_value.stdstr==0 )
		return GetString(err).GetLength();

	return m_value.stdstr->size();
}

void konga::DataElement::redefineType(Type type, const char*formatStr0)
{
	assert(type!=t_unknown);
	int err=0;
	KString formatStr;
	if(formatStr0){
		formatStr=formatStr0;
	}
	if(m_type==type && m_formatStr!=formatStr){
		m_formatStr=formatStr;
	}
	else if(m_type!=type){
		KString value=GetString(err);
		clearValue();
		Type type_saved=m_type;
		m_type=type;
		setValue((const char*)value);
		if(m_err!=0){
			handle_err(m_err, value, type_saved, type);
		}
		m_formatStr=formatStr;
	}
}

KString konga::DataElement::GetString(int&err) const 
{
	if(m_err||IsNull()){
		err=IsNull()?TARGET_MAPPED_TO_NULL:m_err;
		return "";
	}
	err = 0;
	
	if(m_pXmlNode != 0 && *m_pXmlNode!=0){
		                                     
		return KString((*m_pXmlNode)->getXml());
	}

	KString str;
	bool bHasFormat=HasFormat();
	const char* sFormat=(const char*)m_formatStr;

	switch(m_type){
	case t_int:
		if(bHasFormat)
			str.Format(sFormat,m_value.i);
		else {
			str.Format("%d",m_value.i);
		}
		break;
	case t_float:
		{
			if(bHasFormat){
				str.Format(sFormat,m_value.f);
			} else {
				return konga::toString(m_value.f);
			}
			break;
		}
	case t_long:
		if(bHasFormat)
			str.Format(sFormat,m_value.l);
		else
			str = konga::toString(m_value.l);
                                   
		break;
	case t_double:
		{
			if(bHasFormat){
				str.Format(sFormat,m_value.d);
			} else {
				str=konga::toString(m_value.d);
			}
			break;
		}
	case t_string:
		if(m_value.stdstr==0)
			str.Empty();
		else {
			if(bHasFormat)
				str.Format(sFormat,m_value.stdstr->c_str());
			else
				str=*m_value.stdstr;
		}
		break;
	case t_bool:
		str=(strcmp(sFormat,"%s")==0)? (m_value.b?"true":"false"): (m_value.b?"1":"0");
		break;
	case t_date:
		if(m_value.t->IsValid())
		{
			if(bHasFormat){
				str = m_value.t->toString(sFormat);
			}
			else {
				                                                                                           
				                                               
				if (!(0 == m_value.t->GetHour() && 0 == m_value.t->GetMinute() && 0 == m_value.t->GetSecond())){
					                                     
					str=m_value.t->toString("yyyy-mm-dd HH:MM:SS");
				}
				else {
					                                  
					str=m_value.t->toString("yyyy-mm-dd");
				}
			}
		}
		else
			str="";
		break;
	case t_binary:
		str=m_value.binary->GetHexRep();
		break;
	case t_collection: {
		const size_t n=getDECollectionSize();
		int ignoreError=0;
		str="{";
		for(size_t i=0 ; i<n; ++i) {
			DataElement*ds=getDECollectionOf(i);
			if(i>0)str+=",";
			string str1((const char*)ds->GetString(ignoreError));
			if(ds->isCollection()){
				str+=str1;
			}
			else {
				const bool bString=ds->IsAString();
				if(bString) {
					qualifyCsvString(str1, ",{}\"");
				}
				str+=str1;
			}
		}
		str+="}";
		break;
	}

	default:
		err=TRANSFORM_CANNOT_CVT_DE_TYPE;
		break;
	}

	if(err==0&&HasDomain()){
		err=checkDomain(m_formatStr,str);
	}
	return str;
}

double konga::DataElement::GetDouble(int&err) const {
	err=m_err;
	if(m_err||IsNull())return 0.0;

	switch(m_type){
		case t_int:
			return m_value.i;
		case t_float:
			return m_value.f;
		case t_long:
			return m_value.l;
		case t_double:
			return m_value.d;
		case t_string:
			if(m_value.stdstr!=0) return atof(m_value.stdstr->c_str());
			break;
		case t_bool:
			return m_value.b?1.0:0.0;
		case t_bit:
			return m_value.i==0?0.0:1.0;
		case t_date:
			return GetLong(err);
		default:
			err=TRANSFORM_CANNOT_CVT_DE_TYPE;
			return 0.0;
	}
	return 0.0;
}

std::map<std::string, konga::DataElement*>* konga::DataElement::GetMap(int&err) const
{
	err=m_err;
	if(m_err||IsNull())return 0;

	if(m_type!=t_map){
		err=TRANSFORM_CANNOT_CVT_DE_TYPE;
		return 0;
	}
	return m_value.map;
}

namespace {
	time_t to_time_t(const CrDateTime& d, int& err) {
		const int year=d.GetYear();
		if(year>=1970&&year<=2038)
			return d.GetTime();
		else
			err=TRANSFORM_CANNOT_CVT_DE_TYPE;
		return 0;
	}
}

int konga::DataElement::GetInteger(int&err) const {
	err=m_err;
	if(m_err||IsNull())return 0;

	switch(m_type){
		case t_int:
			return m_value.i;
		case t_float:
			return static_cast<int>(m_value.f);
		case t_long:
			return static_cast<int>(m_value.l);
		case t_double:
			return static_cast<int>(m_value.d);
		case t_date:
			if(m_value.t==0)
				err=TRANSFORM_CANNOT_CVT_DE_TYPE;
			else
				return static_cast<int>(to_time_t(*m_value.t, err));
			break;
		case t_bool:
			return m_value.b?1:0;
		case t_bit:
			return m_value.i==0?0:1;
		case t_string:
			if(m_value.stdstr!=0)
				return atoi(m_value.stdstr->c_str());
			break;
		default:
			err=TRANSFORM_CANNOT_CVT_DE_TYPE; break;
	}
	return 0;
}

long long konga::DataElement::GetLong(int&err) const
{
	err=m_err;
	if(m_err||IsNull()) return 0;
	switch(m_type)
	{
		case t_int:
			return m_value.i;
		case t_float:
			return static_cast<long long>(m_value.f);
		case t_long:
			return m_value.l;
		case t_double:
			return static_cast<long long>(m_value.d);
		case t_bool:
			return m_value.b?1:0;
		case t_bit:
			return m_value.i==0?0:1;
		case t_date:
			if(m_value.t==0)
				err=TRANSFORM_CANNOT_CVT_DE_TYPE;
			else
				return static_cast<long long>(to_time_t(*m_value.t, err));
			break;
		case t_string:
			if(m_value.stdstr!=0)
				return atoll(m_value.stdstr->c_str());
			break;
		default:
			err=TRANSFORM_CANNOT_CVT_DE_TYPE;
			return 0;
	}
	return 0;
}

float konga::DataElement::GetFloat(int&err) const
{
	err=m_err;
	if(m_err||IsNull()) return 0.0f;

	switch(m_type)
	{
		case t_int:
			return static_cast<float>(m_value.i);
		case t_float:
			return m_value.f;
		case t_long:
			return static_cast<float>(m_value.l);
		case t_double:
			return static_cast<float>(m_value.d);
		case t_bool:
			return m_value.b?1.0f:0.0f;
		case t_bit:
			return (m_value.i!=0)?1.0f:0.0f;
		case t_date:
			return GetInteger(err);
		default:
			err=TRANSFORM_CANNOT_CVT_DE_TYPE;
			return 0.0f;
	}
}

konga::BinaryData* konga::DataElement::GetBinaryDataPointer(int&err) const
{
	if(m_type==t_binary ){
		err = m_err;
		return m_value.binary;
	}
	err=TRANSFORM_WRONG_TYPE_FOR_BINARY;
	return 0;
}

KString konga::DataElement::ToEdiDate(const char*date_str,int length) {
	bool allNumeric = false;
	int l=(int)strlen(date_str);
	for(int i=0;i<l;i++){
		allNumeric=isdigit(date_str[i])!=0;
		if(allNumeric==false)break;
	}
	if(allNumeric){
		if(l==length)return KString(date_str);
		if(l>length){
			return KString("20")+KString(date_str);
		}
		return KString(&date_str[2]);
	}
	DataElement de(t_date,date_str);
	if(de.m_err||!de.m_value.t->IsValid())return KString();
	if(length==8)return KString(de.Format("%Y%m%d",de.m_err));
	return KString(de.Format("%y%m%d",de.m_err));
}

KString konga::DataElement::FromEdiDate(const char*date_str) {
	bool allNumeric = false;
	int l=(int)strlen(date_str);
	for(int i=0;i<l;i++){
		allNumeric=isdigit(date_str[i])!=0;
		if(allNumeric==false)break;
	}
	if(allNumeric==false)return KString(date_str);
	DataElement de(konga::DataElement::t_string,date_str);
	int err=0;
	std::string stErrMsg;
	if(l==8)return CVTDate(&de,"yyyymmdd","mm/dd/yyyy",err,stErrMsg);
	return CVTDate(&de,"yymmdd","mm/dd/yyyy",err, stErrMsg);
}

KString konga::DataElement::ToOracleDate(const char*date_str, bool* bHasSeconds)
{
	int err = 0;
	KString sRetVal = "";
	*bHasSeconds = false;
	DataElement de(konga::DataElement::t_string,date_str);
	std::string stErrMsg;

	if(strlen(date_str) > 10)
	{
		                                     
		sRetVal = CVTDate(&de, "?m/?d/??yy HH:MM:SS", "?d-mmm-??yy HH:MM:SS", err, stErrMsg);
		*bHasSeconds = true;
	}
	else
	{
		sRetVal = CVTDate(&de, "?m/?d/??yy", "?d-mmm-??yy", err, stErrMsg);
		*bHasSeconds = false;
	}

	return sRetVal;
}

CrDateTime konga::DataElement::GetDate(int&err) const
{
	err=m_err;
	if(m_err||IsNull())return CrDateTime::GetCurrentTime();
	switch(m_type){
	case t_int:
		return CrDateTime(static_cast<time_t>(m_value.i));
	case t_long:
		return CrDateTime(static_cast<time_t>(m_value.l));
                               
                
                                                      
                 
                                                      
	case t_date:
		if(m_value.t)
			return *m_value.t;
		else {
			err=1;
		}
		break;
	case t_string:
		return GetDate(m_value.stdstr->c_str(), m_formatStr, err);
	default:
		err=TRANSFORM_CANNOT_CVT_DE_TYPE; break;
	}
	m_err = err;
	return CrDateTime::GetCurrentTime();
}
namespace {
	int getErrorCode(const std::string&errMsg){
		if(errMsg.empty())return 0;
		int err=-1;
		if(errMsg=="YEAR_OUT_OF_RANGE")err=YEAR_OUT_OF_RANGE;
		else if(errMsg=="MONTH_OUT_OF_RANGE")err=MONTH_OUT_OF_RANGE;
		else if(errMsg=="DAY_OUT_OF_RANGE")err=DAY_OUT_OF_RANGE;
		else if(errMsg=="HOUR_OUT_OF_RANGE")err=HOUR_OUT_OF_RANGE;
		else if(errMsg=="MINUTE_OUT_OF_RANGE")err=MINUTE_OUT_OF_RANGE;
		else if(errMsg=="SECOND_OUT_OF_RANGE")err=SECOND_OUT_OF_RANGE;
		return err;
	}
}


CrDateTime konga::DataElement::GetDate(const char*str0,const char*formatStr, int&err) const {
	if(str0==0||str0[0]==0||IsNull()){
		err=TRANSFORM_INPUT_EMPTY;
		return CrDateTime::GetCurrentTime();
	}
	KString str;
	std::string errMsg;
	if(formatStr[0]!=0){
		if(strcmp(formatStr,konga::CrDateTime::XML_DATE_FORMAT)==0){
			int y=1900, m=1, d=1;
			sscanf(str0, "%4d-%2d-%2d", &y, &m, &d);
			CrDateTime date(y, m, d, -1, -1, -1);
			if(strlen(str0) > 10){
				date.setTZ(&str[10]);
			}
			return date;
		} else if(strcmp(formatStr,konga::CrDateTime::XML_TIME_FORMAT)==0){
			int H=0,M=0,S=0,fs=0;
			sscanf(str0,"%2d:%2d:%2d",&H,&M,&S);
			int k=8;
			if(strlen(str0)>=8 && str0[k]=='.'){
				for(k++; str[k]&&isdigit(str0[k]); k++)
					fs=10*fs+str0[k]-'0';				
			}
			CrDateTime time(-1, -1, -1, H, M, S, fs);
			if(str0[k]!=0)
			time.setTZ(&str0[k]);
			return time;
		} else if(strcmp(formatStr,konga::CrDateTime::XML_DATE_TIME_FORMAT)==0){
			int y=1900, m=1, d=1, H=0, M=0, S=0, fs=0;
			sscanf(str0, "%4d-%2d-%2dT%2d:%2d:%2d", &y, &m, &d, &H, &M, &S);
			int k=19;
			if(strlen(str0)>=19 && str0[k]=='.'){
				for(k++;str0[k] && isdigit(str0[k]);k++)
					fs = 10*fs + str0[k] - '0';				
			}
			CrDateTime dateTime(y, m, d, H, M, S, fs);
			if(str0[k]!=0)
			dateTime.setTZ(&str0[k]);
			return dateTime;
		}
		int itrim=0,c;
		while((c=str0[itrim])){
			if(c!=' ' && c!='\t')break;
			itrim++;
		}
		if(konga::valid_strftime_format(formatStr)){
			int y,m,d,H,M,S,fs=0;
			string tz;
			ReadDate_From_strftime_format(str0, formatStr, y, m, d, H, M, S, tz);
			CrDateTime dateTime(y, m, d, H, M, S, fs);
			dateTime.setTZ(tz);
			return dateTime;
		} else {
			DataElement tmp(t_string,&str0[itrim]);
			str=CVTDate(&tmp, formatStr, "GeneralDate", err, errMsg);
			err=getErrorCode(errMsg);
		}
		if(err){
			return CrDateTime::GetCurrentTime(); 
		}
	} else {
		int itrim=0,c;
		while((c=str0[itrim])){
			if(c!=' '&&c!='\t')break;
			itrim++;
		}
		str=&str0[itrim];
	}
	int year,month,day,hour,min,sec,day_of_week,tz_shift_minutes;

	bool bCheckValidYear=true;
	err=ReadDate(str, year, month, day, hour, min, sec, day_of_week,
		tz_shift_minutes, bCheckValidYear, errMsg);
	CrDateTime t=CrDateTime::GetCurrentTime();
    if(err!=0)return t;
	if(year==-1&&hour!=-1&&min!=-1)year=t.GetYear();
	if(month==-1&&hour!=-1&&min!=-1)month=t.GetMonth();
	if(day==-1&&hour!=-1&&min!=-1)day=t.GetDay();
	if(hour==-1)hour=0;
	if(min==-1)min=0;
	if(sec==-1)sec=0;
	err=CheckDateError(year, month, day, bCheckValidYear, errMsg);
	if(sec<0||sec>=60){
		errMsg="SECOND_OUT_OF_RANGE";
	}
	if(min<0||min>=60){
		errMsg="MINUTE_OUT_OF_RANGE";
	}
	if(hour<0||hour>=24){
		errMsg="HOUR_OUT_OF_RANGE";
	}
	err=getErrorCode(errMsg);
	if(err==0){
		t=CrDateTime(year, month, day, hour, min, sec, 0);
		if(tz_shift_minutes!=0){
			char c=tz_shift_minutes>0? '+': '-';
			tz_shift_minutes=abs(tz_shift_minutes);
			int dh=tz_shift_minutes/60, dm=tz_shift_minutes%60;
			char buf[7];
			sprintf(buf, "%c%02d:%02d", c, dh, dm);
			t.setTZ(buf);
		}
	}
	return t;
}

size_t konga::DataElement::getDECollectionSize() const {
	if( m_type==t_collection && m_value.ds!=0 )
		return m_value.ds->size();
	return 0;
}

DataElement* konga::DataElement::getDECollectionOf(size_t index) const {
	try {
		assert(m_type==t_collection && m_value.ds != 0);
		if( m_type==t_collection && m_value.ds != 0 )
			return m_value.ds->at(index);
	}
	catch( const std::out_of_range& ) {
		throw CrException("index " + konga::toString(index)
			+ " is out of range.");
	}
	throw CrException("Data element " + string(getName()) + " is not a collection.");
}

KString konga::DataElement::Format(const char*formatStr,int&err) const {
	KString str;
	err=0;
	if(m_type==t_date){
		str=m_value.t->toString(formatStr);
	} else {
		int k = konga::strcspn(formatStr,'%'),kp;
		while(formatStr[k]=='%'&&formatStr[k+1]=='%')k+=2+konga::strcspn(&formatStr[k+2],'%');
		if(formatStr[k]==0){
			err=TRANSFORM_FORMAT_STR_EQ_0;
			return str;
		}
		kp=k+1+konga::strcspn(&formatStr[k+1],'%');
		while(formatStr[kp]=='%'&&formatStr[kp+1]=='%')kp+=2+konga::strcspn(&formatStr[kp+2],'%');
		if(formatStr[kp]!=0){
			err=TRANSFORM_FORMAT_STR_GT_1;
			return str;
		}
		char c;
		for(kp=k+1;(c=formatStr[kp])!=0;kp++)
			if(isdigit(c)==false&&c!='.'&&c!='*'&&c!='+'&&c!='-'&&c!=' ')break;
		if(c=='h'||c=='l'||c=='L')c=formatStr[kp+1];
		switch(c){
		case 'd':
		case 'i':
		case 'o':
		case 'u':
		case 'x':
		case 'X':
			str.Format((const char*)formatStr,GetInteger(err)); break;
		case 'e':
		case 'E':
		case 'f':
		case 'g':
		case 'G':
			str.Format((const char*)formatStr,GetDouble(err)); break;
		case 's':
		case 'S':
			str.Format((const char*)formatStr,(const char*)GetString(err)); break;
		default:          
			err=TRANSFORM_FORMAT_STR_SYNTAX_ERROR;
		}
	}
	return str;
}


                                  
                                                                        
 
                                                                                                                         
                                                                                                                         
                 
                                       
                                          
                                        
                                    
                                   
                                   
                              
                                    
                               
                                     
                                
                                      
                                 
                                    
                               
                                      
                                 
                                    
                               
            
                                    

     
 
  

KString konga::CVTDate(DataElement*inputDE,KString input_form_str,KString output_form_str,int&err, std::string& errMsg)
{
	konga::CrDateTime t;
	err=0;
	if(inputDE->getType()==konga::DataElement::t_date){
		t=inputDE->GetDate(err);
		if(err!=0)return "";
		return t.toString((const char*)output_form_str);
	}
	KString input_data_str;
	if(inputDE->getType()==konga::DataElement::t_int || inputDE->getType()==konga::DataElement::t_long){
		if(strlen(input_form_str)==6)
			input_data_str.Format("%.6d",inputDE->GetInteger(err));
		else if(strlen(input_form_str)==8)
			input_data_str.Format("%.8d",inputDE->GetInteger(err));
	}
	else {
		input_data_str = inputDE->GetString(err);
	}
	if(err!=0){
		input_data_str = inputDE->input();
	}
	std::string output_data_str = CVTDate(input_data_str, input_form_str, output_form_str, errMsg);
	err=getErrorCode(errMsg);
	return output_data_str;
}

void konga::DataElement::setNull(bool b) {
	if(m_bNull==b) return;
	if(b) clearValue();
	m_bNull=b;
}

bool konga::DataElement::isGlobalDE(){
	bool b=(getName()[0]=='$');
	assert(!b || (m_token!=0 && m_token->getType()==Token::GlobalDE));
	return  b;
}

bool konga::RepresentsAnInteger(const DataElement& de) {
	const konga::DataElement::Type t = de.getType();
	int err = de.getError();
	if ( de.IsNull() || t == konga::DataElement::t_unknown || err != 0 )
		return false;

	switch(t) {
		case konga::DataElement::t_int:
		case konga::DataElement::t_long:
			return true;
		case konga::DataElement::t_float:
		case konga::DataElement::t_double:
			{
				const double d = de.GetDouble(err);
				const double dfloor = floor(d);
				return d == dfloor;
			}
		case konga::DataElement::t_string:
			{
				const string s((const char*)de.GetString(err));
				if ( s.empty() )
					return false;
				try {
					                                                                                       
					toLongLong(s);
					return true;
				}
				catch( ... ) {
					return false;
				}
			}
		case konga::DataElement::t_unknown:
		case konga::DataElement::t_date:
		case konga::DataElement::t_bool:
		case konga::DataElement::t_timespan:
		case konga::DataElement::t_bit:
		case konga::DataElement::t_collection:
		case konga::DataElement::t_binary:
			return false;
	};
	return false;
}

bool konga::DataElement::isTokenDE(){
	return m_token!=0;
}

konga::DataElement::Type konga::DataElement::getDataElementTypeForArray(int&err, KString& errmsg) const {
	assert(getType()==t_collection);
	Type type=t_unknown;
	std::set<konga::DataElement::Type> type_set;
	for(int i=0, n=getDECollectionSize(); i<n; i++){
		konga::DataElement::Type type1=getDECollectionOf(i)->getType();
		if(type1==t_unknown || type1==type)continue;
		type_set.insert(type1);
	}
	if(type_set.size()==1){
		return *type_set.begin();
	}
	else if(type_set.size()>1){
		errmsg="more than one data type in the array:";
		bool start=true;
		for(std::set<konga::DataElement::Type>::iterator it=type_set.begin(); it!=type_set.end(); it++){
			if(!start)errmsg+=", ";
			errmsg+=konga::DataElement::get_type_name(*it);
			start=false;
		}
		err=-1;
		return *type_set.begin();
	}
	return t_unknown;
}

konga::DataElement::Type konga::DataElement::findFinalDataElementType(){
	Type type=t_unknown;
	if(m_type==t_collection){
		for(int i=0,n=getDECollectionSize(); i<n; i++){
			type=getDECollectionOf(i)->findFinalDataElementType();
			if(type!=t_unknown)return type;
		}
	}
	return m_type;
}

std::string konga::DataElement::toString() const {
	std::string str;
	int err;
	str.append("type=").append(get_type_name(m_type));
	if(m_input.IsEmpty()==false)str.append("\n\tinput=\"").append((const char*)m_input).append("\"");
	else str.append("\n\tstr=").append((const char*)GetString(err));
	return str;
}
