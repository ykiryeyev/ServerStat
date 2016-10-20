package com.kiryeyev.serverstat.monitor.heap;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HeapMonitor.class)
public class HeapMonitorTest {

	@Test
	public void testUpdateStat() {
		PowerMock.mockStatic(Runtime.class);

		Runtime mockedRuntime = EasyMock.createMock(Runtime.class);

		EasyMock.expect(Runtime.getRuntime()).andReturn(mockedRuntime).anyTimes();

		EasyMock.expect(mockedRuntime.freeMemory()).andReturn(42L * 1024L);
		EasyMock.expect(mockedRuntime.totalMemory()).andReturn(420L * 1024L);
		EasyMock.expect(mockedRuntime.freeMemory()).andReturn(84L * 1024L);
		EasyMock.expect(mockedRuntime.totalMemory()).andReturn(420L * 1024L);

		PowerMock.replay(Runtime.class, mockedRuntime);
		HeapMonitor monitor = new HeapMonitor();
		monitor.updateStat();
		monitor.updateStat();
		CompositeStatistics statistic = monitor.getStatistic(200);
		HeapStatistics freeStat = (HeapStatistics) statistic.getChildren().stream()
				.filter((s) -> ((HeapStatistics) s).getType() == HeapStatistics.Type.Free).findFirst().get();
		HeapStatistics usedStat = (HeapStatistics) statistic.getChildren().stream()
				.filter((s) -> ((HeapStatistics) s).getType() == HeapStatistics.Type.Used).findFirst().get();
		assertEquals(42, freeStat.getMin());
		assertEquals(420 - 42, usedStat.getMax());
		assertEquals(84, freeStat.getMax());
		assertEquals(420 - 84, usedStat.getMin());
	}

	@Test
	public void getCurrentHeapInfo() throws Exception {
		PowerMock.mockStatic(Runtime.class);

		Runtime mockedRuntime = EasyMock.createMock(Runtime.class);

		EasyMock.expect(Runtime.getRuntime()).andReturn(mockedRuntime).anyTimes();

		EasyMock.expect(mockedRuntime.freeMemory()).andReturn(42L * 1024L);
		EasyMock.expect(mockedRuntime.totalMemory()).andReturn(420L * 1024L);

		PowerMock.replay(Runtime.class, mockedRuntime);
		HeapMonitor monitor = new HeapMonitor();
		HeapRecord state = monitor.currentState();
		assertEquals(42, state.getFreeSize());
		assertEquals(420 - 42, state.getUsedSize());
	}

}
