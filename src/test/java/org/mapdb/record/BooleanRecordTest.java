/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 * Other contributors include Andrew Wright, Jeffrey Hayes,
 * Pat Fisher, Mike Judd.
 */
package org.mapdb.record;

import junit.framework.TestCase;
import org.mapdb.db.DB;

public class BooleanRecordTest extends TestCase{

    DB db;
   BooleanRecord ai;

    @Override
    protected void setUp() throws Exception {
        db = DB.Maker.memoryDB().make();
        ai= new BooleanRecord.Maker(db, "test").init(true).make();
    }

    @Override
    protected void tearDown() throws Exception {
        db.close();
    }


    /*
     * constructor initializes to given value
     */
    public void testConstructor() {
        assertEquals(true,ai.get());
    }

    /*
     * default constructed initializes to false
     */
    public void testConstructor2() {
        BooleanRecord ai = new BooleanRecord.Maker(db, "test2").make();
        assertEquals(false,ai.get());
    }

    /*
     * get returns the last value set
     */
    public void testGetSet() {

        assertEquals(true,ai.get());
        ai.set(false);
        assertEquals(false,ai.get());
        ai.set(true);
        assertEquals(true,ai.get());

    }

    /*
     * compareAndSet succeeds in changing value if equal to expected else fails
     */
    public void testCompareAndSet() {

        assertTrue(ai.compareAndSet(true,false));
        assertEquals(false,ai.get());
        assertTrue(ai.compareAndSet(false,false));
        assertEquals(false,ai.get());
        assertFalse(ai.compareAndSet(true,false));
        assertFalse((ai.get()));
        assertTrue(ai.compareAndSet(false,true));
        assertEquals(true,ai.get());
    }

    /*
     * compareAndSet in one thread enables another waiting for value
     * to succeed
     */
    public void testCompareAndSetInMultipleThreads() throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            public void run() {
                while(!ai.compareAndSet(false, true)) Thread.yield();
            }});

            t.start();
            assertTrue(ai.compareAndSet(true, false));
            t.join(0);
            assertFalse(t.isAlive());

    }

    /*
     * getAndSet returns previous value and sets to given value
     */
    public void testGetAndSet() {
        assertEquals(true,ai.getAndSet(false));
        assertEquals(false,ai.getAndSet(false));
        assertEquals(false,ai.getAndSet(true));
        assertEquals(true,ai.get());
    }
    /*
     * toString returns current value.
     */
    public void testToString() {
        BooleanRecord ai = new BooleanRecord.Maker(db,"test2").make();
        assertEquals(ai.toString(), Boolean.toString(false));
        ai.set(true);
        assertEquals(ai.toString(), Boolean.toString(true));
    }

}
