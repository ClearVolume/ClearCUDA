package clearcudaj.test;

import static org.junit.Assert.assertEquals;
import jcuda.Sizeof;
import jcuda.driver.CUdevice_attribute;

import org.junit.Test;

import clearcudaj.CudaContext;
import clearcudaj.CudaDevice;
import clearcudaj.CudaDevicePointer;

public class CudaDevicePointerTests
{

	@Test
	public void testMalloc()
	{

		int lLength = 1024;

		try (CudaDevice lCudaDevice = new CudaDevice(0);
				CudaContext lCudaContext = new CudaContext(lCudaDevice);
				CudaDevicePointer lCudaDevicePointer = CudaDevicePointer.malloc(lLength * Sizeof.FLOAT))
		{
			float[] lFloatsIn = new float[lLength];
			lFloatsIn[lLength / 2] = 123;
			lCudaDevicePointer.copyFrom(lFloatsIn, true);

			float[] lFloatsOut = new float[lLength];
			lCudaDevicePointer.copyTo(lFloatsOut, true);
			assertEquals(123, lFloatsOut[lLength / 2], 0);
		}
	}

	@Test
	public void testMallocManaged()
	{

		int lLength = 1024;

		try (CudaDevice lCudaDevice = new CudaDevice(0);
				CudaContext lCudaContext = new CudaContext(lCudaDevice);)
		{
			if (lCudaDevice.getAttribute(CUdevice_attribute.CU_DEVICE_ATTRIBUTE_MANAGED_MEMORY) > 0)
			{
				try (CudaDevicePointer lCudaDevicePointer = CudaDevicePointer.mallocManaged(lLength * Sizeof.FLOAT))
				{
					float[] lFloatsIn = new float[lLength];
					lFloatsIn[lLength / 2] = 123;
					lCudaDevicePointer.copyFrom(lFloatsIn, true);

					float[] lFloatsOut = new float[lLength];
					lCudaDevicePointer.copyTo(lFloatsOut, true);
					assertEquals(123, lFloatsOut[lLength / 2], 0);
				}
			}
		}
	}

}
