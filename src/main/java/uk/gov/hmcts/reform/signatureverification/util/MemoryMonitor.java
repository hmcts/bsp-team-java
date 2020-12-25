package uk.gov.hmcts.reform.signatureverification.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryMonitor {
    private MemoryMXBean memBean;

    public MemoryMonitor() {
        memBean = ManagementFactory.getMemoryMXBean();
    }

    public long getHeapUsed() {
        MemoryUsage mem = memBean.getHeapMemoryUsage();

        return mem.getUsed();
    }

    public long getHeapCommitted() {
        MemoryUsage mem = memBean.getHeapMemoryUsage();

        return mem.getCommitted();
    }

    public long getHeapMax() {
        MemoryUsage mem = memBean.getHeapMemoryUsage();

        return mem.getMax();
    }
}
