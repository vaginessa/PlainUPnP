package com.m3sv.plainupnp.upnp

import com.m3sv.plainupnp.data.upnp.Directory
import com.m3sv.plainupnp.data.upnp.UpnpDevice
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DefaultUpnpNavigator @Inject constructor(private val factory: UpnpFactory,
                                               private val controller: UpnpServiceController) : UpnpNavigator {

    private val contentSubject = PublishSubject.create<ContentState>()

    override val state: Observable<ContentState> = contentSubject

    private val selectedDirectory = PublishSubject.create<Directory>()

    private val browseTo: Subject<BrowseToModel> = PublishSubject.create()

    private var directoriesStructure = Stack<ContentState>()

    private val currentContentDirectory: UpnpDevice?
        get() = controller.selectedContentDirectory

    init {
        browseTo.throttleFirst(250, TimeUnit.MILLISECONDS)
                .doOnNext {
                    browseFuture?.cancel(true)
                    contentSubject.onNext(ContentState.Loading)
                }.subscribe(::navigate, Timber::e)
    }

    override fun navigateHome() {
        directoriesStructure.clear()
        previousState = null
        browseTo.onNext(BrowseToModel("0", currentContentDirectory?.friendlyName ?: "Home"))
    }

    private var browseFuture: Future<*>? = null

    override fun navigateTo(model: BrowseToModel) {
        browseTo.onNext(model)
    }

    private var previousState: ContentState? = null

    private fun navigate(model: BrowseToModel) {
        Timber.d("Browse: ${model.id}")

        browseFuture = factory.createContentDirectoryCommand()?.browse(model.id, null) {
            previousState?.let(directoriesStructure::push)
            val successState = ContentState.Success(model.directoryName, it ?: listOf())

            contentSubject.onNext(successState)
            previousState = successState

            when (model.id) {
                "0" -> {
                    selectedDirectory.onNext(Directory.Home)
                }
                else -> {
                    val subDirectory = Directory.SubDirectory(model.id)
                    selectedDirectory.onNext(subDirectory)
                }
            }
        }
    }

    override fun navigatePrevious(): Boolean {
        browseFuture?.cancel(true)

        return when {
            directoriesStructure.size == 1 -> {
                previousState = directoriesStructure.pop()
                previousState?.let(contentSubject::onNext)
                true
            }
            directoriesStructure.size > 1 -> {
                contentSubject.onNext(directoriesStructure.pop())
                true
            }
            else -> false
        }
    }
}