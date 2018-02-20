/*
 * Copyright (c) 2010, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  Note:  In order to comply with the binary form redistribution
 *         requirement in the above license, the licensee may include
 *         a URL reference to a copy of the required copyright notice,
 *         the list of conditions and the disclaimer in a human readable
 *         file with the binary form of the code that is subject to the
 *         above license.  For example, such file could be put on a
 *         Blu-ray disc containing the binary form of the code or could
 *         be put in a JAR file that is broadcast via a digital television
 *         broadcast medium.  In any event, you must include in any end
 *         user licenses governing any code that includes the code subject
 *         to the above license (in source and/or binary form) a disclaimer
 *         that is at least as protective of Sun as the disclaimers in the
 *         above license.
 *
 *         A copy of the required copyright notice, the list of conditions and
 *         the disclaimer will be maintained at
 *         https://hdcookbook.dev.java.net/misc/license.html .
 *         Thus, licensees may comply with the binary form redistribution
 *         requirement with a text file that contains the following text:
 *
 *             A copy of the license(s) governing this code is located
 *             at https://hdcookbook.dev.java.net/misc/license.html
 */
package net.java.bd.tools.clipinf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author ggeorg
 */
@XmlType(propOrder = {"clipStreamType", "applicationType", "isAtcDelta", "tsRecordingRate", "numSourcePackets",
        "tsTypeLen", "tsTypeValidity", "tsTypeInfoFormatId", "tsTypeInfoNetwork", "tsTypeInfoStream",
        "atcDeltaNum", "atcDelta", "fontsNum", "fonts"})
public class ClipInfo {

    private byte clipStreamType;
    private byte applicationType;
    private int isAtcDelta; // TODO convert to boolean
    private int tsRecordingRate; // in 10KHz
    private int numSourcePackets;
    private short tsTypeLen;
    private byte tsTypeValidity;
    private String tsTypeInfoFormatId;
    private byte[] tsTypeInfoNetwork = new byte[9];
    private byte[] tsTypeInfoStream = new byte[16];
    private byte atcDeltaNum;
    private byte[] atcDelta = null;
    private byte fontsNum;
    private byte[] fonts = null;

    public ClipInfo() {
        // Nothing to do here!
    }

    public byte getClipStreamType() {
        return clipStreamType;
    }

    public void setClipStreamType(byte clipStreamType) {
        this.clipStreamType = clipStreamType;
    }

    public byte getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(byte applicationType) {
        this.applicationType = applicationType;
    }

    public int getIsAtcDelta() {
        return isAtcDelta;
    }

    public void setIsAtcDelta(int isAtcDelta) {
        this.isAtcDelta = isAtcDelta;
    }

    public int getTsRecordingRate() {
        return tsRecordingRate;
    }

    public void setTsRecordingRate(int tsRecordingRate) {
        this.tsRecordingRate = tsRecordingRate;
    }

    public int getNumSourcePackets() {
        return numSourcePackets;
    }

    public void setNumSourcePackets(int numSourcePackets) {
        this.numSourcePackets = numSourcePackets;
    }

    public short getTsTypeLen() {
        return tsTypeLen;
    }

    public void setTsTypeLen(short tsTypeLen) {
        this.tsTypeLen = tsTypeLen;
    }

    public byte getTsTypeValidity() {
        return tsTypeValidity;
    }

    public void setTsTypeValidity(byte tsTypeValidity) {
        this.tsTypeValidity = tsTypeValidity;
    }

    public byte[] getTsTypeInfoNetwork() {
        return tsTypeInfoNetwork;
    }

    public void setTsTypeInfoNetwork(byte[] tsTypeInfoNetwork) {
        this.tsTypeInfoNetwork = tsTypeInfoNetwork;
    }

    public byte[] getTsTypeInfoStream() {
        return tsTypeInfoStream;
    }

    public void setTsTypeInfoStream(byte[] tsTypeInfoStream) {
        this.tsTypeInfoStream = tsTypeInfoStream;
    }

    public String getTsTypeInfoFormatId() {
        return tsTypeInfoFormatId;
    }

    public void setTsTypeInfoFormatId(String tsTypeInfoFormatId) {
        this.tsTypeInfoFormatId = tsTypeInfoFormatId;
    }

    public byte getAtcDeltaNum() {
        return atcDeltaNum;
    }

    public void setAtcDeltaNum(byte AtcDeltaNum) {
        this.atcDeltaNum = AtcDeltaNum;
    }

    public byte[] getAtcDelta() {
        return atcDelta;
    }

