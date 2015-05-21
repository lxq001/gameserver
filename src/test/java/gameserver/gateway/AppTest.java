package gameserver.gateway;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

import com.alibaba.fastjson.JSONObject;

import config.Config;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	public void testConfig(){
	}
	public void testInitInboundPacket(){
		Config.initOPs("utils", "OPUtils.class");
	}
	public void testBeanUtils() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IntrospectionException{
		
		
		
	}
}
