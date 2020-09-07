package com.example.fishingpro.fish

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fishingpro.R
import com.example.fishingpro.databinding.FragmentFishBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_fish.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FishFragment() : Fragment() {

    companion object {
        val TAG = FishFragment::class.java.simpleName
    }

    private val argUserId: FishFragmentArgs by navArgs()

    private val fishViewModel: FishViewModel by viewModels()
    private lateinit var dataBinding: FragmentFishBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentFishBinding.inflate(inflater).also {
            it.fishViewModel = fishViewModel
            it.lifecycleOwner = this
            it.fishRecycleView.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
            it.fishRecycleView.adapter = FishAdapter(FishAdapter.OnFishClickListener {
                Log.d(TAG, "Clicked, open detail")
            })
        }
        dataBinding.fishToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        fishViewModel.catches.observe(viewLifecycleOwner, {
            print(it)
        })

        fishViewModel.activeFilter.observe(viewLifecycleOwner) {
            fish_toolbar.menu.findItem(R.id.action_filter_clear).isVisible = it
        }

        setHasOptionsMenu(true)
        dataBinding.fishToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_filter_month -> {
                    showFilterDialog()
                    true
                }
                R.id.action_filter_clear ->  {
                    fishViewModel.resetFilter()
                    true
                }
                else -> return@setOnMenuItemClickListener true
            }
        }
        return dataBinding.root
    }

    private fun showFilterDialog(){
        with(requireNotNull(activity).supportFragmentManager){
            val ft = beginTransaction()
            val prev = findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            // Create and show the dialog.
            val newFragment: DialogFragment = FilterFragmentDialog(FilterFragmentDialog.OnDialogDismissListener {
                fishViewModel.refreshListByMonth(it.getLong("MONTH"))
            })
            newFragment.show(ft, "dialog")
        }
    }
}