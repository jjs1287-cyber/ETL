/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ITEM_ACCESS_ACCESS_CONTROL_H
#define KONGA_ITEM_ACCESS_ACCESS_CONTROL_H

#include <string>

namespace konga {

	class DbConnection;

	   
                                                                
                                                                          
    
	class AccessControl
	{

	public:
		   
                                                          
     
		typedef unsigned int AllowBits;
		typedef unsigned int DenyBits;

		enum AccessBit {
			ACCESS_BIT_EXECUTE = 1,
			ACCESS_BIT_WRITE = 2,
			ACCESS_BIT_READ = 4,
			ACCESS_BIT_OWNER = 8,
			ACCESS_BIT_DELETE = 16,
			ACCESS_BIT_CHANGE_PERMISSIONS = 32
		};

		                                                                                   
		                                        
		enum {
			ACCESS_LEVEL_DENIED = 0,	                                                                                             
			ACCESS_LEVEL_R = 4,
			ACCESS_LEVEL_RX = 5,
			ACCESS_LEVEL_RW = 6,
			ACCESS_LEVEL_RWX = 7,
			ACCESS_LEVEL_OWNER = 8,

			                                                                                    
			ACCESS_LEVEL_ADMINISTRATOR = ACCESS_BIT_EXECUTE + ACCESS_BIT_WRITE + ACCESS_BIT_READ +
			ACCESS_BIT_OWNER + ACCESS_BIT_DELETE + ACCESS_BIT_CHANGE_PERMISSIONS

		};

		   
                                                                    
                                                                  
     
		static void DeleteAccessControlForItem(int iEntityTypeId, long lItemId, DbConnection& dbConnection);

	public:
		AccessControl(DbConnection& dbConnection)
			: m_dbConnection(dbConnection) {}
		
		virtual ~AccessControl(void) {}

		   
                                                                                                          
                                                                                                       
                                                                                                  
                                  
                                                                     
     
		virtual void AddItem(long iItemId, int iEntityTypeId, AllowBits allowBits, DenyBits denyBits = 0);

		   
                                                                                          
                                            
     
		virtual void ChangeItem(long iItemId, int iEntityTypeId, AllowBits allowBits, DenyBits denyBits);

		   
                                                                            
     
		virtual void DeleteItem(long iItemId, int iEntityTypeId);

		                                                       
		virtual void DeleteItem();

		   
                                                                   
                                                                           
     
		virtual void GetPermissions(long iItemId, int iEntityTypeId, AllowBits& allowBits, DenyBits& denyBits) const;

		   
                                                                                      
                                                                                 
     
		virtual bool HasPermission(long iItemId, int iEntityTypeId, AccessBit accessBit) const;

		                      
		bool HasReadPermission(long iItemId, int iEntityTypeId) const {
			return HasPermission(iItemId, iEntityTypeId, ACCESS_BIT_READ);
		}

		                      
		bool HasWritePermission(long iItemId, int iEntityTypeId) const {
			return HasPermission(iItemId, iEntityTypeId, ACCESS_BIT_WRITE);
		}

		                      
		bool HasExecutePermission(long iItemId, int iEntityTypeId) const {
			return HasPermission(iItemId, iEntityTypeId, ACCESS_BIT_EXECUTE);
		}

		bool HasDeletePermission(long iItemId, int iEntityTypeId) const {
			return HasPermission(iItemId, iEntityTypeId, ACCESS_BIT_DELETE);
		}

		bool HasChangePermissionPermission(long iItemId, int iEntityTypeId) const {
			return HasPermission(iItemId, iEntityTypeId, ACCESS_BIT_CHANGE_PERMISSIONS);
		}

	protected:
		   
                                                                  
                         
     
		virtual int GetId() const = 0;

		   
                                                                                 
                                                                                   
                 
     
		virtual bool IsAdministrator() const = 0;

		                                                     
		virtual bool IsAdminUser() const = 0;

		   
                                                      
                         
     
		virtual void VerifyAllowBits(AllowBits allowBits) const = 0;

		virtual void VerifyDenyBits(DenyBits denyBits) const = 0;

		   
                                                 
     
		virtual std::string GetTableName(int iEntityId) const = 0;

		  
                                                            
     
		virtual const std::string& GetIdName() const = 0;

	protected:

		                                                                        
		virtual void DeleteItem(int iEntityId);

		DbConnection& GetDbConnection() const {
			return m_dbConnection;
		}

	private:
		DbConnection& m_dbConnection;
	};

}

#endif                                      

