package com.sym.juc.unsafe;

import sun.misc.VM;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;


/**
 * A collection of methods for performing low-level, unsafe operations.
 * Although the class and all methods are public, use of this class is
 * limited because only trusted code can obtain instances of it.
 *
 * @author John R. Rose
 * @see #getUnsafe
 */

public final class Unsafe {

    static {
        registerNatives();
        Reflection.registerMethodsToFilter(Unsafe.class, "getUnsafe");
    }

    private static native void registerNatives();

    private static final Unsafe theUnsafe = new Unsafe();

    private Unsafe() {
    }

    @CallerSensitive
    public static Unsafe getUnsafe() {
        Class<?> caller = Reflection.getCallerClass();
        if (!VM.isSystemDomainLoader(caller.getClassLoader()))
            throw new SecurityException("Unsafe");
        return theUnsafe;
    }

    /**
     * 从给定的 Java 变量中获取一个值, 若 <code>o<code>不为空, 则<code>offset<code>作为相对地址, 反之作为绝对地址, 用来确定内存块.
     * 满足以下情况之一, 此方法返回的结果才有意义：
     * <ul>
     *     1.偏移量是从某个 Java 字段的 {@link Field} 上的 {@link #objectFieldOffset} 和引用的对象获得的<code>o<code> 属于与该字段的类兼容的类;
     *     2.偏移量和对象引用 <code>o<code>（空或非空）都是通过 {@link #staticFieldOffset} 和 {@link #staticFieldBase}（分别）从反射 {@link Field} 一些 Java 字段的表示;
     *     3.<code>o<code>引用的对象是一个数组，偏移量是<code>B+NS<code>形式的整数，其中<code>N<code>是一个有效的索引<code>B<code> 和 <code>S<code> 是
     *       {@link #arrayBaseOffset} 和 {@link #arrayIndexScale} 分别从数组的类中获得的值。引用的值是数组的 <code>N<code><em>th<em> 元素;
     * </ul>
     *
     * @param o      变量所在的Java堆对象，如果有，否则为空
     * @param offset 指示变量在 Java 堆对象中的位置（如果有），否则是静态定位变量的内存地址
     * @return 从指定的 Java 变量中获取的值
     * @throws RuntimeException No defined exceptions are thrown, not even {@link NullPointerException}
     */
    public native int getInt(Object o, long offset);

    /**
     * 将值存储到给定的 Java 变量中
     *
     * @param o      变量所在的Java堆对象，如果有，否则为空
     * @param offset 指示变量在 Java 堆对象中的位置（如果有），否则是静态定位变量的内存地址
     * @param x      要存储到指定的 Java 变量中的值
     * @throws RuntimeException No defined exceptions are thrown, not even {@link NullPointerException}
     */
    public native void putInt(Object o, long offset, int x);

    /**
     * 从给定的Java变量中获取参数值(它会忽略限定符的访问限制)
     *
     * @see #getInt(Object, long)
     */
    public native Object getObject(Object o, long offset);

    /**
     * 将引用值存储到给定的 Java 变量中
     *
     * @see #putInt(Object, long, int)
     */
    public native void putObject(Object o, long offset, Object x);

