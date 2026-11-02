# SSH test fixtures

The test_ed25519* private and public keys are intentionally versioned regression fixtures, authorized by the project owner. They cover encrypted and unencrypted Ed25519 key handling. They are test data, not production credentials.

The machine-specific known_hosts file stays local. Live integration tests must use the approved test server/account and its verified host key.
