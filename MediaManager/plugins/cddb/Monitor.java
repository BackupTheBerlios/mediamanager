package plugins.cddb;

/**
* Monitor is a convenient class that is useful
* to communicate between threads by exchanging
* information through this Monitor. The Monitor
* contains several generically useful members which can be
* accessed thread-savely to provide for different needs.
* There is also the availability of a custom call-back function
* for several purposes.
* @author Holger Antelmann
*/
public class Monitor //implements Serializable
{
    /** initialized as running during instanciation */
    public  final StopWatch   timer;
    private       StopWatch[] timers;
    public  volatile boolean test;
    private Object   [] listeners;
    private boolean  [] testers;
    private int         counter;
    private int      [] counters;
    private Object      object;
    private Object   [] objects;
    private Runnable    task;
    private boolean enabled;
    private MonitorDisablerThread killer;


    public Monitor () {
        reInitialize(true, 0, null);
        timer = new StopWatch(true); // initialized as running
    }


    public Monitor (int size) {
        reInitialize(true, size, null);
        timer = new StopWatch(true); // initialized as running
    }


    /**
    * initializes the arrays in this instance with n elements
    * to store/exchange data; task's run() method can be called
    * with runTask()
    * @see #runTask()
    */
    public Monitor (boolean enable, int size, Runnable task) {
        reInitialize(enable, size, task);
        timer = new StopWatch(true); // initialized as running
    }


    /**
    * all objects and arrays are re-initialized as if newly constructed;
    * only the timer is maintained */
    public synchronized void reInitialize (boolean enable, int size, Runnable task) {
        if (killer != null) {
            synchronized (killer) { killer.interrupt(); }
            killer = null;
        }
        this.task      = task;
        this.enabled   = enable;
        if (size > 0) {
            counters = new int[size];
            objects  = new Object[size];
            testers  = new boolean[size];
            timers   = new StopWatch[size];
            for (int i = 0; i < size; i++) {
                timers[i] = new StopWatch(false); // initialized as stopped
                //counters[i] = 0;
            }
        }
    }


    /**
    * This function starts a separate Thread that will disable this
    * Monitor in the given time in milliseconds automatically.
    * If a previous Thread was scheduled to disable the monitor,
    * that previous Thread will be interrupted.
    */
    public synchronized void disableLater (long milliseconds) {

        if (killer != null) {
            synchronized (killer) { killer.interrupt(); }
        }
        killer = new MonitorDisablerThread(this, milliseconds);
        killer.start();
        killer.setPriority(Thread.MAX_PRIORITY);
    }


    /**
    * enable() sets the Monitor to be enabled() and also
    * interrupts threads scheduled through disableLater()
    */
    public synchronized void enable () {
        if (killer != null) {
            synchronized (killer) { killer.interrupt(); }
            killer = null;
        }
        enabled = true;
    }


    public synchronized void disable () { enabled = false; }

    public boolean enabled () { return enabled; }

    public boolean disabled () { return !enabled; }

    public synchronized void increment() { counter++; }

    public synchronized void increment (int i) throws ArrayIndexOutOfBoundsException { counters[i]++; }

    public int getNumber () { return counter; }

    public int getNumber (int i) throws ArrayIndexOutOfBoundsException { return counters[i]; }

    public synchronized void setNumber (int number) { counter = number; }

    public synchronized void setNumber (int i, int number) throws ArrayIndexOutOfBoundsException { counters[i] = number; }

    public synchronized void setObject(Object obj) { object = obj; }

    public synchronized void setObject(int i, Object obj) throws ArrayIndexOutOfBoundsException { objects[i] = obj; }

    public Object getObject() { return object; }

    public Object getObject (int i) throws ArrayIndexOutOfBoundsException { return objects[i]; }

    public StopWatch getStopWatch (int i) throws ArrayIndexOutOfBoundsException { return timers[i]; }

    public synchronized void setTask (Runnable task) { this.task = task; }

    public Runnable getTask () { return task; }

    public synchronized void test (int i, boolean t) throws ArrayIndexOutOfBoundsException { testers[i] = t; }

    public boolean test (int i) throws ArrayIndexOutOfBoundsException { return testers[i]; }

    /** returns the length of the monitor's arrays initialized by the constructor or reInitialize() */
    public int getSize() {
        if (testers == null) return 0; else return testers.length;
    }

    /**
    * lets the Thread that uses the Monitor perform a synchronous custom task
    * that completes before the current thread continues. The task performed
    * is the run() method of the Runnable task given. If the task is null,
    * nothing happens.
    */
    public void runTask () { if (task != null) task.run(); }
}



class MonitorDisablerThread extends Thread
{
    Monitor monitor;
    long    milliseconds;

    MonitorDisablerThread (Monitor monitor, long milliseconds) {
        this.monitor      = monitor;
        this.milliseconds = milliseconds;
    }

    public void run () {
        try {
            sleep(milliseconds);
        } catch (InterruptedException e) {
            return;
        }
        monitor.disable();
    }
}