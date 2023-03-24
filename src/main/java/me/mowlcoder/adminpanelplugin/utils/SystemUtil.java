package me.mowlcoder.adminpanelplugin.utils;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class SystemUtil {

    public static double[] getTps() {
        return AdminPanelPlugin.getInstance().getServer().getTPS();
    }

    public static long getMaxMemoryInMb() {
        return Runtime.getRuntime().maxMemory() / 1_048_576;
    }

    public static long getUsedMemoryInMb() {
        return (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1_048_576;
    }

    public static double getProcessCpuLoad() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name;
        try {
            name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        } catch (MalformedObjectNameException e) {
            return Double.NaN;
        }
        AttributeList list;
        try {
            list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });
        } catch (InstanceNotFoundException | ReflectionException e) {
            return Double.NaN;
        }

        if (list.isEmpty()) {
            return Double.NaN;
        }

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        if (value == -1.0) {
            return Double.NaN;
        }

        return ((int)(value * 1000) / 10.0);
    }

}
