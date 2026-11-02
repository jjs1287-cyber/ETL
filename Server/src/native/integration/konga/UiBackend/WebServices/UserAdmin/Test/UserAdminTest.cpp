#include <iostream>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>

#include <konga/UiBackend/WebServices/UserAdmin/Impl/UserAdminImpl.h>

#include "../UsersAndGroups.hpp"

#ifdef WIN32
const char* user = "Win_User";
const char* admin = "Win_Admin";
#else
const char* user = "emanuel";
const char* admin = "Admin";
#endif

char* password = "****";

using namespace konga;
using namespace std;

void testGetSet();

void testGetSetChanged();

void testUserInfo();

void testGet();

int main(int argc, char* argv[])
{
                

	testUserInfo();
             

                
                       
}

void testUserInfo() {
	User* userInfo = UserAdminImpl(user, password).GetUserInfo();

	userInfo->Email = allocate_xsd__string("Updated Email");
	UserAdminImpl(user, password).SetUserInfo(userInfo);

	delete userInfo;
}

void testGet() {
	
	UsersAndGroups* result = UserAdminImpl(user, password).GetUsersAndGroups();
	delete result;
}

void testGetSet() {
	                                             
	UsersAndGroups* result = UserAdminImpl(admin, password).GetUsersAndGroups();

	                                            
	int N = result->Users->item.m_Size;
	for( int i=0; i < N; ++i ) {
		if ( result->Users->item.m_Array[i].Password != 0 ) {
			cout << "Password not NULL!!" << endl;
			return;
		}
		result->Users->item.m_Array[i].Password = 0;
	}

	UserAdminImpl(admin, password).SetUsersAndGroups(result);
	delete result;
}

void testGetSetChanged() {

	                             
	UsersAndGroups* result = UserAdminImpl(user, password).GetUsersAndGroups();

	                                            
	int N = result->Users->item.m_Size;
	for( int i=0; i < N; ++i ) {
		if ( result->Users->item.m_Array[i].Password != 0 ) {
			cout << "Password not NULL!!" << endl;
			return;
		}
		result->Users->item.m_Array[i].Password = 0;
	}

	char* newFirstName = "Baloo";
	N = result->Users->item.m_Size;
	for( int i=0; i < N; ++i ) {
		result->Users->item.m_Array[i].Password = 0;
		if ( strcmp(result->Users->item.m_Array[i].Login, "mowgli") == 0 ) {
			string stUserId = result->Users->item.m_Array[i].UserId;
			stUserId.replace(0,1,"X");
			result->Users->item.m_Array[i].UserId = allocate_xsd__string(stUserId);;
			result->Users->item.m_Array[i].FirstName = newFirstName;
			result->Users->item.m_Array[i].Password = password;
		}
	}
  
                                 
                             
                                                                    
                                                        
                                                   
   
  

                                     
                             
                                                                         
                                                            
                   
                                                                            
   
  
   

	UserAdminImpl(admin, password).SetUsersAndGroups(result);

	delete result;
}
