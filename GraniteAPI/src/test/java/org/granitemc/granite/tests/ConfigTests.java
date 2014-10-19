package org.granitemc.granite.tests;

import org.granitemc.granite.api.config.CfgObject;
import org.granitemc.granite.api.config.CfgPrimitive;
import org.granitemc.granite.api.config.CfgValue;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigTests {
    @Test
    public void basicTest() {
        CfgObject obj = new CfgObject();
        obj.put("Hello", "one two three");
        obj.put("one.two.three", "test");
        assertEquals(obj.size(), 2);
        assertEquals(obj.get("Hello"), new CfgPrimitive("one two three"));
    }

    @Test
    public void loadHocon() {
        CfgObject obj = (CfgObject) CfgValue.read(getClass().getResourceAsStream("/config/test1.conf"));

        assertTrue(obj.get("test") instanceof CfgObject);
        assertEquals(obj.getString("test.one"), "hey");
        assertEquals(obj.getInteger("test.two"), 123);
        assertNull(obj.getString("test.three"));
        assertEquals(obj.getDouble("test.number"), 123.456, 0.0001);

        assertEquals(obj.get("test.otherobject").getComments().get(0), "This is a comment");
        assertEquals(obj.getString("test.otherobject.hello.0"), "world");
        assertEquals(obj.getString("test.otherobject.hello.1.welcometo.0"), "zombocom");
    }

    @Test
    public void saveHocon() {
        CfgObject obj = (CfgObject) CfgValue.read(getClass().getResourceAsStream("/config/test1.conf"));
        assertEquals(CfgValue.read(obj.writeToString()), obj);
    }
}
