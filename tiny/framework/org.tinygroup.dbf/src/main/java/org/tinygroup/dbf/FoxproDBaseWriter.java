/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.dbf;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * 
 * Created by wcg on 2014/7/7.
 * 
 */
public class FoxproDBaseWriter extends DbfWriter {
    public static final int NAME_LENGTH = 10;
    public static final int RECORD_COUNT_LENGTH = 4;
    public static final int HEADER_LENGTH_LENGTH = 2;
    public static final int RECORD_LENGTH_LENGTH = 2;
    public static final int START_YEAR = 1900;
    int allFieldsLength = 0;
    private void writeField(Field field) throws IOException {
    	byte[] fieldNameBytearr = field.getName().getBytes(getEncode());
    	byte[] destFieldNameBuffer= new byte[NAME_LENGTH];
    	int destlen = fieldNameBytearr.length>NAME_LENGTH?NAME_LENGTH:fieldNameBytearr.length;
    	System.arraycopy(fieldNameBytearr, 0, destFieldNameBuffer, 0, destlen);
    	bodybuffer.write(destFieldNameBuffer);
    	bodybuffer.write(0);
    	bodybuffer.write((byte)field.getType());
    	bodybuffer.write(Util.getByteFromInt(field.getDisplacement(), 4));
    	bodybuffer.write(Util.getByteFromInt(field.getLength(), 1));
    	bodybuffer.write(Util.getByteFromInt(field.getDecimal(), 1));
    	bodybuffer.write(new byte[14]);
    	allFieldsLength += field.getLength();
    }
    
	public void writeFields(List<Field> fieldlist) throws IOException {
		this.setFields(fieldlist);
		for(Field field:fieldlist) {
			writeField(field);
		}
		bodybuffer.write(HEADER_END_CHAR);
	}
	
	protected void writeHeaders() throws IOException {
		headbuffer.write(0X30);
		headbuffer.write(getDateInfoByteArray());
		headbuffer.write(Util.getByteFromInt(getPostion(), RECORD_COUNT_LENGTH)); //4
		headbuffer.write(Util.getByteFromInt(32+32* fields.size()+1, HEADER_LENGTH_LENGTH));//2
		headbuffer.write(Util.getByteFromInt(allFieldsLength, RECORD_LENGTH_LENGTH)); //2
		headbuffer.write(new byte[20]);
	}
	
	public void writeRecord(String... values) throws  IOException , NullPointerException {
		if(fields==null) {
			
			throw new NullPointerException("字段表为空指针,请先调用writeFields方法");
		}
		boolean removed = false;
		int fieldsLength = fields.size();
		int valueLength = values.length;
		if(valueLength!=fieldsLength) {
			if(valueLength-fieldsLength==1) {
				if(!values[0].equals("*")) {
					System.out.println("here");
					throw new IOException(String.format("字段长度为:%d,但该条数据长度为:%d,游标:%d",
							fieldsLength,values.length,getPostion()));
				}
				else {
					removed = true;
				}
			}
			else {
				throw new IOException(String.format("字段长度为:%d,但该条数据长度为:%d,游标:%d",
						fieldsLength,values.length,getPostion()));
			}
	
		}
		next();
		int i = 0;
		if(removed) {
			bodybuffer.write((byte)'*');
			i++;
		}
		for(int j=0;i<valueLength;i++,j++) {
			fillRecord(fields.get(j),values[i]);
		}
	}
	
	private byte[] getDateInfoByteArray() {
		byte[] result = new byte[3];
		Date date = new Date();
		result[0] = Util.getByteFromInt(Integer.parseInt(new SimpleDateFormat("yyyy").format(date))-START_YEAR, 1)[0];
		result[1] = Util.getByteFromInt(Integer.parseInt(new SimpleDateFormat("M").format(date)), 1)[0];
		result[2] = Util.getByteFromInt(Integer.parseInt(new SimpleDateFormat("d").format(date)), 1)[0];
		return result;
	}
	
	private void fillRecord (Field field,String value) throws IOException {
		
		int needFillLength  = field.getLength()-value.length();
		byte[] fillbank = new byte[needFillLength];
		for(int j = 0;j<needFillLength;j++) {
			fillbank[j] = 0x20;
		}
		bodybuffer.write(fillbank);
		bodybuffer.write(value.getBytes(getEncode()));
	}
}