    public void setAtcDelta(byte[] AtcDelta) {
        this.atcDelta = AtcDelta;
    }

    public byte getFontsNum() {
        return fontsNum;
    }

    public void setFontsNum(byte fontsNum) {
        this.fontsNum = fontsNum;
    }

    public byte[] getFonts() {
        return fonts;
    }

    public void setFonts(byte[] fonts) {
        this.fonts = fonts;
    }

    public void readObject(DataInputStream din) throws IOException {
        // 32 bit length
        // 16 bit reserved_for_future_use
        // 8 bit clip_stream_type
        //
        // 4 bit reserved_for_word_align
        // 4 bit application_type
        //
        // 31 bit reserved_for_word_align
        // 1 bit is_atc_delta
        //
        // 32 bit ts_recording_rate (in 10KHz)
        // 32 bit num_source_packets
        //
        // 1016 bit reserved for future use
        //
        // 32 bit ???

        int length = din.readInt();
        System.out.println("ClipInfo length=" + length);

        din.skipBytes(2); // 16 bit reserved

        clipStreamType = din.readByte();
        System.out.println("ClipInfo clipStreamType=" + clipStreamType);

        applicationType = din.readByte();
        applicationType &= 0x0F;
        System.out.println("ClipInfo applicationType=" + applicationType);

        isAtcDelta = din.readInt();
        isAtcDelta &= 0x01;
        System.out.println("ClipInfo isAtcDelta=" + isAtcDelta);

        tsRecordingRate = din.readInt();
        System.out.println("ClipInfo tsRecordingRate=" + (tsRecordingRate / 10000) + "seconds");

        numSourcePackets = din.readInt();
        System.out.println("ClipInfo numSourcePackets=" + numSourcePackets);

        din.skipBytes(128); // reserved

        // ts type info block
        tsTypeLen = din.readShort();
        tsTypeValidity = din.readByte();
        tsTypeInfoFormatId = StringIOHelper.readISO646String(din, 4);
        System.out.println("ClipInfo tsTypeInfoFormatId=" + tsTypeInfoFormatId);
        din.read(tsTypeInfoNetwork, 0, 9);
        din.read(tsTypeInfoStream, 0, 16);

        // TODO: parse AtcDelta
        if (isAtcDelta == 1){
            din.skipBytes(1);
            atcDeltaNum = din.readByte();
            int atcDeltaLen = (int)atcDeltaNum * 14;
            atcDelta = new byte[atcDeltaLen];
            din.read(atcDelta, 0, atcDeltaLen);
        }

        // TODO: parse Fonts
        if (applicationType == 6){
            din.skipBytes(1);
            fontsNum = din.readByte();
            int fontsLen = (int)fontsNum * 6;
            fonts = new byte[fontsLen];
            din.read(fonts, 0, fontsLen);
        }
    }

    public void writeObject(DataOutputStream out) throws IOException {
        int len = 176;
        if (isAtcDelta == 1)
            len = len + 2 + (int)atcDeltaNum * 14;
        if (applicationType == 6)
            len = len + 2 + (int)fontsNum * 14;
        out.writeInt(len);  // length (0x000000b0)

        for (int i = 0; i < 2; i++) {
            out.write(0);    // 8 bit zero
        }

        out.write(getClipStreamType());
        out.write(getApplicationType());

        out.writeInt(getIsAtcDelta() & 0x01);

        out.writeInt(getTsRecordingRate());
        out.writeInt(getNumSourcePackets());

        for (int i = 0; i < 128; i++) { // reserved
            out.write(0);
        }

        // ts type info block
        out.writeShort(tsTypeLen);
        out.writeByte(tsTypeValidity);
        out.write(StringIOHelper.getISO646Bytes(this.getTsTypeInfoFormatId()));
        out.write(tsTypeInfoNetwork, 0, 9);
        out.write(tsTypeInfoStream, 0, 16);

        // TODO: parse AtcDelta
        if (isAtcDelta == 1){
            out.write(0);    // 8 bit zero
            out.write(atcDeltaNum);  //out.writeByte(atcDeltaNum);
            out.write(atcDelta, 0, (int)atcDeltaNum * 14);
        }

        // TODO: parse Fonts
        if (applicationType == 6){
            out.write(0);    // 8 bit zero
            out.write(fontsNum);  //out.writeByte(atcDeltaNum);
            out.write(fonts, 0, (int)fontsNum * 6);
        }

        System.out.println("ClipInfo: " + 176 + " ?=" + (out.size() - 4));
    }
}
