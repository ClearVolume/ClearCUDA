package clearcuda;

import static jcuda.driver.JCudaDriver.cuModuleGetSurfRef;
import static jcuda.driver.JCudaDriver.cuSurfRefSetArray;
import jcuda.driver.CUsurfref;
import coremem.interfaces.HasPeer;

public class CudaSurfaceReference implements HasPeer<CUsurfref>
{

	private final CUsurfref mCUsurfref = new CUsurfref();
	private final String mSurfaceName;

	public CudaSurfaceReference(CudaModule pCudaModule,
															String pSurfaceName)
	{
		mSurfaceName = pSurfaceName;
		cuModuleGetSurfRef(	mCUsurfref,
												pCudaModule.getPeer(),
												pSurfaceName);
	}

	public void setTo(CudaArray pCudaArray)
	{
		cuSurfRefSetArray(mCUsurfref, pCudaArray.getPeer(), 0);
	}

	@Override
	public CUsurfref getPeer()
	{
		return mCUsurfref;
	}

	public String getSurfaceName()
	{
		return mSurfaceName;
	}

	@Override
	public String toString()
	{
		return "CudaSurfaceReference [mSurfaceName=" + mSurfaceName + "]";
	}

}