    /**
     * @see #getInt(Object, long)
     */
    public native boolean getBoolean(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public native void putBoolean(Object o, long offset, boolean x);

    /**
     * @see #getInt(Object, long)
     */
    public native byte getByte(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public native void putByte(Object o, long offset, byte x);

    /**
     * @see #getInt(Object, long)
     */
    public native short getShort(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public native void putShort(Object o, long offset, short x);

    /**
     * @see #getInt(Object, long)
     */
    public native char getChar(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public native void putChar(Object o, long offset, char x);

    /**
     * @see #getInt(Object, long)
     */
    public native long getLong(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public native void putLong(Object o, long offset, long x);

    /**
     * @see #getInt(Object, long)
     */
    public native float getFloat(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public native void putFloat(Object o, long offset, float x);

    /**
     * @see #getInt(Object, long)
     */
    public native double getDouble(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public native void putDouble(Object o, long offset, double x);


    // These work on values in the C heap.

    /**
     * 获取指定地址的byte类型的值, 如果地址为零, 或者不是从{@link #allocateMemory} 获得的内存块, 结果无效.
     *
     * @see #allocateMemory
     */
    public native byte getByte(long address);

    /**
     * 为指定地址设置byte类型的值, 如果地址为零, 或者不是从{@link #allocateMemory} 获得的内存块, 此方法无效
     *
     * @see #getByte(long)
     */
    public native void putByte(long address, byte x);

    /**
     * @see #getByte(long)
     */
    public native short getShort(long address);

    /**
     * @see #putByte(long, byte)
     */
    public native void putShort(long address, short x);

    /**
     * @see #getByte(long)
     */
    public native char getChar(long address);

    /**
     * @see #putByte(long, byte)
     */
    public native void putChar(long address, char x);

    /**
     * @see #getByte(long)
     */
    public native int getInt(long address);

    /**
     * @see #putByte(long, byte)
     */
    public native void putInt(long address, int x);

    /**
     * @see #getByte(long)
     */
    public native long getLong(long address);

    /**
     * @see #putByte(long, byte)
     */
    public native void putLong(long address, long x);

    /**
     * @see #getByte(long)
     */
    public native float getFloat(long address);

    /**
     * @see #putByte(long, byte)
     */
    public native void putFloat(long address, float x);

    /**
     * @see #getByte(long)
     */
    public native double getDouble(long address);

    /**
     * @see #putByte(long, byte)
     */
    public native void putDouble(long address, double x);

    /**
     * Fetches a native pointer from a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     *
     * <p> If the native pointer is less than 64 bits wide, it is extended as
     * an unsigned number to a Java long.  The pointer may be indexed by any
     * given byte offset, simply by adding that offset (as a simple integer) to
     * the long representing the pointer.  The number of bytes actually read
     * from the target address maybe determined by consulting {@link
     * #addressSize}.
     *
     * @see #allocateMemory
     */
    public native long getAddress(long address);

    /**
     * Stores a native pointer into a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     *
     * <p> The number of bytes actually written at the target address maybe
     * determined by consulting {@link #addressSize}.
     *
     * @see #getAddress(long)
     */
    public native void putAddress(long address, long x);

    /// wrappers for malloc, realloc, free:

    /**
     * 以字节为单位分配内存, 可以理解为 C++ 的 malloc函数, 它会返回内存块的基地址. 可以通过
     * 调用 {@link #freeMemory} 处理此内存，或使用 {@link #reallocateMemory} 调整其大小
     *
     * @throws IllegalArgumentException if the size is negative or too large for the native size_t type
     * @throws OutOfMemoryError         if the allocation is refused by the system
     * @see #getByte(long)
     * @see #putByte(long, byte)
     */
    public native long allocateMemory(long bytes);

    /**
     * 将指定内存地址的内存块扩充指定的字节大小, 超过旧块大小的新块的内容未被初始化, 传递给此方法的地址可能为空,
     * 这种情况下会执行内存分配操作.
     *
     * @throws IllegalArgumentException if the size is negative or too large for the native size_t type
     * @throws OutOfMemoryError         if the allocation is refused by the system
     * @see #allocateMemory
     */
    public native long reallocateMemory(long address, long bytes);

    /**
     * 将给定内存块的所有字节设置为指定的值(通常设置为0), 此方法通过两个参数确定内存块的基地址：{@code o}和{@code offset},
     * 当对象引用为空时, 偏移量表示一个绝对基地址.
     */
    public native void setMemory(Object o, long offset, long bytes, byte value);

    /**
     * {@link #setMemory(Object, long, long, byte)}
     */
    public void setMemory(long address, long bytes, byte value) {
        setMemory(null, address, bytes, value);
    }

    /**
     * 通过{@code 对象实例 + 偏移量}确定原内存块和目标内存块, 将原内存块指定的字节数拷贝到目标内存块.
     */
    public native void copyMemory(Object srcBase, long srcOffset,
                                  Object destBase, long destOffset,
                                  long bytes);

    /**
     * {@link #copyMemory(Object, long, Object, long, long)}, 此时不指定对象实例,
     * 偏移量就作为内存块的绝对地址.
     */
    public void copyMemory(long srcAddress, long destAddress, long bytes) {
        copyMemory(null, srcAddress, null, destAddress, bytes);
    }

    /**
     * 释放从 {@link #allocateMemory} 或 {@link #reallocateMemory} 获得的内存块,
     * 若传递给此方法的内存地址未被分配, 这种情况下不采取任何操作
     *
     * @see #allocateMemory
     */
    public native void freeMemory(long address);

    /// random queries

    /**
     * This constant differs from all results that will ever be returned from
     * {@link #staticFieldOffset}, {@link #objectFieldOffset},
     * or {@link #arrayBaseOffset}.
     */
    public static final int INVALID_FIELD_OFFSET = -1;

    /**
     * 获取给定静态字段的内存地址偏移量.
     *
     * @see #getInt(Object, long)
     */
    public native long staticFieldOffset(Field f);

    /**
     * 返回对象成员变量, 相对于此对象的内存地址的偏移量
     */
    public native long objectFieldOffset(Field f);

    /**
     * 获取一个静态类中给定字段的对象指针, 该方法返回的值不能保证是一个真的的对象, 它不应该以任何方式使用,
     * 除了作为 Unsafe 类中 getxx() 和 putxx() 方法的参数.
     */
    public native Object staticFieldBase(Field f);

    /**
     * 判断是否需要初始化一个类, 通常在获取一个类的静态属性的时候使用(因为一个类如果未被初始化, 它的静态属性也不会初始化)
     *
     * @return 仅当调用 {@code ensureClassInitialized} 无效时才为 false
     */
    public native boolean shouldBeInitialized(Class<?> c);

    /**
     * 确保给定的类已初始化, 通常在获取一个类的静态属性时使用.
     */
    public native void ensureClassInitialized(Class<?> c);

    /**
     * 返回数组中第一个元素的偏移地址.
     *
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public native int arrayBaseOffset(Class<?> arrayClass);

    /**
     * The value of {@code arrayBaseOffset(boolean[].class)}
     */
    public static final int ARRAY_BOOLEAN_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(boolean[].class);

    /**
     * The value of {@code arrayBaseOffset(byte[].class)}
     */
    public static final int ARRAY_BYTE_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(byte[].class);

    /**
     * The value of {@code arrayBaseOffset(short[].class)}
     */
    public static final int ARRAY_SHORT_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(short[].class);

    /**
     * The value of {@code arrayBaseOffset(char[].class)}
     */
    public static final int ARRAY_CHAR_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(char[].class);

    /**
     * The value of {@code arrayBaseOffset(int[].class)}
     */
    public static final int ARRAY_INT_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(int[].class);

    /**
     * The value of {@code arrayBaseOffset(long[].class)}
     */
    public static final int ARRAY_LONG_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(long[].class);

    /**
     * The value of {@code arrayBaseOffset(float[].class)}
     */
    public static final int ARRAY_FLOAT_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(float[].class);

    /**
     * The value of {@code arrayBaseOffset(double[].class)}
     */
    public static final int ARRAY_DOUBLE_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(double[].class);

    /**
     * The value of {@code arrayBaseOffset(Object[].class)}
     */
    public static final int ARRAY_OBJECT_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(Object[].class);

    /**
     * 返回数组中一个元素占用的大小.
     *
     * @see #arrayBaseOffset
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public native int arrayIndexScale(Class<?> arrayClass);

    /**
     * The value of {@code arrayIndexScale(boolean[].class)}
     */
    public static final int ARRAY_BOOLEAN_INDEX_SCALE
            = theUnsafe.arrayIndexScale(boolean[].class);

    /**
     * The value of {@code arrayIndexScale(byte[].class)}
     */
    public static final int ARRAY_BYTE_INDEX_SCALE
            = theUnsafe.arrayIndexScale(byte[].class);

    /**
     * The value of {@code arrayIndexScale(short[].class)}
     */
    public static final int ARRAY_SHORT_INDEX_SCALE
            = theUnsafe.arrayIndexScale(short[].class);

    /**
     * The value of {@code arrayIndexScale(char[].class)}
     */
    public static final int ARRAY_CHAR_INDEX_SCALE
            = theUnsafe.arrayIndexScale(char[].class);

    /**
     * The value of {@code arrayIndexScale(int[].class)}
     */
    public static final int ARRAY_INT_INDEX_SCALE
            = theUnsafe.arrayIndexScale(int[].class);

    /**
     * The value of {@code arrayIndexScale(long[].class)}
     */
    public static final int ARRAY_LONG_INDEX_SCALE
            = theUnsafe.arrayIndexScale(long[].class);

    /**
     * The value of {@code arrayIndexScale(float[].class)}
     */
    public static final int ARRAY_FLOAT_INDEX_SCALE
            = theUnsafe.arrayIndexScale(float[].class);

    /**
     * The value of {@code arrayIndexScale(double[].class)}
     */
    public static final int ARRAY_DOUBLE_INDEX_SCALE
            = theUnsafe.arrayIndexScale(double[].class);

    /**
     * The value of {@code arrayIndexScale(Object[].class)}
     */
    public static final int ARRAY_OBJECT_INDEX_SCALE
            = theUnsafe.arrayIndexScale(Object[].class);

    /**
     * Report the size in bytes of a native pointer, as stored via {@link
     * #putAddress}.  This value will be either 4 or 8.  Note that the sizes of
     * other primitive types (as stored in native memory blocks) is determined
     * fully by their information content.
     */
    public native int addressSize();

    /**
     * The value of {@code addressSize()}
     */
    public static final int ADDRESS_SIZE = theUnsafe.addressSize();

    /**
     * Report the size in bytes of a native memory page (whatever that is).
     * This value will always be a power of two.
     */
    public native int pageSize();


    /// random trusted operations from JNI:

    /**
     * 定义一个类, 会跳过JVM的安全检查
     */
    public native Class<?> defineClass(String name, byte[] b, int off, int len,
                                       ClassLoader loader, ProtectionDomain protectionDomain);

    /**
     * Define a class but do not make it known to the class loader or system dictionary.
     * <p>
     * For each CP entry, the corresponding CP patch must either be null or have
     * the a format that matches its tag:
     * <ul>
     * <li>Integer, Long, Float, Double: the corresponding wrapper object type from java.lang
     * <li>Utf8: a string (must have suitable syntax if used as signature or name)
     * <li>Class: any java.lang.Class object
     * <li>String: any object (not just a java.lang.String)
     * <li>InterfaceMethodRef: (NYI) a method handle to invoke on that call site's arguments
     * </ul>
     *
     * @param hostClass context for linkage, access control, protection domain, and class loader
     * @param data      bytes of a class file
     * @param cpPatches where non-null entries exist, they replace corresponding CP entries in data
     */
    public native Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches);


    /**
     * 绕过构造方法和初始化代码来创建对象, 如果尚未初始化类，则对其进行初始化.
     */
    public native Object allocateInstance(Class<?> cls) throws InstantiationException;

    /**
     * 锁定对象, 它必须通过 {@link #monitorExit} 解锁.
     */
    public native void monitorEnter(Object o);

    /**
     * 解锁对象, 它必须已通过 {@link #monitorEnter} 锁定.
     */
    public native void monitorExit(Object o);

    /**
     * 尝试锁定对象, 如果锁定成功返回true, 后续必须通过 {@link #monitorExit} 解锁对象.
     */
    public native boolean tryMonitorEnter(Object o);

    /**
     * Throw the exception without telling the verifier.
     */
    public native void throwException(Throwable ee);

    /**
     * 通过CAS原子性地更新值, 其底层实现为 CPU指令 <code>cmpxchg</code>.
     *
     * @param o        要修改值的对象实例
     * @param offset   对象实例中某field的偏移量, 结合上一个参数来确定其内存地址
     * @param expected 期望值
     * @param x        更新值
     * @return true-更新成功
     */
    public final native boolean compareAndSwapObject(Object o, long offset, Object expected, Object x);

    /**
     * @see #compareAndSwapObject(Object, long, Object, Object)
     */
    public final native boolean compareAndSwapInt(Object o, long offset, int expected, int x);

    /**
     * @see #compareAndSwapObject(Object, long, Object, Object)
     */
    public final native boolean compareAndSwapLong(Object o, long offset, long expected, long x);

    /**
     * 从对象<code>o</code>的指定偏移量获取变量的引用值, 会使用 volatile 的加载语义.
     */
    public native Object getObjectVolatile(Object o, long offset);

    /**
     * 更新对象的变量值(通过相对偏移量来确定变量), 使用 volatile 的存储语义,
     */
    public native void putObjectVolatile(Object o, long offset, Object x);

    /**
     * Volatile version of {@link #getInt(Object, long)}
     */
    public native int getIntVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putInt(Object, long, int)}
     */
    public native void putIntVolatile(Object o, long offset, int x);

    /**
     * Volatile version of {@link #getBoolean(Object, long)}
     */
    public native boolean getBooleanVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putBoolean(Object, long, boolean)}
     */
    public native void putBooleanVolatile(Object o, long offset, boolean x);

    /**
     * Volatile version of {@link #getByte(Object, long)}
     */
    public native byte getByteVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putByte(Object, long, byte)}
     */
    public native void putByteVolatile(Object o, long offset, byte x);

    /**
     * Volatile version of {@link #getShort(Object, long)}
     */
    public native short getShortVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putShort(Object, long, short)}
     */
    public native void putShortVolatile(Object o, long offset, short x);

    /**
     * Volatile version of {@link #getChar(Object, long)}
     */
    public native char getCharVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putChar(Object, long, char)}
     */
    public native void putCharVolatile(Object o, long offset, char x);

    /**
     * Volatile version of {@link #getLong(Object, long)}
     */
    public native long getLongVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putLong(Object, long, long)}
     */
    public native void putLongVolatile(Object o, long offset, long x);

