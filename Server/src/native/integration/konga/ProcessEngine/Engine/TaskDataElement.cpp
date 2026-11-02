/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <algorithm> 				                       
#include <string>

#include <konga/Utils/PortDefs.h>		                                   
#include <konga/Utils/date.h>

#include "TaskDataElement.h"

using namespace std;
using namespace konga;

const char* data_type_str[] = { "undefined", "bool", "int", "long", "single", "float", "double", "string", "variant", "date", "text" };
                                                                                                                          

bool TaskDataElement::is_true() const
{
	if( !m_value.c_str() ) return false;
	else if( stricmp(m_value.c_str(),"true") == 0 ) return true;
	else if( stricmp(m_value.c_str(),"yes") == 0 ) return true;
	else return atof(m_value.c_str()) != 0;
}

  
           
  
bool TaskDataElement::operator == (const TaskDataElement& rhs) const
{
	switch( m_type )
	{
	case t_date:
		if( rhs.m_type == t_date )
		{
			string stErr;
			CrDateTime lhsDate( ::GetDate(m_value.c_str(), stErr ));
			CrDateTime rhsDate( ::GetDate(m_value.c_str(), stErr ));
			return (lhsDate == rhsDate);
		}
		else
			return false;
	case t_string:
	case t_text:
	case t_variant:
		switch( rhs.m_type )
		{
		case t_variant:
		case t_text:
		case t_string:
			{
				if( m_value.c_str() == rhs.m_value.c_str() ) return true;

				  
				                                                                    
				  
				if( !m_value.c_str() || !*m_value.c_str() )
					return !rhs.m_value.c_str() || !*rhs.m_value.c_str();
				if( !rhs.m_value.c_str() || !*rhs.m_value.c_str() )
					return !m_value.c_str() || !*m_value.c_str();

				return stricmp(m_value.c_str(), rhs.m_value.c_str()) == 0;
			}
		default:
			if( m_value.c_str() == rhs.m_value.c_str() ) return true;
			if( !m_value.c_str() || !rhs.m_value.c_str() ) return false;
			double d;
			if( sscanf(m_value.c_str(),"%lf",&d) == 0) return false;
			return d == atof(rhs.m_value.c_str());
			                              
		}
	case t_float:
	case t_double:
	case t_single:
	case t_long:
	case t_int:
		if( m_value.c_str() == rhs.m_value.c_str() ) return true;
		if( !m_value.c_str() || !rhs.m_value.c_str() ) return false;
		if( rhs.m_type == t_string || rhs.m_type == t_text )
		{
			double d;
			if( sscanf(rhs.m_value.c_str(),"%lf",&d) == 0) return false;
			return d == atof(m_value.c_str());
		}
		return atof(m_value.c_str()) == atof(rhs.m_value.c_str());
	case t_bool: return is_true() == rhs.is_true();
	default: return false;                               
	}
}

  
                     
  
bool TaskDataElement::operator <= (const TaskDataElement& rhs) const
{
	if( !m_value.c_str() || !rhs.m_value.c_str() ) return false;
	switch( m_type )
	{
	case t_date:
		if( rhs.m_type == t_date )
		{
			string stErr;
			CrDateTime lhsDate( ::GetDate(m_value.c_str(), stErr ));
			CrDateTime rhsDate( ::GetDate(m_value.c_str(), stErr ));
			return (lhsDate <= rhsDate);
		}
		else
			return false;
	case t_variant:
	case t_text:
	case t_string: return strcmp(m_value.c_str(), rhs.m_value.c_str()) <= 0;
	case t_float:
	case t_double:
	case t_single:
	case t_long:
	case t_int:
		if( rhs.m_type == t_string || rhs.m_type == t_variant || rhs.m_type == t_text ) 
			return strcmp(m_value.c_str(), rhs.m_value.c_str()) <= 0;
		else if( rhs.m_type == t_bool )
		{
			char s[256];
			sprintf(s,"incompatible types (%s<=%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
		else return atof(m_value.c_str()) <= atof(rhs.m_value.c_str());
	case t_bool:
	default: 
		{
			char s[256];
			sprintf(s,"incompatible types (%s<=%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
	}
}

  
                        
  
bool TaskDataElement::operator >= (const TaskDataElement& rhs) const
{
	if( !m_value.c_str() || !rhs.m_value.c_str() ) return false;
	switch( m_type )
	{
	case t_date:
		if( rhs.m_type == t_date )
		{
			string stErr;
			CrDateTime lhsDate( ::GetDate(m_value.c_str(), stErr ));
			CrDateTime rhsDate( ::GetDate(m_value.c_str(), stErr ));
			return (lhsDate >= rhsDate);
		}
		else
			return false;
	case t_variant:
	case t_text:
	case t_string: return strcmp(m_value.c_str(), rhs.m_value.c_str()) >= 0;
	case t_float:
	case t_double:
	case t_single:
	case t_long:
	case t_int:
		if( rhs.m_type == t_string || rhs.m_type == t_variant || rhs.m_type == t_text ) 
			return strcmp(m_value.c_str(), rhs.m_value.c_str()) >= 0;
		else if( rhs.m_type == t_bool )
		{
			char s[256];
			sprintf(s,"incompatible types (%s>=%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
		else return atof(m_value.c_str()) >= atof(rhs.m_value.c_str());
	case t_bool:
	default:
		{
			char s[256];
			sprintf(s,"incompatible types (%s>=%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
	}
}

  
               
  
bool TaskDataElement::operator > (const TaskDataElement& rhs) const
{
	if( !m_value.c_str() || !rhs.m_value.c_str() ) return false;
	switch( m_type )
	{
	case t_date:
		if( rhs.m_type == t_date )
		{
			string stErr;
			CrDateTime lhsDate( ::GetDate(m_value.c_str(), stErr ));
			CrDateTime rhsDate( ::GetDate(m_value.c_str(), stErr ));
			return (lhsDate > rhsDate);
		}
		else
			return false;
	case t_variant:
	case t_text:
	case t_string: return strcmp(m_value.c_str(), rhs.m_value.c_str()) > 0;
	case t_float:
	case t_double:
	case t_single:
	case t_long:
	case t_int:
		if( rhs.m_type == t_string || rhs.m_type == t_variant || rhs.m_type == t_text )
		{
			return strcmp(m_value.c_str(), rhs.m_value.c_str()) > 0;
		}
		else if( rhs.m_type == t_bool )
		{
			char s[256];
			sprintf(s,"incompatible types (%s>%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
		else
		{
			double lhs_val,rhs_val;

			if( m_value.c_str() ) sscanf(m_value.c_str(),"%lf",&lhs_val);
			else lhs_val=0;

			if( rhs.m_value.c_str() ) sscanf(rhs.m_value.c_str(),"%lf",&rhs_val);
			else rhs_val=0;

			return  lhs_val > rhs_val;
		}
	case t_bool:
	default:
		{
			char s[256];
			sprintf(s,"incompatible types (%s>%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
	}
}

  
            
  
bool TaskDataElement::operator < (const TaskDataElement& rhs) const
{
	if( !m_value.c_str() || !rhs.m_value.c_str() ) return false;
	switch( m_type )
	{
	case t_date:
		if( rhs.m_type == t_date )
		{
			string stErr;
			CrDateTime lhsDate( ::GetDate(m_value.c_str(), stErr ));
			CrDateTime rhsDate( ::GetDate(m_value.c_str(), stErr ));
			return (lhsDate < rhsDate);
		}
		else
			return false;
	case t_variant:
	case t_text:
	case t_string: return strcmp(m_value.c_str(), rhs.m_value.c_str()) < 0;
	case t_float:
	case t_double:
	case t_single:
	case t_long:
	case t_int:
		if( rhs.m_type == t_string || rhs.m_type == t_variant || rhs.m_type == t_text ) 
			return strcmp(m_value.c_str(), rhs.m_value.c_str()) < 0;
		else if( rhs.m_type == t_bool )
		{
			char s[256];
			sprintf(s,"incompatible types (%s>%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
		else return atof(m_value.c_str()) < atof(rhs.m_value.c_str());
	case t_bool:
	default:
		{
			char s[256];
			sprintf(s,"incompatible types (%s<%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
	}
}

  
              
  
TaskDataElement TaskDataElement::operator + (const TaskDataElement& rhs) const
{
	switch( m_type )
	{
		                              
             
                            
   
                 
                                                           
                                                           
                                                                                        
   
      
                                                                                 
    
	case t_variant:
	case t_string: return TaskDataElement((m_value + rhs.m_value).c_str(), "no_name", t_string);
	case t_text: return TaskDataElement((m_value + rhs.m_value).c_str(), "no_name", t_text);
	case t_float:
	case t_double:
	case t_single:
	case t_long:
	case t_int:
		if( rhs.m_type == t_string || rhs.m_type == t_variant )
			return TaskDataElement((m_value + rhs.m_value).c_str(), "no_name", t_string);
		else if( rhs.m_type == t_text )
			return TaskDataElement((m_value + rhs.m_value).c_str(), "no_name", t_text);
		else if( rhs.m_type == t_bool )
		{
			char s[256];
			sprintf(s,"incompatible types (%s+%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
		else
		{
			std::string stVal;
			                                                         
			                                                   
			                                                                      
			data_type resulting_type = m_type>rhs.m_type?m_type:rhs.m_type;
			double dRes = (rhs.m_value.c_str()?atof(rhs.m_value.c_str()):0) + (m_value.c_str()?atof(m_value.c_str()):0);
			                                                                                      
			if( resulting_type == t_int || resulting_type == t_long )
				stVal = toString((int)dRes);
			else
				stVal = toString(dRes);
			return TaskDataElement(stVal.c_str(),	"no_name", resulting_type);
		}
	case t_bool: return TaskDataElement();            
	default:
		{
			char s[256];
			sprintf(s,"incompatible types (%s+%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
	}
}

  
                        
  
TaskDataElement TaskDataElement::operator * (const TaskDataElement& rhs) const
{
	switch( m_type )
	{
	case t_variant:
	case t_string: return TaskDataElement((m_value + '*' + rhs.m_value).c_str(), "no_name", t_string);
	case t_text: return TaskDataElement((m_value + '*' + rhs.m_value).c_str(), "no_name", t_text);
	case t_float:
	case t_double:
	case t_single:
	case t_long:
	case t_int:
		if( rhs.m_type == t_string || rhs.m_type == t_variant )
			return TaskDataElement((m_value + '*' + rhs.m_value).c_str(), "no_name", t_string);
		else if( rhs.m_type == t_text )
			return TaskDataElement((m_value + '*' + rhs.m_value).c_str(), "no_name", t_text);
		else if( rhs.m_type == t_bool )
		{
			char s[256];
			sprintf(s,"incompatible types (%s*%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
		else
		{
			                                                         
			                                                   
			                                                                      
			data_type resulting_type = m_type>rhs.m_type?m_type:rhs.m_type;
			double dRes = (rhs.m_value.c_str()?atof(rhs.m_value.c_str()):0)*(m_value.c_str()?atof(m_value.c_str()):0);

			                                                                                      
			std::string stVal;
			if( resulting_type == t_int || resulting_type == t_long )
				stVal = toString((int)dRes);
			else
				stVal = toString(dRes);
			return TaskDataElement(stVal.c_str(), "no_name", resulting_type);
		}
	case t_bool: return TaskDataElement();            
	default:
		{
			char s[256];
			sprintf(s,"incompatible types (%s*%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
	}
}

  
                  
  
TaskDataElement TaskDataElement::operator / (const TaskDataElement& rhs) const
{
	switch( m_type )
	{
	case t_variant:
	case t_string: return TaskDataElement((m_value + '/' + rhs.m_value).c_str(), "no_name", t_string);
	case t_text: return TaskDataElement((m_value + '/' + rhs.m_value).c_str(), "no_name", t_text);
	case t_float:
	case t_double:
	case t_single:
	case t_long:
	case t_int:
		if( rhs.m_type == t_string || rhs.m_type == t_variant )
			return TaskDataElement((m_value + '/' + rhs.m_value).c_str(), "no_name", t_string);
		if( rhs.m_type == t_text )
			return TaskDataElement((m_value + '/' + rhs.m_value).c_str(), "no_name", t_text);
		else if( rhs.m_type == t_bool )
		{
			char s[256];
			sprintf(s,"incompatible types (%s/%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
		else
		{
			double doub = rhs.m_value.c_str()?atof(rhs.m_value.c_str()):0;
			if( !doub )
			{
				char err_str[256];
				sprintf(err_str,"divide by zero; \"%s\"/\"%s\"",
					m_value.c_str()?m_value.c_str():"",rhs.m_value.c_str()?rhs.m_value.c_str():"");
				throw err_str; 
			}
			double d = m_value.c_str() ? atof(m_value.c_str()) : 0;

			data_type resulting_type = m_type>rhs.m_type?m_type:rhs.m_type;
			double dRes = d/doub;
			                                                                                      
			std::string stVal;
			if( resulting_type == t_int || resulting_type == t_long )
				stVal = toString((int)dRes);
			else
				stVal = toString(dRes);
			return TaskDataElement(stVal.c_str(), "no_name", resulting_type);
		}
	case t_bool: return TaskDataElement();             
	default:
		{
			char s[256];
			sprintf(s,"incompatible types (%s/%s)",data_type_str[m_type],data_type_str[rhs.m_type]);
			throw s;
		}
	}
}

  
              
  
TaskDataElement TaskDataElement::operator - () const
{
	switch( m_type )
	{
	case t_text:
	case t_string: throw "unary '-' not defined for strings";
	case t_float:
	case t_double:
	case t_single:
	case t_long:
	case t_int:	{
		char tmp[64];
		sprintf(tmp,"%.14g",-(m_value.c_str()?atof(m_value.c_str()):0));
		TaskDataElement tde(*this);
		tde.m_value = tmp;
		return tde;
		}
	case t_bool:  throw "unary '-' not defined for boolean type";
	default:
		{
			char s[256];
			sprintf(s,"incompatible type ((unary)-%s)",data_type_str[m_type]);
			throw s;
		}
	}
}

TaskDataElement::operator double () const
{
	return atof(m_value.c_str());
}

                         
TaskDataElement& TaskDataElement::operator = (double value)
{
	m_type = t_double;
	m_value = toString(value);
	                                                                                                                       
	return *this;
}

