/*
 * Copyright (C) 2021 The TagD Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.tagd.android.mvp

import android.os.Bundle
import io.tagd.android.app.FragmentActivity
import io.tagd.arch.control.IApplication
import io.tagd.arch.present.mvp.PresentableView
import io.tagd.arch.present.mvp.Presenter

abstract class MvpActivity<V : PresentableView, P : Presenter<V>> : FragmentActivity(),
    PresentableView {

    protected var presenter: P? = null

    override val app: IApplication?
        get() = application as IApplication

    override fun interceptOnCreate(savedInstanceState: Bundle?) {
        super.interceptOnCreate(savedInstanceState)
        presenter = onCreatePresenter(savedInstanceState)
        presenter?.onCreate()
    }

    protected abstract fun onCreatePresenter(savedInstanceState: Bundle?): P?

    @Suppress("UNCHECKED_CAST")
    override fun <V : PresentableView> presenter(): Presenter<V>? = presenter as? Presenter<V>

    override fun interceptOnStart() {
        super.interceptOnStart()
        presenter?.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    override fun onAwaiting() {
        super.onAwaiting()
        presenter?.onAwaiting()
    }

    override fun onInject() {
        super.onInject()
        presenter?.onInject()
    }

    override fun onReady() {
        super.onReady()
        presenter?.onReady()
    }

    override fun onPause() {
        presenter?.onPause()
        super.onPause()
    }

    override fun onStop() {
        presenter?.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        presenter?.onDestroy()
        release()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (presenter != null && presenter?.canHandleBackPress() == true) {
            presenter?.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun release() {
        awaitReadyLifeCycleEventsDispatcher().unregister(this)
        presenter = null
    }
}