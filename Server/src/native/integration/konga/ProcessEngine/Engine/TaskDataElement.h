/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

                                                              
  
                                                                      

#ifndef TASKDATAELEMENT_H
#define TASKDATAELEMENT_H

#include <string>

#include <konga/Utils/StringUtils.h>	             

enum data_type { t_undefined=0, t_bool, t_int, t_long, t_single, t_float, t_double, t_string, t_variant, t_date, t_text };
extern const char* data_type_str[];

class TaskDataElement  
{
public:

	explicit TaskDataElement(const char *value="-1", const char *name="no_name",
		data_type type=t_undefined, int id=0, bool bIsNull = false) :
		m_type(type), m_id(id), m_bIsNull(bIsNull) 
		{
			if( !name ) m_name = "-1";
			else m_name = name;
			char buf[128];	                                                          
			switch(type) {	                                                           
			case t_int:		                                                     
			case t_long:
				if( !value ) m_value = "-1";
				else { sprintf(buf,"%d",atoi(value)); m_value = buf; }
				break;
			case t_float:
			case t_single:
			case t_double:
				if( !value ) m_value = "-1";
				else { sprintf(buf,"%.14g",atof(value)); m_value = buf; }
				break;
			case t_string:
			case t_text:
				if( !value ) m_value = "";
				else m_value = value;
				break;
			default:
				m_value = value;
			}
		}

	TaskDataElement(const TaskDataElement& t)
	{ 
		m_value = t.m_value;
		m_name = t.m_name;
		m_type = t.m_type;
		m_id = t.m_id;
	} 

	explicit TaskDataElement(double value)
	{
		m_type = t_double;
		char tmp[100];
		sprintf(tmp,"%.14g",value);
		m_value = tmp;
		m_name = "no_name";
		m_id = 0;
		                                                                                                                                    
	}

	               
	explicit TaskDataElement(long value)
	{
		m_type = t_long;
		char tmp[100];
		sprintf(tmp,"%ld",value);
		m_value = tmp;
		m_name = "no_name";
		m_id = 0;
		                                                                                                                                  
	}

	explicit TaskDataElement(bool value)
	{
		m_type = t_bool;
		m_value = konga::toString(value?1:0);
		m_name = "no_name";
		m_id = 0;
		                                                                                                                                  
	}
	                                                


	~TaskDataElement() {}

	TaskDataElement& operator = (const TaskDataElement& rhs)
	{ 
		                                                                                                               
		if( &rhs == this ) return *this;

		m_type = rhs.m_type;
		char tmp[64];
		                                                                                      
		if( m_type == t_int || m_type == t_long )
		{
			sprintf(tmp,"%d", atoi(rhs.m_value.c_str()));
			m_value = tmp;
		}
		else
			m_value = rhs.m_value;

		m_name = rhs.m_name;
		m_id = rhs.m_id;
		return *this; 
	}


	                                    
	                    
	                 
	                                         
	         
	                                                              
	                                         

	                                                   

	                         
	TaskDataElement& operator = (double value);
	bool operator == (const TaskDataElement& rhs) const;
	inline bool operator != (const TaskDataElement& rhs) const { return !operator==(rhs); }
	bool operator <= (const TaskDataElement& rhs) const;
	bool operator >= (const TaskDataElement& rhs) const;
	bool operator > (const TaskDataElement& rhs) const;
	bool operator < (const TaskDataElement& rhs) const;
	inline bool operator && (const TaskDataElement& rhs) const { return is_true() && rhs.is_true(); }
	inline bool operator || (const TaskDataElement& rhs) const { return is_true() || rhs.is_true(); }

	TaskDataElement operator + (const TaskDataElement& t) const;
	inline TaskDataElement operator - (const TaskDataElement& rhs) const { return operator + ( -rhs ); }
	TaskDataElement operator * (const TaskDataElement& t) const;
	TaskDataElement operator / (const TaskDataElement& t) const;
	TaskDataElement operator - () const;
	inline bool operator ! () const { return !is_true(); }

	operator double () const;

                                                           
                                                           
                                                           
                                                           

	bool is_true() const;
	bool is_null() const { return m_bIsNull; }
	void set_null(bool bIsNull) { m_bIsNull = bIsNull; }

	bool IsUndefined() const { return m_id == -1 || m_type == t_undefined; }
	int GetId() const { return m_id; };
	int GetType() const { return m_type; };

	std::string m_value;	                                     
	std::string m_name;		                                
	data_type m_type;		                              
	int m_id;				                                                                    
	bool m_bIsNull;			                                                                
};

#endif                     

