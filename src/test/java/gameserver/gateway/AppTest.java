package gameserver.gateway;

import config.Config;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	public void testConfig(){
		Config.initConfig("netty_config");
	}
	public void testInitInboundPacket(){
		Config.initOPs("utils", "OPUtils.class");
	}
}
