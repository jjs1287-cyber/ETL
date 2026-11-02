/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef FAULT_TOLERANT_OBJECT_H
#define FAULT_TOLERANT_OBJECT_H

#ifdef DS_CLUSTER

#ifdef DS_USE_COM
	#import "../CrMonitorControl.exe" no_namespace
#endif

class FaultTolerantObject
{
public:
	enum CurrentServer { csPRIMARY, csSECONDARY };
	enum FTState { ftNORMAL, ftWAITING, ftDISCONNECTED, ftSWITCHED_INTENTION };

public:
	FaultTolerantObject();
	virtual ~FaultTolerantObject();

	void Init();
	void Cleanup();

	void StartHeartbeat();
	void StopHeartbeat();
	void SendHeartbeat();
	void SetWaitingIntention() { m_bSetWaitingIntention = true; }

	std::string Intention() const { return m_stIntention; }

	std::string GetCurrentServer() const 
	{ 
		return m_enCurrentServer == csPRIMARY ? m_stPrimaryMachineName : m_stSecondaryMachineName;
	}

protected:
	void DoLoadPrimaryMonitor(const std::string& stPrimaryMachineName);
	void DoLoadSecondaryMonitor(const std::string& stSecondaryMachineName);

	AppHealthStatus SendHeartbeatToPrimary();
	AppHealthStatus SendHeartbeatToSecondary();

	void LoadPrimaryMonitor(const std::string& stPrimaryMachineName);
	void LoadSecondaryMonitor(const std::string& stSecondaryMachineName);

	bool IsPrimaryTheCurrentServer() const;
	bool IsSecondaryTheCurrentServer() const;

	void AbandonAllJobs();

	void RegisterIntentionToSwitchServers();
	void SwitchServers();
	void EnterWaitingState();

private:
	bool m_bSetWaitingIntention;
	int m_iHeartbeatTimerInterval;
	UINT m_iHeartbeatTimerId;
	FTState m_ftState;

	CurrentServer m_enCurrentServer;

	std::string m_stPrimaryMachineName, m_stSecondaryMachineName;
	std::string m_stIntention;

	#ifdef DS_USE_COM
	ICrMonitorControlPtr m_PrimaryMonitor;
	ICrMonitorControlPtr m_SecondaryMonitor;
	#endif              
};

#endif              

#endif                           

