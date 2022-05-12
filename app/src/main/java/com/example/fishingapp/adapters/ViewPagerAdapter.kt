package com.example.fishingapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.fishingapp.concursos.BaseOrCondicionListFragment
import com.example.fishingapp.concursos.RankingListFragment
import com.example.fishingapp.databinding.FragmentBaseOrCondicionListBinding

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class DemoCollectionPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
        var fragment = when(i){
            0 -> BaseOrCondicionListFragment()
            1 -> RankingListFragment()
            else -> Fragment()
        }
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, i + 1)
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position){
            0 -> return "Bases y Condiciones"
            1 -> return "Ranking"
        }
        return ""
    }
}
private const val ARG_OBJECT = "object"