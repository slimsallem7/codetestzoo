package com.zoostudio.exception;

public class ZooException {
	public class JSON{
		public final static int JSON_PARSE_ERROR = 101;
		public final static String JSON_PARSE_DESC = "Lỗi parse JSON";
	}
	public class NETWORK {
		public final static int NETWORK_ERROR = 102;
		public final static String NETWORK_ERROR_DESC = "Không thể kết nối lên server";
	}
}
