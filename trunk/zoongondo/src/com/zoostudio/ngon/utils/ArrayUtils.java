package com.zoostudio.ngon.utils;

import java.util.ArrayList;

public class ArrayUtils {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList copyArray(ArrayList source) {
		ArrayList desArr = new ArrayList();
		for (int i = 0, n = source.size(); i < n; i++) {
			Object src = source.get(i);
			Object des = PipedDeepCopy.copy(src);
			desArr.add(des);
		}
		return desArr;
	}
	
}