    /**
     * Volatile version of {@link #getFloat(Object, long)}
     */
    public native float getFloatVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putFloat(Object, long, float)}
     */
    public native void putFloatVolatile(Object o, long offset, float x);

    /**
     * Volatile version of {@link #getDouble(Object, long)}
     */
    public native double getDoubleVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putDouble(Object, long, double)}
     */
    public native void putDoubleVolatile(Object o, long offset, double x);

    /**
     * 有序、延迟版本的 {@link #putObjectVolatile(Object, long, Object)}方法,
     * 不保证值的改变会被其它线程立即看到, 只有在field被volatile修饰时有效.
     */
    public native void putOrderedObject(Object o, long offset, Object x);

    /**
     * Ordered/Lazy version of {@link #putIntVolatile(Object, long, int)}
     */
    public native void putOrderedInt(Object o, long offset, int x);

    /**
     * Ordered/Lazy version of {@link #putLongVolatile(Object, long, long)}
     */
    public native void putOrderedLong(Object o, long offset, long x);

    /**
     * 唤醒被 {@link #park(boolean, long)} 阻塞的给定线程, 如果这个线程并未被阻塞,
     * 那么后续对给定线程再调用 {@link #park(boolean, long)} 方法, 它就不会被阻塞.
     *
     * @param thread 需要被唤醒的线程
     */
    public native void unpark(Object thread);

    /**
     * 阻塞当前线程, 以下情况之一发生时返回：
     * <p>
     * 1.调用{@link #unpark(Object)}
     * 2.线程被中断;
     * 3.<code>isAbsolute</code>为false, 并且<code>time</code>不为零, 并且指定时间(纳秒)已经过去;
     * 4.<code>isAbsolute</code>为true, 且已经到达从 Epoch 以来的给定期限(毫秒)
     * 5.虚假唤醒, 即无缘无故地返回
     * </p>
     */
    public native void park(boolean isAbsolute, long time);

    /**
     * Gets the load average in the system run queue assigned
     * to the available processors averaged over various periods of time.
     * This method retrieves the given <tt>nelem</tt> samples and
     * assigns to the elements of the given <tt>loadavg</tt> array.
     * The system imposes a maximum of 3 samples, representing
     * averages over the last 1,  5,  and  15 minutes, respectively.
     *
     * @return the number of samples actually retrieved; or -1
     * if the load average is unobtainable.
     * @params loadavg an array of double of size nelems
     * @params nelems the number of samples to be retrieved and
     * must be 1 to 3.
     */
    public native int getLoadAverage(double[] loadavg, int nelems);

    // The following contain CAS-based Java implementations used on
    // platforms not supporting native instructions

    /**
     * Atomically adds the given value to the current value of a field
     * or array element within the given object <code>o</code>
     * at the given <code>offset</code>.
     *
     * @param o      object/array to update the field/element in
     * @param offset field/element offset
     * @param delta  the value to add
     * @return the previous value
     * @since 1.8
     */
    public final int getAndAddInt(Object o, long offset, int delta) {
        int v;
        do {
            v = getIntVolatile(o, offset);
        } while (!compareAndSwapInt(o, offset, v, v + delta));
        return v;
    }

    /**
     * Atomically adds the given value to the current value of a field
     * or array element within the given object <code>o</code>
     * at the given <code>offset</code>.
     *
     * @param o      object/array to update the field/element in
     * @param offset field/element offset
     * @param delta  the value to add
     * @return the previous value
     * @since 1.8
     */
    public final long getAndAddLong(Object o, long offset, long delta) {
        long v;
        do {
            v = getLongVolatile(o, offset);
        } while (!compareAndSwapLong(o, offset, v, v + delta));
        return v;
    }

    /**
     * Atomically exchanges the given value with the current value of
     * a field or array element within the given object <code>o</code>
     * at the given <code>offset</code>.
     *
     * @param o        object/array to update the field/element in
     * @param offset   field/element offset
     * @param newValue new value
     * @return the previous value
     * @since 1.8
     */
    public final int getAndSetInt(Object o, long offset, int newValue) {
        int v;
        do {
            v = getIntVolatile(o, offset);
        } while (!compareAndSwapInt(o, offset, v, newValue));
        return v;
    }

    /**
     * Atomically exchanges the given value with the current value of
     * a field or array element within the given object <code>o</code>
     * at the given <code>offset</code>.
     *
     * @param o        object/array to update the field/element in
     * @param offset   field/element offset
     * @param newValue new value
     * @return the previous value
     * @since 1.8
     */
    public final long getAndSetLong(Object o, long offset, long newValue) {
        long v;
        do {
            v = getLongVolatile(o, offset);
        } while (!compareAndSwapLong(o, offset, v, newValue));
        return v;
    }

    /**
     * Atomically exchanges the given reference value with the current
     * reference value of a field or array element within the given
     * object <code>o</code> at the given <code>offset</code>.
     *
     * @param o        object/array to update the field/element in
     * @param offset   field/element offset
     * @param newValue new value
     * @return the previous value
     * @since 1.8
     */
    public final Object getAndSetObject(Object o, long offset, Object newValue) {
        Object v;
        do {
            v = getObjectVolatile(o, offset);
        } while (!compareAndSwapObject(o, offset, v, newValue));
        return v;
    }


    /**
     * 内存屏障, 禁止load操作重排序.
     * 屏障前的load操作不能被重排序到屏障后, 屏障后的load操作不能被重排序到屏障前.
     */
    public native void loadFence();

    /**
     * 内存屏障, 禁止store操作重排序.
     * 屏障前的store操作不能被重排序到屏障后, 屏障后的store操作不能被重排序到屏障前.
     */
    public native void storeFence();

    /**
     * 内存屏障, 禁止 load、store 操作重排序.
     */
    public native void fullFence();

    /**
     * Throws IllegalAccessError; for use by the VM.
     *
     * @since 1.8
     */
    private static void throwIllegalAccessError() {
        throw new IllegalAccessError();
    }

}
