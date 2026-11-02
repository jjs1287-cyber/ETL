/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#if defined(DS_USE_CORBA)

#include <map>

#if defined(ORBACUS)
	#include <ob/corba.h>
                  
#elif defined(MICO)
	#include <corba.h>
	#define USE_BOA
#elif defined(TAO)
	WINBASEAPI BOOL WINAPI TryEnterCriticalSection(LPCRITICAL_SECTION lpCriticalSection);
	#include <tao/corba.h>
#else
	#include <corba.h>
#endif

#if defined(ORBIX) || defined(TAO) || defined(MICO) || defined(CORBA_NAMESPACE)

	  
	        
	  
	#define CORBA_Boolean CORBA::Boolean
	#define CORBA_Float CORBA::Float
	#define CORBA_Double CORBA::Double
	#define CORBA_String CORBA::String
	#define CORBA_ULong CORBA::ULong
	#define CORBA_Long CORBA::Long
	#define CORBA_Any CORBA::Any

	  
	             
	  
	#define CORBA__tc_long CORBA::_tc_long
	#define CORBA__tc_float CORBA::_tc_float
	#define CORBA__tc_double CORBA::_tc_double
	#define CORBA__tc_boolean CORBA::_tc_boolean
	#define CORBA__tc_string CORBA::_tc_string
	#define CORBA__tc_void CORBA::_tc_void
	#define CORBA_TCKind CORBA::TCKind

	#define CORBA_tk_null CORBA::tk_null
	#define CORBA_tk_void CORBA::tk_void
	#define CORBA_tk_short CORBA::tk_short
	#define CORBA_tk_long CORBA::tk_long
	#define CORBA_tk_ushort CORBA::tk_ushort
	#define CORBA_tk_ulong CORBA::tk_ulong
	#define CORBA_tk_float CORBA::tk_float
	#define CORBA_tk_double CORBA::tk_double
	#define CORBA_tk_boolean CORBA::tk_boolean
	#define CORBA_tk_char CORBA::tk_char
	#define CORBA_tk_octet CORBA::tk_octet
	#define CORBA_tk_any CORBA::tk_any
	#define CORBA_tk_TypeCode CORBA::tk_TypeCode
	#define CORBA_tk_Principal CORBA::tk_Principal
	#define CORBA_tk_string CORBA::tk_string
	#define CORBA_tk_longlong CORBA::tk_longlong
	#define CORBA_tk_ulonglong CORBA::tk_ulonglong
	#define CORBA_tk_longdouble CORBA::tk_longdouble
	#define CORBA_tk_wchar CORBA::tk_wchar
	#define CORBA_tk_wstring CORBA::tk_wstring

	  
	                 
	  
	#define CORBA_dk_all CORBA::dk_all
	#define CORBA_dk_Attribute CORBA::dk_Attribute
	#define CORBA_dk_Constant CORBA::dk_Constant
	#define CORBA_dk_Exception CORBA::dk_Exception
	#define CORBA_dk_Interface CORBA::dk_Interface
	#define CORBA_dk_Module CORBA::dk_Module
	#define CORBA_dk_Operation CORBA::dk_Operation
	#define CORBA_dk_Typedef CORBA::dk_Typedef
	#define CORBA_dk_Alias CORBA::dk_Alias
	#define CORBA_dk_Struct CORBA::dk_Struct
	#define CORBA_dk_Union CORBA::dk_Union
	#define CORBA_dk_Enum CORBA::dk_Enum
	#define CORBA_dk_Primitive CORBA::dk_Primitive
	#define CORBA_dk_String CORBA::dk_String
	#define CORBA_dk_Sequence CORBA::dk_Sequence
	#define CORBA_dk_Array CORBA::dk_Array
	#define CORBA_dk_Repository CORBA::dk_Repository

	  
	            
	   
	#define CORBA_is_nil CORBA::is_nil
	#define CORBA_release CORBA::release
	#define CORBA_string_dup CORBA::string_dup
	#define CORBA_string_free CORBA::string_free

	  
	           
	  
	#define CORBA_Any CORBA::Any
	#define CORBA_ARG_IN CORBA::ARG_IN
	#define CORBA_ARG_OUT CORBA::ARG_OUT
	#define CORBA_ARG_INOUT CORBA::ARG_INOUT

	#define CORBA_BOA_var CORBA::BOA_var
	#define CORBA_BOA_ptr CORBA::BOA_ptr
	#define CORBA_ORB_var CORBA::ORB_var
	#define CORBA_ORB_ptr CORBA::ORB_ptr
	#define CORBA_Object_var CORBA::Object_var
	#define CORBA_Object_ptr CORBA::Object_ptr

	#define CORBA_Environment_ptr CORBA::Environment_ptr
	#define CORBA_Exception CORBA::Exception
	#define CORBA_SystemException CORBA::SystemException
	#define CORBA_NamedValue_ptr CORBA::NamedValue_ptr
	#define CORBA_NVList_ptr CORBA::NVList_ptr
	#define CORBA_ORB_init CORBA::ORB_init
	#define CORBA_PolicyList CORBA::PolicyList
	#define CORBA_Request_ptr CORBA::Request_ptr
	#define CORBA_String_var CORBA::String_var
	#define CORBA_String_ptr CORBA::String_ptr
	#define CORBA_SystemException CORBA::SystemException
	#define CORBA_TypeCode_ptr CORBA::TypeCode_ptr

	  
	                                           
	  
	#define CORBA_Contained CORBA::Contained
	#define CORBA_Contained_var CORBA::Contained_var
	#define CORBA_Contained_ptr CORBA::Contained_ptr
	#define CORBA_ContainedSeq_var CORBA::ContainedSeq_var
	#if defined(MICO)
		#define CORBA_ContainedSeq_ptr CORBA::ContainedSeq*
	#else
		#define CORBA_ContainedSeq_ptr CORBA::ContainedSeq_ptr
	#endif
	#define CORBA_Container CORBA::Container
	#define CORBA_Container_var CORBA::Container_var
	#define CORBA_Container_ptr CORBA::Container_ptr
	#define CORBA_ContainedSeq CORBA::ContainedSeq
	#define CORBA_InterfaceDef CORBA::InterfaceDef
	#define CORBA_InterfaceDef_var CORBA::InterfaceDef_var
	#define CORBA_InterfaceDef_ptr CORBA::InterfaceDef_ptr
	#define CORBA_IRObject CORBA::IRObject
	#define CORBA_IRObject_var CORBA::IRObject_var
	#define CORBA_IRObject_ptr CORBA::IRObject_ptr
	#define CORBA_ModuleDef_var CORBA::ModuleDef_var
	#define CORBA_ModuleDef CORBA::ModuleDef
	#define CORBA_OperationDef CORBA::OperationDef
	#define CORBA_OperationDef_var CORBA::OperationDef_var
	#define CORBA_OperationDef_ptr CORBA::OperationDef_ptr
	#define CORBA_ParDescriptionSeq CORBA::ParDescriptionSeq
	#define CORBA_ParDescriptionSeq_var CORBA::ParDescriptionSeq_var
	#define CORBA_ParDescriptionSeq_ptr CORBA::ParDescriptionSeq_ptr
	#define CORBA_Repository CORBA::Repository
	#define CORBA_Repository_var CORBA::Repository_var
	#define CORBA_Repository_ptr CORBA::Repository_ptr

	                                            
	                                                
	#define CORBA_Identifier char*
	#define CORBA_RepositoryId char*

	#define CORBA_OP_ONEWAY CORBA::OP_ONEWAY
	#define CORBA_PARAM_IN CORBA::PARAM_IN
	#define CORBA_PARAM_OUT CORBA::PARAM_OUT
	#define CORBA_PARAM_INOUT CORBA::PARAM_INOUT

#endif                                                   

extern CORBA_ORB_ptr g_orb;

#if defined(USE_BOA)
	extern CORBA_BOA_ptr g_boa;
#else
	extern PortableServer::POA_ptr g_poa;
#endif

void CORBA_Init();
void CORBA_CleanUp();
std::string CORBA_TypeKindToString(CORBA_TCKind tk);

#endif                         

