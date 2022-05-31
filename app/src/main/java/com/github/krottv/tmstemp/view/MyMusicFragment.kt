package com.github.krottv.tmstemp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.github.krottv.tmstemp.R
import com.github.krottv.tmstemp.data.remote.LibraryRemoteDataSourceRetrofit
import com.github.krottv.tmstemp.data.remote.MyMusicRemoteDataSourceRetrofit
import com.github.krottv.tmstemp.presentation.AlbumsViewModel
import com.github.krottv.tmstemp.presentation.MyMusicViewModel
import com.github.krottv.tmstemp.presentation.TracksViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MyMusicFragment : Fragment() {

    lateinit var viewBinder: MyMusicFragmentBinder
    private val viewModel: AlbumsViewModel by inject()
    private val myMusicViewModel: TracksViewModel =
        TracksViewModel(MyMusicRemoteDataSourceRetrofit())



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = MyMusicFragmentBinder(this)

        return viewBinder.onCreateView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (parentFragment as NavHostFragment).parentFragment?.view?.findViewById<View>(R.id.library)
            ?.setOnClickListener {
                val navController = findNavController()

                val action =
                    MyMusicFragmentDirections.actionMyMusicFragmentToLibraryMusicFragment()
                navController.navigate(action)
            }

        (parentFragment as NavHostFragment).parentFragment?.view?.findViewById<View>(R.id.iTunes)
            ?.setOnClickListener {
                val navController = findNavController()

                val action =
                    MyMusicFragmentDirections.actionMyMusicFragmentToITunesMusicFragment()
                navController.navigate(action)
            }

        viewModel.loadData()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateITunes.collect {
                    viewBinder.onDataLoaded(it)
                }
            }
        }

       myMusicViewModel.loadTracks()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myMusicViewModel.stateITunes.collect {
                    viewBinder.tracksLoaded(it)
                }
            }
        }
    }
}