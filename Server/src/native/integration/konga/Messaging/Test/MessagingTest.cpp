/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include "stdafx.h"

#include <iostream>

#include <konga/Utils/CrException.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Messaging/MessageSender.h>
#include <konga/Messaging/ProcessMessage.h>
#include <konga/Messaging/CheckOperationQueue.h>
#include <konga/Messaging/OperationMessageUtils.h>

using namespace konga;
using namespace std;

int testProcessMessage();

int testGetOperationQueue();

int testWaitForOperation();

int testAddOperationToQueue();

int main(int argc, char* argv[])
{
	return testAddOperationToQueue();

                                 
                                  
                               
}

int testAddOperationToQueue() {

	try {
		konga::AddOperationToQueue(427, 1010, UNDEFINED_OPERATION_INSTANCE_ID, "Emanuel", "Testing");
	}
	catch ( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}

int testWaitForOperation() {

	try {
		UserInfo user(0);
		OPERATION_QUEUE_WAIT_RESULT res = konga::WaitForOperation(user, 48, 10, 100, 0, 0);
		switch(res) {
			case OPERATION_QUEUE_WAIT_RESULT_DONE:
				cout << "Operation has exited." << endl;
				break;
			case OPERATION_QUEUE_WAIT_RESULT_NOT_THERE:
				cout << "The operation was not in the queue." << endl;
				break;
			case OPERATION_QUEUE_WAIT_RESULT_TIME_OUT:
				cout << "Wait timed out." << endl;
				break;

		};
		if ( res == OPERATION_QUEUE_WAIT_RESULT_TIME_OUT ) {
			cout << "Wait timed out" << endl;
		}
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testGetOperationQueue() {

	try {

		UserInfo user(0);

		vector<OperationQueueEntry> queue = konga::GetOperationQueue(user, -1, 0, 0);

		for( vector<OperationQueueEntry>::const_iterator cit = queue.begin(); cit != queue.end(); ++cit) {
			const OperationQueueEntry& entry = *cit;
			cout << "Entry:" << endl << entry.getMsgId() << " " << entry.getOperationId() << " " << entry.isExecuting() << " " << entry.receivedAt().toString("yyyy-mm-dd HH:MM:SS") << endl;
		}
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testProcessMessage() {

	try {
		ProcessMessage msg;
		msg.SetFrom("MessagingTest");
		msg.SetProcessName("Buddy Holly");
		msg.SetSubject("Subject");
		msg.SetTaskName("Task Name");
		msg.SetTo("Bjorne");

		msg.AddDataElement("name", "value", DET_STRING);

		MessageSender sender;
		sender.AddMessageToQueue(msg);
	}
	catch ( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}
