package jira.For.Android.Connector;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ksoap2.serialization.SoapObject;


public class SoapObjectBuilder {

	private static final String NAMESPACE = "com.jira4android";
	
	//don't change implementation, order of added properties needs to be kept
	private Map<String, String> propertiesMap = new LinkedHashMap<String, String>();
	private String methodName;
	
    private SoapObjectBuilder() {
    }
	
	public static SoapObjectBuilder start(){
		return new SoapObjectBuilder();
	}

	
	
	public SoapObjectBuilder withMethod(String methodName){
		this.methodName = methodName;
		return this;
	}
	
	public SoapObjectBuilder withProperty(String key, String value){
		propertiesMap.put(key, value);
		return this;
	}
	
	public SoapObject build(){
		SoapObject result = new SoapObject(NAMESPACE, methodName);
		for(Entry<String, String> entry : propertiesMap.entrySet()){
			result.addProperty(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
