// src/components/traffic/TrafficStream.jsx
import React, { useState, useEffect } from 'react';
import { fetchTrafficData } from '../../services/sdnApi';

export default function TrafficStream() {
    const [trafficLogs, setTrafficLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isConnected, setIsConnected] = useState(true);

    useEffect(() => {
        const interval = setInterval(async () => {
            try {
                const data = await fetchTrafficData();
                
                const enrichedData = {
                    ...data,
                    id: Date.now(),
                    timestamp: new Date().toLocaleTimeString(),
                    threatLevel: data.tcpFlagSynCount > 35 ? "ATTACK_DETECTED" : "NORMAL"
                };

                setTrafficLogs(prev => [enrichedData, ...prev.slice(0, 19)]);
                setLoading(false);
                setIsConnected(true);
            } catch (error) {
                setIsConnected(false);
            }
        }, 2000);

        return () => clearInterval(interval);
    }, []);

    return (
        <div style={{ background: '#0a0e1a', color: '#e2e8f0', padding: '24px', borderRadius: '12px', fontFamily: 'Inter, sans-serif', border: '1px solid #1f2937' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                <h3 style={{ margin: 0, fontSize: '18px', fontWeight: 600, color: '#e2e8f0' }}>Live SDN Traffic & ML Feature Stream</h3>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '13px', fontFamily: 'JetBrains Mono, monospace' }}>
                    <span style={{ height: '8px', width: '8px', borderRadius: '50%', backgroundColor: isConnected ? '#22d3ee' : '#dc2626', display: 'inline-block' }}></span>
                    <span style={{ color: '#8b95a7' }}>{isConnected ? "Connected" : "Disconnected"}</span>
                </div>
            </div>

            <div style={{ overflowX: 'auto' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '13px', fontFamily: 'JetBrains Mono, monospace' }}>
                    <thead>
                        <tr style={{ borderBottom: '1px solid #1f2937', color: '#8b95a7', fontSize: '12px' }}>
                            <th style={{ padding: '12px' }}>TIME</th>
                            <th style={{ padding: '12px' }}>DEVICE</th>
                            <th style={{ padding: '12px' }}>PKT RATE</th>
                            <th style={{ padding: '12px' }}>SYN COUNT</th>
                            <th style={{ padding: '12px' }}>SYN/ACK RATIO</th>
                            <th style={{ padding: '12px' }}>ML FEATURE VECTOR</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr><td colSpan="6" style={{ padding: '24px', textAlign: 'center', color: '#8b95a7' }}>Waiting for traffic stream...</td></tr>
                        ) : (
                            trafficLogs.map((log) => {
                                // Array mapping matching ModelInputFeatures.toFeatureVector()
                                const featureVectorArray = [
                                    log.packetCount || 0,
                                    log.byteCount || 0,
                                    log.forwardPacketRate || 0,
                                    log.backwardPacketRate || 0,
                                    log.packetLengthStdDev || 0,
                                    log.tcpFlagSynCount || 0,
                                    log.tcpFlagAckCount || 0,
                                    log.interArrivalTimeMs || 0,
                                    log.synToAckRatio || 0
                                ];

                                return (
                                    <tr key={log.id} style={{ borderBottom: '1px solid #1f2937' }}>
                                        <td style={{ padding: '12px', color: '#8b95a7' }}>{log.timestamp}</td>
                                        <td style={{ padding: '12px', color: '#3b82f6', fontWeight: 600 }}>{log.deviceId}</td>
                                        <td style={{ padding: '12px' }}>{log.forwardPacketRate} p/s</td>
                                        <td style={{ padding: '12px' }}>{log.tcpFlagSynCount}</td>
                                        <td style={{ padding: '12px', color: '#22d3ee' }}>{log.synToAckRatio?.toFixed(1)}</td>
                                        <td style={{ padding: '12px', fontFamily: 'monospace', fontSize: '11px', color: '#8b95a7' }}>
                                            [{featureVectorArray.map(v => Number(v).toFixed(1)).join(', ')}]
                                        </td>
                                    </tr>
                                );
                            })
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
/*/ src/components/traffic/TrafficStream.jsx
/import React, { useState, useEffect } from 'react';
import { fetchTrafficData } from '../../services/sdnApi';

export default function TrafficStream() {
    const [trafficLogs, setTrafficLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isConnected, setIsConnected] = useState(true);

    useEffect(() => {
        const interval = setInterval(async () => {
            try {
                const data = await fetchTrafficData();

                const enrichedData = {
                    ...data,
                    id: Date.now(),
                    timestamp: new Date().toLocaleTimeString(),
                    threatLevel: data.tcpFlagSynCount > 40 ? "ATTACK_DETECTED" : data.tcpFlagSynCount > 25 ? "HIGH_VOLUME" : "NORMAL"
                };

                setTrafficLogs(prev => [enrichedData, ...prev.slice(0, 19)]);
                setLoading(false);
                setIsConnected(true);
            } catch (error) {
                setIsConnected(false);
            }
        }, 2000);

        return () => clearInterval(interval);
    }, []);

    return (
        <div style={{ background: '#0a0e1a', color: '#e2e8f0', padding: '24px', borderRadius: '12px', fontFamily: 'Inter, sans-serif', border: '1px solid #1f2937' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                <h3 style={{ margin: 0, fontSize: '18px', fontWeight: 600, color: '#e2e8f0' }}>Live Network Telemetry Stream</h3>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '13px', fontFamily: 'JetBrains Mono, monospace' }}>
                    <span style={{
                        height: '8px',
                        width: '8px',
                        borderRadius: '50%',
                        backgroundColor: isConnected ? '#22d3ee' : '#dc2626',
                        boxShadow: isConnected ? '0 0 8px #22d3ee' : 'none',
                        display: 'inline-block'
                    }}></span>
                    <span style={{ color: '#8b95a7' }}>{isConnected ? "Kafka Stream Connected" : "Disconnected / Reconnecting..."}</span>
                </div>
            </div>

            <div style={{ overflowX: 'auto' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '13px', fontFamily: 'JetBrains Mono, monospace' }}>
                    <thead>
                        <tr style={{ borderBottom: '1px solid #1f2937', color: '#8b95a7', fontSize: '12px' }}>
                            <th style={{ padding: '12px' }}>TIME</th>
                            <th style={{ padding: '12px' }}>DEVICE</th>
                            <th style={{ padding: '12px' }}>SOURCE IP → DEST IP</th>
                            <th style={{ padding: '12px' }}>PKT RATE</th>
                            <th style={{ padding: '12px' }}>SYN COUNT</th>
                            <th style={{ padding: '12px' }}>STATUS</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr>
                                <td colSpan="6" style={{ padding: '24px', textAlign: 'center', color: '#8b95a7' }}>
                                    Listening for Mininet telemetry packets...
                                </td>
                            </tr>
                        ) : trafficLogs.length === 0 ? (
                            <tr>
                                <td colSpan="6" style={{ padding: '24px', textAlign: 'center', color: '#8b95a7' }}>
                                    No traffic events captured yet.
                                </td>
                            </tr>
                        ) : (
                            trafficLogs.map((log) => {
                                let rowBackground = 'transparent';
                                let badgeColor = '#15803d';
                                let badgeBg = 'rgba(21, 128, 61, 0.15)';

                                if (log.threatLevel === 'ATTACK_DETECTED') {
                                    rowBackground = 'rgba(220, 38, 38, 0.08)';
                                    badgeColor = '#dc2626';
                                    badgeBg = 'rgba(220, 38, 38, 0.2)';
                                } else if (log.threatLevel === 'HIGH_VOLUME') {
                                    rowBackground = 'rgba(245, 158, 11, 0.06)';
                                    badgeColor = '#f59e0b';
                                    badgeBg = 'rgba(245, 158, 11, 0.2)';
                                }

                                return (
                                    <tr key={log.id} style={{ borderBottom: '1px solid #1f2937', backgroundColor: rowBackground }}>
                                        <td style={{ padding: '12px', color: '#8b95a7' }}>{log.timestamp}</td>
                                        <td style={{ padding: '12px', fontWeight: 500, color: '#3b82f6' }}>{log.deviceId || 'device-1'}</td>
                                        <td style={{ padding: '12px' }}>{log.sourceIp} → {log.destinationIp}</td>
                                        <td style={{ padding: '12px' }}>{log.forwardPacketRate} p/s</td>
                                        <td style={{ padding: '12px' }}>{log.tcpFlagSynCount}</td>
                                        <td style={{ padding: '12px' }}>
                                            <span style={{
                                                padding: '4px 8px',
                                                borderRadius: '4px',
                                                fontSize: '11px',
                                                fontWeight: 600,
                                                color: badgeColor,
                                                backgroundColor: badgeBg
                                            }}>
                                                {log.threatLevel}
                                            </span>
                                        </td>
                                    </tr>
                                );
                            })
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
*/