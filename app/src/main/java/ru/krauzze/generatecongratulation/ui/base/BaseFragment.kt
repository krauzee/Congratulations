package ru.krauzze.generatecongratulation.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.krauzze.generatecongratulation.GenerateCongratulationApplication.Companion.APP_SCOPE
import ru.krauzze.generatecongratulation.util.objectScopeName
import toothpick.Scope
import toothpick.Toothpick

abstract class BaseFragment: Fragment() {

    private var instanceStateSaved: Boolean = false

    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment)?.fragmentScopeName
            ?: APP_SCOPE
    }

    protected lateinit var scope: Scope
        private set

    internal lateinit var fragmentScopeName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()

        scope = if (Toothpick.isScopeOpen(fragmentScopeName)) {
            Toothpick.openScope(fragmentScopeName)
        } else {
            Toothpick.openScopes(parentScopeName, fragmentScopeName)
        }

        scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)

        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) {
            //destroy this fragment with scope
            Toothpick.closeScope(scope.name)
        }
    }

    private fun isRealRemoving(): Boolean =
        (isRemoving && !instanceStateSaved)
                || ((parentFragment as? BaseFragment)?.isRealRemoving() ?: false)

    private fun needCloseScope(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }

    open fun onBackPressed() { }

    companion object {
        private const val STATE_SCOPE_NAME = "state_scope_name"
    }
}