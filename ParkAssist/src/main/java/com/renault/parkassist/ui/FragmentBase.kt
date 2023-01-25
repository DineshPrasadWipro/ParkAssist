package com.renault.parkassist.ui

import android.os.Bundle
import android.os.Trace.beginSection
import android.os.Trace.endSection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.core.KoinComponent

abstract class FragmentBase : Fragment(), KoinComponent {

    protected abstract val layout: Int

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        beginSection("${this::class.simpleName} inflate")
        val view = inflater.inflate(layout, container, false)
        endSection()
        return view
    }

    override fun onStart() {
        super.onStart()
        onBind()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    /**
     * Implement this method to implement the actual binding between viewModel and view
     *
     * Example:
     * onBind {
     *      viewModel.liveData.observe {
     *          view.doSomething(it)
     *      }
     *     view.click.observe {
     *          viewModel.doSomething()
     *      }
     * }
     */
    protected abstract fun onBind()

    /**
     * Use this method to bind an view element to a liveData
     *
     * Example:
     * viewModel.liveData.observe {
     *      view.doSomething(it)
     * }
     *
     * @param block The code executed on liveData change event
     */
    protected fun <T> LiveData<T>.observe(block: (t: T) -> Unit) {
        observe(viewLifecycleOwner, Observer {
            block(it)
        })
    }

    /**
     * Use this method to bind an view model action to an observable.
     * This method also capture the subscription into a composite disposable in order
     * to smoothly dispose all observables in fragment's onStop() method.
     *
     * Example:
     * view.click.observe {
     *      viewModel.doSomething()
     * }
     *
     * @param block The code executed on the observable onNext() method called
     */
    protected fun <T> Observable<T>.observe(block: (t: T) -> Unit) =
        compositeDisposable.add(
            observeOn(AndroidSchedulers.mainThread())
                .subscribe {
            block(it)
        })
}