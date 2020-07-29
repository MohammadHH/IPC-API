package com.exalt.ipc.powerMock;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Service.class)
public class PowerMockTest {
	@Test
	public void testMethodInvokingPrivateMethod() throws Exception {
		Service service = new Service();
		Service serviceSpy = PowerMockito.spy(service);
		PowerMockito.when(serviceSpy, "sumPrivate", 3, 3).thenReturn(6);
		Assertions.assertEquals(6, serviceSpy.mulBy2(3), "mulBy2(3) = 6");
		PowerMockito.verifyPrivate(serviceSpy, Mockito.times(1)).invoke("sumPrivate", 3, 3);

	}
}
