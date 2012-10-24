package com.zoostudio.exception;

public class ZooException {
	public class JSON{
		public final static int JSON_PARSE_ERROR = 101;
		public final static String JSON_PARSE_DESC = "Lỗi parse JSON";
		public static final int ERROR_UPLOAD_IMAGE = 102; 
		public class ADD_DISH{
			public static final int ERROR_ADD_DISH = 103;
		}
	}
	public class NETWORK {
		public final static int NETWORK_ERROR = 202;
		public final static String NETWORK_ERROR_DESC = "Không thể kết nối lên server";
		public static final int UN_AUTH = 203;
		public static final int RELOGIN = 204;
		public static final int NO_INTERNET = 205;
		public static final int CANT_GET_DATA = 206;
	}
}
