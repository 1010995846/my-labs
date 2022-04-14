package cn.cidea.framework.common.api;

/**
 * 描述: Twitter的分布式自增ID雪花算法snowflake (Java版)
 * <p>
 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)，
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点） ，
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 一共加起来刚好64位，为一个Long型。(转换成字符串长度为18)
 * <p>
 * snowflake生成的ID整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和workerId作区分），
 * 并且效率较高。据说：snowflake每秒能够产生26万个ID。
 *
 * @author Charlotte
 */
public class SnowFlake {

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1480166465631L;

    /**
     * 每一位置占用的位数，依次为时间戳，数据中心，机器标志，序列号，合起来最大为long的最大长度63
     * 最左的时间戳位数等于63减去其余位置的位数
     * 一年31536000000 < 2^35
     * 例：若时间戳剩余位数为43，则从开始时间${@link #START_STMP}算起至少可使用2^8即256年
     */
    /**
     * 数据中心占用的位数，最多2^${value}个节点
     */
    private final static long DATA_CENTER_BIT = 5;
    /**
     * 机器标识占用的位数，最多2^${value}个节点
     */
    private final static long MACHINE_BIT = 5;
    /**
     * 序列号占用的位数，当时间戳相同时使用，时间戳以毫秒为单位，即每毫秒最多可生产2^${value}个ID
     */
    private final static long SEQUENCE_BIT = 12;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    /**
     * 数据中心，分区1
     */
    private long datacenterId;
    /**
     * 机器标识，分区2
     */
    private long machineId;
    /**
     * 并发序列号
     */
    private volatile long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private volatile long lastStamp = -1L;

    private static SnowFlake snowFlake;

    public static SnowFlake getInstance() {
        if (snowFlake == null) {
            synchronized (SnowFlake.class) {
                if (snowFlake == null) {
                    snowFlake = new SnowFlake(1, 1);
                }
            }
//            throw new BeanInitializationException("datacenterId and machineId is undefined");
        }
        return snowFlake;
    }

    public static SnowFlake getInstance(long datacenterId, long machineId) {
        if (snowFlake == null) {
            synchronized (SnowFlake.class) {
                if (snowFlake == null) {
                    snowFlake = new SnowFlake(datacenterId, machineId);
                }
            }
        }
        return snowFlake;
    }


    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATA_CENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currSatmp = getNewstmp();
        if (currSatmp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currSatmp == lastStamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currSatmp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = currSatmp;

        return (currSatmp - START_STMP) << TIMESTAMP_LEFT //时间戳部分
                | datacenterId << DATA_CENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStamp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

}
