package com.updatecontrols.correspondence.memory;

public class PeerRecord {
	public String protocolName;
	public String peerName;
	public int peerId;

	public PeerRecord(String protocolName, String peerName, int peerId) {
		this.protocolName = protocolName;
		this.peerName = peerName;
		this.peerId = peerId;
	}
}