package io.github.kurok1.processing.output;

import java.math.BigDecimal;
import java.util.Arrays;

class StructJsonFormatWriterTest {
    
    public static void main(String[] args) throws Exception {
        StructJsonFormatWriter writer = new StructJsonFormatWriter();
        Struct struct = Struct.newInstance();
        struct.addStringProperty("key1", "value1");
        struct.addIntegerProperty("key2", 1);
        struct.addNumberProperty("key3", new BigDecimal("3.14"));
        struct.addListProperty("key4", Arrays.asList("1", "2", "3"));
        Struct obj = Struct.newInstance();
        obj.addStringProperty("key5", "value5");
        obj.addStringProperty("key6", "value6");
        Struct s = Struct.newInstance();
        s.addStringProperty("key7", "value7");
        obj.addStructProperty("struct", s);
        struct.addStructProperty("key5", obj);
        Struct obj1 = Struct.newInstance();
        obj1.addStringProperty("key7", "value7");
        obj1.addStringProperty("key8", "value8");
        Struct obj2 = Struct.newInstance();
        obj2.addStringProperty("key9", "value9");
        obj2.addStringProperty("key10", "value10");
        Struct obj3 = Struct.newInstance();
        obj3.addStringProperty("key11", "value11");
        obj3.addStringProperty("key12", "value12");
        struct.addListProperty("list", Arrays.asList(obj1, obj2, obj3));
        
        writer.write("", struct, null);
    }
}