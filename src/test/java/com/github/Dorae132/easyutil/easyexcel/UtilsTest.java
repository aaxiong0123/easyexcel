package com.github.Dorae132.easyutil.easyexcel;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.github.Dorae132.easyutil.easyexcel.ExcelProperties;
import com.github.Dorae132.easyutil.easyexcel.ExcelUtils;
import com.github.Dorae132.easyutil.easyexcel.common.Pair;
import com.github.Dorae132.easyutil.easyexcel.export.ExcelCol;
import com.github.Dorae132.easyutil.easyexcel.export.FillSheetModeEnums;
import com.github.Dorae132.easyutil.easyexcel.export.IDataSupplier;
import com.github.Dorae132.easyutil.easyexcel.export.IExcelProcessor;
import com.github.Dorae132.easyutil.easyexcel.read.IReadDoneCallBack;
import com.github.Dorae132.easyutil.easyexcel.read.IRowConsumer;
import com.google.common.collect.Lists;

@SuppressWarnings("unchecked")
public class UtilsTest {

	static class TestValue {
		@ExcelCol(title = "姓名")
		private String name;
		@ExcelCol(title = "年龄", order = 1)
		private String age;
		@ExcelCol(title = "学校", order = 3)
		private String school;
		@ExcelCol(title = "年级", order = 2)
		private String clazz;

		public TestValue(String name, String age, String school, String clazz) {
			super();
			this.name = name;
			this.age = age;
			this.school = school;
			this.clazz = clazz;
		}
	}

	private static List<TestValue> getData(int count) {
		List<TestValue> dataList = Lists.newArrayListWithCapacity(count);
		for (int i = 0; i < count; i++) {
			dataList.add(new TestValue("张三" + i, "age: " + i, null, "clazz: " + i));
		}
		return dataList;
	}

	// @Test
	// public void testCommonMode() throws Exception {
	// List<TestValue> dataList = getData(100000);
	// long start = System.currentTimeMillis();
	// ExcelProperties<TestValue, File> properties =
	// ExcelProperties.produceCommonProperties("", dataList,
	// "C:\\Users\\Dorae\\Desktop\\ttt\\", "common.xlsx", 0, null, 0, null);
	// File file = (File) ExcelUtils.excelExport(properties,
	// FillSheetModeEnums.COMMON_MODE.getValue());
	// System.out.println("commonMode: " + (System.currentTimeMillis() - start));
	// }

	@Test
	public static void testAppend() throws Exception {
		List<TestValue> dataList = getData(100000);
		long start = System.currentTimeMillis();
		IExcelProcessor processor = new IExcelProcessor<String, File>() {

			@Override
			public String process(File f) {
				return f.getName();
			}
		};
		ExcelProperties<TestValue, File> properties = ExcelProperties.produceAppendProperties("",
				"C:\\Users\\Dorae\\Desktop\\ttt\\", "append.xlsx", 0, TestValue.class, 0, processor,
				new IDataSupplier<TestValue>() {
					private int i = 0;

					@Override
					public Pair<List<TestValue>, Boolean> getDatas() {
						boolean hasNext = i < 9;
						i++;
						return Pair.of(dataList, hasNext);
					}
				});
		String result = (String) ExcelUtils.excelExport(properties, FillSheetModeEnums.PARALLEL_APPEND_MODE.getValue());
		System.out.println("apendMode: " + (System.currentTimeMillis() - start));
	}

	@Test
	public static void testRead() throws Exception {
		AtomicInteger count = new AtomicInteger(0);
		long start = System.currentTimeMillis();
		ExcelUtils.excelRead(ExcelProperties.produceReadProperties("C:\\Users\\Dorae\\Desktop\\ttt\\",
				"append_7f4ef636ff5e47e19504e17d963c4026.xlsx"), new IRowConsumer<String>() {
					@Override
					public void consume(List<String> row) {
						System.out.println(row);
						count.incrementAndGet();
						try {
							TimeUnit.MICROSECONDS.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new IReadDoneCallBack<Void>() {
					@Override
					public Void call() {
						System.out.println(
								"end, count: " + count.get() + "\ntime: " + (System.currentTimeMillis() - start));
						return null;
					}
				}, 3, true);
		System.out.println("main end" + count.get());
	}

	public static void main(String[] args) throws Exception {
//		testAppend();
		testRead();
	}

}
