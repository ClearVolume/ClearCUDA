package clearcudaj.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import jcuda.Sizeof;

import org.junit.Test;

import clearcudaj.CudaComputeCapability;
import clearcudaj.CudaContext;
import clearcudaj.CudaDevice;
import clearcudaj.CudaDevicePointer;
import clearcudaj.CudaFunction;
import clearcudaj.CudaHostPointer;
import clearcudaj.CudaModule;

public class CudaModuleAndFunctionTests
{

	@Test
	public void test() throws IOException
	{

		try (CudaDevice lCudaDevice = new CudaDevice(0);
				CudaContext lCudaContext = new CudaContext(lCudaDevice);
				CudaModule lCudaModule = CudaModule.moduleFromPTX(CudaCompilerTests.getPTX());)
		{

			CudaComputeCapability lComputeCapability = lCudaDevice.getComputeCapability();
			System.out.println(lComputeCapability);
			assertNotNull(lCudaModule);

			CudaFunction lFunction = lCudaModule.getFunction("bozo");
			assertNotNull(lFunction);

			int length = 1024;
			float[] a = new float[length];
			for (int i = 0; i < length; i++)
				a[i] = i;
			float[] b = new float[length];
			for (int i = 0; i < length; i++)
				b[i] = length - i;
			float[] c = new float[length];

			try (CudaDevicePointer lPtrA = CudaDevicePointer.malloc(length * Sizeof.FLOAT);
					CudaDevicePointer lPtrB = CudaDevicePointer.malloc(length * Sizeof.FLOAT);
					CudaDevicePointer lPtrC = CudaDevicePointer.malloc(length * Sizeof.FLOAT);)
			{
				lPtrA.copyFrom(a, true);
				lPtrB.copyFrom(b, true);


				lFunction.setBlockDim(32);
				lFunction.setGridDim(1024 / 32);
				lFunction.launch(length, lPtrA, lPtrB, lPtrC);

				lPtrC.copyTo(c, true);
				for (int i = 0; i < length; i++)
					assertEquals(length, c[i], 0);
			}

			try (CudaHostPointer lPtrA = CudaHostPointer.mallocPinned(length * Sizeof.FLOAT);
					CudaHostPointer lPtrB = CudaHostPointer.mallocPinned(length * Sizeof.FLOAT);
					CudaHostPointer lPtrC = CudaHostPointer.mallocPinned(length * Sizeof.FLOAT);)
			{
				lPtrA.copyFrom(a, true);
				lPtrB.copyFrom(b, true);

				lFunction.setBlockDim(32);
				lFunction.setGridDim(1024 / 32);
				lFunction.launch(length, lPtrA, lPtrB, lPtrC);

				lPtrC.copyTo(c, true);
				for (int i = 0; i < length; i++)
					assertEquals(length, c[i], 0);
			}
		}

	}

}
