package ar.edu.utn.frba.mobile.smarket.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.Result

class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.putExtra("barCode", rawResult.text)
        setResult(Activity.RESULT_OK, intent)
        this.finish()
    }


}
