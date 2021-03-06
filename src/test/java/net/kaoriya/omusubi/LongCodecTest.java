package net.kaoriya.omusubi;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import java.nio.LongBuffer;

import net.kaoriya.omusubi.io.LongOutputStream;

public class LongCodecTest
{
    public static class ThroughCodec extends LongCodec {
        public int compressedLen = 0;
        public int decompressedLen = 0;

        public void compress(LongBuffer src, LongOutputStream dst) {
            long[] l = src.array();
            dst.write(l);
            this.compressedLen += l.length;
        }

        public void decompress(LongBuffer src, LongOutputStream dst) {
            long[] l = new long[src.remaining()];
            src.get(l);
            dst.write(l);
            this.decompressedLen += l.length;
        }
    }

    @Test
    public void compress() {
        ThroughCodec c = new ThroughCodec();
        byte[] b = c.compress(new long[] { 0, 1, 2, 3, 4, 5, 6, 7 });
        assertArrayEquals(new byte[] {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 3,
            0, 0, 0, 0, 0, 0, 0, 4,
            0, 0, 0, 0, 0, 0, 0, 5,
            0, 0, 0, 0, 0, 0, 0, 6,
            0, 0, 0, 0, 0, 0, 0, 7,
        }, b);
        assertEquals(8, c.compressedLen);
    }

    @Test
    public void decompress() {
        ThroughCodec c = new ThroughCodec();
        long[] l = c.decompress(new byte[] {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 3,
            0, 0, 0, 0, 0, 0, 0, 4,
            0, 0, 0, 0, 0, 0, 0, 5,
            0, 0, 0, 0, 0, 0, 0, 6,
            0, 0, 0, 0, 0, 0, 0, 7,
        });
        assertArrayEquals(new long[] { 0, 1, 2, 3, 4, 5, 6, 7 }, l);
        assertEquals(8, c.decompressedLen);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public static class DummyCodec extends LongCodec {
        public void compress(LongBuffer src, LongOutputStream dst) {}
        public void decompress(LongBuffer src, LongOutputStream dst) {}
    }

    @Test
    public void newCompressStream() {
        thrown.expect(UnsupportedOperationException.class);
        DummyCodec c = new DummyCodec();
        c.newCompressStream(null);
    }

    @Test
    public void newDecompressStream() {
        thrown.expect(UnsupportedOperationException.class);
        DummyCodec c = new DummyCodec();
        c.newDecompressStream(null);
    }
}
