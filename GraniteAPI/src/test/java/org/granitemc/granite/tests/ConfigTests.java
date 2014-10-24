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
        assertEquals(obj.getValue("Hello"), new CfgPrimitive("one two three"));
    }

    @Test
    public void loadHocon() {
        CfgObject obj = (CfgObject) CfgValue.read(getClass().getResourceAsStream("/config/test1.conf"));

        assertTrue(obj.getValue("test") instanceof CfgObject);
        assertEquals(obj.getString("test.one"), "hey");
        assertEquals(obj.getInteger("test.two"), 123);
        assertNull(obj.getString("test.three"));
        assertEquals(obj.getDouble("test.number"), 123.456, 0.0001);

        assertEquals(obj.getValue("test.otherobject").getComments().get(0), "This is a comment");
        assertEquals(obj.getString("test.otherobject.hello.0"), "world");
        assertEquals(obj.getString("test.otherobject.hello.1.welcometo.0"), "zombocom");
    }

    @Test
    public void saveHocon() {
        CfgObject obj = (CfgObject) CfgValue.read(getClass().getResourceAsStream("/config/test1.conf"));
        assertEquals(CfgValue.read(obj.writeToString()), obj);
    }

    @Test
    public void defaults() {
        CfgObject obj = new CfgObject();
        obj.addDefault("one", 1);
        obj.addDefault("two.three", "fortytwo");
        obj.addDefault("two.four", "morethanthree");
        obj.addDefault("two.levelone.leveltwo", false);
        obj.put("two.four", 4);

        assertEquals(obj.getInteger("one"), 1);
        assertEquals(obj.getString("two.three"), "fortytwo");
        assertEquals(obj.getInteger("two.four"), 4);
        assertFalse(obj.getBoolean("two.levelone.leveltwo"));
    }
}
