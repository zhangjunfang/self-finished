package org.tinygroup.nettyremote.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestTime {
	private static Pattern pattern = Pattern.compile("[{](.)*?[}]");

	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			String s = format("连接服务端{0}:{1}发生异常", "127.0.0.1", "1010");
		}
		long time2 = System.currentTimeMillis();
		System.out.println("format:");
		System.out.println(time2 - time1);

		long time3 = System.currentTimeMillis();
		for (int j = 0; j < 1000000; j++) {
			String s1 = "连接服务端" + "1010" + ":" + "127.0.0.1" + "发生异常";
		}
		long time4 = System.currentTimeMillis();
		System.out.println("plus:");
		System.out.println(time4 - time3);

		long time5 = System.currentTimeMillis();
		for (int h = 0; h < 1000000; h++) {
			String s3 = new StringBuilder().append("连接服务端").append("1010")
					.append(":").append("127.0.0.1").append("发生异常").toString();
		}
		long time6 = System.currentTimeMillis();
		System.out.println("StringBuilder:");
		System.out.println(time6 - time5);
	}

	public static String format(String message, Object... args) {
		Matcher matcher = pattern.matcher(message);
		StringBuffer stringBuffer = new StringBuffer();
		int start = 0;
		int count = 0;
		while (matcher.find(start)) {
			stringBuffer.append(message.substring(start, matcher.start()));
			stringBuffer.append(args[count++]);
			start = matcher.end();
			if (count == args.length) {
				break;
			}
		}
		stringBuffer.append(message.substring(start, message.length()));
		return stringBuffer.toString();
	}
}
