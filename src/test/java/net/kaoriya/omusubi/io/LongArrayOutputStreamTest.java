package net.kaoriya.omusubi.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

public class LongArrayOutputStreamTest
{
    @Test
    public void ctor() {
        LongArrayOutputStream s = new LongArrayOutputStream();
        assertEquals(0, s.count());
        assertArrayEquals(new long[0], s.toLongArray());

        s.write(123);
        s.write(456);
        assertEquals(2, s.count());
        assertArrayEquals(new long[] { 123, 456 }, s.toLongArray());
    }

    @Test
    public void ctor2() {
        LongArrayOutputStream s = new LongArrayOutputStream(5);
        s.write(1);
        s.write(2);
        s.write(3);
        s.write(4);
        s.write(5);
        assertEquals(5, s.count());
        assertArrayEquals(new long[] { 1, 2, 3, 4, 5 }, s.toLongArray());
    }

    @Test
    public void writeArray() {
        LongArrayOutputStream s = new LongArrayOutputStream(3);
        s.write(new long[] { 1, 2, 3, 4, 5 });
        assertEquals(5, s.count());
        assertArrayEquals(new long[] { 1, 2, 3, 4, 5 }, s.toLongArray());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void calcNewSize_minus() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Required length was minus");
        LongArrayOutputStream.calcNewSize(0, -1, 10);
    }

    @Test
    public void calcNewSize_overflow() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Buffer overflow");
        LongArrayOutputStream.calcNewSize(0x40000000, 0x3fffffff, 0x40000000);
    }
}
