package com.rnlib.adyen.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.adyen.checkout.base.ActionComponentData
import com.adyen.checkout.base.model.payments.response.Action
import com.adyen.checkout.core.code.Lint
import com.adyen.checkout.core.exception.CheckoutException
import com.adyen.checkout.core.log.LogUtil
import com.adyen.checkout.core.log.Logger
import com.adyen.checkout.redirect.RedirectUtil
import com.rnlib.adyen.ActionHandler
import com.rnlib.adyen.AdyenPaymentModule
import com.rnlib.adyen.R
import com.rnlib.adyen.ReactNativeUtils
import com.rnlib.adyen.service.CallResult
import com.rnlib.adyen.service.ComponentService
import org.json.JSONObject

class RedirectActivity : AppCompatActivity(), ActionHandler.DetailsRequestedInterface {

    companion object {
        var ready: Boolean = false
    }

    private lateinit var localBroadcastManager: LocalBroadcastManager

    @Suppress(Lint.PROTECTED_IN_FINAL)
    protected lateinit var actionHandler: ActionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirect)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(callResultReceiver, IntentFilter(ComponentService.getCallResultAction(this)))

        actionHandler = ActionHandler(this, this)
        actionHandler.restoreState(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        ready = true
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(callResultReceiver)
        ready = false
    }

    private val callResultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context != null && intent != null) {
                if (intent.hasExtra(ComponentService.API_CALL_RESULT_KEY)) {
                    val callResult = intent.getParcelableExtra<CallResult>(ComponentService.API_CALL_RESULT_KEY)
                    handleCallResult(callResult)
                } else {
                    throw CheckoutException("No extra on callResultReceiver")
                }
            }
        }
    }

    @Suppress(Lint.PROTECTED_IN_FINAL)
    protected fun handleCallResult(callResult: CallResult) {
        when (callResult.type) {
            CallResult.ResultType.ACTION -> {
                val action = Action.SERIALIZER.deserialize(JSONObject(callResult.content))
                actionHandler.handleAction(this, action, this::sendResult)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            handleIntent(intent)
        } else {
        }
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            // Redirect response
            Intent.ACTION_VIEW -> {
                val data = intent.data
                if (data != null && data.toString().startsWith(RedirectUtil.REDIRECT_RESULT_SCHEME)) {
                    actionHandler.handleRedirectResponse(data)
                } else {
                }
            }
            else -> {
            }
        }
    }

    private fun sendResult(content: String) {
        TODO("Not yet implemented")
    }

    override fun requestDetailsCall(actionComponentData: ActionComponentData) {
        Logger.d(LogUtil.getTag(), actionComponentData.details.toString())

        when {
            actionComponentData.details?.has("threeds2.fingerprint")!! -> {
                AdyenPaymentModule.getPromise()!!.resolve(actionComponentData.details?.get("threeds2.fingerprint"))
            }
            actionComponentData.details?.has("threeds2.challengeResult")!! -> {
                AdyenPaymentModule.getPromise()!!.resolve(actionComponentData.details?.get("threeds2.challengeResult"))
                finish()
            }
            else -> {
                AdyenPaymentModule.getPromise()!!.resolve(ReactNativeUtils.convertJsonToMap(actionComponentData.details))
                finish()
            }
        }
    }

    override fun onError(errorMessage: String) {
        Logger.d(LogUtil.getTag(), errorMessage)
    }
}