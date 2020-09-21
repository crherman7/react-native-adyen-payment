package com.rnlib.adyen.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.adyen.checkout.core.log.LogUtil
import com.adyen.checkout.core.log.Logger
import com.rnlib.adyen.ui.RedirectActivity

class RedirectService: JobIntentService() {
    companion object {
        val TAG = LogUtil.getTag()
        const val redirectJobId = 21
        const val REDIRECT_CALL_RESULT_KEY = "redirect_call_result_key"
        const val API_CALL_RESULT_KEY = "payments_api_call_result"
        private const val adyenCheckoutBaseActionSuffix = ".adyen.checkout"
        private const val callResultSuffix = "$adyenCheckoutBaseActionSuffix.CALL_RESULT"
    }
    override fun onHandleWork(intent: Intent) {
        val callResult = intent.getParcelableExtra<CallResult>(REDIRECT_CALL_RESULT_KEY)
        startActivity()
        handleCallResult(callResult)
    }
    private fun getCallResultAction(context: Context): String {
        return context.packageName + callResultSuffix
    }
    private fun startActivity() {
        val intent = Intent(this, RedirectActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
    }
    private fun handleCallResult(callResult: CallResult) {
        while(!RedirectActivity.ready) {
            Logger.d(TAG, "LocalBroadcastManager is not ready")
        }
        if (callResult.content.contains("\"type\":\"redirect\"")) {
            RedirectActivity.threeDs = true
        }
        if (callResult.type != CallResult.ResultType.WAIT) {
            // send response back to activity
            val resultIntent = Intent()
            resultIntent.action = getCallResultAction(this)
            resultIntent.putExtra(API_CALL_RESULT_KEY, callResult)
            val localBroadcastManager = LocalBroadcastManager.getInstance(this)
            localBroadcastManager.sendBroadcast(resultIntent)
        }
    }
}