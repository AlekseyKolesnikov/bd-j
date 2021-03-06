/*  
 * Copyright (c) 2008, Sun Microsystems, Inc.
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


package net.java.bd.tools.playlist;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.xml.bind.annotation.XmlType;

/**
 * BD-ROM Part 3-1 5.3.8 PIP Metadata Block Header.
 */
@XmlType(propOrder={"refToPlayItemID", "refToSecondaryVideoStreamID", "pipTimelineType", "isLumaKey", "isTrickPlaying", "upperLimitLumaKey"})
public class MetadataBlockHeader {
    
        private int refToPlayItemID;
        private int refToSecondaryVideoStreamID;
        private PIPTimelineType pipTimelineType;
        private boolean isLumaKey;
        private boolean isTrickPlaying;
        private Integer upperLimitLumaKey;
        //private long metadataBlockDataStartAddress;
        
    public void readObject(DataInputStream din) throws IOException {
        // 16 bit ref_to_PlayItem_id                            2
        //  8 bit ref_to_secondary_video_stream_id      1
        //  8 bit reserved                                                      1
        //  4 bit pip_timeline_type                                     .
        //        flags()
        //  1 bit     is_luma_key                                       .
        //  1 bit     trick_playing_flag                        .
        // 10 bit reserved_for_word_align                       .
        //        if (is_luma_key)
        //  8 bit reserved                                                      1
        //  8 bit upper_limit_luma_key                          1
        //        else
        // 16 bit reserved                                                      2
        //        endif
        // 16 bit reserved                                                      2
        // 32 bit metadata_block_data_start_addr        4
        setRefToPlayItemID(din.readUnsignedShort());
        setRefToSecondaryVideoStreamID(din.readUnsignedByte());
        din.skipBytes(1);
        int value = din.readUnsignedShort();
        int type = (value & 0xf000) >> 12;
        Enum[] pipTimelineTypes = PIPTimelineType.values();
        for (int i = 0; i < pipTimelineTypes.length; i++) {
            if (pipTimelineTypes[i].ordinal() == type) {
                setPipTimelineType((PIPTimelineType) pipTimelineTypes[i]);
                break;
            }
        }
        setIsLumaKey((value & 0x0800) != 0);
        setIsTrickPlaying((value & 0x0400) != 0);
        value = din.readUnsignedShort();
        if (getIsLumaKey()) {
                setUpperLimitLumaKey(value & 0xff);
        }
        din.skipBytes(2);
        
        // Note - metadata_block_data_start_addr is read by ExtDataBlock.readObject(..) method.
        //byte[] metadataBlockDataStartAddressBytes = new byte[4];
        //din.readFully(metadataBlockDataStartAddressBytes);
        //setMetadataBlockDataStartAddress(UnsignedIntHelper.convertToLong(metadataBlockDataStartAddressBytes));
    }
    
    public void writeObject(DataOutputStream dout) throws IOException {
        dout.writeShort(getRefToPlayItemID());
        dout.writeByte(getRefToSecondaryVideoStreamID());
        dout.writeByte(0);
        int value = getPipTimelineType().ordinal() << 12;
        if (getIsLumaKey()) {
                value |= 0x0800;
        }
        if (getIsTrickPlaying()) {
                value |= 0x0400;
        }
        dout.writeShort(value);
        value = 0;
        if (getIsLumaKey()) {
                value = getUpperLimitLumaKey();
        }
        dout.writeShort(value);
        dout.writeShort(0);
        
        // Note - metadata_block_data_start_addr is written by ExtDataBlock.writeObject(..) method.
        // dout.write(UnsignedIntHelper.convertToBytes(getMetadataBlockDataStartAddress()));
    }
    
    public int getRefToPlayItemID() {
        return refToPlayItemID;
    }
    
    public void setRefToPlayItemID(int refToPlayItemID) {
        this.refToPlayItemID = refToPlayItemID;
    }
    
    public int getRefToSecondaryVideoStreamID() {
        return refToSecondaryVideoStreamID;
    }
    
    public void setRefToSecondaryVideoStreamID(int refToSecondaryVideoStreamID) {
        this.refToSecondaryVideoStreamID = refToSecondaryVideoStreamID;
    }
    
    public PIPTimelineType getPipTimelineType() {
        return pipTimelineType;
    }
    
    public void setPipTimelineType(PIPTimelineType pipTimelineType) {
        this.pipTimelineType = pipTimelineType;
    }
    
    public boolean getIsLumaKey() {
        return isLumaKey;
    }
    
    public void setIsLumaKey(boolean isLumaKey) {
        this.isLumaKey = isLumaKey;
    }
    
    public boolean getIsTrickPlaying() {
        return isTrickPlaying;
    }
    
    public void setIsTrickPlaying(boolean isTrickPlaying) {
        this.isTrickPlaying = isTrickPlaying;
    }
    
    public Integer getUpperLimitLumaKey() {
        return upperLimitLumaKey;
    }
    
    public void setUpperLimitLumaKey(Integer upperLimitLumaKey) {
        this.upperLimitLumaKey = upperLimitLumaKey;
    }
    
    //public long getMetadataBlockDataStartAddress() {
    //  return metadataBlockDataStartAddress;
    //}
    
    //public void setMetadataBlockDataStartAddress(long metadataBlockDataStartAddress) {
    //  this.metadataBlockDataStartAddress = metadataBlockDataStartAddress;
    //}

    
    // PIP Timeline Type
    enum PIPTimelineType {
        reserved,
        SYNCHRONOUS,
        ASYNC_SUBPATH,
        ASYNC_PLAYITEM;

        public byte getEncoding() {
            return (byte) ordinal();
        }
    }
}
