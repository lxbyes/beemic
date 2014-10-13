
package com.beemic.common;

/**
 * 调用Speex库的接口
 * <p>通过JNI调用C/C++库Speex，对底层进行了封装
 * @author Kevin
 * @version 2014-04-04
 */
public class Speex  {

	/* quality
	 * 1 : 4kbps (very noticeable artifacts, usually intelligible)
	 * 2 : 6kbps (very noticeable artifacts, good intelligibility)
	 * 4 : 8kbps (noticeable artifacts sometimes)
	 * 6 : 11kpbs (artifacts usually only noticeable with headphones)
	 * 8 : 15kbps (artifacts not usually noticeable)
	 */
	private static final int DEFAULT_COMPRESSION = 8;

	public Speex() {
	}

	public void init() {
		load();	
		open(DEFAULT_COMPRESSION);
	}
	
	private void load() {
		try {
			System.loadLibrary("speex");//加载类库
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public native int open(int compression);
	public native int getFrameSize();
	public native int decode(byte encoded[], short lin[], int size);//解码
	public native int encode(short lin[], int offset, byte encoded[], int size);//编码
	public native void close();
	
}
