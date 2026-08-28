package com.example.demo.dto.traffic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelInputFeatures {

    private String deviceId;

    // Standard Metrics
    private long packetCount;
    private long byteCount;
    private String timestamp;

    private double forwardPacketRate;
    private double backwardPacketRate;
    private double packetLengthStdDev;
    private int tcpFlagSynCount;
    private int tcpFlagAckCount;
    private double interArrivalTimeMs;

    // Additional Features Required by XGBoost Model
    private double flowDuration;
    private double flowBytesPerSec;
    private double flowPktsPerSec;
    private double pktLenMean;
    private double fwdPktLenMean;
    private double bwdPktLenMean;
    private double flowIatMean;
    private double flowIatStd;
    private double fwdIatMean;
    private int tcpFlagFinCount;
    private int tcpFlagRstCount;
    private int initFwdWinBytes;
    private int initBwdWinBytes;
    private int fwdSegSizeMin;

    // Derived/Engineered Features
    private double synToAckRatio;
    private boolean isHighVolume;

    /**
     * Converts features into a 17-element double array strictly aligned
     * with the XGBoost trained model feature order.
     */
    public double[] toFeatureVector() {
        return new double[] {
                this.flowDuration > 0 ? this.flowDuration : this.interArrivalTimeMs, // 1. Flow Duration
                this.flowBytesPerSec,                                                // 2. Flow Byts/s
                this.flowPktsPerSec > 0 ? this.flowPktsPerSec : (forwardPacketRate + backwardPacketRate), // 3. Flow Pkts/s
                this.pktLenMean,                                                     // 4. Pkt Len Mean
                this.packetLengthStdDev,                                             // 5. Pkt Len Std
                this.fwdPktLenMean,                                                  // 6. Fwd Pkt Len Mean
                this.bwdPktLenMean,                                                  // 7. Bwd Pkt Len Mean
                this.flowIatMean > 0 ? this.flowIatMean : this.interArrivalTimeMs,   // 8. Flow IAT Mean
                this.flowIatStd,                                                     // 9. Flow IAT Std
                this.fwdIatMean > 0 ? this.fwdIatMean : this.interArrivalTimeMs,     // 10. Fwd IAT Mean
                (double) this.tcpFlagSynCount,                                       // 11. SYN Flag Cnt
                (double) this.tcpFlagAckCount,                                       // 12. ACK Flag Cnt
                (double) this.tcpFlagFinCount,                                       // 13. FIN Flag Cnt
                (double) this.tcpFlagRstCount,                                       // 14. RST Flag Cnt
                (double) this.initFwdWinBytes,                                       // 15. Init Fwd Win Byts
                (double) this.initBwdWinBytes,                                       // 16. Init Bwd Win Byts
                (double) this.fwdSegSizeMin                                          // 17. Fwd Seg Size Min
        };
    }
}

/*package com.example.demo.dto.traffic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelInputFeatures {
    private String deviceId;

    // Added Packet, Byte, and Duration metrics
    private long packetCount;
    private long byteCount;
    private String timestamp;

    private double forwardPacketRate;
    private double backwardPacketRate;
    private double packetLengthStdDev;
    private int tcpFlagSynCount;
    private int tcpFlagAckCount;
    private double interArrivalTimeMs;

    // Derived/Engineered Features
    private double synToAckRatio;
    private boolean isHighVolume;


    public double[] toFeatureVector() {
        return new double[] {
                (double) packetCount,
                (double) byteCount,
                forwardPacketRate,
                backwardPacketRate,
                packetLengthStdDev,
                (double) tcpFlagSynCount,
                (double) tcpFlagAckCount,
                interArrivalTimeMs,
                synToAckRatio
        };
    }
}   */